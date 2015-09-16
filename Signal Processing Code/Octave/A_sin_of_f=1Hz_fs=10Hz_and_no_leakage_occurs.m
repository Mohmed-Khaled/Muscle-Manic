clc
%1 FS 
%2 FS coefficients 
t=linspace(-3,3,1000);
x_t=zeros(1,length(t));
for n=-1:1:1
  if(n~=0)
   x_t+=(-1/(j*2*pi*n))*(exp(j*pi*n)+exp(-j*pi*n)-2)*(exp(j*n*2*pi*t));
  end
end
figure(1)
subplot(2,2,1)
plot(t,x_t)

x_t=zeros(1,length(t));
for n=-5:1:5
  if(n~=0)
   x_t+=(-1/(j*2*pi*n))*(exp(j*pi*n)+exp(-j*pi*n)-2)*(exp(j*n*2*pi*t));
  end
end
subplot(2,2,2)
plot(t,x_t)
x_t=zeros(1,length(t));
for n=-10:1:10
  if(n~=0)
   x_t+=(-1/(j*2*pi*n))*(exp(j*pi*n)+exp(-j*pi*n)-2)*(exp(j*n*2*pi*t));
  end
end
subplot(2,2,3)
plot(t,x_t)
x_t=zeros(1,length(t));
for n=-1000:1:1000
  if(n~=0)
   x_t+=(-1/(j*2*pi*n))*(exp(j*pi*n)+exp(-j*pi*n)-2)*(exp(j*n*2*pi*t));
  end
end
subplot(2,2,4)
plot(t,x_t)
%3 Period to inf and FT 
%4 DTFT , sampling & Periodic Spectrum
%5 DFT ,sampling frequencies and periodic time domain
%6 FFT and quicker algorithm  
%7 FFT leakage 
%2 A sin of f=1Hz fs=10Hz and no leakage occurs because 1Hz can be represented using DFT
t=0:0.1:0.9;
y=sin(2*pi*t);
Y=fft(y);
f=-5:5-1;
figure(2)
stem(f,abs(fftshift(Y)))
%2 A sin of f=1Hz fs=10Hz and leakage occurs because 1Hz can be represented using DFT
t=0:0.1:0.9;
y=sin(2*pi*1.1*t);
Y=fft(y);
f=-5:5-1;
figure(3)
stem(f,abs(fftshift(Y)))