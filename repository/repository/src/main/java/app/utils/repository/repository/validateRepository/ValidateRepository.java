package app.utils.repository.repository.validateRepository;

import java.util.Collection;

import app.utils.repository.repository.IRepository;
import app.utils.repository.utils.RepositoryException;

public class ValidateRepository<T,K> implements IRepository<T,K>{

	private final IRepository<T,K> repository;
	private final IValidtor<K> getValidator;
	private final IValidtor<Void> getAllValidator;
	private final IValidtor<T> insertValidator;
	private final IValidtor<T> deleteValidator;
	private final IValidtor<T> updateValidator;
	
	
	public ValidateRepository(	IValidtor<K> getValidator, IValidtor<Void> getAllValidator,
								IValidtor<T> insertValidator, IValidtor<T> deleteValidator,
								IValidtor<T> updateValidator, IRepository<T,K> repository) {
		this.repository = repository;
		this.getValidator = getValidator;
		this.getAllValidator = getAllValidator;
		this.insertValidator = insertValidator;
		this.deleteValidator = deleteValidator;
		this.updateValidator = updateValidator;
	}
	
	@Override
	public T get(K key) throws RepositoryException {
		if(getValidator != null)
			getValidator.validate(key);
		
		return repository.get(key);
	}

	@Override
	public Collection<T> getAll() throws RepositoryException {
		if(getAllValidator != null)
			getAllValidator.validate(null);
		
		return repository.getAll();
	}

	@Override
	public boolean insert(T elem) throws RepositoryException {
		if(insertValidator != null)
			insertValidator.validate(elem);
		
		return repository.insert(elem);
	}

	@Override
	public boolean delete(T elem) throws RepositoryException {
		if(deleteValidator != null)
			deleteValidator.validate(elem);
		
		return repository.delete(elem);
	}

	@Override
	public boolean update(T elem) throws RepositoryException {
		if(updateValidator != null)
			updateValidator.validate(elem);
		
		return repository.update(elem);
	}

}
