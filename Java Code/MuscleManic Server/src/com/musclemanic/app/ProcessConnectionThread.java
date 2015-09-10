package com.musclemanic.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.microedition.io.StreamConnection;

public class ProcessConnectionThread implements Runnable {
	
	
	static ProcessCommand processCommand;
	private StreamConnection mConnection;
	private InputStream inStream;
	private String lineRead;
	private int command;
    
    public ProcessConnectionThread(StreamConnection connection)
    {
        mConnection = connection;
    }

    @Override
    public void run() {
        try {

            // prepare to receive data
    	    inStream = mConnection.openInputStream();
            System.out.println("waiting for input");
    	    BufferedReader bReader2 = new BufferedReader(new InputStreamReader(inStream));

            while (true) {
        	    lineRead = bReader2.readLine();
                System.out.println("Input:" + lineRead);
            	if (lineRead != null){
                    command = Integer.parseInt(lineRead);
                    processCommand.doCommand(command);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	try {
				inStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }    
}
