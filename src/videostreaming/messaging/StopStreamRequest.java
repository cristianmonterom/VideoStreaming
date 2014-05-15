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
public class StopStreamRequest extends RequestResponse {

	@Override
	String Type() {
		return ProtocolMessages.Request.getValue();
	}

	@Override
	String Action() {
		return ProtocolMessages.StopStream.getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON() {
		JSONObject obj = new JSONObject();
		obj.put("request", ProtocolMessages.StopStream.getValue());

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
