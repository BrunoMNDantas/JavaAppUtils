package apputils.repository.repository.validate;

import apputils.repository.repository.IRepository;
import apputils.repository.utils.RepositoryException;

import java.util.Collection;

public class ValidateRepository<T,K,F> implements IRepository<T,K,F>{

	private final IRepository<T,K,F> repository;
	private final IValidtor<K> getValidator;
	private final IValidtor<Void> getAllValidator;
	private final IValidtor<Void> getAllFilteredValidator;
	private final IValidtor<T> insertValidator;
	private final IValidtor<T> deleteValidator;
	private final IValidtor<T> updateValidator;
	
	
	public ValidateRepository(	IValidtor<K> getValidator,
								IValidtor<Void> getAllValidator, IValidtor<Void> getAllFilteredValidator,
								IValidtor<T> insertValidator, IValidtor<T> deleteValidator,
								IValidtor<T> updateValidator, IRepository<T,K,F> repository) {
		this.repository = repository;
		this.getValidator = getValidator;
		this.getAllValidator = getAllValidator;
		this.getAllFilteredValidator = getAllFilteredValidator;
		this.insertValidator = insertValidator;
		this.deleteValidator = deleteValidator;
		this.updateValidator = updateValidator;
	}
	
	@Override
	public T get(K key) throws RepositoryException {
		if(getValidator != null && !getValidator.validate(key))
			throw new RepositoryException("Invalid get operation for key[" + key + "]");
		
		return repository.get(key);
	}

	@Override
	public Collection<T> getAll() throws RepositoryException {
		if(getAllValidator != null && !getAllValidator.validate(null))
			throw new RepositoryException("Invalid getAll operation");
		
		return repository.getAll();
	}

	@Override
	public Collection<T> getAll(F filter) throws RepositoryException {
		if(getAllFilteredValidator != null && !getAllFilteredValidator.validate(null))
			throw new RepositoryException("Invalid getAllFiltered operation");

		return repository.getAll(filter);
	}

	@Override
	public boolean insert(T elem) throws RepositoryException {
		if(insertValidator != null && !insertValidator.validate(elem))
			throw new RepositoryException("Invalid insert operation for elem[" + elem + "]");
		
		return repository.insert(elem);
	}

	@Override
	public boolean delete(T elem) throws RepositoryException {
		if(deleteValidator != null && !deleteValidator.validate(elem))
			throw new RepositoryException("Invalid delete operation for elem[" + elem + "]");
		
		return repository.delete(elem);
	}

	@Override
	public boolean update(T elem) throws RepositoryException {
		if(updateValidator != null && !updateValidator.validate(elem))
			throw new RepositoryException("Invalid update operation for elem[" + elem + "]");
		
		return repository.update(elem);
	}

}
