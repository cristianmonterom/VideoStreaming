package videostreaming;
import java.io.IOException;

import videostreaming.messaging.*;
import videostreaming.common.*;


public class ClientThread implements Runnable{
	Client client;
	StatusResponse response;
	
	public ClientThread(Client client, StatusResponse status) {
		// TODO Auto-generated constructor stub
		this.client = client;
		this.response = status;
	}
	
	
	public void run()
	{
		sendStatusResponse();
		receiveRequest();
	}
	
	private void sendStatusResponse(){
		String str = response.ToJSON();
		try{
			client.getOutput().writeUTF(str);
		}catch(IOException ex){
			ex.printStackTrace();
		}
		
		System.err.println("send from thread as server= "+str);
	}
	
	private void  receiveRequest()
	{
		StartStreamRequest resquestRcvd = new StartStreamRequest();
		String rcvdReqStr = "";
		
		try{
			rcvdReqStr = client.getInput().readUTF();
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
		
		resquestRcvd.FromJSON(rcvdReqStr);
		
		System.err.println("Req rcvd" + resquestRcvd.ToJSON());
		
	}

}
