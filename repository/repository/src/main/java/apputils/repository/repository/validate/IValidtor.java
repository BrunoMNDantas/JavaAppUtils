package apputils.repository.repository.validate;

import apputils.repository.utils.RepositoryException;

@FunctionalInterface
public interface IValidtor<T> {
	
	public boolean validate(T elem) throws RepositoryException;

}
