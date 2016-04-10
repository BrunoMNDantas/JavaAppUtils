package apputils.repository.repository;

import apputils.repository.utils.RepositoryException;

import java.util.Collection;

public class ThreadSafeRepository<T,K,F> implements IRepository<T,K,F>{

	protected final IRepository<T,K,F> repository;
	protected final Object lock;
	
	public ThreadSafeRepository(IRepository<T,K,F> repository, Object lock) {
		this.repository = repository;
		this.lock = lock;
	}
	
	public ThreadSafeRepository(IRepository<T,K,F> repository) {
		this(repository, new Object());
	}
	
	@Override
	public T get(K key) throws RepositoryException {
		synchronized (lock) {
			return repository.get(key);
		}
	}

	@Override
	public Collection<T> getAll() throws RepositoryException {
		synchronized (lock) {
			return repository.getAll();
		}
	}

	@Override
	public Collection<T> getAll(F filter) throws RepositoryException {
		synchronized (lock) {
			return repository.getAll(filter);
		}
	}

	@Override
	public boolean insert(T elem) throws RepositoryException {
		synchronized (lock) {
			return repository.insert(elem);
		}
	}

	@Override
	public boolean delete(T elem) throws RepositoryException {
		synchronized (lock) {
			return repository.delete(elem);
		}
	}

	@Override
	public boolean update(T elem) throws RepositoryException {
		synchronized (lock) {
			return repository.update(elem);
		}
	}

}
