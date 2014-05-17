package videostreaming.messaging;

import org.json.simple.JSONObject;

import videostreaming.common.ProtocolMessages;

public class Stream extends RequestResponse {
	String data;
	boolean lastMessage = true;

	public Stream(boolean _lastMessage) {
		this.lastMessage = _lastMessage;
	}

	public Stream() {

	}

	@Override
	String Type() {
		return ProtocolMessages.Response.getValue();
	}

	@Override
	String Action() {
		return ProtocolMessages.Image.getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON() {
		JSONObject obj = new JSONObject();
		obj.put(Type(), Action());
		if (!this.lastMessage) {
			obj.put("data", this.data);
		} else {
			obj.put("data", this.data + endMessage);
		}
		return obj.toJSONString();
	}

	@Override
	public void FromJSON(String _response) {
		JSONObject obj = null;

		try {
			obj = (JSONObject) parser.parse(_response);
		} catch (org.json.simple.parser.ParseException e) {
			System.err.println("StreamResponse: Message is not valid");
		}

		try {
			this.data = (String) obj.get("data");
		} catch (Exception e) {
			System.err.println("StatusResponse: Message format is not valid");
		}

	}

}
