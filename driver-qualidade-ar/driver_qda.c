#include <linux/init.h>
#include <linux/module.h>

MODULE_AUTHOR("Equipe Qualidade do Ar <devtitans>");
MODULE_DESCRIPTION("Módulo para o dispositivo de qualidade do ar via USB");
MODULE_LICENSE("GPL");

static int __init module_begin(void) {
    printk(KERN_INFO "Módulo carregado com sucesso!");
    return 0;
}

static void __exit module_end(void) {
    printk(KERN_INFO "Módulo descarregando...");
}

module_init(module_begin);
module_exit(module_end);