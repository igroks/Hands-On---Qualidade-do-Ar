#include "serial_write.h"

bool ignore = true;
int LDR_value = 0;

MODULE_DEVICE_TABLE(usb, id_table);

module_usb_driver(smartlamp_driver);

static int usb_probe(struct usb_interface *interface, const struct usb_device_id *id) {
    struct usb_endpoint_descriptor *usb_endpoint_in, *usb_endpoint_out;

    printk(KERN_INFO "SmartLamp: Dispositivo conectado ...\n");

    // Detecta portas e aloca buffers de entrada e saída de dados na USB
    smartlamp_device = interface_to_usbdev(interface);
    ignore =  usb_find_common_endpoints(interface->cur_altsetting, &usb_endpoint_in, &usb_endpoint_out, NULL, NULL);  // AQUI
    usb_max_size = usb_endpoint_maxp(usb_endpoint_in);
    usb_in = usb_endpoint_in->bEndpointAddress;
    usb_out = usb_endpoint_out->bEndpointAddress;
    usb_in_buffer = kmalloc(usb_max_size, GFP_KERNEL);
    usb_out_buffer = kmalloc(usb_max_size, GFP_KERNEL);


    usb_write_serial("SET", 10);

    // printk("LDR Value: %d\n", LDR_value);

    return 0;
}

static void usb_disconnect(struct usb_interface *interface) {
    printk(KERN_INFO "SmartLamp: Dispositivo desconectado.\n");
    kfree(usb_in_buffer);                   // Desaloca buffers
    kfree(usb_out_buffer);
}

static int usb_write_serial(char *cmd, int param) {
    int ret, actual_size;    
    char resp_expected[MAX_RECV_LINE];      // Resposta esperada do comando  
    
    // use a variavel usb_out_buffer para armazernar o comando em formato de texto que o firmware reconheça

    sprintf(usb_out_buffer, "%s %d\n", cmd, param);
    printk(KERN_INFO "Buffer eh: %s", usb_out_buffer);
    // Grave o valor de usb_out_buffer com printk

    // Envie o comando pela porta Serial
    ret = usb_bulk_msg(smartlamp_device, usb_sndbulkpipe(smartlamp_device, usb_out), usb_out_buffer, strlen(usb_out_buffer), &actual_size, 1000);
    if (ret) {
        printk(KERN_ERR "SmartLamp: Erro de codigo %d ao enviar comando!\n", ret);
        return -1;
    }



    // Use essa variavel para fazer a integração com a função usb_read_serial
    // resp_expected deve conter a resposta esperada do comando enviado e deve ser comparada com a resposta recebida
    sprintf(resp_expected, "RES %s", cmd);

    return -1; 
}