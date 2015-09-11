%A sin of f=1Hz fs=10Hz and no leakage occurs because 1Hz can be represented using DFT
t=0:0.1:0.9;
y=sin(2*1.1*pi*t);
Y=fft(y);

f=-5:5-1;
figure(1)
stem(f,0.1*abs(fftshift(Y))) %0.1 represents the Ts 


%A 32 sample sin of f=1Hz fs=32/3
fs=32/3;
t=0:1/fs:31/fs;
f=linspace(-fs/2,fs/2,length(t));
y=sin(2*pi*t);
Y=fft(y);
figure(3)
plot(f,abs(fftshift(Y))/32)
  

w=linspace(-pi,pi,1000);
F_om=zeros(1,1000);
for i=1:1000
  for n=0:31
    F_om(i)+=y(n+1)*exp(-j*w(i)*n);
  end
end
figure(4)
plot(w,(F_om))