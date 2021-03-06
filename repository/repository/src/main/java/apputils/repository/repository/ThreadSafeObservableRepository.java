package apputils.repository.repository;

import java.util.Collection;
import java.util.function.Consumer;

public class ThreadSafeObservableRepository<T,K,F> extends ThreadSafeRepository<T,K,F>{
	
	private final ObservableRepository<T,K,F> repo;
	
	public ThreadSafeObservableRepository(	Consumer<T> onGet, Consumer<Collection<T>> onGetAll,
											Consumer<Collection<T>> onGetAllFiltered, Consumer<T> onInsert,
											Consumer<T> onDelete, Consumer<T> onUpdate, 
											IRepository<T,K,F> repository, Object lock) {
		super(new ObservableRepository<>(onGet, onGetAll, onGetAllFiltered, onInsert, onDelete, onUpdate, repository), lock);
		this.repo = (ObservableRepository<T,K,F>)super.repository;
	}
	
	public ThreadSafeObservableRepository(IRepository<T,K,F> repository, Object lock){
		super(new ObservableRepository<>(repository), lock);
		this.repo = (ObservableRepository<T,K,F>)super.repository;
	}
	
	public ThreadSafeObservableRepository(	Consumer<T> onGet, Consumer<Collection<T>> onGetAll,
											Consumer<Collection<T>> onGetAllFiltered, Consumer<T> onInsert,
											Consumer<T> onDelete, Consumer<T> onUpdate, 
											IRepository<T,K,F> repository) {
		super(new ObservableRepository<>(onGet, onGetAll, onGetAllFiltered, onInsert, onDelete, onUpdate, repository));
		this.repo = (ObservableRepository<T,K,F>)super.repository;
	}
	
	public ThreadSafeObservableRepository(IRepository<T,K,F> repository){
		super(new ObservableRepository<>(repository));
		this.repo = (ObservableRepository<T,K,F>)super.repository;
	}
	
	
	
	public void registerOnGet(Consumer<T> onGet){
		synchronized (super.lock) {
			repo.registerOnGet(onGet);
		}
	}
	
	public void unregisterOnGet(Consumer<T> onGet){
		synchronized (super.lock) {
			repo.unregisterOnGet(onGet);
		}
	}
	
	public void registerOnGetAll(Consumer<Collection<T>> onGetAll){
		synchronized (super.lock) {
			repo.registerOnGetAll(onGetAll);
		}
	}
	
	public void unregisterOnGetAll(Consumer<Collection<T>> onGetAll){
		synchronized (super.lock) {
			repo.unregisterOnGetAll(onGetAll);
		}
	}

	public void registerOnGetAllFiltered(Consumer<Collection<T>> onGetAllFiltered){
		synchronized (super.lock) {
			repo.registerOnGetAllFiltered(onGetAllFiltered);
		}
	}

	public void unregisterOnGetAllFiltered(Consumer<Collection<T>> onGetAllFiltered){
		synchronized (super.lock) {
			repo.unregisterOnGetAllFiltered(onGetAllFiltered);
		}
	}

	public void registerOnInsert(Consumer<T> onInsert){
		synchronized (super.lock) {
			repo.registerOnInsert(onInsert);
		}
	}
	
	public void unregisterOnInsert(Consumer<T> onInsert){
		synchronized (super.lock) {
			repo.unregisterOnInsert(onInsert);
		}
	}
	
	public void registerOnDelete(Consumer<T> onDelete){
		synchronized (super.lock) {
			repo.registerOnDelete(onDelete);
		}
	}
	
	public void unregisterOnDelete(Consumer<T> onDelete){
		synchronized (super.lock) {
			repo.unregisterOnDelete(onDelete);
		}
	}
	
	public void registerOnUpdate(Consumer<T> onUpdate){
		synchronized (super.lock) {
			repo.registerOnUpdate(onUpdate);
		}
	}
	
	public void unregisterOnUpdate(Consumer<T> onUpdate){
		synchronized (super.lock) {
			repo.unregisterOnUpdate(onUpdate);
		}
	}
	
}
