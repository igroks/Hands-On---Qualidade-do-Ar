#include <linux/module.h>
#include <linux/usb.h>
#include <linux/slab.h>

#define MAX_RECV_LINE 1000

#define VENDOR_ID 0x10c4
#define PRODUCT_ID 0xea60

static struct usb_device *qda_device;
static uint usb_in, usb_out;
static char *usb_in_buffer, *usb_out_buffer;       
static int usb_max_size;                          

static const struct usb_device_id id_table[] = { { USB_DEVICE(VENDOR_ID, PRODUCT_ID) }, {} };

static int  usb_probe(struct usb_interface *ifce, const struct usb_device_id *id);
static void usb_disconnect(struct usb_interface *ifce);  
static int usb_read_serial(void);

static struct usb_driver qda_driver = {
    .name        = "qda",
    .probe       = usb_probe,
    .disconnect  = usb_disconnect,
    .id_table    = id_table,
};

typedef struct AirQualityData {
  int pm2_5;
  int pm10;
  bool isValid;
} AirQualityData;

