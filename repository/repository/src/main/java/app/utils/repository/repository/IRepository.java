package app.utils.repository.repository;

import java.util.Collection;

import app.utils.repository.utils.RepositoryException;

public interface IRepository<T,K> {

	public T get(K key) throws RepositoryException;
	public Collection<T> getAll() throws RepositoryException;
	public boolean insert(T elem) throws RepositoryException;
	public boolean delete(T elem) throws RepositoryException;
	public boolean update(T elem) throws RepositoryException;
	
}
