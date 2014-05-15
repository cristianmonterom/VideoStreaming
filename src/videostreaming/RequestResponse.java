package videostreaming;

import org.json.simple.parser.JSONParser;


public abstract class RequestResponse {
	protected static final JSONParser parser = new JSONParser();
	
	abstract String Type();
	
	abstract String ToJSON();

	abstract void FromJSON(String jst);

}
