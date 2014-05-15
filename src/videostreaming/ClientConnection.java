package videostreaming;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import videostreaming.common.Constants;


public class ClientConnection extends Connection {
	
	String hostname="";
	
	
	 public ClientConnection(String host)
	 {
	    	this.hostname = host;
	    	System.out.println("ejecuta constructor");
	 }


//	@Override
//	public void establishConnection() {
//		// TODO Auto-generated method stub
//		
//	}
//	 
	 
	@Override
	 public void establishConnection()
	 {
		 try{
			 socket = new Socket(this.hostname,Constants.PORT.getValue());
			 inputStream = socket.getInputStream();
			 outputStream = socket.getOutputStream();
			 input = new DataInputStream(inputStream);
			 output = new DataOutputStream(outputStream);
		 }catch(IOException ex){
			 ex.printStackTrace();
		 }
		
	 } 
}
