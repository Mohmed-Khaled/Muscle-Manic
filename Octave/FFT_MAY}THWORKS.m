fs = 100;                                % Sample frequency (Hz)
t = 0:1/fs:(10)-1/fs;                      % 10 sec sample
x = (1.3)*sin(2*pi*15*t)         % 15 Hz component
  + (1.7)*sin(2*pi*40*(t-2));     % 40 Hz component
 
% + 2.5*gallery('normaldata',size(t),4);
  
m = length(x);          % Window length
n = pow2(nextpow2(m));  % Transform length
y = fft(x,n);           % DFT
f = (0:n-1)*(fs/n);     % Frequency range
power = y.*conj(y)/n;   % Power of the DFT

plot(f,power)
xlabel('Frequency (Hz)')
ylabel('Power')
title('{\bf Periodogram}')