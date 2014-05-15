package videostreaming;
// test11

//primer texto de cristian
//segundo texto de cristian
//verch
// 2nd commit santiago

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import videostreaming.common.*;


public class ArgumentParser {
	@Option(name="-sport",usage="Sets server port")
	private int serverPort;
	
	@Option(name="-remote",usage="Sets remote IP to connect to")
	private String hostname = null;
	
	@Option(name="-rport",usage="Sets remote port")
	private int remotePort;
	
	@Option(name="-rate",usage="Sets rate")
	private int rate;
	
	
	
	public void Parse(String[] args){
		CmdLineParser parser = new CmdLineParser(this);
	       
		try{
			parser.parseArgument(args);
	    }catch(CmdLineException e){
	    	System.err.println("--------Invalid arguments---------");
	    	System.exit(1);
	    }
		
		
		if( remotePort >= 0){
//			if( hostname.isEmpty() ){
//				System.err.println("-remote 'remoteHost' must be provided");
//				System.exit(1);
//			}
			//gcomo
		}
		
		if(rate<100){
			System.err.println("Rate limit is too small. Using default rate 100");
			rate = 100;
		}
	}
	
	public ArgumentParser()
	{
		serverPort = Constants.PORT.getValue();
		remotePort = Constants.PORT.getValue();
		hostname = "";
		rate = Constants.RATE.getValue();
	}

	public int getServerPort() {
		return serverPort;
	}

	public String getHostname() {
		return hostname;
	}

	public int getRemotePort() {
		return remotePort;
	}

	public int getRate() {
		return rate;
	}
	
	
}
