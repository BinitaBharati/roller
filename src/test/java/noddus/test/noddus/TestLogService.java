package noddus.test.noddus;

import static org.junit.Assert.*;

import java.util.concurrent.Callable;
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

import bharati.binita.roller.Person;

public class TestLogService {
	
	private ExecutorService excSrvc;
	private Runnable r1;
	private AtomicInteger atomicInt;
	
	//This will get invoked only once before all test cases are executed.
	@Before
	public void init() {	
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
			Thread.sleep(10*1000);
		}
		
		excSrvc.shutdown();
		 
		excSrvc.awaitTermination(120, TimeUnit.SECONDS);
		Thread.sleep(6*60*1000);
		System.out.println("About to call logCount");
		//int logEntryCount = getLogCount();
		
		
	}
	
	
	private void writeToLog() throws Exception {

    	HttpClient httpClient = new HttpClient();
    	httpClient.setFollowRedirects(false);
    	Request request = httpClient.POST("http://192.168.10.12:7070/hichki/");
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
        System.out.println(res);
        return Integer.parseInt(res);
        //assertTrue( true );
    
	
	}
	
	

}
