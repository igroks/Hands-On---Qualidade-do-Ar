#define pin 32

void setup() {
  Serial.begin(9600);
}

void loop() {
  float sensor_volt;
  float RS_air;
  float R0;
  float sensorValue;

  for( int x = 0; x < 100; x++) {
    sensorValue = sensorValue + analogRead(pin);
  }

  sensorValue = sensorValue / 100.0;

  sensor_volt = (sensorValue / 1024) * 5.0;
  RS_air = (5.0 - sensor_volt) / sensor_volt;
  R0 = RS_air / 9.9;

  Serial.Print("sensor_volt: ");
  Serial.Print(sensor_volt);
  Serial.Print("R0: ");
  Serial.Print(R0);
  delay(1000);
}
