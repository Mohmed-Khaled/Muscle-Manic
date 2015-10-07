#define number_of_samples 60
#define weight 0.6
long long  distance=0;
short left_motion[100]={15,15,16,16,16,16,15,15
        ,17,16,18,18,19,19,19,20
        ,21,24,26,26,26,26,27,27
        ,26,26,26,26,25,24,25,25
        ,26,26,26,25,27,27,27,27
        ,27,27,26,26,25,25,24,24
        ,24,25,25,24,25,25,25,24
        ,23,22,21,21,21,20,20,20
        ,20,19,19,18,19,19,19,18
        ,18,18,18,18,18,17,16,15
        ,15,14,14,13,13,13,12,12
        ,11,10,10,10, 9, 9, 9, 8
        , 8, 8, 8, 7};
    
short right_motion[100]={77,79,88,93,92,94,96,105,
    106,105,105,102,103,102,105,105,112,117,
    115,116,124,127,136,136,137,134,130,126,
    123,119,115,114,112,110,114,110,107,103,
    101,97,93,89,87,85,82,80,77,74,73,71,69,
    66,64,61,59,58,57,55,54,52,50,49,48,46,
    45,43,41,41,42,41,41,40,38,37,38,38,37,
    36,35,34,34,35,36,42,49,52,55,60,61,62,
    63,66,64,74,74,74,79,78,85,85};
int i=0,j=0;
void setup() {
  Serial.begin(9600);  
  pinMode(A0,INPUT);
  pinMode(13,OUTPUT);
}

void loop() {
    distance=0;
    short samples[number_of_samples];
    short dis[number_of_samples][number_of_samples];    
    for(i=0;i<number_of_samples;i++)
    {
        samples[i]=analogRead(A0);
        delay(2);
    }
    
    for(i=0;i<number_of_samples;i++)
    {
      for(j=0;j<number_of_samples;j++)
      {
        //dis[i][j]=0;
         dis[i][j]=(samples[i]-left_motion[j])*(samples[i]-left_motion[j]);
         //dis_right[i][j]=(samples[i]-right_motion[j])*(samples[i]-right_motion[j]);
      }
    }
    i=0;
    j=0; 
    while(i<number_of_samples-1 && j<number_of_samples-1)
    {   
        if(weight*dis[i+1][j+1]<=dis[i+1][j] && weight*dis[i+1][j+1]<=dis[i][j+1])
        //if(true)
        {
              distance+=sqrt(dis[i+1][j+1]);
              i++;
              j++;  
        }
        
       else if(dis[i+1][j]<=weight*dis[i+1][j+1] && dis[i+1][j]<=dis[i][j+1])
        {
            distance+=sqrt(dis[i+1][j]);
            i++;
        }
        
        else
        {
            distance+=sqrt(dis[i][j+1]);
            j++;
        }
        
    }
    Serial.println("a");
    delay(2000);
    
    
    if(i==99 && j<99)
    {
      for(;j<=99;j++)
      {
        distance+=dis[i][j];
                //cout<<"i: "<<i<<" j: "<<j<<endl;

      }
    }
    else if(j==99 && i<99)
    {
      for(;i<=99;i++)
      {
        distance+=dis[i][j];
        //cout<<"i: "<<i<<" j: "<<j<<endl;
      }
    }
    //end_timer=millis();
    //Serial.println(String(end_timer-start_timer));
    delay(1000);
    
}
