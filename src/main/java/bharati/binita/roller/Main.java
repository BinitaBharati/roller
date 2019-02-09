package bharati.binita.roller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * 
 * @author binita.bharati@gmail.com
 *
 */
public class Main {
	
	public  static Properties prop;
	
	public static void main(String[] args) throws Exception {	
		//Init property
		prop = new Properties();
		
		try {
				//Load property file
				InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties");
				prop.load(input);
			
				} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 /**
		    * Clear the existing log files folder -- This is done just to verify test case, this may not be 
		      desired in a ideal situation.
		  */	   
		
		File outputLogDir = new File(prop.getProperty("file.rollover.path"));
		File[] files = outputLogDir.listFiles();
		for (File file : files) {
			file.delete();
		}
		
		//Start server
		startJetty();
		
		//Init the RollingFileHandler
		RollingFileHandler rfh = RollingFileHandler.getInstance();
		rfh.handleIncoming();
				
		
	}
	
	static void startJetty() throws Exception {
		 	
		 	Server server = new Server(7070);
	        ServletContextHandler handler = new ServletContextHandler(server, "/hichki");
	        handler.addServlet(LogRequestHandler.class, "/");
	        server.start();
	 
     }
	
	

}
