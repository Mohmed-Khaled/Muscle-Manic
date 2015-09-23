%Code taken from http://wiki.octave.org/Instrument_control_package#Serial
%{
This code shows how to use the instrument control package. 
%}
pkg load instrument-control
close all
clear all
clc
if (exist("serial") == 3)
    disp("Serial: Supported")
else
    disp("Serial: Unsupported")
endif
tic;
# Opens serial port COM2 with baudrate of 115200 (config defaults to 8-N-1)
adruino_serial = serial("COM4", 115200) 
# Flush input and output buffers
srl_flush(adruino_serial); 
%srl_write(s1, "Hello world!");
%srl_write(s1, uint8(98));
%srl_fwrite(s1, [142 , 300], 'int16');
# Blocking read call, returns uint8 array of exactly 12 bytes read
%data = srl_read(s1, 12)  
  no_of_samples=2500;
  srl_flush(adruino_serial); 
  s=uint16(zeros(1,no_of_samples));
  %number=uint16(0);
  for i=1:1:no_of_samples
    rec=srl_read(adruino_serial,1);
    while(rec==44)
    rec=srl_read(adruino_serial,1);
    end
      %rec=srl_read(adruino_serial,1);
     number=uint16(rec)*100;
     rec=srl_read(adruino_serial,1);
     number+=uint16(rec);
     s(i)=number;
   end
  s(1)=s(2);
  plot(s);
  %fileID = fopen('left_mohamed_2.bin','w')
  %fileID = fopen('right_mohamed_2.bin','w');
  fileID = fopen('amr_l.bin','w');
  fwrite(fileID,s,'double');
  %right is wrong
  fclose(fileID);
fclose(adruino_serial)
toc;