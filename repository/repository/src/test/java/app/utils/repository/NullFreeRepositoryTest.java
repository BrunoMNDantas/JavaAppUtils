package app.utils.repository;

import java.util.Collection;
import java.util.function.Consumer;

import app.utils.repository.repository.IRepository;
import app.utils.repository.repository.NullFreeRepository;
import app.utils.repository.repository.ObservableRepository;
import app.utils.repository.utils.RepositoryException;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class NullFreeRepositoryTest  extends TestCase {
	
    public NullFreeRepositoryTest( String testName ) {
        super( testName );
    }

    public static Test suite() {
        return new TestSuite( NullFreeRepositoryTest.class );
    }
    
    private static final ObservableRepository<Person,String> baseRepository = new ObservableRepository<>(new IRepository<Person, String>() {
    	@Override
    	public Person get(String key) throws RepositoryException { return null; }
    	@Override
    	public Collection<Person> getAll() throws RepositoryException { return null; }
    	@Override
    	public boolean insert(Person elem) throws RepositoryException { return true; }
    	@Override
    	public boolean delete(Person elem) throws RepositoryException { return true; }
    	@Override
    	public boolean update(Person elem) throws RepositoryException { return true; }
	});

    public void test() {
    	final int get = 0;
    	final int insert = 1;
    	final int delete = 2;
    	final int update = 3;
    	final boolean[] called = new boolean[4];
    	
    	registerObservers(get, insert, delete, update, called);
    	
    	NullFreeRepository<Person,String> repository = new NullFreeRepository<>(baseRepository);
    	
    	try {
    		repository.get(null);
    		repository.insert(null);
    		repository.delete(null);
    		repository.update(null);
    		
    		Assert.assertFalse(called[get]);
    		Assert.assertFalse(called[insert]);
    		Assert.assertFalse(called[delete]);
    		Assert.assertFalse(called[update]);
    		Assert.assertEquals(repository.getAll().size(), 0);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
    	
    	repository = new NullFreeRepository<>(false, false, false, false, false, false, baseRepository);
    	
    	try {
    		repository.get(null);
    		repository.insert(null);
    		repository.delete(null);
    		repository.update(null);
    		
    		Assert.assertTrue(called[get]);
    		Assert.assertTrue(called[insert]);
    		Assert.assertTrue(called[delete]);
    		Assert.assertTrue(called[update]);
    		Assert.assertEquals(repository.getAll(), null);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
    }

	private void registerObservers(final int get, final int insert, final int delete, final int update, final boolean[] called) {
		Consumer<Person> onGet = (p)->{called[get] = !called[get];};
    	Consumer<Person> onInsert = (p)->{called[insert] = !called[insert];};
    	Consumer<Person> onDelete = (p)->{called[delete] = !called[delete];};
    	Consumer<Person> onUpdate = (p)->{called[update] = !called[update];};
    	
    	baseRepository.registerOnGet(onGet);
    	baseRepository.registerOnInsert(onInsert);
    	baseRepository.registerOnDelete(onDelete);
    	baseRepository.registerOnUpdate(onUpdate);
	}
    
}
