/**
 * 
 */
package videostreaming.messaging;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import videostreaming.common.ProtocolMessages;

/**
 * @author Cristian
 *
 */
public class OverloadResponse extends RequestResponse {

	private String[] clients;
	private String server;
	private int port;
	
	public OverloadResponse (String[] _clients, String _server, int _port) {
		this.clients = _clients;
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
		JSONArray clientList = new JSONArray();
		for (int i = 0; i < this.clients.length; i++) {
			clientList.add(this.clients[i]);
		}
		JSONObject obj = new JSONObject();
		JSONObject objServer = new JSONObject();
		objServer.put("ip", this.server);
		objServer.put("port", this.port);
		obj.put(Type(), ProtocolMessages.Overloaded.getValue());
		obj.put("clients", clientList);
		obj.put("server", objServer);

		return obj.toJSONString() + endMessage;
	}

	@Override
	public void FromJSON(String _response) {
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(_response);
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
			System.exit(-1);
		}		
	}

}
