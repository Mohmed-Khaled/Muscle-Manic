package com.musclemanic.app;

import java.io.IOException;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class RemoteBluetoothClient implements DiscoveryListener{

	//object used for waiting
	private static Object lock = new Object();

	//vector containing the devices discovered
	//private static Vector<RemoteDevice> vecDevices = new Vector<RemoteDevice>();
	
	//array containing preknown devices
	private static RemoteDevice[] pairedDevices;
	private static String connectionURL = null;

	public static void main(String[] args) throws IOException {

	    RemoteBluetoothClient client = new RemoteBluetoothClient();

	    //display local device address and name
	    LocalDevice localDevice = LocalDevice.getLocalDevice();
	    System.out.println("Address: "+localDevice.getBluetoothAddress());
	    System.out.println("Name: "+localDevice.getFriendlyName());

	    //find devices
	    DiscoveryAgent agent = localDevice.getDiscoveryAgent();
/*	    System.out.println("Starting device inquiry...");
	    agent.startInquiry(DiscoveryAgent.GIAC, client);*/
	    
	    pairedDevices = agent.retrieveDevices(DiscoveryAgent.PREKNOWN);
	    int pairedCount = pairedDevices.length; 

	    if (pairedCount > 0)
	    	System.out.println("Paired devices found.");
	    else
	    	System.out.println("Paired devices not found.");
	    
/*	    try {
	        synchronized(lock){
	            lock.wait();
	        }
	    }
	    catch (InterruptedException e) {
	        e.printStackTrace();
	    }


	    System.out.println("Device Inquiry Completed. ");

	    //print all devices in vecDevices
	    int deviceCount = vecDevices.size();

	    if(deviceCount <= 0){
	        System.out.println("No Devices Found.");
	        System.exit(0);
	    }
	    else{
	        //print bluetooth device addresses and names in the format [ No. address (name) ]
	        System.out.println("Bluetooth Devices: ");
	        for (int i = 0; i <deviceCount; i++) {
	            RemoteDevice remoteDevice = (RemoteDevice)vecDevices.elementAt(i);
	            System.out.println((i+1)+". "+remoteDevice.getBluetoothAddress()+" ("+remoteDevice.getFriendlyName(true)+")");
	        }
	    }

	    System.out.print("Choose Device index: ");
	    BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));

	    String chosenIndex = bReader.readLine();
	    int index = Integer.parseInt(chosenIndex.trim());

	    //check for services
	    RemoteDevice remoteDevice = (RemoteDevice)vecDevices.elementAt(index-1);*/
	    RemoteDevice remoteDevice = null;
    	System.out.println("Finding HC-05");
        for (int i = 0; i < pairedCount; i++) {
            remoteDevice = (RemoteDevice) pairedDevices[i];
            if (remoteDevice.getFriendlyName(true).equalsIgnoreCase("HC-05")){
    	    	System.out.println("HC-05 found.");
            	break;
            }
        }
	    UUID[] uuidSet = new UUID[1];
	    //uuidSet[0]=new UUID(0x1105); //OBEX Object Push service
	    uuidSet[0]=new UUID(0x1101); //serial port service

	    System.out.println("\nSearching for service...");
	    agent.searchServices(null,uuidSet,remoteDevice,client);

	    try {
	        synchronized(lock){
	            lock.wait();
	        }
	    }
	    catch (InterruptedException e) {
	        e.printStackTrace();
	    }

	    if(connectionURL == null){
	        System.out.println("Device does not support Simple SPP Service.");
	        System.exit(0);
	    }

	    //connect to the server and send a line of text
	    StreamConnection streamConnection = (StreamConnection)Connector.open(connectionURL);

        Thread processThread = new Thread(new ProcessConnectionThread(streamConnection));
        processThread.start();

/*        	    //send string
	    OutputStream outStream=streamConnection.openOutputStream();
	    PrintWriter pWriter=new PrintWriter(new OutputStreamWriter(outStream));
	    pWriter.write("Test String from Remote bluetooth Client\r\n");
	    pWriter.flush();

	    //read response
	    InputStream inStream=streamConnection.openInputStream();
	    BufferedReader bReader2=new BufferedReader(new InputStreamReader(inStream));
	    String lineRead=bReader2.readLine();
	    System.out.println(lineRead);*/


	}

	//methods of DiscoveryListener
	public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
	    //add the device to the vector
/*	    if(!vecDevices.contains(btDevice)){
	        vecDevices.addElement(btDevice);
	    }*/
	}

	//implement this method since services are not being discovered
	public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
		for (int i = 0; i < servRecord.length; i++) {
			connectionURL = servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
			if (connectionURL == null) {
				continue;
			                                              }	
		}
	}

	//implement this method since services are not being discovered
	public void serviceSearchCompleted(int transID, int respCode) {
	    synchronized(lock){
	        lock.notify();
	    }
	}


	public void inquiryCompleted(int discType) {
	    synchronized(lock){
	        lock.notify();
	    }

	}//end method
	
}
