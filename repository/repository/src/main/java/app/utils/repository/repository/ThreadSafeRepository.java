package app.utils.repository.repository;

import java.util.Collection;

import app.utils.repository.utils.RepositoryException;

public class ThreadSafeRepository<T,K> implements IRepository<T,K>{

	protected final IRepository<T,K> repository;
	protected final Object lock = new Object();
	
	public ThreadSafeRepository(IRepository<T,K> repository) {
		this.repository = repository;
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
