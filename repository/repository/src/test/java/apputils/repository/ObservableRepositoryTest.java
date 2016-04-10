package apputils.repository;

import apputils.repository.repository.MemoryRepository;
import apputils.repository.repository.ObservableRepository;
import apputils.repository.utils.IKeyExtractor;
import apputils.repository.utils.RepositoryException;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;


public class ObservableRepositoryTest  extends TestCase {
	
    public ObservableRepositoryTest( String testName ) {
        super( testName );
    }

    public static Test suite() {
        return new TestSuite( ObservableRepositoryTest.class );
    }
    
    
    private static final IKeyExtractor<Person, String> KEY_EXTRACTOR = (person)->person.name;
    private static final ObservableRepository<Person,String,Predicate<Person>> repository = new ObservableRepository<>(new MemoryRepository<>(KEY_EXTRACTOR));

    public void test() {
    	final Person person = new Person("Ronaldo", 30);
    	final int getObserver = 0;
    	final int getAllObserver = 1;
		final int getAllFilteredObserver = 2;
    	final int insertObserver = 3;
    	final int deleteObserver = 4;
    	final int updateObserver = 5;
    	final int[] observers = new int[6];
    	
    	Consumer<Person> onGet = (p) -> { 
    		if(p.equals(person))
    			observers[getObserver]++;
    	};
    	Consumer<Collection<Person>> onGetAll = (ps)->{
    		if(ps.size()==1)
    			observers[getAllObserver]++;
    	};
		Consumer<Collection<Person>> onGetAllFiltered = (ps)->{
			if(ps.size()==1)
				observers[getAllFilteredObserver]++;
		};
    	Consumer<Person> onInsert = (p)->{
    		if(p.equals(person))
    			observers[insertObserver]++;
    	};
    	Consumer<Person> onDelete = (p)->{
    		if(p.equals(person))
    			observers[deleteObserver]++;
    	};
    	Consumer<Person> onUpdate = (p)->{
    		if(p.equals(person))
    			observers[updateObserver]++;
    	};
    	
    	register(onGet, onGetAll, onGetAllFiltered, onInsert, onDelete, onUpdate);
    	
    	try {
    		useRepository(person);
			
    		unregister(onGet, onGetAll, onGetAllFiltered, onInsert, onDelete, onUpdate);
        	
        	useRepository(person);
		} catch (RepositoryException e) {
			Assert.fail(e.getMessage());
		}
    	
    	for(int i=0; i<observers.length; ++i)
    		Assert.assertEquals(observers[1], 1);
    }

	private void useRepository(final Person person) throws RepositoryException {
		repository.insert(person);
		repository.get("Ronaldo");
		repository.getAll();
		repository.getAll((p)->p.age==30);
		repository.update(person);
		repository.delete(person);
	}

	private void unregister(Consumer<Person> onGet, Consumer<Collection<Person>> onGetAll,
							Consumer<Collection<Person>> onGetAllFiltered, Consumer<Person> onInsert,
							Consumer<Person> onDelete, Consumer<Person> onUpdate) {
		repository.unregisterOnGet(onGet);
		repository.unregisterOnGetAll(onGetAll);
		repository.unregisterOnGetAllFiltered(onGetAllFiltered);
		repository.unregisterOnInsert(onInsert);
		repository.unregisterOnDelete(onDelete);
		repository.unregisterOnUpdate(onUpdate);
	}

	private void register(	Consumer<Person> onGet, Consumer<Collection<Person>> onGetAll,
							Consumer<Collection<Person>> onGetAllFiltered, Consumer<Person> onInsert,
							Consumer<Person> onDelete, Consumer<Person> onUpdate) {
		repository.registerOnGet(onGet);
    	repository.registerOnGetAll(onGetAll);
		repository.registerOnGetAllFiltered(onGetAllFiltered);
    	repository.registerOnInsert(onInsert);
    	repository.registerOnDelete(onDelete);
    	repository.registerOnUpdate(onUpdate);
	}
    
}
