#include "probe.h"

MODULE_AUTHOR("Equipe Qualidade do Ar <devtitans>");
MODULE_DESCRIPTION("Módulo para o dispositivo de qualidade do ar via USB");
MODULE_LICENSE("GPL");

MODULE_DEVICE_TABLE(usb, id_table);
bool ignore = true;

module_usb_driver(smartlamp_driver);

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

    return 0;
}

static void usb_disconnect(struct usb_interface *interface) {
    printk(KERN_INFO "Dispositivo desconectado do serial\n");
    kfree(usb_in_buffer);
    kfree(usb_out_buffer);}