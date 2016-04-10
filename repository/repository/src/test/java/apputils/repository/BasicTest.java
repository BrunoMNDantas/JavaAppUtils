package apputils.repository;

import apputils.repository.repository.*;
import apputils.repository.repository.cache.CacheRepository;
import apputils.repository.repository.clonable.CloneableRepository;
import apputils.repository.repository.logger.ILog;
import apputils.repository.repository.logger.Log;
import apputils.repository.repository.logger.LoggerRepository;
import apputils.repository.repository.validate.ValidateRepository;
import apputils.repository.utils.IKeyExtractor;
import apputils.repository.utils.RepositoryException;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Predicate;


public class BasicTest  extends TestCase {
	
    public BasicTest( String testName ) {
        super( testName );
    }

    public static Test suite() {
        return new TestSuite( BasicTest.class );
    }
    
    
    private static final IKeyExtractor<Person, String> KEY_EXTRACTOR = (person)->person.name;
	private static final Predicate<Person> FILTER = (person)->person.age < 5;

    public void testBasic() {
    	try {
			test(new MemoryRepository<>(KEY_EXTRACTOR));
			test(new CacheRepository<>( new MemoryRepository<>(KEY_EXTRACTOR), KEY_EXTRACTOR));
			test(new NullFreeRepository<>( new MemoryRepository<>(KEY_EXTRACTOR)));
			test(new ObservableRepository<>( new MemoryRepository<>(KEY_EXTRACTOR)));
			test(new ThreadSafeRepository<>( new MemoryRepository<>(KEY_EXTRACTOR)));
			test(new CloneableRepository<>( new MemoryRepository<>(KEY_EXTRACTOR)));
			test(new ValidateRepository<>(null, null, null, null, null, null, ( new MemoryRepository<>(KEY_EXTRACTOR))));
			
			try(ILog log =  new Log(new FileWriter("D:/Desktop/Log.json"))) {
				test(new LoggerRepository<>(new MemoryRepository<>(KEY_EXTRACTOR),log));	
			}
		} catch (RepositoryException | IOException e) {
			Assert.fail(e.getMessage());
		}
    }
    
    public void test(IRepository<Person,String,Predicate<Person>> repository) throws RepositoryException{
		Person p = new Person("Ronaldo", 30);
		
		Assert.assertNull(repository.get("Ronaldo"));
		Assert.assertTrue(repository.getAll().isEmpty());
		Assert.assertTrue(repository.getAll(FILTER).isEmpty());

		repository.insert(new Person("Anne", 3));
		repository.insert(new Person("John", 3));
		repository.insert(new Person("Mary", 30));
		Assert.assertTrue(repository.getAll(FILTER).size()==2);
		repository.delete(repository.get("Anne"));
		repository.delete(repository.get("John"));
		repository.delete(repository.get("Mary"));

		repository.insert(p);
		Assert.assertTrue(repository.get("Ronaldo").equals(p));
		Assert.assertTrue(repository.getAll().size()==1);
		
		repository.delete(p);
		Assert.assertNull(repository.get("Ronaldo"));
		Assert.assertTrue(repository.getAll().isEmpty());			
		
		repository.insert(p);
		p.age = 99;
		repository.update(p);
		Assert.assertTrue(repository.get("Ronaldo").equals(p));
	}
    
}
