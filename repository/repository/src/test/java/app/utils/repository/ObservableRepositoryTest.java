package app.utils.repository;

import java.util.Collection;
import java.util.function.Consumer;

import app.utils.repository.repository.MemoryRepository;
import app.utils.repository.repository.ObservableRepository;
import app.utils.repository.utils.KeyExtractor;
import app.utils.repository.utils.RepositoryException;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class ObservableRepositoryTest  extends TestCase {
	
    public ObservableRepositoryTest( String testName ) {
        super( testName );
    }

    public static Test suite() {
        return new TestSuite( ObservableRepositoryTest.class );
    }
    
    
    private static final KeyExtractor<Person, String> KEY_EXTRACTOR = (person)->person.name;
    private static final ObservableRepository<Person,String> repository = new ObservableRepository<>(new MemoryRepository<>(KEY_EXTRACTOR));

    public void test() {
    	final Person person = new Person("Ronaldo", 30);
    	final int getObserver = 0;
    	final int getAllObserver = 1;
    	final int insertObserver = 2;
    	final int deleteObserver = 3;
    	final int updateObserver = 4;
    	final int[] observers = new int[5];
    	
    	Consumer<Person> onGet = (p) -> { 
    		if(p.equals(person))
    			observers[getObserver]++;
    	};
    	Consumer<Collection<Person>> onGetAll = (ps)->{
    		if(ps.size()==1)
    			observers[getAllObserver]++;
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
    	
    	register(onGet, onGetAll, onInsert, onDelete, onUpdate);
    	
    	try {
    		useRepository(person);
			
    		unregister(onGet, onGetAll, onInsert, onDelete, onUpdate);
        	
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
		repository.update(person);
		repository.delete(person);
	}

	private void unregister(Consumer<Person> onGet, Consumer<Collection<Person>> onGetAll, Consumer<Person> onInsert,
			Consumer<Person> onDelete, Consumer<Person> onUpdate) {
		repository.unregisterOnGet(onGet);
		repository.unregisterOnGetAll(onGetAll);
		repository.unregisterOnInsert(onInsert);
		repository.unregisterOnDelete(onDelete);
		repository.unregisterOnUpdate(onUpdate);
	}

	private void register(Consumer<Person> onGet, Consumer<Collection<Person>> onGetAll, Consumer<Person> onInsert,
			Consumer<Person> onDelete, Consumer<Person> onUpdate) {
		repository.registerOnGet(onGet);
    	repository.registerOnGetAll(onGetAll);
    	repository.registerOnInsert(onInsert);
    	repository.registerOnDelete(onDelete);
    	repository.registerOnUpdate(onUpdate);
	}
    
}
