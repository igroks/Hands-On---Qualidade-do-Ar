#include "probe.h"
#include <linux/string.h>

MODULE_AUTHOR("Equipe Qualidade do Ar <devtitans>");
MODULE_DESCRIPTION("Módulo para o dispositivo de qualidade do ar via USB");
MODULE_LICENSE("GPL");

MODULE_DEVICE_TABLE(usb, id_table);
bool ignore = true;

module_usb_driver(smartlamp_driver);

static int  usb_read_serial(void);

static char recv_line[MAX_RECV_LINE];              // Armazena dados vindos da USB até receber um caractere de nova linha '\n'


static int usb_probe(struct usb_interface *interface, const struct usb_device_id *id) {
    struct usb_endpoint_descriptor *usb_endpoint_in, *usb_endpoint_out;

    printk(KERN_INFO "Dispositivo conectado ao serial\n");

    smartlamp_device = interface_to_usbdev(interface);
    ignore =  usb_find_common_endpoints(interface->cur_altsetting, &usb_endpoint_in, &usb_endpoint_out, NULL, NULL);
    usb_max_size = usb_endpoint_maxp(usb_endpoint_in);
    usb_in = usb_endpoint_in->bEndpointAddress;
    usb_out = usb_endpoint_out->bEndpointAddress;
    usb_in_buffer = kmalloc(usb_max_size, GFP_KERNEL);
    usb_out_buffer = kmalloc(usb_max_size, GFP_KERNEL);

    int a = usb_read_serial();

    printk(KERN_INFO "Mensagem gerada: %s", recv_line);

    return 0;
}

static void usb_disconnect(struct usb_interface *interface) {
    printk(KERN_INFO "Dispositivo desconectado do serial\n");
    kfree(usb_in_buffer);
    kfree(usb_out_buffer);
}


static int usb_read_serial() {
    int recv_size = 0;                      // Quantidade de caracteres no recv_line
    int ret, actual_size, i, pos = 0;
    int retries = 10;                       // Tenta algumas vezes receber uma resposta da USB. Depois desiste.
    char resp_expected[MAX_RECV_LINE];      // Resposta esperada do comando
    char *resp_pos;                         // Posição na linha lida que contém o número retornado pelo dispositivo
    long resp_number = -1;

    while (retries > 0) {

        // Lê dados da USB
        ret = usb_bulk_msg(smartlamp_device, usb_rcvbulkpipe(smartlamp_device, usb_in), usb_in_buffer, min(usb_max_size, MAX_RECV_LINE), &actual_size, 1000);
        if (ret) {
            printk(KERN_ERR "SmartLamp: Erro ao ler dados da USB (tentativa %d). Codigo: %d\n", ret, retries--);
            continue;
        }

        printk(KERN_INFO "Veio aqui 2\n");

        for(i = 0; i < actual_size; i++) {
            printk(KERN_INFO "Veio aqui 3\n");
            recv_line[pos] = '\0';
            char * pointer = strstr(recv_line, "RES isso");
            if(pointer != NULL) {
                printk(KERN_INFO "Achou no recv_line[%d]: %c", pointer - recv_line, pointer);
                printk(KERN_INFO "Veio aqui 4\n");
                recv_line[pos] = '\0';
                printk(KERN_INFO "Gerou: %s", recv_line);
                return 0;
            }
            printk(KERN_INFO "Veio aqui 5\n");

            recv_line[pos] = usb_in_buffer[i];
            pos++;
        }

        printk(KERN_INFO "Continuando...\n");

        // Para cada caractere recebido ...
        // for (i=0; i<actual_size; i++) {

        //     if (usb_in_buffer[i] == '\n') {  // Temos uma linha completa
        //         recv_line[recv_size] = '\0';
        //         printk(KERN_INFO "SmartLamp: Recebido uma linha: '%s'\n", recv_line);

        //         printk(KERN_INFO "%s", recv_line);

        //     }
        //     else {
        //         recv_line[recv_size] = usb_in_buffer[i];
        //         recv_size++;
        //     }
        // }
    }

    printk(KERN_INFO "Veio aqui 7\n");
    recv_line[pos] = '\0';

    printk(KERN_INFO "Nao conseguiu, mas mensagem gerada foi: %s", recv_line);
    


    return -1; 
}