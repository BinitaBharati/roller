package bharati.binita.roller;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import bharati.binita.roller.Main;
import bharati.binita.roller.Person;

/**
 * 
 * @author binita.bharati@gmail.com
 * 
 * The goal of the test case is to start the roller web server on the run, send it X amount of POST requests at  `/hichki`;
 *  and then send it a single GET request at `/hichki`. The goal of the GET request is to count the number of times the POST
 *  request has been invoked by desirializing the disk file(s).
 *
 */

public class TestRollingService {
	
	private ExecutorService excSrvc;
	private Runnable r1;
	private AtomicInteger atomicInt;
	private Properties prop;
	
	   //This will get invoked only once before all test cases are executed.
		@Before
		public void init() throws Exception {	
			
			Thread t1 = new Thread(new Runnable() {
				
				public void run() {
					try {
						runProcess("java -cp installation/target/roller-0.0.1-SNAPSHOT.jar bharati.binita.roller.Main &");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			t1.start();
			
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
			
			excSrvc = Executors.newFixedThreadPool(10);
			atomicInt = new AtomicInteger(0);
			r1 = new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					try {
						writeToLog();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			
			
		}

		@Test
		public void test() throws Exception {
			for (int i = 0 ; i < 10 ; i++) {
				excSrvc.submit(r1);
				Thread.sleep(Integer.parseInt(prop.getProperty("file.rollover.timeout.secs"))*1000);
			}
			
			excSrvc.shutdown();
			 
			excSrvc.awaitTermination(120, TimeUnit.SECONDS);
			int logEntryCount = getLogCount();
			assertTrue(logEntryCount == 10);
						
		}
		
		
		private void writeToLog() throws Exception {

	    	HttpClient httpClient = new HttpClient();
	    	httpClient.setFollowRedirects(false);
	    	Request request = httpClient.POST("http://localhost:7070/hichki/");
	    	request.header(HttpHeader.CONTENT_TYPE, "application/json");
	    	int uniqueId = atomicInt.incrementAndGet();
	    	Person person = new Person(uniqueId, "NAME-"+uniqueId);
	    	Gson gson = new Gson();	
	    	request.content(new StringContentProvider(gson.toJson(person)));
	    	httpClient.start();
	    	ContentResponse response = request.send();
			
			
	        String res = new String(response.getContent());
	        System.out.println(res);
	        //assertTrue( true );
	    
		
		}
		
		private int getLogCount() throws Exception {

	    	HttpClient httpClient = new HttpClient();
	    	httpClient.setFollowRedirects(false);
	    	httpClient.start();
	    	ContentResponse response = httpClient.GET("http://localhost:7070/hichki/");
			
	        String res = new String(response.getContent());
	        return Integer.parseInt(res.trim());	    
		
		}
		
		private static void runProcess(String command) throws Exception {
	        Process pro = Runtime.getRuntime().exec(command);
	        printLines(command + " stdout:", pro.getInputStream());
	        printLines(command + " stderr:", pro.getErrorStream());
	        pro.waitFor();
	        System.out.println(command + " exitValue() " + pro.exitValue());
	      }
		
		private static void printLines(String cmd, InputStream ins) throws Exception {
	        String line = null;
	        BufferedReader in = new BufferedReader(
	            new InputStreamReader(ins));
	        while ((line = in.readLine()) != null) {
	            System.out.println(cmd + " " + line);
	        }
	      }

}
