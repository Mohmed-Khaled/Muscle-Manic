fileID = fopen('left.bin');
A = fread(fileID,[10000 1],'double');
fs=10000;
f=0:fs/length(A):fs-fs/length(A);
spectrum=(fft(A));
spectrum(1)=0;
spectrum(2)=0;
power = spectrum.*conj(spectrum)/length(A); 
figure(1)
%plot(f(1:(length(A)/8)),power(1:(length(A)/8)))
plot(f,power)
%axis([0 800 0 100000])

fclose(fileID);

fileID = fopen('left_2.bin');
A = fread(fileID,[10000 1],'double');
fs=10000;
f=0:fs/length(A):fs-fs/length(A);
spectrum=(fft(A));
spectrum(1)=0;
spectrum(2)=0;
power = spectrum.*conj(spectrum)/length(A); 
figure(2)
plot(f(1:(length(A)/8)),power(1:(length(A)/8)))
axis([0 800 0 100000])

fclose(fileID);

fileID = fopen('right.bin');
A = fread(fileID,[10000 1],'double');
f=0:fs/length(A):fs-fs/length(A);
spectrum2=(fft(A));
spectrum2(1)=0;
spectrum2(2)=0;
power2 = spectrum2.*conj(spectrum2)/length(A); 
figure(3)
plot(f(1:(length(A)/8)),power2(1:(length(A)/8)))
axis([0 800 0 100000])

fclose(fileID);


fileID = fopen('right.bin');
A = fread(fileID,[10000 1],'double');
f=0:fs/length(A):fs-fs/length(A);
spectrum2=(fft(A));
spectrum2(1)=0;
spectrum2(2)=0;
power2 = spectrum2.*conj(spectrum2)/length(A); 
figure(4)
plot(f(1:(length(A)/8)),power2(1:(length(A)/8)))
axis([0 800 0 100000])

fclose(fileID);
