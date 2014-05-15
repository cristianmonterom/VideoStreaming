package videostreaming.messaging;

import org.json.simple.JSONObject;

import videostreaming.common.CommonFunctions;

public class StatusResponse extends RequestResponse {
	private String streaming;
	private int totalClients;
	private String rateLimit;
	private String handover;
	private static String action = "status";

	@Override
	String Type() {
		return "response";
	}

	@Override
	String Action() {
		return this.action;
	}

	public StatusResponse() {

	}

	public StatusResponse(boolean mode, int totalClients, boolean rateLimit,
			boolean handover) {
		this.streaming = mode ? "local" : "remote";
		this.totalClients = totalClients;
		this.rateLimit = CommonFunctions.convertBoolean(rateLimit);
		this.handover = CommonFunctions.convertBoolean(handover);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON() {
		JSONObject obj = new JSONObject();
		obj.put(Type(), this.action);
		obj.put("streaming", this.streaming);
		obj.put("clients", this.totalClients);
		obj.put("ratelimiting", this.rateLimit);
		obj.put("handover", this.handover);

		return obj.toJSONString();
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
//		this.streaming = (String) obj.get("streaming");
//		this.totalClients = (Integer.parseInt(obj.get("clients").toString()));
//		this.rateLimit = Boolean.parseBoolean(obj.get("ratelimiting")
//				.toString());
//		this.handover = Boolean.parseBoolean(obj.get("handover").toString());
	}

}
