struct AirQualityData {
  float pm2_5;
  float pm10;
  bool isValid;
};

void setup() {
  AirQualityData data;
  data.pm2_5 = 2.5;
  data.pm10 = 5.0;
  data.isValid = true;
  
  Serial.begin(9600);
  Serial.write("RES isso\n");
}

void loop(){

}