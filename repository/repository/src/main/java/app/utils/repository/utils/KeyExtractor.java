package app.utils.repository.utils;

@FunctionalInterface
public interface KeyExtractor<T,K> {

	public K extract(T t) throws RepositoryException;
	
}
