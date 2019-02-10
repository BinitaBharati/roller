package bharati.binita.roller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;


import com.google.gson.Gson;

/**
 * 
 * @author binita.bharati@gmail.com
 *
 */

public class LogRequestHandler extends HttpServlet{
	
	Logger logger = Logger.getLogger(LogRequestHandler.class);
	
	/**
	 * This Get method will return the number of log items that have been serialized to the log file(s)
	 */
	 @Override
	 protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	            throws ServletException, IOException {
		    int count = 0;
		 	File outputLogDir = new File(Main.prop.getProperty("file.rollover.path"));
			File[] files = outputLogDir.listFiles();
			for (File file : files) {
				logger.info("Reading file --> "+file.getAbsolutePath());
				InputStream fis = new FileInputStream(file);
				int eachFilePersonCount = ProtoHelper.readDelimitedEntriesFromFile(fis);
				logger.info("LogRequestHandler: doGet person count in  --> "+file.getAbsolutePath() + " is "+eachFilePersonCount);
				count = count + eachFilePersonCount;
			}
	        resp.setStatus(HttpStatus.OK_200);
	        resp.getWriter().println(count);
	    }
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BufferedReader br = req.getReader();
		Gson gson = new Gson();
		Person person = gson.fromJson(br, Person.class);
		RollingFileHandler rfh = RollingFileHandler.getInstance();
		boolean status = rfh.receiveIncoming(person);
		if (status) {
			resp.setStatus(HttpStatus.OK_200);
	        resp.getWriter().println("SUCCESS!");
		} else {
			resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
	        resp.getWriter().println("FAILED!");
		}
		
	}

}
