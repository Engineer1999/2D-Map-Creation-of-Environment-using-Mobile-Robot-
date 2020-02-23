/*
 * Receiver Code for laptop arduino
 */
#include <SPI.h>
#include <nRF24L01.h>
#include <RF24.h>

RF24 radio(7, 8); // CE, CSN
/*
 *  Address "00001" is for transmitting signal from this module to Car 1
 *  Address "00002" is for transmitting signal from this module to Car 2 
 *  Address "00003" is for receiving signal from Car 1
 *  Address "00004" is for receiving signal from Car 2
 */
const byte address[][6] = {"30000","40000","10000","20000"};
void setup() {
  // put your setup code here, to run once:
  radio.begin();
  Serial.begin(115200);
  radio.openReadingPipe(1, address[2]);
  radio.openReadingPipe(2, address[3]);  
  radio.setPALevel(RF24_PA_MIN);
  radio.startListening();
}
void loop() {
  // put your main code here, to run repeatedly:
  if(radio.available())
  {
        char text1[32] = "" ;
        radio.read(&text1,sizeof(text1));
        Serial.println(text1);
  }
}
