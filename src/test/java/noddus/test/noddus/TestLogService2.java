package noddus.test.noddus;

import static org.junit.Assert.*;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.junit.Test;

public class TestLogService2 {

	@Test
	public void test() throws Exception {
		System.out.println(getLogCount());
	}
	
	private int getLogCount() throws Exception {

    	HttpClient httpClient = new HttpClient();
    	httpClient.setFollowRedirects(false);
    	httpClient.start();
    	ContentResponse response = httpClient.GET("http://192.168.10.12:7070/hichki/");
		
        String res = new String(response.getContent());
        System.out.println(res);
        return Integer.parseInt(res.trim());
        //assertTrue( true );
    
	
	}

}
