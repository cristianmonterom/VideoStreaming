package videostreaming;
// test11

//primer texto de cristian
//verch
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;


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
		
		
		if(!hostname.isEmpty()){
			if(remotePort == 0){
				System.err.println("-rport 'remotePort' must be provided");
				System.exit(1);
			}
		}
		
		if(rate<100){
			System.err.println("Rate too low. Using default -rate 100");
			rate = 100;
		}
	}
	
	public ArgumentParser()
	{
		serverPort = 6262;
		hostname = "";
		rate = 100;
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
