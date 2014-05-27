package videostreaming;

import java.io.IOException;
//import java.io.PrintWriter;


import java.util.ArrayList;

import sun.misc.Cleaner;
import videostreaming.messaging.StartStreamRequest;
import videostreaming.messaging.StartStreamResponse;
import videostreaming.messaging.StatusResponse;
import videostreaming.messaging.StopStreamResponse;
import videostreaming.messaging.Stream;


/**
 * 
 * @author santiago
 *
 */
public class ClientThread implements Runnable {
	Client client;
	StatusResponse response;
	Thread stopStreamThread;
	StopStreamCapture stopStreamCapture;
	private ArrayList<Client> clientListRef = new ArrayList<Client>();
	

	public ClientThread(Client client, StatusResponse status, ArrayList<Client> clientList){
		this.client = client;
		this.response = status;
		stopStreamCapture = new StopStreamCapture(client.getIn());
		stopStreamThread = new Thread(stopStreamCapture);
		clientListRef = clientList;
	}

	public void run() {

		sendStatusResponse();
		receiveStartStreamRequest();
		sendStartStreamResponse();
		try {
			Thread.sleep(800);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}

		// While condition execute following
		stopStreamThread.start();
		do {
			sendImage();
		} while (stopStreamCapture.isRequestedToStop() == false);
		
		sendStopStreamResponse();
			
//		System.out.println("si se borra? =>index actual:"+clientListRef.indexOf(this.client)+"el string: "+this.client.toString()+"_source:"+Thread.currentThread().getStackTrace()[1].getFileName());
		clientListRef.remove(this.client);
		
		System.out.println("HERE THE CONNECTION MUST BE CLOSED _SOURCE:"+Thread.currentThread().getStackTrace()[1].getFileName());

	}

	private void sendStatusResponse() {
		String str = response.ToJSON();
		client.getOut().println(str);
		System.err.println("mensaje status response enviado ok" + str);
	}

	private void receiveStartStreamRequest() {
		StartStreamRequest resquestRcvd = new StartStreamRequest();
		String rcvdReqStr = "";

		try {
			rcvdReqStr = client.getIn().readLine();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		resquestRcvd.FromJSON(rcvdReqStr);

		includeServicePort(resquestRcvd.getServicePortInfo());
	}

	private void sendStartStreamResponse() {
		StartStreamResponse startStreamResponse = new StartStreamResponse();
		String str = startStreamResponse.ToJSON();
		client.getOut().println(str);
//		System.err.println("start stream RESPONSE SENT" + str);
	}

	private void sendStopStreamResponse() {
		StopStreamResponse stopStreamResponse = new StopStreamResponse();
		String str = stopStreamResponse.ToJSON();
		client.getOut().println(str);
//		System.err.println("SendInfo OK _from=> "+Thread.currentThread().getStackTrace()[1].getFileName()+":" + str);
	}
	
	private void sendImage() {
		byte[] rawImage;

		Stream streamMsg = null;
		String str = null;

		rawImage = client.getImage().getImageToDisplay();

		try {
			str = new String(rawImage, "UTF-8");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		streamMsg = new Stream(str);
		client.getOut().println(streamMsg.ToJSON());
	}
	
	private void includeServicePort(int portToInclude)
	{
		this.client.setServicePort(portToInclude);
	}
	
}
