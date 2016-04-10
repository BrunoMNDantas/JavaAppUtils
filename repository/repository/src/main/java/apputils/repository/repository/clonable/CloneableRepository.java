package apputils.repository.repository.clonable;

import apputils.repository.repository.IRepository;
import apputils.repository.utils.RepositoryException;

import java.util.Collection;
import java.util.LinkedList;

public class CloneableRepository<T extends ICloneable<T>,K,F> implements IRepository<T,K,F> {

	private final IRepository<T,K,F> repository;
	
	public CloneableRepository(IRepository<T,K,F> repository) {
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
	public Collection<T> getAll(F filter) throws RepositoryException {
		Collection<T> elems = repository.getAll(filter);

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
