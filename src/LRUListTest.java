import student.TestCase;

/**
 * @author zshuai8 @author jiaheng1
 * @version 10/23/2016
 *
 */

public class LRUListTest extends TestCase {
    /**
     * the list we will use
     */
    LRUList<byte[]> list;
    
    /**
     * run before every test case
     */
    public void setUp() {
        list = new LRUList<byte[]>(0);
    }
    
    /**
     * test some functions of the linked list
     */
    public void testBasic() {
        assertTrue(list.isEmpty());
        assertEquals(list.size(), 0);
        assertTrue(list.isFull());
        list.toString();
        
        byte[] byte1 = {(byte)0}; 
        byte[] byte2 = {(byte)1}; 
        byte[] byte3 = {(byte)2};
        list.add(byte1);
        assertFalse(list.isEmpty());
        assertEquals(list.size(), 1);
        assertEquals(list.get(0), byte1);
   
        list.add(byte3);
        list.pushToFront(1);
        assertEquals(list.get(0), byte3);
        
        
        list.clear();
        assertTrue(list.isEmpty());
        assertEquals(list.size(), 0);
        
        byte[] byte4 = null;
        try {
            list.add(byte4);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        
        list.clear();
        list.add(byte1);
        list.add(byte2);
        assertEquals(list.get(0), byte2);
        assertEquals(list.get(1), byte1);        
        assertFalse(list.isFull());
        list.toString();
        list.removeEnd();
        assertFalse(list.isEmpty());
        list.removeEnd();
        assertTrue(list.isEmpty());
              
    }
    
    
    
}
