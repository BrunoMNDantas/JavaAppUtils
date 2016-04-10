package apputils.repository.repository;

import apputils.repository.utils.IKeyExtractor;
import apputils.repository.utils.RepositoryException;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MemoryRepository<T,K> implements IRepository<T,K,Predicate<T>> {

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
	public Collection<T> getAll(Predicate<T> predicate) throws RepositoryException {
		if(elems.isEmpty())
			return new LinkedList<>();

		Collection<T> filtered = elems.values().stream().
													filter(predicate).
													collect(Collectors.toCollection(LinkedList::new));

		return filtered;
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
