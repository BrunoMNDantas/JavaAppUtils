package apputils.repository.repository.loggerRepository;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONException;
import org.json.JSONObject;

public class Log implements ILog {

	public static enum MessageType{
		TRACE,
		FATAL,
		ERROR,
		WARNING,
		INFO,
		DEBUG;

		MessageType(){}
		
	}

	public static class Message{
		public final MessageType type;
		public final String tag;
		public final String msg;
		public final Date time;
		public final long threadID;

		public Message(MessageType type, String tag, String msg, Date time, long threadID) {
			this.type = type;
			this.tag = tag;
			this.msg = msg;
			this.time = time;
			this.threadID = threadID;
		}
	}

	private static final long POLL_TIMEOUT = 1000;
	private static final int JSON_IDENTATION = 5;
	public static final String START_JSON_KEY = "start";
	public static final String END_JSON_KEY = "end";
	public static final String MESSAGES_JSON_KEY = "messages";
	public static final String TYPE_JSON_KEY = "type";
	public static final String TAG_JSON_KEY = "tag";
	public static final String MESSAGE_JSON_KEY = "message";
	public static final String TIME_JSON_KEY = "time";
	public static final String THREAD_ID_JSON_KEY = "threadID";
	
	
	private final PrintWriter writer;
	private final BlockingQueue<Message> queue = new LinkedBlockingQueue<>();
	private final AtomicBoolean stop = new AtomicBoolean(false);
	private final CountDownLatch stopped = new CountDownLatch(1);

	
	public Log(Writer writer){
		this.writer = new PrintWriter(new BufferedWriter(writer));
		
		Thread thread = new Thread(()->{
			try{
				init();

				run();

				finish();	
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				stopped.countDown();
			}
		});

		thread.setPriority(Thread.MIN_PRIORITY);
		thread.setDaemon(false);
		thread.start();
	}

	private void init() throws Exception {
		print("{");
		padAndPrint("\"" + START_JSON_KEY + "\" : \"" + new Date() + "\",");
	}

	private void run() throws Exception {
		padAndPrint("\"" + MESSAGES_JSON_KEY + "\" : [");
		
		Message message;
		boolean hasMessages = false;
		while(!stop.get()){
			message = queue.poll(POLL_TIMEOUT, TimeUnit.MILLISECONDS);
			
			if(message != null){
				if(hasMessages)
					padAndPrint(",");
					
				padAndPrint(getMessageData(message));
				hasMessages = true;
			}
		}
		
		List<Message> messages = new LinkedList<>();
		queue.drainTo(messages);
		
		if(!messages.isEmpty()){
			if(hasMessages)
				padAndPrint(",");
			padAndPrint(getMessagesData(messages));
		}
		
		padAndPrint("],");
	}
	
	private void finish() throws Exception {
		padAndPrint("\"" + END_JSON_KEY + "\" : \"" + new Date() + "\"");
		print("}");
		writer.close();
	}

	
	private String getMessagesData(List<Message> messages) throws JSONException {
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<messages.size(); ++i){
			sb.append(getMessageData(messages.get(i)));
			
			if(i==messages.size()-1) // LAST without ','
				sb.append("\n");
			else
				sb.append(",\n");
		}
		
		return sb.toString();
	}

	private String getMessageData(Message message) throws JSONException {
		JSONObject data = new JSONObject();

		data.put(TAG_JSON_KEY, message.tag);
		data.put(TYPE_JSON_KEY, message.type);
		data.put(TIME_JSON_KEY, message.time);
		data.put(MESSAGE_JSON_KEY, message.msg);
		data.put(THREAD_ID_JSON_KEY, message.threadID);

		return data.toString(JSON_IDENTATION);
	}

	private void padAndPrint(String data) throws Exception {
		print(StringUtils.padLeft(data, JSON_IDENTATION));
	}
	
	private void print(String data) throws Exception {
		try{		
			writer.println(data);
		} finally{
			writer.flush();				
		}
	}

	

	@Override
	public void error(String tag, String msg){
		log(MessageType.ERROR, tag, msg);
	}

	@Override
	public void info(String tag, String msg){
		log(MessageType.INFO, tag, msg);
	}

	@Override
	public void warning(String tag, String msg){
		log(MessageType.WARNING, tag, msg);
	}

	@Override
	public void debug(String tag, String msg){
		log(MessageType.DEBUG, tag, msg);
	}
	
	@Override
	public void fatal(String tag, String msg){
		log(MessageType.FATAL, tag, msg);
	}
	
	@Override
	public void trace(String tag, String msg){
		log(MessageType.TRACE, tag, msg);
	}

	@Override
	public void close() throws IOException {
		try {
			stop();	
		} catch (Exception e) {
			throw new IOException("Error stopping log!", e);
		}
	}
	
	
	
	public void log(MessageType type, String tag, String msg){
		queue.add(new Message(type, tag, msg, new Date(), Thread.currentThread().getId()));
	}
	
	public void stop() throws InterruptedException {
		stop.set(true);
		stopped.await();
	}
	
	public void stop(long timeout, TimeUnit unit) throws InterruptedException {
		stop.set(true);
		stopped.await(timeout, unit);
	}

}
