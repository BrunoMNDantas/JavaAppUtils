package apputils.repository;

import apputils.repository.repository.IRepository;
import apputils.repository.repository.MemoryRepository;
import apputils.repository.repository.validate.IValidtor;
import apputils.repository.repository.validate.ValidateRepository;
import apputils.repository.utils.IKeyExtractor;
import apputils.repository.utils.RepositoryException;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.function.Predicate;


public class VelidateRepositoryTest  extends TestCase {
	
    public VelidateRepositoryTest( String testName ) {
        super( testName );
    }

    public static Test suite() {
        return new TestSuite( VelidateRepositoryTest.class );
    }
    
    private static final IKeyExtractor<Person,String> KEY_EXTRACTOR = (p)->p.name;
    private static final IRepository<Person,String,Predicate<Person>> baseRepository = new MemoryRepository<>(KEY_EXTRACTOR);

    
    public void test() {    	
    	final RepositoryException getEx = new RepositoryException();
    	final RepositoryException getAllEx = new RepositoryException();
		final RepositoryException getAllFilteredEx = new RepositoryException();
    	final RepositoryException insertEx = new RepositoryException();
    	final RepositoryException deleteEx = new RepositoryException();
    	final RepositoryException updateEx = new RepositoryException();
    	
    	IValidtor<String> getValidator = (p) -> { throw getEx; };
    	IValidtor<Void> getAllValidator = (p) -> { throw getAllEx; };
		IValidtor<Void> getAllFilteredValidator = (p) -> { throw getAllFilteredEx; };
    	IValidtor<Person> insertValidator = (p) -> { throw insertEx; };
    	IValidtor<Person> deleteValidator = (p) -> { throw deleteEx; };
    	IValidtor<Person> updateValidator = (p) -> { throw updateEx; };
    	
    	ValidateRepository<Person,String,Predicate<Person>> repository =
				new ValidateRepository<>(	getValidator, getAllValidator,
											getAllFilteredValidator, insertValidator,
											deleteValidator, updateValidator, baseRepository);
    	
    	try {
			repository.get(null);
			Assert.fail();
		} catch (RepositoryException e) {
			Assert.assertEquals(e, getEx);
		}
    	try {
			repository.getAll();
			Assert.fail();
		} catch (RepositoryException e) {
			Assert.assertEquals(e, getAllEx);
		}
		try {
			repository.getAll((p)->true);
			Assert.fail();
		} catch (RepositoryException e) {
			Assert.assertEquals(e, getAllFilteredEx);
		}
    	try {
			repository.insert(null);
			Assert.fail();
		} catch (RepositoryException e) {
			Assert.assertEquals(e, insertEx);
		}
    	try {
			repository.delete(null);
			Assert.fail();
		} catch (RepositoryException e) {
			Assert.assertEquals(e, deleteEx);
		}
    	try {
			repository.update(null);
			Assert.fail();
		} catch (RepositoryException e) {
			Assert.assertEquals(e, updateEx);
		}
    }

    
}
