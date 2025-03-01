#include <HardwareSerial.h>
#include "DHT.h"

// SDS011 serial connection
#define SDS_RX 16 // Ajuste para o pino RX correto da sua placa
#define SDS_TX 17 // Ajuste para o pino TX correto da sua placa
#define LED 34 // Led de alerta
#define DO 26 // Digital
#define AO 32 // Analógico
#define RL 10.0 // Resistor da Carga
#define DHTPIN 2 // Pino do DHT11
#define DHTTYPE DHT11


HardwareSerial sds(2); // Use Serial1 para o SDS011 (no ESP32, Serial1 é customizável)
DHT dht(DHTPIN, DHTTYPE);

// Estrutura para armazenar os valores do sensor
struct SDS011 {
  int pm2_5;
  int pm10;
  bool isValid; // Indica se os dados são válidos
};

struct MQ9 {
  int sensor_volt;  // Tensão do sensor (em Volts)
  int RS_gas;       // Resistência do sensor (em ohms)
  int ratio;        // Razão (RS / R0)
  int gasCon;
};

struct DHT11a {
	int temperature;
	int humidity;
};

struct AllData {
  SDS011 data1;
  MQ9 data2;
  DHT11a data3;
};

int alert = 0;
float R0 = 9.6;

// Função para ler os dados da serial
SDS011 readSDS011() {
  SDS011 data1 = {0, 0, false};
  
  // Aguarda o byte inicial 0xAA
  while (sds.available() && sds.read() != 0xAA) { }

  byte buffer[10];
  buffer[0] = 0xAA;

  // Verifica se há pelo menos 9 bytes disponíveis para leitura
  if (sds.available() >= 9) {
    sds.readBytes(&buffer[1], 9);

    // Verifica se o último byte é o byte de término correto (0xAB)
    if (buffer[9] == 0xAB) {
      int pm25int = (buffer[3] << 8) | buffer[2];
      int pm10int = (buffer[5] << 8) | buffer[4];
      data1.pm2_5 = conversor(pm25int / 10.0);
      data1.pm10 = conversor(pm10int / 10.0);
      data1.isValid = true;
    }
  }
  return data1;
}

// Função para enviar comandos ao SDS011
void writeToSerial(const byte* command, size_t length) {
  sds.write(command, length);
}

//função para ler os dados do MQ9
// Estrutura para armazenar os dados do sensor MQ9

MQ9 readMq9() {
  MQ9 data = {0, 0, 0, 0};  // Inicializa a estrutura com zeros

  // Lê o valor analógico do sensor (valor entre 0 e 1023)
  int sensorValue = analogRead(AO);  
  
  // Converte o valor analógico para tensão (em Volts)
  float volt = ((float)sensorValue / 4095.0) * 3.3;
  
  // Calcula a resistência do sensor (RS)
  float gas = ((3.3 * RL)/volt) - RL;
  
  // Calcula a razão entre a resistência do sensor e o valor de calibração (R0)
  float ratio = gas / R0;

  // Atribui os valores calculados à estrutura de dados
  data.sensor_volt = conversor(volt);
  data.RS_gas = conversor(gas);
  data.ratio = conversor(ratio);
  data.gasCon = conversor(200 * pow(ratio, -1.50));

  return data;  // Retorna a estrutura com os dados
}

int conversor(float value) {
  return (int)(round(value * 100));
}
void setup() {
  // Inicializa a comunicação Serial com o computador
  Serial.begin(9600);
  delay(100);
  Serial.println("Initializing SDS011 Air Quality Monitor...");
  // Inicializa a comunicação HardwareSerial com o SDS011
  sds.begin(9600, SERIAL_8N1, SDS_RX, SDS_TX);
  dht.begin();
  // Exemplo: enviar comando para colocar o sensor no modo de espera
  byte sleepCommand[] = {0xAA, 0xB4, 0x06, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0x05, 0xAB};
  writeToSerial(sleepCommand, sizeof(sleepCommand));

  pinMode(DO, INPUT);
}

void loop() {
  
  if(Serial.available() > 0) {
    String cmd = Serial.readString();
    SDS011 data1 = readSDS011();
    MQ9 data2 = readMq9();
    DHT11a data3 = {0, 0};
    AllData result;

    // Leitura da umidade
  	float humidity = dht.readHumidity();
  	data3.humidity = humidity;
  
  	// Leitura da temperatura em Celsius
  	float temperature = dht.readTemperature(); 
  	data3.temperature = temperature;

    //alert = digitalRead(DO);

    ///*
    if (data1.isValid) {
      Serial.println("---Data1---");
      Serial.print("PM2.5: ");
      Serial.print(data1.pm2_5);
      Serial.print(" ug/m3  ");
      Serial.print("PM10: ");
      Serial.print(data1.pm10);
      Serial.println(" ug/m3");
      Serial.println("\n");
    } else {
      Serial.println("Failed to read valid data from SDS011.");
    }
    Serial.println("---Data2---");
    Serial.print("sensor_volt: ");
    Serial.print(data2.sensor_volt);
    Serial.print(", RS_gas: ");
    Serial.print(data2.RS_gas);
    Serial.print(", ratio: ");
    Serial.print(data2.ratio);
    Serial.print(", Gas Concetration: ");
    Serial.print(data2.gasCon);
    Serial.print(" ppm.");
    Serial.println("\n\n");

    Serial.println("---Data3---");
    Serial.print("Temperature: ");
    Serial.print(data3.temperature);
    Serial.print("C, ");
    Serial.print("Umidade: ");
    Serial.print(data3.humidity);
    Serial.print("%.");
    Serial.println("\n\n");
    
    //*/
    /*
    //(RES struct /n)
    if(data1.isValid) {
      result.data1 = data1;
      //Serial.write((byte*)&data, sizeof(data));
    } else {
      result.data1 = {0,0,false}; 
    }
  
    result.data2 = data2;
    result.data3 = data3;
  
    Serial.print("RES ");
    Serial.write((byte*)&result, sizeof(result));
    Serial.print(" \n");
    */
  }
  
  delay(1000);
}
