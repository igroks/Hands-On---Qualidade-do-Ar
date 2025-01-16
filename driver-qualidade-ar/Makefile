obj-m += driver_qda.o \
		 probe.o \
		 serial_read.o \
		 serial_write.o
PWD := $(CURDIR)

# EXTRA_CFLAGS := -msoft-float -msse -msse2 -msse3 -msse4

all:
	make -C /lib/modules/$(shell uname -r)/build M=$(PWD) modules

clean:
	make -C /lib/modules/$(shell uname -r)/build M=$(PWD) clean