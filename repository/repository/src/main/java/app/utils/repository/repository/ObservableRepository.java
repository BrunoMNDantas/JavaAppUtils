package app.utils.repository.repository;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

import app.utils.repository.utils.RepositoryException;

public class ObservableRepository<T,K> implements IRepository<T,K>{
	
	private final Collection<Consumer<T>> onGet;
	private final Collection<Consumer<Collection<T>>> onGetAll;
	private final Collection<Consumer<T>> onInsert;
	private final Collection<Consumer<T>> onDelete;
	private final Collection<Consumer<T>> onUpdate;
	private final IRepository<T,K> repository;
	
	
	
	public ObservableRepository(Consumer<T> onGet, Consumer<Collection<T>> onGetAll, Consumer<T> onInsert, 
								Consumer<T> onDelete, Consumer<T> onUpdate, 
								IRepository<T,K> repository) {
		this.onGet = new LinkedList<>();
		this.onGetAll = new LinkedList<>();
		this.onInsert = new LinkedList<>();
		this.onDelete = new LinkedList<>();
		this.onUpdate = new LinkedList<>();
		this.repository = repository;
		
		if(onGet != null)
			this.onGet.add(onGet);
		
		if(onGetAll != null)
			this.onGetAll.add(onGetAll);
		
		if(onInsert != null)
			this.onInsert.add(onInsert);
		
		if(onDelete != null)
			this.onDelete.add(onDelete);
		
		if(onUpdate != null)
			this.onUpdate.add(onUpdate);
	}
	
	public ObservableRepository(IRepository<T,K> repository) {
		this(null, null, null, null, null, repository);
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
