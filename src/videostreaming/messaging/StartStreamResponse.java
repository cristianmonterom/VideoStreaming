package videostreaming.messaging;

import org.json.simple.JSONObject;

import videostreaming.common.ProtocolMessages;

public class StartStreamResponse extends RequestResponse {

	@Override
	String Type() {
		return ProtocolMessages.Response.getValue();
	}

	@Override
	String Action() {
		return ProtocolMessages.StartingStream.getValue();
	}

	@Override
	public String ToJSON() {
		JSONObject obj = new JSONObject();
		obj.put("response", ProtocolMessages.StartingStream.getValue());
		return obj.toJSONString() + endMessage;
	}

	@Override
	public void FromJSON(String _response) {
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(_response);
		} catch (org.json.simple.parser.ParseException e) {
			System.err.println("StartStreamResponse: Message is not valid");
		}	
	}

}
