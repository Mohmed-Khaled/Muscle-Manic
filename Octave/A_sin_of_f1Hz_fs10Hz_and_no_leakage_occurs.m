%A sin of f=1Hz fs=10Hz and no leakage occurs because 1Hz can be represented using DFT
t=0:0.1:0.9;
y=sin(2*pi*t);
Y=fft(y);
f=-5:5-1;
stem(f,abs(fftshift(Y)))