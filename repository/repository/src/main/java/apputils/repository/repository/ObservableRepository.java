package apputils.repository.repository;

import apputils.repository.utils.RepositoryException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

public class ObservableRepository<T,K,F> implements IRepository<T,K,F>{
	
	private final Collection<Consumer<T>> onGet;
	private final Collection<Consumer<Collection<T>>> onGetAll;
	private final Collection<Consumer<Collection<T>>> onGetAllFiltered;
	private final Collection<Consumer<T>> onInsert;
	private final Collection<Consumer<T>> onDelete;
	private final Collection<Consumer<T>> onUpdate;
	private final IRepository<T,K,F> repository;
	
	
	
	public ObservableRepository(Consumer<T> onGet, Consumer<Collection<T>> onGetAll,
								Consumer<Collection<T>> onGetAllFiltered, Consumer<T> onInsert,
								Consumer<T> onDelete, Consumer<T> onUpdate, 
								IRepository<T,K,F> repository) {
		this.onGet = new LinkedList<>();
		this.onGetAll = new LinkedList<>();
		this.onGetAllFiltered = new LinkedList<>();
		this.onInsert = new LinkedList<>();
		this.onDelete = new LinkedList<>();
		this.onUpdate = new LinkedList<>();
		this.repository = repository;
		
		if(onGet != null)
			this.onGet.add(onGet);
		
		if(onGetAll != null)
			this.onGetAll.add(onGetAll);

		if(onGetAllFiltered != null)
			this.onGetAllFiltered.add(onGetAllFiltered);

		if(onInsert != null)
			this.onInsert.add(onInsert);
		
		if(onDelete != null)
			this.onDelete.add(onDelete);
		
		if(onUpdate != null)
			this.onUpdate.add(onUpdate);
	}
	
	public ObservableRepository(IRepository<T,K,F> repository) {
		this(null, null, null, null, null, null, repository);
	}
	
	
	
	@Override
	public T get(K key) throws RepositoryException {
		T elem = repository.get(key);
		
		for(Consumer<T> action : onGet)
			action.accept(elem);
		
		return elem;
	}

	@Override
	public Collection<T> getAll() throws RepositoryException {
		Collection<T> elems = repository.getAll();
		
		for(Consumer<Collection<T>> action : onGetAll)
			action.accept(elems);
	
		return elems;
	}

	@Override
	public Collection<T> getAll(F filter) throws RepositoryException {
		Collection<T> elems = repository.getAll(filter);

		for(Consumer<Collection<T>> action : onGetAllFiltered)
			action.accept(elems);

		return elems;
	}
	
	@Override
	public boolean insert(T elem) throws RepositoryException {
		if(!repository.insert(elem))
			return false;
		
		for(Consumer<T> action : onInsert)
			action.accept(elem);
		
		return true;
	}

	@Override
	public boolean delete(T elem) throws RepositoryException {
		if(!repository.delete(elem))
			return false;
		
		for(Consumer<T> action : onDelete)
			action.accept(elem);
		
		return true;
	}

	@Override
	public boolean update(T elem) throws RepositoryException {
		if(!repository.update(elem))
			return false;
		
		for(Consumer<T> action : onUpdate)
			action.accept(elem);
		
		return true;
	}

	
	
	public void registerOnGet(Consumer<T> onGet){
		if(onGet != null)
			this.onGet.add(onGet);
	}
	
	public void unregisterOnGet(Consumer<T> onGet){
		if(onGet != null)
			this.onGet.remove(onGet);
	}
	
	public void registerOnGetAll(Consumer<Collection<T>> onGetAll){
		if(onGetAll != null)
			this.onGetAll.add(onGetAll);
	}
	
	public void unregisterOnGetAll(Consumer<Collection<T>> onGetAll){
		if(onGetAll != null)
			this.onGetAll.remove(onGetAll);
	}

	public void registerOnGetAllFiltered(Consumer<Collection<T>> onGetAllFiltered){
		if(onGetAllFiltered != null)
			this.onGetAllFiltered.add(onGetAllFiltered);
	}

	public void unregisterOnGetAllFiltered(Consumer<Collection<T>> onGetAllFiltered){
		if(onGetAllFiltered != null)
			this.onGetAllFiltered.remove(onGetAllFiltered);
	}

	public void registerOnInsert(Consumer<T> onInsert){
		if(onInsert != null)
			this.onInsert.add(onInsert);
	}
	
	public void unregisterOnInsert(Consumer<T> onInsert){
		if(onInsert != null)
			this.onInsert.remove(onInsert);
	}
	
	public void registerOnDelete(Consumer<T> onDelete){
		if(onDelete != null)
			this.onDelete.add(onDelete);
	}
	
	public void unregisterOnDelete(Consumer<T> onDelete){
		if(onDelete != null)
			this.onDelete.remove(onDelete);
	}
	
	public void registerOnUpdate(Consumer<T> onUpdate){
		if(onUpdate != null)
			this.onUpdate.add(onUpdate);
	}
	
	public void unregisterOnUpdate(Consumer<T> onUpdate){
		if(onUpdate != null)
			this.onUpdate.remove(onUpdate);
	}
	
}
