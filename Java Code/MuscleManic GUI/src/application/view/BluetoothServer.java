package application.view;

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

import application.util.ProcessConnectionThread;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class BluetoothServer implements DiscoveryListener {

    @FXML
    private Label statusLabel;

    //object used for waiting
  	private static Object lock = new Object();

  	//array containing preknown devices
  	private static RemoteDevice[] pairedDevices;
  	private static String connectionURL = null;

  	private static Thread processThread;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	setStatus("Not Connected");
    }

    public void setStatus(String stat){
    	this.statusLabel.setText(stat);
    }

    @FXML
    private void handleConnect() throws IOException {
    	startBluetoothServer();
    }

	private void startBluetoothServer() throws IOException {
		BluetoothServer server = new BluetoothServer();
	    //display local device address and name
	    LocalDevice localDevice = LocalDevice.getLocalDevice();

	    //find devices
	    DiscoveryAgent agent = localDevice.getDiscoveryAgent();

	    pairedDevices = agent.retrieveDevices(DiscoveryAgent.PREKNOWN);
	    int pairedCount = pairedDevices.length;

	    if (!(pairedCount > 0)){
	    	setStatus("Can not detect device");
	    }

	    RemoteDevice remoteDevice = null;
        for (int i = 0; i < pairedCount; i++) {
            remoteDevice = (RemoteDevice) pairedDevices[i];
            if (remoteDevice.getFriendlyName(true).equalsIgnoreCase("HC-05")){
            	break;
            }
        }
	    UUID[] uuidSet = new UUID[1];
	    uuidSet[0]=new UUID(0x1101); //serial port service

	    agent.searchServices(null,uuidSet,remoteDevice,server);

	    try {
	        synchronized(lock){
	            lock.wait();
	        }
	    }
	    catch (InterruptedException e) {
	        e.printStackTrace();
	    }

	    if(connectionURL == null){
	    	setStatus("Error Connecting");
	    }else{
	    	setStatus("Connecting...");
	    }

	    StreamConnection streamConnection = (StreamConnection)Connector.open(connectionURL);

        processThread = new Thread(new ProcessConnectionThread(streamConnection));
    	setStatus("Connected");
        processThread.start();

	}

	@Override
	public void deviceDiscovered(RemoteDevice arg0, DeviceClass arg1) {
		// TODO Auto-generated method stub

	}
	@Override
	public void inquiryCompleted(int arg0) {
	    synchronized(lock){
	        lock.notify();
	    }
	}
	@Override
	public void serviceSearchCompleted(int arg0, int arg1) {
	    synchronized(lock){
	        lock.notify();
	    }

	}
	@Override
	public void servicesDiscovered(int arg0, ServiceRecord[] servRecord) {
		for (int i = 0; i < servRecord.length; i++) {
			connectionURL = servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
			if (connectionURL == null) {
				continue;

			}
		}

	}
}
