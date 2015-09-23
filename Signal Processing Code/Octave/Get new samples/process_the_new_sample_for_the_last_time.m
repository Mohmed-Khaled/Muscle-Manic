clc
pkg load all
fileID = fopen('right_mohamed.bin');
A= fread(fileID,[10000 1],'double');
%B=B-min(B);
%max(B)
fclose(fileID);
tic();
lastindex=1;
figure(22)
plot(A)
%while lastindex<10000
  fs=5000;
  %A=B(1:2048);
  %A=B(lastindex:lastindex+1999);
  %lastindex+=2000;
  f=0:fs/length(A):fs-fs/length(A);
  spectrum=(fft(A));
  %abs(spectrum(7))
  %abs(spectrum(8))
  power = spectrum.*conj(spectrum)/length(A); 
  
  %pdc=power(1)
  toc();
  power(1)=0;
  power(2)=0;
  [ max_value, max_index ] = max(power(1:1900))
  %norm=(max_value/pdc)*10000
  %figure(int8(lastindex/2000))
  %power(3)
  figure(2)
  plot(f(1:(length(A)/8)),abs(power(1:(length(A)/8))))
  axis([0 200 0 500000])
  %end