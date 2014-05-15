package videostreaming.messaging;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import videostreaming.common.CommonFunctions;
import videostreaming.common.ProtocolMessages;
import videostreaming.common.StreamingMode;

public class Messages {

	private static String endMessage = "\n";

	@SuppressWarnings("unchecked")
	public static String statusResponse(StreamingMode _streaming,
			String[] _clients, boolean _isRateLimited, boolean _isHandOver) {
		JSONArray clientList = new JSONArray();
		for (int i = 0; i < _clients.length; i++) {
			clientList.add(_clients[i]);
		}
		JSONObject obj = new JSONObject();
		obj.put("response", ProtocolMessages.Status.getValue());
		obj.put("streaming", _streaming.toString().toLowerCase());
		obj.put("clients", clientList);
		obj.put("ratelimiting", CommonFunctions.convertBoolean(_isRateLimited));
		obj.put("handover", CommonFunctions.convertBoolean(_isHandOver));

		return obj.toJSONString() + endMessage;
	}

	@SuppressWarnings("unchecked")
	public static String startStreamRequest() {
		JSONObject obj = new JSONObject();
		obj.put("request", ProtocolMessages.StartSream.getValue());

		return obj.toJSONString() + endMessage;
	}

	@SuppressWarnings("unchecked")
	public static String stopStreamRequest() {
		JSONObject obj = new JSONObject();
		obj.put("request", ProtocolMessages.StopStream.getValue());

		return obj.toJSONString() + endMessage;
	}

	@SuppressWarnings("unchecked")
	public static String startStreamResponse() {
		JSONObject obj = new JSONObject();
		obj.put("response", ProtocolMessages.StartingStream.getValue());

		return obj.toJSONString() + endMessage;
	}

	@SuppressWarnings("unchecked")
	public static String stopStreamResponse() {
		JSONObject obj = new JSONObject();
		obj.put("response", ProtocolMessages.StoppedStream.getValue());

		return obj.toJSONString() + endMessage;
	}

	@SuppressWarnings("unchecked")
	public static String sendImageResponse(byte[] _image) {
		JSONObject obj = new JSONObject();
		obj.put("response", ProtocolMessages.Image.getValue());
		obj.put("data", _image);

		return obj.toJSONString() + endMessage;
	}

	@SuppressWarnings("unchecked")
	public static String overloadedResponse(
			String[] _clients, String _server, int _port) {
		JSONArray clientList = new JSONArray();
		for (int i = 0; i < _clients.length; i++) {
			clientList.add(_clients[i]);
		}
		JSONObject obj = new JSONObject();
		JSONObject objServer = new JSONObject();
		objServer.put("ip", _server);
		objServer.put("port", _port);
		obj.put("response", ProtocolMessages.Overloaded.getValue());
		obj.put("clients", clientList);
		obj.put("server", objServer);

		return obj.toJSONString() + endMessage;
	}
	

}
