package app.utils.repository.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Cache<T, K> {
	
	private Map<K, T> cache = new HashMap<>();
	
	
	public T get(K key){
		return (cache.containsKey(key))? cache.get(key) : null;		
	}
	
	public Collection<T> getAll() {
		 return cache.isEmpty()? new LinkedList<>() : cache.values();
	}
	
	public void remove(K key){
		if(cache.containsKey(key))
			cache.remove(key);
	}
	
	public void add(K key, T value){
		cache.put(key, value);
	}
	
	public void clear(){
		cache = new HashMap<>();
	}

}