package apputils.repository.repository.loggerRepository;

import java.io.Closeable;

public interface ILog extends Closeable{

	public void error(String tag, String msg);
	public void info(String tag, String msg);
	public void warning(String tag, String msg);
	public void debug(String tag, String msg);
	public void fatal(String tag, String msg);
	public void trace(String tag, String msg);
	
}
