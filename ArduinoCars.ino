#include<Servo.h>           //Servo library
#include <SPI.h>
#include "nRF24L01.h"
#include <RF24.h>

#define trigPin1 A0
#define echoPin1 3
#define trigPin2 A1
#define echoPin2 5
#define trigPin3 A2
#define echoPin3 6
#define M10 2 //right motor
#define M11 4
#define M20 A4 //left motor
#define M21 A5
//#define motor_speed 9

double duration, distance, RightSensorVal,FrontSensorVal,LeftSensorVal;
int axisTheta = 0, currentAxis = 2;
unsigned long stx,sty,ct;
String turn = "no turn";
double spd = 7,y=0,x=0;

RF24 radio(7, 8); // CE, CSN
const byte address[][6] = {"30000","10000"};

void setup()
{
  radio.begin();
  Serial.begin (115200);
  radio.openReadingPipe(1, address[0]);
  radio.openWritingPipe(address[1]);
  radio.setPALevel(RF24_PA_MIN);
  radio.stopListening();
  pinMode(trigPin1, OUTPUT);
  pinMode(echoPin1, INPUT);
  pinMode(trigPin2, OUTPUT);
  pinMode(echoPin2, INPUT);
  pinMode(trigPin3, OUTPUT);
  pinMode(echoPin3, INPUT);
  pinMode(M10, OUTPUT);
  pinMode(M11, OUTPUT);
  pinMode(M20, OUTPUT);
  pinMode(M21, OUTPUT);
  //pinMode(motor_speed, OUTPUT);
  //analogWrite(motor_speed, 220);
  SonarSensor(trigPin1, echoPin1);
  RightSensorVal = distance;
  SonarSensor(trigPin2, echoPin2);
  LeftSensorVal = distance;
  SonarSensor(trigPin3, echoPin3);
  FrontSensorVal = distance;
  delay(5000);
  stx=millis();
  sty=millis();
}

void loop() {

  //Following code calls Sonar Sensr function and calculates distance of object to its right left and front  
  
  
  /*Serial.println("Right Sensor Value:" + String(RightSensorVal));
  Serial.println("Left Sensor Value:" + String(LeftSensorVal));
  Serial.println("Front Sensor Value:" + String(FrontSensorVal));*/
  SonarSensor(trigPin1, echoPin1);
    RightSensorVal = distance;
    SonarSensor(trigPin2, echoPin2);
    LeftSensorVal = distance;
    SonarSensor(trigPin3, echoPin3);
    FrontSensorVal = distance;
  
  //Code to transmit data will come here
  while(FrontSensorVal >= 15)
  {
    //motor code goes here
    digitalWrite(M10, HIGH);
    digitalWrite(M11, LOW);
    //Serial.println("motor1");
    digitalWrite(M20, HIGH);
    digitalWrite(M21, LOW);
    //Serial.println("motor2 ");
    x = getX();
    y = getY();
SonarSensor(trigPin1, echoPin1);
    RightSensorVal = distance;
    SonarSensor(trigPin2, echoPin2);
    LeftSensorVal = distance;
    SonarSensor(trigPin3, echoPin3);
    FrontSensorVal = distance;
    
    if(RightSensorVal<=400)
    {
      String msg = String(x) + " " + String(y) + " " + String(RightSensorVal) + " " + String(0+axisTheta);
      Serial.println(msg);
      //Serial.println(msg.length());
      char messageR[msg.length()+1];
      msg.toCharArray(messageR,msg.length()+1);
      //Serial.println(messageR);
      radio.write(&messageR,sizeof(messageR));
      delay(20);
    }

    if(LeftSensorVal<=400)
    {
      String msg = String(x) + " " + String(y) + " " + String(LeftSensorVal) + " " + String(180+axisTheta);
      Serial.println(msg);
      //Serial.println(msg.length());
      char messageL[msg.length()+1];
      msg.toCharArray(messageL,msg.length()+1);
      //Serial.println(messageL);
      radio.write(&messageL,sizeof(messageL));
      delay(20);
    }
if(FrontSensorVal<=400)
    {
      String msg = String(x) + " " + String(y) + " " + String(FrontSensorVal) + " " + String(90+axisTheta);
      Serial.println(msg);
      //Serial.println(msg.length());
      char messageF[msg.length()+1];
      msg.toCharArray(messageF,msg.length()+1);
      //Serial.println(messageF);
      radio.write(&messageF,sizeof(messageF)); 
      delay(50); 
    }
    
  }

  //stop motor
  digitalWrite(M10, LOW);
  digitalWrite(M11, LOW);
  digitalWrite(M20, LOW);
  digitalWrite(M21, LOW);
  if(RightSensorVal >=15)
  {
    //right turn
    takeRightTurn();
  }
  else if(LeftSensorVal>=15)
  {
    //left turn
    takeLeftTurn();
  }
else
  {
    //take two consecutive right turns
    takeRightTurn();
    takeRightTurn();
  }
}

