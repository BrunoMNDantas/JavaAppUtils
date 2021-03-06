package apputils.repository.repository.cache;

import apputils.repository.repository.IRepository;
import apputils.repository.utils.IKeyExtractor;
import apputils.repository.utils.RepositoryException;

import java.util.Collection;

public class CacheRepository<T,K,F> implements IRepository<T,K,F>{
	
	private final Cache<T,K> cache = new Cache<>();
	private final IRepository<T,K,F> repository;
	private final IKeyExtractor<T,K> keyExtractor; 
	private boolean allLoaded = false;
	
	
	public CacheRepository(IRepository<T,K,F> repository, IKeyExtractor<T,K> keyExtractor) {
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
	public Collection<T> getAll(F filter) throws RepositoryException {
		Collection<T> elems = repository.getAll(filter);

		if(elems != null){
			K key;
			for(T elem : elems){
				key = keyExtractor.extract(elem);
				if(cache.get(key) == null)
					cache.add(key, elem);
			}
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
