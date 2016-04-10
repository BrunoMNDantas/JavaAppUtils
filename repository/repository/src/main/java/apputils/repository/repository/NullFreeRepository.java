package apputils.repository.repository;

import apputils.repository.utils.RepositoryException;

import java.util.Collection;
import java.util.LinkedList;

public class NullFreeRepository<T,K,F> implements IRepository<T,K,F> {
	
	private final boolean ignoreNullOnGet;
	private final boolean returnEmptyOnGetAll;
	private final boolean returnEmptyOnGetAllFiltered;
	private final boolean ignoreNullOnInsert; 
	private final boolean ignoreNullOnDelete; 
	private final boolean ignoreNullOnUpdate;
	private final boolean resultOnIgnore;
	private final IRepository<T,K,F> repository;
	
	
	public NullFreeRepository(	boolean ignoreNullOnGet,
								boolean returnEmptyOnGetAll, boolean returnEmptyOnGetAllFiltered,
								boolean ignoreNullOnInsert, boolean ignoreNullOnDelete, 
								boolean ignoreNullOnUpdate, boolean resultOnIgnore,
								IRepository<T,K,F> repository) {
		this.ignoreNullOnGet = ignoreNullOnGet;
		this.returnEmptyOnGetAll = returnEmptyOnGetAll;
		this.returnEmptyOnGetAllFiltered = returnEmptyOnGetAllFiltered;
		this.ignoreNullOnInsert = ignoreNullOnInsert;
		this.ignoreNullOnDelete = ignoreNullOnDelete;
		this.ignoreNullOnUpdate = ignoreNullOnUpdate;
		this.resultOnIgnore = resultOnIgnore;
		this.repository = repository;
	}
	
	public NullFreeRepository(IRepository<T,K,F> repository) {
		this(true, true, true, true, true, true, true, repository);
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
	public Collection<T> getAll(F filter) throws RepositoryException {
		Collection<T> elems = repository.getAll(filter);

		if(returnEmptyOnGetAllFiltered && elems == null)
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
