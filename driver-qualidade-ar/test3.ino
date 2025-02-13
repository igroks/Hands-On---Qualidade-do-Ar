void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  pinMode(23, OUTPUT);
  randomSeed(analogRead(A0));
}

void loop() {
  // put your main code here, to run repeatedly:
  if(Serial.available() > 0) {
    String cmd = Serial.readString();
    processCommand(cmd);
  }
}

void processCommand(String command) {
  command.toUpperCase();
  
  if (command.startsWith("GET")) {
    Serial.print("RES ");
    Serial.println(millis() % 100);
    
  }
  else if (command.startsWith("SET")) {
    String value = command.substring(4);
    value.trim();
    analogWrite(23, value.toInt());
    Serial.println("RES 0");
  } else {
    Serial.println("RES -1");
  }
}