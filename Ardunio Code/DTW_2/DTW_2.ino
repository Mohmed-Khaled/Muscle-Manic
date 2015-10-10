#define number_of_samples 35
#define weight 0.6

unsigned short right_motion[number_of_samples] = { 
    10, 7, 6, 6, 6, 6, 6, 6, 5, 6,
    6, 8, 15, 32, 88, 105, 116, 127, 118, 101,
    70, 43, 23, 14, 8, 70, 7, 8, 9, 9,
    17, 12, 9, 8, 6};

unsigned short left_motion[number_of_samples] = { 
    0, 8, 6, 6, 6, 14, 20, 17, 10, 8, 
    9, 7, 7, 8, 8, 9, 9, 11, 15, 26, 
    103, 200, 303, 388, 417, 379, 184, 76, 40, 40, 
    30, 23, 18, 17, 18};
      
//left//unsigned short motion_samples[number_of_samples] = {0, 9, 9, 9, 9, 11, 13, 90, 192, 318, 367, 386, 333, 325, 301, 253, 124, 51, 30, 20, 15, 13, 12, 13, 12, 13, 11, 12, 10, 11, 10, 11, 10, 11, 10};
//right//unsigned short motion_samples[number_of_samples] = {0, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 6, 6, 6, 7, 8, 21, 36, 56, 67, 55, 41, 24, 14, 10, 9, 9, 7, 8, 8, 8, 8, 6, 7};
//left//unsigned short motion_samples[number_of_samples] = {0, 6, 12, 12, 11, 12, 12, 12, 12, 12, 11, 12, 12, 13, 13, 17, 43, 81, 124, 210, 302, 314, 263, 105, 44, 20, 11, 8, 8, 7, 6, 6, 6, 6, 6};

unsigned short  motion_samples[number_of_samples] = {0};
unsigned short dis_left[number_of_samples][number_of_samples] = {0},dis_right[number_of_samples][number_of_samples] = {0};
unsigned long int distance_r = 0, distance_l = 0;
  
void setup() {
  Serial.begin(115200);
  pinMode(A0,INPUT);
}

void loop() {
  
  for(int i=0;i<number_of_samples;i++)
  {
    motion_samples[i] = analogRead(A0);
    Serial.println(motion_samples[i]);
    delay(30);
  }
  
  dis_left[number_of_samples][number_of_samples] = {0},dis_right[number_of_samples][number_of_samples] = {0};
  distance_r = 0, distance_l = 0;
  
  for (int i = 0; i < number_of_samples; i++)
  {
    for (int j = 0; j < number_of_samples; j++)
    {
      dis_left[i][j] = (motion_samples[i] - left_motion[j])*(motion_samples[i] - left_motion[j]);
      dis_right[i][j] = (motion_samples[i] - right_motion[j])*(motion_samples[i] - right_motion[j]);
    }
  }

  Serial.println("LEFT-------------------------------------------------------------");
  int i = 0, j = 0;
  while (i < number_of_samples - 1 && j < number_of_samples - 1)
  {
    Serial.print("i: ");
    Serial.print(i);
    Serial.print("  j: ");
    Serial.println(j);
    Serial.print(weight*dis_left[i + 1][j + 1]);
    Serial.print("  ");
    Serial.print(dis_left[i + 1][j]);
    Serial.print("  ");
    Serial.println(dis_left[i][j + 1]);
    if (weight*dis_left[i + 1][j + 1] <= dis_left[i + 1][j] && weight*dis_left[i + 1][j + 1] <= dis_left[i][j + 1])
    {
      distance_l += dis_left[i + 1][j + 1];
      i++;
      j++;
    }
    else if (dis_left[i + 1][j] <= weight*dis_left[i + 1][j + 1] && dis_left[i + 1][j] <= dis_left[i][j + 1])
    {
      distance_l += dis_left[i + 1][j];
      i++;
    }
    else
    {
      distance_l += dis_left[i][j + 1];
      j++;
    }
  }
  if (i == number_of_samples - 1 && j < number_of_samples - 1)
  {
    for (; j <= number_of_samples - 1; j++)
    {
      distance_l += dis_left[i][j];

    }
  }
  else if (j == number_of_samples - 1 && i < number_of_samples - 1)
  {
    for (; i <= number_of_samples - 1; i++)
    {
      distance_l += dis_left[i][j];
    }
  }

  Serial.println("RIGHT-------------------------------------------------------------");
  i = 0, j = 0;
  while (i < number_of_samples - 1 && j < number_of_samples - 1)
  {
    Serial.print("i: ");
    Serial.print(i);
    Serial.print("  j: ");
    Serial.println(j);
    Serial.print(weight*dis_right[i + 1][j + 1]);
    Serial.print("  ");
    Serial.print(dis_right[i + 1][j]);
    Serial.print("  ");
    Serial.println(dis_right[i][j + 1]);
    if (weight*dis_right[i + 1][j + 1] <= dis_right[i + 1][j] && weight*dis_right[i + 1][j + 1] <= dis_right[i][j + 1])
    {
      distance_r += dis_right[i + 1][j + 1];
      i++;
      j++;
    }

    else if (dis_right[i + 1][j] <= weight*dis_right[i + 1][j + 1] && dis_right[i + 1][j] <= dis_right[i][j + 1])
    {
      distance_r += dis_right[i + 1][j];
      i++;
    }
    else
    {
      distance_r += dis_right[i][j + 1];
      j++;
    }
  }
  if (i == number_of_samples - 1 && j < number_of_samples - 1)
  {
    for (; j <= number_of_samples - 1; j++)
    {
      distance_r += dis_right[i][j];

    }
  }
  else if (j == number_of_samples - 1 && i < number_of_samples - 1)
  {
    for (; i <= number_of_samples - 1; i++)
    {
      distance_r += dis_right[i][j];
    }
  }

  Serial.println("RESULTS-------------------------------------------------------------");
  Serial.print("Distance from right: ");
  Serial.println(sqrt(distance_r));
  Serial.print("Distance from left: ");
  Serial.println(sqrt(distance_l));

  Serial.println("--------------------------------------------------------------------");
  delay(10000);

}
