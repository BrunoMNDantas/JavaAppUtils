package app.utils.repository;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import app.utils.repository.repository.MemoryRepository;
import app.utils.repository.repository.ThreadSafeRepository;
import app.utils.repository.utils.KeyExtractor;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class ThreadSafeRepositoryTest  extends TestCase {
	
    public ThreadSafeRepositoryTest( String testName ) {
        super( testName );
    }

    public static Test suite() {
        return new TestSuite( ThreadSafeRepositoryTest.class );
    }
    
    
    private static final KeyExtractor<Person, String> KEY_EXTRACTOR = (person)->person.name;
    private static final ThreadSafeRepository<Person,String> repository = new ThreadSafeRepository<>(new MemoryRepository<>(KEY_EXTRACTOR));

    private final int nThreads = 16;
	private final int insertPerThread = 512;
    
    public void test() {
    	runInsert();
    	runGet();
    	runGetAll();
    	runUpdate();
    	runDelete();
    }

    private void runUpdate() {
		CountDownLatch start = new CountDownLatch(1);
    	CountDownLatch end = new CountDownLatch(nThreads);
    	AtomicInteger success = new AtomicInteger(0);
    	
    	for(int i=0; i<nThreads; ++i) 
    		runUpdateThread(start, end, i, success);
    	
    	try {
    		start.countDown();
    		end.await();
    		assertEquals(nThreads * insertPerThread, success.get());	
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	private void runUpdateThread(CountDownLatch start, CountDownLatch end, int id, AtomicInteger success) {
		new Thread(()->{
			int count = 0;
			int found = 0;
			Person p;
			try {
				start.await();
				
				while(count++<insertPerThread){
					p = repository.get(id + "-" + count);
					p.age = 0;
					repository.update(p);
					p = repository.get(id + "-" + count);
					if(p!=null && p.age==0)
						++found;
				}
				
			} catch(Exception e) {
			} finally {
				while(found-->0)
					success.incrementAndGet();
				end.countDown();
			}
		}).start();
	}
    
    private void runDelete() {
		CountDownLatch start = new CountDownLatch(1);
    	CountDownLatch end = new CountDownLatch(nThreads);
    	
    	for(int i=0; i<nThreads; ++i) 
    		runDeleteThread(start, end, i);
    	
    	try {
    		start.countDown();
    		end.await();
    		assertEquals(0, repository.getAll().size());	
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	private void runDeleteThread(CountDownLatch start, CountDownLatch end, int id) {
		new Thread(()->{
			Person p;
			int count = 0;
			try {
				start.await();
				
				while(count++<insertPerThread){
					p = repository.get(id + "-" + count);
					repository.delete(p);
				}
			} catch(Exception e) {
			} finally {
				end.countDown();
			}
		}).start();
	}
    
    private void runGetAll() {
		CountDownLatch start = new CountDownLatch(1);
    	CountDownLatch end = new CountDownLatch(nThreads);
    	AtomicInteger success = new AtomicInteger(0);
    	
    	for(int i=0; i<nThreads; ++i) 
    		runGetAllThread(start, end, i, success);
    	
    	try {
    		start.countDown();
    		end.await();
    		assertEquals(nThreads, success.get());	
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	private void runGetAllThread(CountDownLatch start, CountDownLatch end, int id, AtomicInteger success) {
		new Thread(()->{
			int count = 0;
			int total = nThreads * insertPerThread;
			boolean fail = false;
			try {
				start.await();
				
				while(count++<insertPerThread){
					if(repository.getAll().size()!=total){
						fail = true;
						break;
					}
				}
			} catch(Exception e) {
				fail = true;
			} finally {
				if(!fail)
					success.incrementAndGet();
				end.countDown();
			}
		}).start();
	}
    
    private void runGet() {
		CountDownLatch start = new CountDownLatch(1);
    	CountDownLatch end = new CountDownLatch(nThreads);
    	AtomicInteger success = new AtomicInteger(0);
    	
    	for(int i=0; i<nThreads; ++i) 
    		runGetThread(start, end, i, success);
    	
    	try {
    		start.countDown();
    		end.await();
    		assertEquals(nThreads*insertPerThread, success.get());	
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	private void runGetThread(CountDownLatch start, CountDownLatch end, int id, AtomicInteger success) {
		new Thread(()->{
			int count = 0;
			int found = 0;
			Person p;
			try {
				start.await();
				
				while(count++<insertPerThread){
					p = repository.get(id + "-" + count);
					if(p!=null && p.age==id)
						++found;
				}
				
			} catch(Exception e) {
			} finally {
				while(found-->0)
					success.incrementAndGet();
				end.countDown();
			}
		}).start();
	}

	private void runInsert() {
		CountDownLatch start = new CountDownLatch(1);
    	CountDownLatch end = new CountDownLatch(nThreads);
    	
    	for(int i=0; i<nThreads; ++i) 
    		runInsertThread(start, end, i);
    	
    	try {
    		start.countDown();
    		end.await();
    		assertEquals(nThreads*insertPerThread, repository.getAll().size());	
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	private void runInsertThread(CountDownLatch start, CountDownLatch end, int id) {
		new Thread(()->{
			int count = 0;
			try {
				start.await();
				
				while(count++<insertPerThread)
					repository.insert(new Person(id+"-"+count, id));
				
			} catch(Exception e) {
			} finally {
				end.countDown();
			}
		}).start();
	}
    
}
