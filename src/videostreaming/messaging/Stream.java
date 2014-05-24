package videostreaming.messaging;

import org.json.simple.JSONObject;

import videostreaming.common.ProtocolMessages;

public class Stream extends RequestResponse {
	String imageData;
	boolean lastMessage = true;
	boolean correctMessage = true;

	public Stream(boolean _lastMessage, String strDataToSend) {
		this.lastMessage = _lastMessage;
		this.imageData = strDataToSend;
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
			obj.put("data", this.imageData);
		} else {
			obj.put("data", this.imageData + endMessage);
		}
		return obj.toJSONString();
	}

	@Override
	public void FromJSON(String _response) {
		JSONObject obj = null;

		try {
			obj = (JSONObject) parser.parse(_response);
		} catch (org.json.simple.parser.ParseException e) {
			this.imageData = "";
			this.correctMessage=false;
//			System.err.println("Streame: Message is not valid");
		}

		try {
			this.imageData = (String) obj.get("data");
			if (this.imageData.isEmpty()){
				this.correctMessage=false;
			}
		} catch (Exception e) {
			this.imageData="";
			this.correctMessage=false;
//			System.err.println("Status: Message format is not valid");
		}

	}

	public String getImageData() {
		return imageData;
	}

	public boolean isCorrectMessage() {
		return correctMessage;
	}

	
}
