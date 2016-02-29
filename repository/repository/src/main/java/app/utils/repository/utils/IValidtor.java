package app.utils.repository.utils;

@FunctionalInterface
public interface IValidtor<T> {
	
	public boolean validate(T elem) throws RepositoryException;

}
