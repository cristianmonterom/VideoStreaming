package videostreaming.messaging;

import org.json.simple.JSONObject;
import videostreaming.common.ProtocolMessages;

/**
 * @author Cristian
 *
 */
public class StopStreamResponse extends RequestResponse{
	
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
