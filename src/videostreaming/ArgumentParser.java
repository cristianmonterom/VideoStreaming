package videostreaming;

// test11

//primer texto de cristian
//segundo texto de cristian
//verch
// 2nd commit santiago

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import videostreaming.common.CommonFunctions;
import videostreaming.common.Constants;

public class ArgumentParser {
	@Option(name = "-sport", usage = "Sets server port")
	private int serverPort;

	@Option(name = "-remote", usage = "Sets remote IP to connect to")
	private String hostname = null;

	@Option(name = "-rport", usage = "Sets remote port")
	private int remotePort;

	@Option(name = "-rate", usage = "Sets rate")
	private int rate;

	public void Parse(String[] args) {
		CmdLineParser parser = new CmdLineParser(this);

		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.err.println("--------Invalid arguments---------");
			System.exit(1);
		}

		if (hostname.isEmpty()) {
			if (remotePort == 0) {

			} else if (remotePort > 0) {
				System.err.println("-remote 'remoteHost' must be provided");
				System.exit(1);
			} else {
				System.err.println("--------Invalid arguments---------");
				System.exit(1);
			}

		} else {
			if (!CommonFunctions.isPortValid(remotePort)) {
				remotePort = Constants.PORT.getValue();
			}
		}

		if (rate < 100) {
			System.err
					.println("Rate limit is too small. Using default rate 100");
			rate = 100;
		}
	}

	public ArgumentParser() {
		serverPort = Constants.PORT.getValue();
		remotePort = 0;
		hostname = "";
		rate = Constants.RATE.getValue();
	}

	public int getServerPort() {
		if (CommonFunctions.isPortValid(serverPort)) {
			return serverPort;
		} else {
			return Constants.PORT.getValue();
		}
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
