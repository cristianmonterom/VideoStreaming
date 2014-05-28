/**
 * 
 */
package videostreaming.messaging;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import videostreaming.common.ProtocolMessages;
import videostreaming.Client;;

/**
 * @author Cristian
 *
 */
public class OverloadResponse extends RequestResponse {
	private String server = null;
	private int port;
	private ArrayList<Client> allClients = new ArrayList<Client>();
	
	public ArrayList<Client> getAllClients() {
		return allClients;
	}

	public OverloadResponse () {
	}
	
	@SuppressWarnings("unchecked")
	public OverloadResponse (ArrayList<Client> list, String _server, int _port) {
		this.allClients = (ArrayList<Client>) list.clone();

		this.server = _server;
		this.port = _port;
		
	}
	@Override
	String Type() {
		return ProtocolMessages.Response.getValue();
	}

	@Override
	String Action() {
		return ProtocolMessages.Overloaded.getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON() {
		JSONObject obj = new JSONObject();
		JSONArray clientList = new JSONArray();
		
		obj.put(Type(), ProtocolMessages.Overloaded.getValue());
		
		JSONObject client = null;
		for(Client currentClient : allClients){
			client = new JSONObject();
			client.put("ip", currentClient.getIpAddress());
			client.put("port", currentClient.getServicePort());
			clientList.add(client);
		}

		
		
		obj.put("clients", clientList);
		
		JSONObject objServer = new JSONObject();
		
		if(this.server!=null){
			objServer.put("ip", this.server);
			objServer.put("port", this.port);
			obj.put("server", objServer);
		}
		return obj.toJSONString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void FromJSON(String _response) {
		JSONObject obj = null;
		
		JSONArray clientList = new JSONArray();
		
		Client newClient = null;
		
		try {
			obj = (JSONObject) parser.parse(_response);
		} catch (org.json.simple.parser.ParseException e) {
			System.err.println("Error: Message is not valid");
		}
			clientList = (JSONArray) (obj.get("clients"));
			
			int i = 0;
			System.out.println(clientList.size());
			for(Object item:clientList){
				newClient = new Client();
				newClient.setIpAddress((String)((JSONObject) item).get("ip"));
				newClient.setServicePort(Integer.parseInt((((JSONObject) item).get("port")).toString())  );
				System.out.println(((JSONObject) item).get("ip").toString());
				System.out.println((((JSONObject) item).get("port")));
				
				this.allClients.add(newClient);
			}
	}

}
