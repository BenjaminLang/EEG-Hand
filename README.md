# EEG-Hand
Personal project that I have been developing to investigate the possibility of controlling robots via EEG signals.

# Operation
I have been developing software to take EEG data provided by the Muse headband and through machine learning algorithms, control a robotic hand that I designed and 3D printed. It uses a linear kernel support vector machine algorithm on data in the alpha, beta, gamma, delta and theta frequency bands to construct a classifier to make future predictions. 

# Dependencies
This project uses the JSAT (Java Statistical Analysis Tool) library to provide the machine learning algorithms.

# Additional Information
I am currently working on wireless communication between the Java program and an Arduino microcontroller via a ESP8266 Wi-Fi module in hopes to be able to remotely control the 3D printed hand. This project is a work in progress and gradual bug fixes and features will be added on in the future.


