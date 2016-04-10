package apputils.repository.repository.validateRepository;

import apputils.repository.utils.RepositoryException;

@FunctionalInterface
public interface IValidtor<T> {
	
	public boolean validate(T elem) throws RepositoryException;

}
