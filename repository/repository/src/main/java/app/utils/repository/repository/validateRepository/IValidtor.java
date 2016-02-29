package app.utils.repository.repository.validateRepository;

import app.utils.repository.utils.RepositoryException;

@FunctionalInterface
public interface IValidtor<T> {
	
	public boolean validate(T elem) throws RepositoryException;

}
