package app.utils.repository.repository;

import java.util.Collection;

import app.utils.repository.utils.Cache;
import app.utils.repository.utils.KeyExtractor;
import app.utils.repository.utils.RepositoryException;

public class CacheRepository<T,K> implements IRepository<T,K>{
	
	private final Cache<T,K> cache = new Cache<>();
	private final IRepository<T,K> repository;
	private final KeyExtractor<T,K> keyExtractor; 
	private boolean allLoaded = false;
	
	
	public CacheRepository(IRepository<T,K> repository, KeyExtractor<T,K> keyExtractor) {
		this.repository = repository;
		this.keyExtractor = keyExtractor;
	}
	

	@Override
	public T get(K key) throws RepositoryException{
		T elem;
		
		if((elem=cache.get(key)) == null){
			elem = repository.get(key);
			
			if(elem != null)
				cache.add(key, elem);
		}
		
		return elem;
	}
	
	@Override
	public Collection<T> getAll() throws RepositoryException {
		Collection<T> elems;
		
		if(allLoaded) {
			elems = cache.getAll();
		} else {
			elems = repository.getAll();
			
			K key;
			for(T elem : elems){
				key = keyExtractor.extract(elem);
				if(cache.get(key) == null)
					cache.add(key, elem);
			}	
			
			allLoaded = true;
		}
		
		return elems;
	}

	@Override
	public boolean insert(T elem) throws RepositoryException{
		if(!repository.insert(elem))
			return false;
		
		K key = keyExtractor.extract(elem);
		cache.add(key, elem);
		return true;
	}

	@Override
	public boolean delete(T elem) throws RepositoryException {
		if(!repository.delete(elem))
			return false;
		
		K key = keyExtractor.extract(elem);
		cache.remove(key);
		return true;
	}

	@Override
	public boolean update(T elem) throws RepositoryException{
		if(!repository.update(elem))
			return false;
		
		K key = keyExtractor.extract(elem);
		cache.remove(key);
		cache.add(key, elem);
		return true;
	}

}
