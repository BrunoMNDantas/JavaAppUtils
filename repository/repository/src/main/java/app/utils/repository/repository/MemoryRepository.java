package app.utils.repository.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import app.utils.repository.utils.IKeyExtractor;
import app.utils.repository.utils.RepositoryException;

public class MemoryRepository<T,K> implements IRepository<T,K> {

	private final IKeyExtractor<T,K> keyExtractor;
	private final Map<K,T> elems = new HashMap<>();
	
	public MemoryRepository(IKeyExtractor<T,K> keyExtractor) {
		this.keyExtractor = keyExtractor;
	}
	
	
	@Override
	public T get(K key) throws RepositoryException {
		return elems.get(key);
	}
	
	@Override
	public Collection<T> getAll() throws RepositoryException {
		return elems.isEmpty() ? new LinkedList<>() : elems.values();
	}

	@Override
	public boolean insert(T elem) throws RepositoryException {
		K key = keyExtractor.extract(elem);
		
		if(elems.containsKey(key))
			throw new RepositoryException("Elem with key[" + key + "] already exists!");
		
		elems.put(key, elem);
		return true;
	}

	@Override
	public boolean delete(T elem) throws RepositoryException {
		K key = keyExtractor.extract(elem);
		elems.remove(key);
		return true;
	}

	@Override
	public boolean update(T elem) throws RepositoryException {
		K key = keyExtractor.extract(elem);
		
		if(!elems.containsKey(key))
			throw new RepositoryException("Inexistente element[" + key + "]");
		
		elems.remove(key);
		elems.put(key, elem);
		return true;
	}

}
