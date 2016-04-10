package apputils.repository.repository;

import java.util.Collection;
import java.util.LinkedList;

import apputils.repository.utils.RepositoryException;

public class NullFreeRepository<T,K> implements IRepository<T,K> {
	
	private final boolean ignoreNullOnGet;
	private final boolean returnEmptyOnGetAll; 
	private final boolean ignoreNullOnInsert; 
	private final boolean ignoreNullOnDelete; 
	private final boolean ignoreNullOnUpdate;
	private final boolean resultOnIgnore;
	private final IRepository<T,K> repository;
	
	
	public NullFreeRepository(	boolean ignoreNullOnGet, boolean returnEmptyOnGetAll, 
								boolean ignoreNullOnInsert, boolean ignoreNullOnDelete, 
								boolean ignoreNullOnUpdate, boolean resultOnIgnore,
								IRepository<T,K> repository) {
		this.ignoreNullOnGet = ignoreNullOnGet;
		this.returnEmptyOnGetAll = returnEmptyOnGetAll;
		this.ignoreNullOnInsert = ignoreNullOnInsert;
		this.ignoreNullOnDelete = ignoreNullOnDelete;
		this.ignoreNullOnUpdate = ignoreNullOnUpdate;
		this.resultOnIgnore = resultOnIgnore;
		this.repository = repository;
	}
	
	public NullFreeRepository(IRepository<T,K> repository) {
		this(true, true, true, true, true, true, repository);
	}

	@Override
	public T get(K key) throws RepositoryException {
		if(ignoreNullOnGet && key == null)
			return null;
		
		return repository.get(key); 
	}

	@Override
	public Collection<T> getAll() throws RepositoryException {
		Collection<T> elems = repository.getAll();
		
		if(returnEmptyOnGetAll && elems == null)
			return new LinkedList<>();
		
		return elems;
	}

	@Override
	public boolean insert(T elem) throws RepositoryException {
		if(ignoreNullOnInsert && elem == null)
			return resultOnIgnore;
		
		return repository.insert(elem);
	}

	@Override
	public boolean delete(T elem) throws RepositoryException {
		if(ignoreNullOnDelete && elem == null)
			return resultOnIgnore;
		
		return repository.delete(elem);
	}

	@Override
	public boolean update(T elem) throws RepositoryException {
		if(ignoreNullOnUpdate && elem == null)
			return resultOnIgnore;
		
		return repository.update(elem);
	}

}
