package bharati.binita.roller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

/**
 * 
 * @author binita.bharati@gmail.com This class holds a LinkedBlockingQueue where
 *         concurrent incoming request dump their data. This class uses a single
 *         thread to read the LinkedBlockingQueue for further processing.
 *
 */

public class RollingFileHandler {

	static final Logger logger = Logger.getLogger(RollingFileHandler.class);

	private LinkedBlockingQueue<Person> personQ;
	private FileOutputStream fos;
	private int fileRollOverTimeOutSecs;
	private boolean rollEverEmptyFile;

	private static RollingFileHandler instance;
	private PeriodicFlushTimer periodicFlushTimer;

	private long lastUpdatedTimeSecs;
	private boolean fileHasData;

	public static RollingFileHandler getInstance() {
		if (instance == null) {
			instance = new RollingFileHandler();
		}
		return instance;
	}

	private RollingFileHandler() {
		try {
			File file = new File(
					Main.prop.getProperty("file.rollover.path") + Main.prop.getProperty("file.rollover.name"));
			file.createNewFile();

			fos = new FileOutputStream(file);
			fileRollOverTimeOutSecs = Integer.parseInt(Main.prop.getProperty("file.rollover.timeout.secs"));
			rollEverEmptyFile = Boolean.parseBoolean(Main.prop.getProperty("file.rollover.empty"));

			periodicFlushTimer = PeriodicFlushTimer.getIntance();

			personQ = new LinkedBlockingQueue<Person>(
					Integer.parseInt(Main.prop.getProperty("max.concurrent.request")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method will only be called by a single thread.
	 */
	public synchronized void handleIncoming() {
		List<Person> tmpList = new ArrayList<Person>();
		while (true) {		
			try {				
				if (periodicFlushTimer.isFlushTimerReady()) {
					if (rollEverEmptyFile || fileHasData) {
						flushFile();
						fileHasData = false;
						periodicFlushTimer.setFlushTimerReady(false);
					}
				}
				Person myPerson = personQ.poll();
				if (myPerson == null) {
					Thread.sleep(100);
				} else {
					ProtoHelper.writeDelimitedToFile(fos, myPerson);
					if (!fileHasData) {
						fileHasData = true;
					}
				}				
			}
			 catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void flushFile() throws Exception {
		fos.close();
		File existingFile = new File(
				Main.prop.getProperty("file.rollover.path") + Main.prop.getProperty("file.rollover.name"));
		String updatedFileName = Main.prop.getProperty("file.rollover.name") + "_" + System.currentTimeMillis() / 1000;
		existingFile.renameTo(new File(Main.prop.getProperty("file.rollover.path") + updatedFileName));
		fos = new FileOutputStream(
				new File(Main.prop.getProperty("file.rollover.path") + Main.prop.getProperty("file.rollover.name")));
	}

	/**
	 * 
	 * @param person This method will be invoked concurrently by multiple request
	 *               threads
	 */
	public boolean receiveIncoming(Person person) {
		Thread t1 = Thread.currentThread();
		logger.info(t1.getName() + " --> About to put " + person + " to queue");	
		try {
			//The below call will throw Exception if LinkedBlockingQueue capacity is exceeded.
			//Catch that and inform the client about the failure of the operation
			personQ.add(person);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		logger.info(t1.getName() + " --> Finished putting " + person + " to queue");
		return true;
	}

}
