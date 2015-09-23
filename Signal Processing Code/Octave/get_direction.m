function [c]=get_direction(noofsamples,fs)
%-- This function is responsibe for the signal processing   

%---Open the samples file and read them in a matrix
  fileID = fopen('samples.bin');
  samples= fread(fileID,[noofsamples 1],'uint16');
  fclose(fileID);
  
%NEEDS IMPROVEMENT
%NEEDS IMPROVEMENT
%NEEDS IMPROVEMENT
  f=0:fs/noofsamples:fs-fs/noofsamples;
  spectrum=(fft(samples))/noofsamples;
  spectrum(1)=0;
  spectrum(2)=0;
  spectrum(3)=0;
  %spectrum(4)=0;
  %spectrum(5)=0;
  %spectrum(2)=0;
  power =( spectrum.*conj(spectrum));
  %[ max_value, max_index ] = max(spectrum(1:noofsamples/32))
  [ max_value, max_index ] = max(power(1:noofsamples/64))
  %subplot(2,2,1)
  %stem(f(1:noofsamples/64),abs(spectrum)(1:noofsamples/64));
  %grid
  %subplot(2,2,2)
  %stem(f(1:noofsamples/64),power(1:noofsamples/64));
  %grid
  %subplot(2,2,3)
  %plot(samples);
  %grid
  
%NEEDS IMPROVEMENT
%NEEDS IMPROVEMENT
%NEEDS IMPROVEMENT
%---Determine the direction and return a character 
  if(max_value<=4 && max_value>=1 && max_index<12)
    %pause(0.5); for the 512 samples and fs =2500
    pause(0.5);
    c='r';
  elseif(max_value>40 && max_index<12)
    %pause(0.5); for the 512 samples and fs =2500
    pause(0.5);
    c='l';
    else
    c='n';
  end