package apputils.repository;

import apputils.repository.repository.MemoryRepository;
import apputils.repository.repository.clonableRepository.CloneableRepository;
import apputils.repository.utils.IKeyExtractor;
import apputils.repository.utils.RepositoryException;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class CloneableRepositoryTest  extends TestCase {
	
    public CloneableRepositoryTest( String testName ) {
        super( testName );
    }

    public static Test suite() {
        return new TestSuite( CloneableRepositoryTest.class );
    }
    
    private static final IKeyExtractor<Person,String> KEY_EXTRACTOR = (p)->p.name;
    private static final CloneableRepository<Person,String> repository = new CloneableRepository<>(new MemoryRepository<>(KEY_EXTRACTOR));
    

    public void test() {
    	try {
			Person p = new Person("Ronaldo", 30);
			repository.insert(p);
			Person obtained = repository.get("Ronaldo");
			
			Assert.assertNotNull(obtained);
			Assert.assertTrue(p != obtained);
			
		} catch (RepositoryException e) {
			Assert.fail(e.getMessage());
		}
    }

}
