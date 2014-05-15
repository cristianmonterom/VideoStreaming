package videostreaming;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import videostreaming.messaging.RequestResponse;
import videostreaming.messaging.StatusResponse;


public class ImageCaptureThread implements Runnable {
	
	private DataOutputStream output;
	private DataInputStream	input;
	
	
	public ImageCaptureThread( DataInputStream dIS, DataOutputStream dOS)
	{
		this.output = dOS;
		this.input = dIS;
	}
	
	
	public void run() {
		
		System.err.println("Aqui DEBE recibir las imagenes");
		//read response message from server
		receiveResponse();
		
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
		System.out.println("eh !!!:  "+rcvdRespFromServer.ToJSON());
		
	}
}


