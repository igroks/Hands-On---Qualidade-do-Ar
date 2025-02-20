#include <linux/module.h>
#include <linux/usb.h>
#include <linux/slab.h>
#include <linux/string.h>

MODULE_AUTHOR("DevTITANS <devtitans@icomp.ufam.edu.br>");
MODULE_DESCRIPTION("Driver de acesso ao SmartLamp (ESP32 com Chip Serial CP2102");
MODULE_LICENSE("GPL");

#define MAX_RECV_LINE 100

typedef struct AirQualityData {
  int pm2_5;
  int pm10;
  bool isValid;
} AirQualityData;

typedef struct MQ9 {
  int sensor_volt;
  int RS_gas;
  int ratio;
  int gasCon;
} MQ9;

typedef struct AllData {
  AirQualityData data1;
  MQ9 Data2;
} AllData;

static MQ9* mq9Data = NULL;
static AirQualityData* sds011 = NULL;
static AllData* sensor_data = NULL;

static struct usb_device *smartlamp_device;
static uint usb_in, usb_out;
static char *usb_in_buffer, *usb_out_buffer;
static int usb_max_size;

#define VENDOR_ID 0x10c4
#define PRODUCT_ID 0xea60
static const struct usb_device_id id_table[] = { { USB_DEVICE(VENDOR_ID, PRODUCT_ID) }, {} };

static int  usb_probe(struct usb_interface *ifce, const struct usb_device_id *id);
static void usb_disconnect(struct usb_interface *ifce);
static int  usb_read_serial(void);   

static ssize_t attr_show(struct kobject *sys_obj, struct kobj_attribute *attr, char *buff);
static ssize_t attr_store(struct kobject *sys_obj, struct kobj_attribute *attr, const char *buff, size_t count);   

static struct kobj_attribute  sensor_attribute = __ATTR(sensor, S_IRUGO | S_IWUSR, attr_show, attr_store);
static struct attribute      *attrs[]       = { &sensor_attribute.attr, NULL };
static struct attribute_group attr_group    = { .attrs = attrs };
static struct kobject        *sys_obj;

MODULE_DEVICE_TABLE(usb, id_table);

bool ignore = true;
int LDR_value = 0;

static struct usb_driver smartlamp_driver = {
    .name        = "smartlamp",
    .probe       = usb_probe,
    .disconnect  = usb_disconnect,
    .id_table    = id_table,
};

module_usb_driver(smartlamp_driver);

static int usb_probe(struct usb_interface *interface, const struct usb_device_id *id) {
    struct usb_endpoint_descriptor *usb_endpoint_in, *usb_endpoint_out;

    printk(KERN_INFO "SmartLamp: Dispositivo conectado ...\n");

    sys_obj = kobject_create_and_add("airquality", kernel_kobj);
    ignore = sysfs_create_group(sys_obj, &attr_group);

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
    printk(KERN_INFO "SmartLamp: Dispositivo desconectado.\n");
    if (sys_obj) kobject_put(sys_obj);
    kfree(usb_in_buffer);
    kfree(usb_out_buffer);
}

static char recv_line[MAX_RECV_LINE];

static int usb_write_serial(char *cmd, int param) {
    int ret, actual_size;    
    
    sprintf(usb_out_buffer, "%s %d\n", cmd, param);
    printk(KERN_INFO "Enviando comando: %s", usb_out_buffer);

    ret = usb_bulk_msg(smartlamp_device, usb_sndbulkpipe(smartlamp_device, usb_out), usb_out_buffer, strlen(usb_out_buffer), &actual_size, 1000);
    if (ret) {
        printk(KERN_ERR "SmartLamp: Erro de codigo %d ao enviar comando!\n", ret);
        return -1;
    }

    return 1; 
}

static int usb_read_serial(void) {
    int ret, actual_size, i, pos = 0;
    int retries = 10;
    char * pointer;
    int value;

    int res = usb_write_serial("GET", 0);

    if(res == -1) {
        printk(KERN_ERR "Erro ao enviar comando\n");
        return -2;
    }

    while (retries > 0) {

        ret = usb_bulk_msg(smartlamp_device, usb_rcvbulkpipe(smartlamp_device, usb_in), usb_in_buffer, min(usb_max_size, MAX_RECV_LINE), &actual_size, 1000);
        if (ret) {
            printk(KERN_ERR "Driver Qualidade do Ar: Erro ao ler dados da USB (tentativa %d).\n", retries--);
            continue;
        }


        for(i = 0; i < actual_size; i++) {
            recv_line[pos] = usb_in_buffer[i];
            pos++;
        }

        for(i = 0; i < pos; i++) {
            if(recv_line[i] == '\n') {
                recv_line[pos] = '\0';
                pointer = (char*)strstr(recv_line, "RES");

                sensor_data = (AllData*) (pointer + 4);
                if(!sensor_data) {
                    printk(KERN_INFO "Eh nulo\n");
                    return -1;
                }
                value = 1;

                sds011 = &sensor_data->data1;
                mq9Data = &sensor_data->Data2;

                if(!sds011) {
                    printk(KERN_INFO "Eh nulo\n");
                    return -1;
                }

                if(!mq9Data) {
                    printk(KERN_INFO "Eh nulo\n");
                    return -1;
                }

                printk(KERN_INFO "Dado recebido: isValid = %d\n", sds011->isValid);
                printk(KERN_INFO "Dado recebido: pm10 = %d\n", (int)sds011->pm10);
                printk(KERN_INFO "Dado recebido: pm2_5 = %d\n", (int)sds011->pm2_5);

                printk(KERN_INFO "Dado recebido: ratio = %d\n", (int)mq9Data->ratio);
                printk(KERN_INFO "Dado recebido: RSGas = %d\n", (int)mq9Data->RS_gas);
                printk(KERN_INFO "Dado recebido: sensorVolt = %d\n", (int)mq9Data->sensor_volt);
                printk(KERN_INFO "Dado recebido: gasCon = %d\n", (int)mq9Data->gasCon);

                return value;
            }
        }

    }

    return -2;
}

static ssize_t attr_show(struct kobject *sys_obj, struct kobj_attribute *attr, char *buff) {
    int value = -1;
    const char *attr_name = attr->attr.name;

    printk(KERN_INFO "SmartLamp: Lendo %s ...\n", attr_name);

    value = usb_read_serial();

    // if (sizeof(buff) < sizeof(AllData)) {
        // Handle error: buffer too small
    //    return -EINVAL; // Or appropriate error
    //}



    // memcpy(buff, &sensor_data, sizeof(AllData));

    sprintf(buff, "%d %d %d %d %d %d %d", 
        sensor_data->data1.pm2_5, 
        sensor_data->data1.pm10, 
        sensor_data->data1.isValid,
        sensor_data->Data2.sensor_volt,
        sensor_data->Data2.RS_gas,
        sensor_data->Data2.ratio,
        sensor_data->Data2.gasCon);

    return strlen(buff);
}

static ssize_t attr_store(struct kobject *sys_obj, struct kobj_attribute *attr, const char *buff, size_t count) {
    long ret, value;
    const char *attr_name = attr->attr.name;

    ret = kstrtol(buff, 10, &value);
    if (ret) {
        printk(KERN_ALERT "SmartLamp: valor de %s invalido.\n", attr_name);
        return -EACCES;
    }

    printk(KERN_INFO "SmartLamp: Setando %s para %ld ...\n", attr_name, value);

    usb_write_serial("SET", value);

    if (ret < 0) {
        printk(KERN_ALERT "SmartLamp: erro ao setar o valor do %s.\n", attr_name);
        return -EACCES;
    }

    return strlen(buff);
}
