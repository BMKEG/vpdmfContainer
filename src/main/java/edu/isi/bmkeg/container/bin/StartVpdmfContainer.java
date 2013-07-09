package edu.isi.bmkeg.container.bin;

import java.io.File;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * Starts a Jetty server containing a given WebApplication with given DB+User+Passwd.
 * 
 */
public class StartVpdmfContainer {

	public static class Options {

		@Option(name = "-port", usage = "Port Number", required = false, metaVar = "PORT")
		public int port = 8080;
		
		@Option(name = "-webapp", usage = "Web Application war or Directory", required = true, metaVar = "PATH")
		public File webapp;
		
		@Option(name = "-context", usage = "Context path", required = true, metaVar = "CONTEXTPATH")
		public String context;
		
		@Option(name = "-l", usage = "Database login", required = true, metaVar = "LOGIN")
		public String login = "";

		@Option(name = "-p", usage = "Database password", required = true, metaVar = "PASSWD")
		public String password = "";

		@Option(name = "-db", usage = "Database name", required = true, metaVar  = "DBNAME")
		public String dbName = "";
	}

	public static void main(String[] args) throws Exception {

		Options options = new Options();
		
		CmdLineParser parser = new CmdLineParser(options);

		try {
			
			parser.parseArgument(args);
			
			if( !options.webapp.exists() ) {
				throw new CmdLineException(parser, options.webapp.getAbsolutePath() + " does not exist.");
			}
		
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			System.err.print("Arguments: ");
			parser.printSingleLineUsage(System.err);
			System.err.println("Starts an Application Container.");
			System.err.println("\n\n Options: \n");
			parser.printUsage(System.err);
			System.exit(-1);
		}

        Server server = new Server(options.port);
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/" + options.context);
        webapp.setWar(options.webapp.getAbsolutePath());
        webapp.setInitParameter("bmkeg.dbUrl", options.dbName);
        webapp.setInitParameter("bmkeg.dbUser", options.login);
        webapp.setInitParameter("bmkeg.dbPassword", options.password);
        server.setHandler(webapp);
        server.start();
        server.join();
	}

}
