package videostreaming;
import java.io.IOException;


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
	}
	
	private void sendStatusResponse(){
		String str = response.ToJSON();
		try{
			client.getOutput().writeUTF(str);
		}catch(IOException ex){
			ex.printStackTrace();
		}
		
		System.err.println("mandado desde el thread= "+str);
	}

}