void takeRightTurn()
{
  
  if(currentAxis == 2)
  {
      sty = millis();
      currentAxis = 1;
  }
  else if(currentAxis == -2)
  {   
      sty = millis();
      currentAxis = -1;
  }
  else if(currentAxis == 1)
  {
      stx = millis();
      currentAxis = -2;
  }
  else if(currentAxis == -1)
  { 
      stx = millis();
      currentAxis = 2;
 }
  digitalWrite(M20, HIGH);
  digitalWrite(M21, LOW);
  digitalWrite(M10, LOW);
  digitalWrite(M11, HIGH);
  delay(1000);
  digitalWrite(M10, LOW);
  digitalWrite(M11, LOW);
  digitalWrite(M20, LOW);
  digitalWrite(M21, LOW);

  turn = "right";
}

void takeLeftTurn()
{ 
  if(currentAxis == 2)
  {
      sty = millis();
      currentAxis = -1;
  }
  else if(currentAxis == -2)
  {
      sty = millis();
      currentAxis = 1;
  }
  else if(currentAxis == 1)
  {
      stx = millis();    
      currentAxis = 2;
 }
  else if(currentAxis == -1)
  {
      stx = millis();
      currentAxis = -2;
  }

  turn = "left";
  digitalWrite(M20, LOW);
  digitalWrite(M21, HIGH);
  digitalWrite(M10, HIGH);
  digitalWrite(M11, LOW);
  delay(1000);
  digitalWrite(M10, LOW);
  digitalWrite(M11, LOW);
  digitalWrite(M20, LOW);
  digitalWrite(M21, LOW);
}

void stopMotor()
{
  digitalWrite(M10, LOW);
  digitalWrite(M11, LOW);
  digitalWrite(M20, LOW);
  digitalWrite(M21, LOW);
}
void getAxisTheta(int currentAxis)
{
  //change incorrect
  if((currentAxis == 2 && turn.equals("right")) || (currentAxis == -2 && turn.equals("left")))
{
    axisTheta = -90;
  }
  else if((currentAxis == 2 && turn.equals("left")) || (currentAxis == -2 && turn.equals("right")))
  {
    axisTheta = 90;
  }
  else if((currentAxis == 1 && turn.equals("right")) || (currentAxis == -1 && turn.equals("left")) )
  {
    axisTheta = 180;
  }
  else if((currentAxis == 1 && turn.equals("left")) || (currentAxis == -1 && turn.equals("right")))
  {
    axisTheta = 0;
  }
}

double getX()
{
  ct = millis();
  if(currentAxis == 1 || currentAxis == -1)
  {
    x = x + (currentAxis*(((millis() - stx)*spd)/1000.0));
    stx = millis();
  }

  return x;
}

double getY()
{
  ct = millis();
  if(currentAxis == 2 || currentAxis == -2)
  {
    y = y + ((currentAxis/2)*(((millis() - sty)*spd)/1000.0));
    sty = millis();
  }

  return y;
}

void SonarSensor(int trigPin,int echoPin)
{
digitalWrite(trigPin, LOW);
delayMicroseconds(2);
digitalWrite(trigPin, HIGH);
delayMicroseconds(10);
digitalWrite(trigPin, LOW);
duration = pulseIn(echoPin, HIGH);

distance = (duration/2) / 29.1;

}
