fileID = fopen('samples.bin');
samples = fread(fileID,[512 1],'uint16');
fclose(fileID);
fs=2048;
f=0:fs/length(samples):fs-(fs/length(samples));
t=0:1/fs:(length(samples)-1)/fs;
spectrum=fft(samples)/length(samples);
subplot(2,2,1)
plot(t,samples);
xlabel('time domain')
spectrum(1)=0;
spectrum(2)=0;
subplot(2,2,2)
stem(f(1:length(samples)/2),abs(spectrum)(1:length(samples)/2));
