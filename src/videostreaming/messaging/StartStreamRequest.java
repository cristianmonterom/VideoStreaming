/**
 * 
 */
package videostreaming.messaging;

import org.json.simple.JSONObject;

import videostreaming.common.ProtocolMessages;

/**
 * @author Cristian
 *
 */
public class StartStreamRequest extends RequestResponse {

	private int servicePortInfo;
	
	public StartStreamRequest(){}
	
	public StartStreamRequest(int servicePort) {
		this.servicePortInfo = servicePort;
		
	}

	@Override
	String Type() {
		return ProtocolMessages.Request.getValue();
	}

	@Override
	String Action() {
		return ProtocolMessages.StartSream.getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON() {
		JSONObject obj = new JSONObject();
		obj.put(Type(), ProtocolMessages.StartSream.getValue());
		obj.put("sport", this.servicePortInfo);
		return obj.toJSONString();
	}

	@Override
	public void FromJSON(String _response) {
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(_response);
		} catch (org.json.simple.parser.ParseException e) {
			System.err.println("StartStreamRequest: Message is not valid");
		}
		
		this.servicePortInfo = (Integer.parseInt(obj.get("sport").toString()));
	}

	public int getServicePortInfo() {
		return servicePortInfo;
	}
}
