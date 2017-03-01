import student.TestCase;

// -------------------------------------------------------------------------
/**
 *  Test the hash function 
 *
 *  @author CS3114 staff
 *  @version Aug 27, 2016
 */
public class HashTest extends TestCase {

    /**
     * Test the hash function
     * test insertion, expansion and removal
     */
    public void testInsertionRemoval() {
        
        Hash hasher = new Hash();
        assertEquals(10, hasher.getSize());
        
        Hash myHash = new Hash(10);
        BufferPool bufferP = new BufferPool("output.txt", 5, 32);
        MemoryManager pool = new MemoryManager(32, bufferP);
        myHash.getPool(pool);
        String input = "Hellow World!";
       
        assertEquals(myHash.remove(input), -1);
        assertNotNull(myHash.insert(input, "", 0));
        assertNull(myHash.insert(input, "", 0));
        assertNotSame(myHash.remove(input), -1);
        
        String num1 = "1";
        String num2 = "2";
        String num3 = "3";
        String num4 = "4";
        String num5 = "5";

        assertNotNull(myHash.insert(num1, "", 0));
        assertNotNull(myHash.remove(num1));
        assertNotNull(myHash.insert(num2, "", 0));
        assertNotNull(myHash.remove(num2));
        assertNotNull(myHash.insert(num3, "", 0));
        assertNotNull(myHash.remove(num3));
        assertNotNull(myHash.insert(num4, "", 0));
        assertNotNull(myHash.remove(num4));
        assertNotNull(myHash.insert(num5, "", 0));
        assertNotNull(myHash.remove(num5));

        
        myHash.insert(input, "", 0);
        assertEquals(1, myHash.getElements());
        assertEquals(10, myHash.getSize());
        
        assertNotNull(myHash.insert(num1, "", 0));
        assertNotNull(myHash.insert(num2, "", 0));
        assertNotNull(myHash.insert(num3, "", 0));
        assertNotNull(myHash.insert(num4, "", 0));
        assertNotNull(myHash.insert(num5, "", 0));
        assertEquals(6, myHash.getElements());
        assertEquals(20, myHash.getSize());
        
        assertNotSame(myHash.remove(num3), -1);
        assertEquals(myHash.remove(num3), -1);
        assertEquals(5, myHash.getElements());
        
        assertNotNull(myHash.insert("11", "", 0));
        
        myHash.dump();
        assertEquals(0, myHash.getElements());
        
    } 
    
    /**
     * test if the hashtable deals with tombstone correctly
     */
    public void testTomeStone() {

        Hash hasher = new Hash(10);
        BufferPool bufferP = new BufferPool("output.txt", 5, 32);
        MemoryManager pool = new MemoryManager(32, bufferP);
        hasher.getPool(pool);
        
        //they can be hashed into the same slot
        assertNotNull(hasher.insert("5", "", 0));
        assertNotNull(hasher.insert("11", "", 0));
        assertNotNull(hasher.insert("112", "", 0));
        
        //to see if the hashtable skip tombstones to find duplicate objects
        assertNotSame(hasher.remove("5"), -1);
        assertNotSame(hasher.remove("11"), -1);
        assertNull(hasher.insert("112", "", 0));
        
        assertNotNull(hasher.insert("5", "", 0));
        assertNotNull(hasher.insert("11", "", 0));
        assertNull(hasher.insert("112", "", 0));
        assertNotSame(hasher.remove("11"), -1);
        assertNotNull(hasher.insert("117", "", 0));
        assertNotSame(hasher.remove("112"), -1);
        assertNotNull(hasher.insert("11", "", 0));
        
        assertNotNull(hasher.insert("221", "", 0));
        assertNotNull(hasher.insert("222", "", 0));
        assertNotNull(hasher.insert("223", "", 0));
        assertNotNull(hasher.insert("224", "", 0));
        assertNotNull(hasher.insert("225", "", 0));
        assertNotNull(hasher.insert("226", "", 0));
        assertNotNull(hasher.insert("227", "", 0));
        assertNotNull(hasher.insert("228", "", 0));
        assertNotNull(hasher.insert("229", "", 0));
        assertNotNull(hasher.insert("230", "", 0)); 
        assertNotNull(hasher.insert("231", "", 0));
        assertNotSame(hasher.remove("223"), -1);
        assertNull(hasher.insert("222", "", 0));
        assertNotNull(hasher.insert("233", "", 0));
        assertNotSame(hasher.remove("227"), -1);
        assertNotSame(hasher.remove("228"), -1);
        assertNull(hasher.insert("224", "", 0));
        assertNotNull(hasher.insert("234", "", 0));
        assertNotNull(hasher.insert("235", "", 0));
        
        assertNull(hasher.insert("226", "", 0));  
    }
}
