package apputils.repository.repository.clonableRepository;

import java.util.Collection;
import java.util.LinkedList;

import apputils.repository.repository.IRepository;
import apputils.repository.utils.RepositoryException;

public class CloneableRepository<T extends ICloneable<T>,K> implements IRepository<T,K> {

	private final IRepository<T,K> repository;
	
	public CloneableRepository(IRepository<T,K> repository) {
		this.repository = repository;
	}
	
	@Override
	public T get(K key) throws RepositoryException {
		T elem = repository.get(key);
		try {
			return elem == null ? elem : elem.clone();
		} catch (CloneNotSupportedException e) {
			throw new RepositoryException("Error cloning elem[" + elem + "]", e);
		}
	}

	@Override
	public Collection<T> getAll() throws RepositoryException {
		Collection<T> elems = repository.getAll();
		
		if(elems == null)
			return elems;
		
		Collection<T> elemsClone = new LinkedList<>();
		for(T elem : elems){
			try {
				elemsClone.add(elem == null ? elem : elem.clone());
			} catch (CloneNotSupportedException e) {
				throw new RepositoryException("Error cloning elem[" + elem + "]", e);
			}
		}
		
		return elemsClone;
	}

	@Override
	public boolean insert(T elem) throws RepositoryException {
		try {
			return repository.insert(elem == null? elem : elem.clone());
		} catch (CloneNotSupportedException e) {
			throw new RepositoryException("Error cloning elem[" + elem + "]", e);
		}
	}

	@Override
	public boolean delete(T elem) throws RepositoryException {
		try {
			return repository.delete(elem == null ? elem : elem.clone());
		} catch (CloneNotSupportedException e) {
			throw new RepositoryException("Error cloning elem[" + elem + "]", e);
		}
	}

	@Override
	public boolean update(T elem) throws RepositoryException {
		try {
			return repository.update(elem == null ? elem : elem.clone());
		} catch (CloneNotSupportedException e) {
			throw new RepositoryException("Error cloning elem[" + elem + "]", e);
		}
	}

}
