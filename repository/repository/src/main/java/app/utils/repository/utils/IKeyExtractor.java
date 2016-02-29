package app.utils.repository.utils;

@FunctionalInterface
public interface IKeyExtractor<T,K> {

	public K extract(T t) throws RepositoryException;
	
}
