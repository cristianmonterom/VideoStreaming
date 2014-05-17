package videostreaming;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import videostreaming.messaging.*;


public class ImageCaptureThread implements Runnable {
	
	private DataOutputStream output;
	private DataInputStream	input;
	
	
	public ImageCaptureThread( DataInputStream dIS, DataOutputStream dOS)
	{
		this.output = dOS;
		this.input = dIS;
	}
	
	
	public void run() {
		
		System.err.println("Should receive messages to int and start streaming");
		//read response message from server
		receiveResponse();
		sendStartStreamRequest();
		
	}
	
	private void receiveResponse()
	{
		String strFromServer = null;
		RequestResponse rcvdRespFromServer;
		try{
			strFromServer= input.readUTF();
		}
		catch(IOException ioEx){
			ioEx.printStackTrace();
		}
		
		
		System.err.println(strFromServer);
		rcvdRespFromServer = new StatusResponse();
		rcvdRespFromServer.FromJSON(strFromServer);
//		
		System.out.println("receiving as client !!!:  "+rcvdRespFromServer.ToJSON());
		
	}
	
	private void sendStartStreamRequest()
	{
		StartStreamRequest request = new StartStreamRequest();
		request = new StartStreamRequest();
		
		try{
//			output.writeUTF(request.ToJSON());
			output.writeUTF("hola");
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
		
		System.err.println("send as client"+request.ToJSON());
	}
}


