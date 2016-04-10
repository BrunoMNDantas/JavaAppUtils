package apputils.repository.repository;

import apputils.repository.utils.RepositoryException;

import java.util.Collection;

public interface IRepository<T,K,F> {

	public T get(K key) throws RepositoryException;
	public Collection<T> getAll() throws RepositoryException;
	public Collection<T> getAll(F filter) throws RepositoryException;
	public boolean insert(T elem) throws RepositoryException;
	public boolean delete(T elem) throws RepositoryException;
	public boolean update(T elem) throws RepositoryException;
	
}
