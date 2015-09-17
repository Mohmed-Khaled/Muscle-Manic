package com.musclemanic.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class ArduinoPort {
	
	static SerialPort serialPort;
	static ProcessCommand processCommand;
	private static String lineRead;
	private static int command;
	
	
    public static void main(String[] args) {
	    System.out.print("Choose port number: ");
	    BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
	    String chosenIndex = null;
		try {
			chosenIndex = bReader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    int index = Integer.parseInt(chosenIndex.trim());
	    String portName = "COM" + String.valueOf(index); 
        serialPort = new SerialPort(portName);
	    System.out.print("Choose baud rate: ");
	    BufferedReader bReader2 = new BufferedReader(new InputStreamReader(System.in));
	    String chosenRate = null;
		try {
			chosenRate = bReader2.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    int baudRate = Integer.parseInt(chosenRate.trim());
        processCommand = new ProcessCommand();
        try {
            serialPort.openPort();//Open port
            serialPort.setParams(baudRate, 8, 1, 0);//Set params
            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask
            serialPort.setEventsMask(mask);//Set mask
            serialPort.addEventListener(new SerialPortReader());//Add SerialPortEventListener
            System.out.println("Connected to port.");
            System.out.println("waiting for input");
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }
        
    /*
     * In this class must implement the method serialEvent, through it we learn about 
     * events that happened to our port. But we will not report on all events but only 
     * those that we put in the mask. In this case the arrival of the data and change the 
     * status lines CTS and DSR
     */
    static class SerialPortReader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR()){//If data is available
                if(event.getEventValue() == 1){//Check bytes count in the input buffer
                    //Read data, if 1 byte available 
                    try {
                        byte buffer[] = serialPort.readBytes(1);
                        lineRead = new String (buffer);
                        System.out.println("Input:" + lineRead);
                        command = Integer.parseInt(lineRead);
                        processCommand.doCommand(command);
                    }
                    catch (SerialPortException ex) {
                        System.out.println(ex);
                    }
                }
            }
            else if(event.isCTS()){//If CTS line has changed state
                if(event.getEventValue() == 1){//If line is ON
                    System.out.println("CTS - ON");
                }
                else {
                    System.out.println("CTS - OFF");
                }
            }
            else if(event.isDSR()){///If DSR line has changed state
                if(event.getEventValue() == 1){//If line is ON
                    System.out.println("DSR - ON");
                }
                else {
                    System.out.println("DSR - OFF");
                }
            }
        }
    }
}
