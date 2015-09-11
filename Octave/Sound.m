f=800;
fs=44100;
t=linspace(0,5,fs*4);
y=sin(2*pi*f*t)+sin(4*pi*f*t);
sound(y,fs)
y=sin(2*pi*f*t)+sin(4*pi*f*t)+ 2.5*gallery('normaldata',size(t),4);
sound(y,fs)
y=sin(2*pi*f*t)+sin(4*pi*f*t)+ 0.1*gallery('normaldata',size(t),4);
sound(y,fs)