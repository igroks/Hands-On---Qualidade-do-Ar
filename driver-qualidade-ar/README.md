# Descrição

O driver de qualidade do ar foi feito para ser compatível com o firmware localizado na pasta `/firmware`

Ele possui 3 partes:

* Leitura da Serial

* Escrita na Serial

* Gravar e Captar mudanças em Arquivos



# Instalação

Para carregar este driver, é preciso dos seguintes pacotes `make`.

```
sh
$ make
...
$ sudo insmod driver_qda.ko 
$ dmesg | tail 
[ 0.000000] Módulo carregado com sucesso! 
$ sudo rmmod nome_do_modulo 
$ dmesg | tail 
[ 0.000000] Módulo descarregado com sucesso!
```

Para descarregar, basta aplicar o seguinte comando

```
sh

$ sudo rmmod driver_qda.ko
```


# Uso

Em progresso...

# Desenvolvedor

* Luiz Gabriel Antunes Sena (Task 3.0, Task 3.1, Task 3.2)