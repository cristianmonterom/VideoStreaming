package videostreaming;
import java.io.IOException;
import java.util.Arrays;

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
		sendImage();
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
	
	private void sendImage()
	{
		byte[] rawImage;
		byte[] partialImage;
		int start=0;
		int end=0;
		int remaining_data;
		boolean last_message = false;
		
		Stream streamMsg = null;
		
		rawImage = client.getRawImage();
		
		start = 0;
		remaining_data= rawImage.length;
		
		if(rawImage.length > Constants.IMAGE_DATA_SIZE.getValue()){
			end = Constants.IMAGE_DATA_SIZE.getValue();
		}else{
			end = rawImage.length;
			last_message = true;
		}
		
		//Process images to split and send multiple messages
		do{
			partialImage = Arrays.copyOfRange(rawImage, start, end);
			partialImage.toString();
			streamMsg = new Stream(last_message,partialImage.toString());		//check true or false
			try{
				client.getOutput().writeUTF(streamMsg.ToJSON());
			}catch(IOException ex){
				ex.printStackTrace();
			}
			
			start = end +1;
			remaining_data -= Constants.IMAGE_DATA_SIZE.getValue();
			if(remaining_data<=0) break;		//no more data
			
			if(remaining_data<=Constants.IMAGE_DATA_SIZE.getValue()){
				end += remaining_data;
				last_message = true;
				
			}else{
				end += Constants.IMAGE_DATA_SIZE.getValue();
			}

		}while(remaining_data>0);
		
	}

}
