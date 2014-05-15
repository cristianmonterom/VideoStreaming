package videostreaming;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import videostreaming.common.Constants;

public class ServerConnection extends Connection{
	
	
	public ServerConnection()
	{
		try{
	    	server = new ServerSocket(Constants.PORT.getValue()+4);
	    	socket = new Socket();
		}
		catch(IOException connEx)
		{
			connEx.printStackTrace();
			//should retry to act as server
		}
	}
	
	@Override
	public void establishConnection(){
		try{
//			server = new ServerSocket(/*Constants.PORT.getValue()*/2356);
//	    	socket = new Socket();
	    	
	        System.out.println("esperando conexion");
			socket = server.accept();
			inputStream = socket.getInputStream();
	        outputStream = socket.getOutputStream();

		}catch(IOException ex){
			ex.printStackTrace();
		}
        
        output = new DataOutputStream(outputStream);
        input = new DataInputStream(inputStream);
	}
}
