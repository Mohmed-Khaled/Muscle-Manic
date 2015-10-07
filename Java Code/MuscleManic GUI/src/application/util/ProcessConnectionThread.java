package application.util;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.StreamConnection;

public class ProcessConnectionThread implements Runnable {


	static ProcessCommand processCommand;
	private StreamConnection mConnection;
	private InputStream inStream;
	private String lineRead;
	private byte buffer[];
	private int command;

    public ProcessConnectionThread(StreamConnection connection)
    {
        mConnection = connection;
    }

    @Override
    public void run() {
        try {
        	processCommand = new ProcessCommand();
        	buffer = new byte[1];
            // prepare to receive data
    	    inStream = mConnection.openInputStream();
            System.out.println("waiting for input");
            System.out.println(inStream.toString());

            while (true) {
            	inStream.read(buffer);
            	lineRead = new String(buffer);
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
