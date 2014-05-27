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
	private String server;
	private int port;
	private ArrayList<Client> allClients = new ArrayList<Client>();
	
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
		
		JSONObject client = new JSONObject();
		for(Client currentClient : allClients){
			client.put("ip", currentClient.getIpAddress());
			client.put("port", currentClient.getServicePort());
			clientList.add(client);
		}
		obj.put("clients", clientList);
		
		JSONObject objServer = new JSONObject();
		objServer.put("ip", this.server);
		objServer.put("port", this.port);
		obj.put("server", objServer);
		return obj.toJSONString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void FromJSON(String _response) {
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(_response);
		} catch (org.json.simple.parser.ParseException e) {
			System.err.println("Error: Message is not valid");
		}
		
		try {
			this.allClients = (ArrayList<Client>) obj.get("clients");
			this.port = (Integer.parseInt(obj.get("port").toString()));
			this.server = (String) obj.get("server");
		} catch (Exception e) {
			System.err.println("Error: Message format is not valid");
		}
	}

}
