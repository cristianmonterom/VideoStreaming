package videostreaming.messaging;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import videostreaming.common.ProtocolMessages;
import videostreaming.Client;

/**
 * @author Cristian
 *
 */
public class StopStreamResponse extends RequestResponse{
	
	private ArrayList<Client> clientList = new ArrayList<Client>();
	private String myIpAddress;
	
	public StopStreamResponse()
	{
		
	}
	
	public StopStreamResponse(ArrayList<Client> list)
	{
		this.clientList = list;
		
		try {
			this.myIpAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	String Type() {
		return ProtocolMessages.Response.getValue();
	}

	@Override
	String Action() {
		return ProtocolMessages.StoppedStream.getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON() {
		JSONObject obj = new JSONObject();
		obj.put(Type(), ProtocolMessages.StoppedStream.getValue());
		return obj.toJSONString();
	}

	@Override
	public void FromJSON(String _response) {
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(_response);
		} catch (org.json.simple.parser.ParseException e) {
			System.err.println("StopStreamResponse: Message is not valid");
		}
	}

}
