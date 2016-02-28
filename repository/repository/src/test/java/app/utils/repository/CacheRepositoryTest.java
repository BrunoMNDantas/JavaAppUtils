package app.utils.repository;

import java.util.Collection;
import java.util.function.Consumer;

import app.utils.repository.repository.CacheRepository;
import app.utils.repository.repository.IRepository;
import app.utils.repository.repository.MemoryRepository;
import app.utils.repository.repository.ObservableRepository;
import app.utils.repository.utils.KeyExtractor;
import app.utils.repository.utils.RepositoryException;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class CacheRepositoryTest  extends TestCase {
	
    public CacheRepositoryTest( String testName ) {
        super( testName );
    }

    public static Test suite() {
        return new TestSuite( CacheRepositoryTest.class );
    }
    
    private static final KeyExtractor<Person,String> KEY_EXTRACTOR = (p)->p.name;
    private static final IRepository<Person,String> baseRepository = new MemoryRepository<>(KEY_EXTRACTOR);
    private static final ObservableRepository<Person,String> observedRepository = new ObservableRepository<>(baseRepository);
    

    public void test() {
    	final int get = 0;
    	final int getAll = 1;
    	final int insert = 2;
    	final int delete = 3;
    	final int update = 4;
    	final boolean[] called = new boolean[5];
    	
    	registerObservers(get, getAll, insert, delete, update, called);
    	
    	CacheRepository<Person,String> repository = new CacheRepository<>(observedRepository, KEY_EXTRACTOR);
    	
    	try {
    		Person p = new Person("Ronaldo", 30);
    		baseRepository.insert(p);

    		Person aux = new Person("Aux", 30);
    		repository.get("Ronaldo");
    		repository.getAll();
    		repository.insert(aux);
    		repository.update(aux);
    		repository.delete(aux);
    		
    		
    		Assert.assertTrue(called[get]);
    		Assert.assertTrue(called[getAll]);
    		Assert.assertTrue(called[insert]);
    		Assert.assertTrue(called[delete]);
    		Assert.assertTrue(called[update]);
    		
    		for(int i=0; i<called.length; ++i)
    			called[i] = false;
    		
    		repository.get("Ronaldo");
    		repository.getAll();
    		Assert.assertFalse(called[get]);
    		Assert.assertFalse(called[getAll]);
    		
    		for(int i=0; i<called.length; ++i)
    			called[i] = false;
    		
    		repository.insert(aux);
    		repository.get("Aux");
    		repository.getAll();
    		Assert.assertFalse(called[get]);
    		Assert.assertFalse(called[getAll]);
    		Assert.assertTrue(called[insert]);
    		
		} catch (RepositoryException e) {
			Assert.fail(e.getMessage());
		}
    }

	private void registerObservers(	final int get, final int getAll, final int insert, 
									final int delete, final int update, final boolean[] called) {
		Consumer<Person> onGet = (p)->{called[get] = !called[get];};
		Consumer<Collection<Person>> onGetAll = (ps)->{called[getAll] = !called[getAll];};
    	Consumer<Person> onInsert = (p)->{called[insert] = !called[insert];};
    	Consumer<Person> onDelete = (p)->{called[delete] = !called[delete];};
    	Consumer<Person> onUpdate = (p)->{called[update] = !called[update];};
    	
    	observedRepository.registerOnGet(onGet);
    	observedRepository.registerOnGetAll(onGetAll);
    	observedRepository.registerOnInsert(onInsert);
    	observedRepository.registerOnDelete(onDelete);
    	observedRepository.registerOnUpdate(onUpdate);
	}
    
}
