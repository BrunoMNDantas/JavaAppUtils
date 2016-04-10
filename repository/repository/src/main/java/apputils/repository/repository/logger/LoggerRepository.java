package apputils.repository.repository.logger;

import apputils.repository.repository.IRepository;
import apputils.repository.utils.RepositoryException;
import apputils.repository.utils.Utils;

import java.util.Collection;
import java.util.Date;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class LoggerRepository<T,K,F> implements IRepository<T,K,F> {

	@FunctionalInterface
	private static interface ExceptionSupplier<T>{
		public T get() throws RepositoryException;
	}
	
	private static final String TAG = "LoggerRepository";
	

	
	private final IRepository<T,K,F> repository;
	private final ILog log;
	
	
	
	public LoggerRepository(IRepository<T,K,F> repository, ILog log) {
		this.repository = repository;
		this.log = log;
	}
	
	
	
	@Override
	public T get(K key) throws RepositoryException {
		return execute(
			() -> repository.get(key), 
			(elem) -> "get(K key) returned [" + elem + "]", 
			(ex) -> "get(K key) thrown Exception : " + Utils.getStackTrace(ex));
	}
	
	@Override
	public Collection<T> getAll() throws RepositoryException {
		return execute(
			() -> repository.getAll(), 
			(elems) -> "getAll() returned [" + elems + "]", 
			(ex) -> "getAll() thrown Exception : " + Utils.getStackTrace(ex));
	}

	@Override
	public Collection<T> getAll(F filter) throws RepositoryException {
		return execute(
				() -> repository.getAll(filter),
				(elems) -> "getAll(F filter) returned [" + elems + "]",
				(ex) -> "getAll(F filter) thrown Exception : " + Utils.getStackTrace(ex));
	}

	@Override
	public boolean insert(T elem) throws RepositoryException {
		return execute(
			() -> repository.insert(elem), 
			(res) -> "insert(T elem) inserted [" + elem + "] and returned [" + res + "]", 
			(ex) -> "insert(T elem) thrown Exception : " + Utils.getStackTrace(ex));
	}

	@Override
	public boolean delete(T elem) throws RepositoryException {
		return execute(
			() -> repository.delete(elem), 
			(res) -> "delete(T elem) deleted [" + elem + "] and returned [" + res + "]", 
			(ex) -> "delete(T elem) thrown Exception : " + Utils.getStackTrace(ex));
	}

	@Override
	public boolean update(T elem) throws RepositoryException {
		return execute(
			() -> repository.update(elem), 
			(res) -> "update(T elem) updated [" + elem + "] and returned [" + res + "]", 
			(ex) -> "update(T elem) thrown Exception : " + Utils.getStackTrace(ex));
	}
	
	
	
	
	private <J> J execute(	ExceptionSupplier<J> action, Function<J,String> onSucess, 
							Function<Exception,String> onFail) throws RepositoryException {
		Date entered = new Date();
		BiConsumer<String, String> logger = null;
		String msg = null;
		
		try{
			J res = action.get();
			
			logger = log::trace;
			msg = onSucess.apply(res);
			
			return res;
		}catch(RepositoryException | RuntimeException ex){
			logger = log::error;
			msg += onFail.apply(ex);
			throw ex;
		} finally{
			Date exited = new Date();
			log(entered, exited, logger, msg);
		}
	}
	
	private void log(Date entered, Date exited, BiConsumer<String, String> logger, String msg) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Entered : ").append(entered.toString()).append("\n");
		sb.append(msg).append("\n");
		sb.append("Exited : ").append(exited.toString());
		
		logger.accept(TAG, sb.toString());
	}
	
}
