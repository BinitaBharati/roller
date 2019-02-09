package bharati.binita.roller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author binita.bharati@gmail.com
 * This timer will continually set a volatile flushTimerReady to true.
 * flushTimerReady is used by RollingFileHandler thread to decide when flush
 * has to be done. 
 *
 */

public class PeriodicFlushTimer {
	
	private volatile boolean flushTimerReady;
	private ScheduledExecutorService ses;
	public static PeriodicFlushTimer instance;
	
	public static PeriodicFlushTimer getIntance() {
		if (instance != null) {
			return instance;
		}
		instance = new PeriodicFlushTimer();
		return instance;
	}
	
	private PeriodicFlushTimer() {
		
		ses = Executors.newScheduledThreadPool(1);
		Runnable r1 = new Runnable() {
			
			public void run() {
				flushTimerReady = true;
				
				
			}
		};
		ses.scheduleAtFixedRate(r1, 0, 
				Integer.parseInt(Main.prop.getProperty("file.rollover.timeout.secs")), TimeUnit.SECONDS);
		
	}

	public boolean isFlushTimerReady() {
		return flushTimerReady;
	}

	public void setFlushTimerReady(boolean flushTimerReady) {
		this.flushTimerReady = flushTimerReady;
	}
	
	
	
	

}
