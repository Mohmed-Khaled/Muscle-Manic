#include <SoftwareSerial.h>
int data;
int value;
char dir;
//Orange is Tx ,White is Rx 
SoftwareSerial mySerial(10, 11); // RX, TX Rx is connected to the TX
int green=7,yellow=8,blue=9;
void setup(){
  Serial.begin(115200);//for the processing sketchs 
  mySerial.begin(9600);
  pinMode(A0,INPUT);
  pinMode(green,OUTPUT);
  pinMode(yellow,OUTPUT);
  pinMode(blue,OUTPUT);
  delay(1000);
}

void loop(){    
    //data= analogRead(A5);//now write the next analog value to data[0]
    //mapped = map(data[0], 0, 1023, 0, 600);
    
    value=analogRead(A0);
    //value=1023;
    Serial.write(value/100);
    Serial.write(value%100);
    Serial.write(44);
    delayMicroseconds(400);
    if(Serial.available())
    {
     dir=Serial.read();
     delay(1);
     if(dir=='r')
     {
     //  mySerial.println(1);
       mySerial.write("1");
       mySerial.write("\r\n");
       digitalWrite(blue,HIGH);
       digitalWrite(yellow,0);
       digitalWrite(green,0);
       //delay(500);
     } 
     else if(dir=='l')
     {
       mySerial.write("2");
       mySerial.write("\r\n");
       //mySerial.println(2);
       digitalWrite(blue,0);
       digitalWrite(yellow,0);
       digitalWrite(green,1);
       //delay(500);
     }
     else if(dir=='n')
     {
       digitalWrite(blue,0);
       digitalWrite(yellow,1);
       digitalWrite(green,0);
     }

    }
}
