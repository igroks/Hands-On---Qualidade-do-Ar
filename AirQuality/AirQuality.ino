#include <SoftwareSerial.h>

// sds011 serial pins
#define SDS_RX 1
#define SDS_TX 3
SoftwareSerial sds(SDS_RX, SDS_TX);


void setup() {
  // Initialize Serial communication with the computer
  serial.begin(115200);
  delay(100);
  Serial.println("Initializing sds011 Air Quality Monitor...");

  //Initialize SoftwareSerial communication with sds011
  sds.begin(9600);
}

void loop() {
 while (sds.available() && sds.read() != 0xAA) { }

 //Once we have the start byte, attempt to read the next 9 bytes
 byte buffer[10];
 buffer[0] = 0xAA;
 if (sds.available() >= 9) {
  sds.readBytes(&buffer[1], 9);

  //check if the last byte is the correct ending byte
  if (buffer[9] == 0xAB) {
    int pm25int = (buffer[3] << 8) | buffer[2];
    int pm10int = (buffer[5] << 8) | buffer[4];
    float pm2_5 = pm25int / 10.0;
    float pm10 = pm10int / 10.0;

    //Print the values
    Serial.print("PM2.5: ");
    Serial.print(pm2_5, 2);
    Serial.print(" ug/m2  ");
    Serial.print("PM10: ");
    Serial.print(pm10, 2);
    Serial.print(" ug/m2  ");
  }
 }
 delay(1000);
}
