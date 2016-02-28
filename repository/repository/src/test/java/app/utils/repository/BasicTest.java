package app.utils.repository;

import java.io.FileWriter;

import app.utils.log.ILog;
import app.utils.log.Log;
import app.utils.repository.repository.CacheRepository;
import app.utils.repository.repository.IRepository;
import app.utils.repository.repository.LoggerRepository;
import app.utils.repository.repository.MemoryRepository;
import app.utils.repository.repository.NullFreeRepository;
import app.utils.repository.repository.ObservableRepository;
import app.utils.repository.repository.ThreadSafeRepository;
import app.utils.repository.utils.KeyExtractor;
import app.utils.repository.utils.RepositoryException;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class BasicTest  extends TestCase {
	
    public BasicTest( String testName ) {
        super( testName );
    }

    public static Test suite() {
        return new TestSuite( BasicTest.class );
    }
    
    
    private static final KeyExtractor<Person, String> KEY_EXTRACTOR = (person)->person.name;

    public void testBasic() {
    	try {
			test(new MemoryRepository<>(KEY_EXTRACTOR));
			test(new CacheRepository<>( new MemoryRepository<>(KEY_EXTRACTOR), KEY_EXTRACTOR));
			test(new NullFreeRepository<>( new MemoryRepository<>(KEY_EXTRACTOR)));
			test(new ObservableRepository<>( new MemoryRepository<>(KEY_EXTRACTOR)));
			test(new ThreadSafeRepository<>( new MemoryRepository<>(KEY_EXTRACTOR)));
			
			try(ILog log =  new Log(new FileWriter("D:/Desktop/Log.json"))) {
				test(new LoggerRepository<>(new MemoryRepository<>(KEY_EXTRACTOR),log));	
			}
		} catch (Throwable e) {
			Assert.fail(e.getMessage());
		}
    }
    
    public void test(IRepository<Person,String> repository) throws RepositoryException{
		Person p = new Person("Ronaldo", 30);
		
		Assert.assertNull(repository.get("Ronaldo"));
		Assert.assertTrue(repository.getAll().isEmpty());
		
		repository.insert(p);
		Assert.assertEquals(repository.get("Ronaldo"), p);
		Assert.assertTrue(repository.getAll().size()==1);
		
		repository.delete(p);
		Assert.assertNull(repository.get("Ronaldo"));
		Assert.assertTrue(repository.getAll().isEmpty());			
		
		repository.insert(p);
		p.age = 99;
		repository.update(p);
		Assert.assertEquals(repository.get("Ronaldo"), p);
	}
    
}
