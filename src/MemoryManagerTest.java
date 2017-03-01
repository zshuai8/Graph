import student.TestCase;

/**
 * 
 * @author zshuai8, @author jiaheng1
 * @version 9/10/2016
 *
 */
public class MemoryManagerTest extends TestCase {
    
    private MemoryManager pool;
    
    /**
     * set up the bufferPool and memory manager for test
     */
    public void setUp() {
        BufferPool bufferP = new BufferPool("output.txt", 5, 32);
        pool = new MemoryManager(32, bufferP);
    }
    
    /**
     * test simple insert
     */
    public void testInsert() {
        
        String input = "123";
        byte[] space = input.getBytes();
        int size = space.length;
        Handle spaceHandle =  pool.insert(space, size);
        assertEquals("123", new String(pool.get(spaceHandle)));
        assertEquals(pool.retrieveSizeTable()[0], 0);
    }
    
    /**
     * test simple remove
     */
    public void testRemove() {
        
        String input = "12345678";
        byte[] space = input.getBytes();
        int size = space.length;
        Handle spaceHandle =  pool.insert(space, size);
        pool.remove(spaceHandle);
        assertEquals("12345678", new String(pool.get(spaceHandle)));
    }


    /**
     * test insert large size byte array
     */

    public void testInsertLarge() {
        
        long input = 0xfffffff;
        byte[] space = new byte[(int)input];
        int size = space.length;
        assertNull(pool.insert(space, size));
        
    }
}