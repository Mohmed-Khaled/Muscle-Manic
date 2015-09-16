#include <Wire.h>
#include "Kalman.h" // Source: https://github.com/TKJElectronics/KalmanFilter


#define Forward 2
#define Back 3
#define Left 6
#define Right 4
#define Center 5

Kalman kalmanX; // Create the Kalman instances
Kalman kalmanY;
Kalman kalmanZ;

/* IMU Data */
double accX, accY, accZ;
double gyroX, gyroY, gyroZ;
int16_t tempRaw;

double kalAngleX, kalAngleY, kalAngleZ; // Calculated angle using a Kalman filter

uint32_t timer;
uint8_t i2cData[14]; // Buffer for I2C data

// TODO: Make calibration routine

void setup() {
  Serial.begin(115200);
  Wire.begin();
  TWBR = ((F_CPU / 400000L) - 16) / 2; // Set I2C frequency to 400kHz

  i2cData[0] = 7; // Set the sample rate to 1000Hz - 8kHz/(7+1) = 1000Hz
  i2cData[1] = 0x00; // Disable FSYNC and set 260 Hz Acc filtering, 256 Hz Gyro filtering, 8 KHz sampling
  i2cData[2] = 0x00; // Set Gyro Full Scale Range to ±250deg/s
  i2cData[3] = 0x00; // Set Accelerometer Full Scale Range to ±2g
  while (i2cWrite(0x19, i2cData, 4, false)); // Write to all four registers at once
  while (i2cWrite(0x6B, 0x01, true)); // PLL with X axis gyroscope reference and disable sleep mode

  while (i2cRead(0x75, i2cData, 1));
  if (i2cData[0] != 0x68) { // Read "WHO_AM_I" register
    Serial.print(F("Error reading sensor"));
    while (1);
  }
  pinMode(Center, OUTPUT);
  pinMode(Left, OUTPUT);
  pinMode(Right, OUTPUT);
  pinMode(Forward, OUTPUT);
  pinMode(Back, OUTPUT);
  delay(1000); // Wait for sensor to stabilize

  /* Set kalman and gyro starting angle */
  while (i2cRead(0x3B, i2cData, 6));
  accX = (i2cData[0] << 8) | i2cData[1];
  accY = (i2cData[2] << 8) | i2cData[3];
  accZ = (i2cData[4] << 8) | i2cData[5];

  // Source: http://www.freescale.com/files/sensors/doc/app_note/AN3461.pdf eq. 25 and eq. 26
  // atan2 outputs the value of -π to π (radians) - see http://en.wikipedia.org/wiki/Atan2
  // It is then converted from radians to degrees
  double roll  = atan2(accY, accZ) * RAD_TO_DEG;
  double pitch = atan(-accX / sqrt(accY * accY + accZ * accZ)) * RAD_TO_DEG;
  double yaw  = atan2(accX, accZ) * RAD_TO_DEG;

  kalmanX.setAngle(roll); // Set starting angle
  kalmanY.setAngle(pitch);
  kalmanZ.setAngle(yaw);

  timer = micros();
}

void loop() {
  /* Update all the values */
  while (i2cRead(0x3B, i2cData, 14));
  accX = ((i2cData[0] << 8) | i2cData[1]);
  accY = ((i2cData[2] << 8) | i2cData[3]);
  accZ = ((i2cData[4] << 8) | i2cData[5]);
  tempRaw = (i2cData[6] << 8) | i2cData[7];
  gyroX = (i2cData[8] << 8) | i2cData[9];
  gyroY = (i2cData[10] << 8) | i2cData[11];
  gyroZ = (i2cData[12] << 8) | i2cData[13];

  double dt = (double)(micros() - timer) / 1000000; // Calculate delta time
  timer = micros();

  // Source: http://www.freescale.com/files/sensors/doc/app_note/AN3461.pdf eq. 25 and eq. 26
  // atan2 outputs the value of -π to π (radians) - see http://en.wikipedia.org/wiki/Atan2
  // It is then converted from radians to degrees
  double roll  = atan2(accY, accZ) * RAD_TO_DEG;
  double pitch = atan(-accX / sqrt(accY * accY + accZ * accZ)) * RAD_TO_DEG;
  double yaw  = atan2(accX, accZ) * RAD_TO_DEG;
  
  double gyroXrate = gyroX / 131.0; // Convert to deg/s
  double gyroYrate = gyroY / 131.0; // Convert to deg/s
  double gyroZrate = gyroZ / 131.0; // Convert to deg/s
  
  // This fixes the transition problem when the accelerometer angle jumps between -180 and 180 degrees
  if ((roll < -90 && kalAngleX > 90) || (roll > 90 && kalAngleX < -90)) {
    kalmanX.setAngle(roll);
    kalAngleX = roll;
  } else
    kalAngleX = kalmanX.getAngle(roll, gyroXrate, dt); // Calculate the angle using a Kalman filter

  if (abs(kalAngleX) > 90)
    gyroYrate = -gyroYrate; // Invert rate, so it fits the restriced accelerometer reading
    
  kalAngleY = kalmanY.getAngle(pitch, gyroYrate, dt);
  kalAngleZ = kalmanZ.getAngle(yaw, gyroZrate, dt); 
  /*
  Serial.print("("); Serial.print(kalAngleX); Serial.print(" , ");
  Serial.print(kalAngleY); Serial.print(")"); 
*/
  if(kalAngleX < -20 && kalAngleX > -90){
    digitalWrite(Center, LOW);
    digitalWrite(Left, LOW);
    digitalWrite(Right, LOW);
    digitalWrite(Forward, HIGH);
    digitalWrite(Back, LOW);
    Serial.write("B"); 
  } else if(kalAngleX > 20 && kalAngleX < 90){
    digitalWrite(Center, LOW);
    digitalWrite(Left, LOW);
    digitalWrite(Right, LOW);
    digitalWrite(Forward, LOW);
    digitalWrite(Back, HIGH);
    Serial.write("F"); 
  } else if(kalAngleY < -20 && kalAngleY > -90){
    digitalWrite(Center, LOW);
    digitalWrite(Left, HIGH);
    digitalWrite(Right, LOW);
    digitalWrite(Forward, LOW);
    digitalWrite(Back, LOW);
    Serial.write("L"); 
  } else if(kalAngleY > 20 && kalAngleY < 90){
    digitalWrite(Center, LOW);
    digitalWrite(Left, LOW);
    digitalWrite(Right, HIGH);
    digitalWrite(Forward, LOW);
    digitalWrite(Back, LOW);
    Serial.write("R"); 
  } else if(kalAngleY > -20 && kalAngleY < 20 && (kalAngleX > -20 || kalAngleX < 20)){
    digitalWrite(Center, HIGH);
    digitalWrite(Left, LOW);
    digitalWrite(Right, LOW);
    digitalWrite(Forward, LOW);
    digitalWrite(Back, LOW);
  } 
#if 0 // Set to 1 to print the temperature
  Serial.print("\t");

  double temperature = (double)tempRaw / 340.0 + 36.53;
  Serial.print(temperature); Serial.print("\t");
#endif

//  Serial.print("\r\n");
  delay(100);
}
