import java.nio.ByteBuffer;

import student.TestCase;

/**
 * @author zshuai8 @author jiaheng1
 * @version 12/02/2016
 *
 */
public class BufferPoolTest extends TestCase {  
    
    /**
     * test basic methods of bufferPool class
     */
    public void testBasic() {
        
        BufferPool bufferP = new BufferPool("output.txt", 1, 1);
        byte [] a = new byte[1];
        ByteBuffer aa = ByteBuffer.wrap(a);
        bufferP.writeIn(aa.array(), 1, 1);
        assertEquals(bufferP.discRead(), 1);
        assertEquals(bufferP.discWrite(), 0);
        assertEquals(bufferP.getCounter(), 1);
        bufferP.writeOut(aa.array(), 1, 1);
        
        try {
            bufferP.flushAll();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        byte [] b = new byte[10];
        ByteBuffer bb = ByteBuffer.wrap(b);
        bufferP.writeIn(bb.array(), 10, 2);
        assertEquals(bufferP.discRead(), 11);
        assertEquals(bufferP.discWrite(), 11);
        assertEquals(bufferP.getCounter(), 3);
        bufferP.writeOut(bb.array(), 10, 2);
        try {
            bufferP.flushAll();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    

}