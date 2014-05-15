package videostreaming;

import org.json.simple.JSONObject;


public class StatusResponse extends RequestResponse {
//	private String status;		//
	private String mode;
	private int totalClients;
	private boolean rateLimit;
	private boolean handover;
	private static String action = "status";
	
	@Override
	String Type() {
		return "response";
	}
	
	public StatusResponse()
	{
		
	}
	
	public StatusResponse(boolean mode, int totalClients, boolean rateLimit, boolean handover)
	{
		this.mode = mode?"local":"remote";
		this.totalClients = totalClients;
		this.rateLimit = rateLimit?true:false;
		this.handover = handover?true:false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	String ToJSON() {
		JSONObject obj=new JSONObject();
		obj.put(Type(), StatusResponse.action);
		obj.put("streaming", this.mode);
		obj.put("clients",this.totalClients);
		obj.put("ratelimiting", this.rateLimit);
		obj.put("handover",this.handover);
		
		return obj.toJSONString();
	}

	@Override
	void FromJSON(String jst) {
		JSONObject obj=null;
		
		try {
			obj = (JSONObject) parser.parse(jst);
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		this.mode = (String)obj.get("streaming");
		this.totalClients = (Integer.parseInt(obj.get("clients").toString()));
		this.rateLimit = Boolean.parseBoolean(obj.get("ratelimiting").toString());
		this.handover = Boolean.parseBoolean(obj.get("handover").toString());
	}
		

}
