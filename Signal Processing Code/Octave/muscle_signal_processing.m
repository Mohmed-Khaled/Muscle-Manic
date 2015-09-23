%----Configuration lines
pkg load all
close all
clear all
clc
if (exist("serial") == 3)
    disp("Serial: Supported")
else
    disp("Serial: Unsupported")
endif
arduino_serial = serial("COM4", 115200) 
set(arduino_serial,'timeout',20)

%-----Read 2048 Samples and process them
no_of_samples=512;
fs=2500;
samples=uint16(zeros(1,no_of_samples)); %--Set the samples initially to zeros 
%j=0;
%while(j<=1)
while(1)
  %j=j+1;
  tic;
  srl_flush(arduino_serial); 
  %Numbers are sent as no followed by a ',' (comma) so we wait until the recieved byte is equal to a comma to start recieving the numbers
  samples(1)=srl_read(arduino_serial,1);
   while(samples(1)!=44)  
        samples(1)=uint16(srl_read(arduino_serial,1));
      end
    samples(1)=uint16(srl_read(arduino_serial,1))*100;
    samples(1)=samples(1)+uint16(srl_read(arduino_serial,1));
    srl_read(arduino_serial,1); %read the comma and ignore it
  for i=2:1:no_of_samples
      samples(i)=uint16(srl_read(arduino_serial,1))*100;
      samples(i)=samples(i)+uint16(srl_read(arduino_serial,1));
      srl_read(arduino_serial,1); %read the comma and ignore it
  end
  
  samples(1)=samples(2); 
  %----Save the read samples to a binary file that will be accessed by the get_direction function
  fileID = fopen('samples.bin','w');
  fwrite(fileID,samples,'uint16');
  fclose(fileID);
  srl_write(arduino_serial, get_direction(no_of_samples,fs));
  toc;
end
fclose(arduino_serial);