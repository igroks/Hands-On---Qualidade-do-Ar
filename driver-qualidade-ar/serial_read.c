#include "probe.h"
#include <linux/string.h>

MODULE_AUTHOR("Equipe Qualidade do Ar <devtitans>");
MODULE_DESCRIPTION("Módulo para o dispositivo de qualidade do ar via USB");
MODULE_LICENSE("GPL");

MODULE_DEVICE_TABLE(usb, id_table);
bool ignore = true;

module_usb_driver(qda_driver);

static char recv_line[MAX_RECV_LINE];

static AirQualityData *data_recv;

static int usb_probe(struct usb_interface *interface, const struct usb_device_id *id) {
    struct usb_endpoint_descriptor *usb_endpoint_in, *usb_endpoint_out;

    printk(KERN_INFO "Dispositivo conectado ao serial\n");

    qda_device = interface_to_usbdev(interface);
    ignore =  usb_find_common_endpoints(interface->cur_altsetting, &usb_endpoint_in, &usb_endpoint_out, NULL, NULL);
    usb_max_size = usb_endpoint_maxp(usb_endpoint_in);
    usb_in = usb_endpoint_in->bEndpointAddress;
    usb_out = usb_endpoint_out->bEndpointAddress;
    usb_in_buffer = kmalloc(usb_max_size, GFP_KERNEL);
    usb_out_buffer = kmalloc(usb_max_size, GFP_KERNEL);

    int code = usb_read_serial();

    if(code == 0) {
        printk(KERN_INFO "Driver Qualidade do Ar: Dado recebido com sucesso!");
    } else if(code == -1) {
        printk(KERN_ERR "Driver Qualidade do Ar: Dado foi perdido.");
    } else if(code == -2) {
        printk(KERN_ERR "Driver Qualidade do Ar: Não foi possível se ler do USB");
    }


    return 0;
}

static void usb_disconnect(struct usb_interface *interface) {
    printk(KERN_INFO "Dispositivo desconectado do serial\n");
    kfree(usb_in_buffer);
    kfree(usb_out_buffer);
}

static int usb_read_serial() {
    int ret, actual_size, i, pos = 0;
    int retries = 10;

    while (retries > 0) {

        ret = usb_bulk_msg(qda_device, usb_rcvbulkpipe(qda_device, usb_in), usb_in_buffer, min(usb_max_size, MAX_RECV_LINE), &actual_size, 1000);
        if (ret) {
            printk(KERN_ERR "Driver Qualidade do Ar: Erro ao ler dados da USB (tentativa %d).\n", retries--);
            continue;
        }


        for(i = 0; i < actual_size; i++) {
            recv_line[pos] = usb_in_buffer[i];
            pos++;
        }

        recv_line[pos] = '\0';
        char * pointer = strstr(recv_line, "###");
        if(pointer != NULL && *(pointer + 3 + sizeof(AirQualityData)) == '#') {
            struct AirQualityData *data;
            data = (struct AirQualityData*)(pointer + 3);
            if(data == NULL) {
                return -1;
            }
            // *data_recv = *data;
            printk(KERN_INFO "Driver Qualidade do Ar: Data recebida");
            printk(KERN_INFO "pm2_5: %d", data->pm2_5);
            printk(KERN_INFO "pm10: %d", data->pm10);
            printk(KERN_INFO "isValid: %d", data->isValid);


            return 0;
        }

    }

    return -2; 
}