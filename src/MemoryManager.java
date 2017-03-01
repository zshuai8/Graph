import java.nio.ByteBuffer;

/**
 * 
 * @author zshuai8, @author jiaheng1
 * @version 2016/9/10
 *
 */
public class MemoryManager {
    
    private int poolSize;
    private int defaultSize;
    private DLList list;
    private int expansionCounter;
    private int[] expandedSize;
    private BufferPool bufferP;
    
/**
 *@param size is the input size for memory pool
 *@param buf is the BufferPool to use
 * initialize pool size, list, 
 * expandedSize for storing expanded poolsize when pool gets expanded
 * (could be multiple times)
 * default size is the size we want the pool to expand with
 * 
 * expansionCounter is used to track down the next following size 
 * pool should have
 * 
 */
    public MemoryManager(int size, BufferPool buf) {
        
        poolSize = size;
        defaultSize = size;
        expansionCounter = 1;
        list = new DLList(size);
        list.setPoolSize(poolSize);
        expandedSize = new int[16];
        bufferP = buf;
    }
    
/**
 * getter method for getting list
 * @return the list which is linked to the memory pool
 */

    public DLList getList() {
        
        return list;
    }
    
/**
 * insert byte array
 * @param space is the input byte array
 * @param size is the size of the byte array
 * @return a handle which stores the value of the stored bytes
 */

    public Handle insert(byte[] space, int size) {
        
        if (size > 65536) {
            
            return null;
        }
            
        byte[] lenb = new byte[2];
        ByteBuffer lenbb = ByteBuffer.wrap(lenb);
        int len = size;
        lenbb.putShort(0, (short)len); 
        
        int index = bestfit(size);
        if (index != -1) {
            
            bufferP.writeIn(lenbb.array(), 2, index);
        }
        else {
             
            while (index == -1) {
                
                expandPool();
                index = bestfit(size);
            }
            bufferP.writeIn(lenbb.array(), 2, index);
        }
        bufferP.writeIn(space, size, index + 2);
        list.replaceForAdd(index, size);
        
        Handle newHandle = new Handle(index);
        return newHandle;
    }
    
    /**
     * check if the pool needs to be expanded
     */
    private void expandPool() {
        
        expansionCounter++;
        int newSize = defaultSize * expansionCounter; 
        list.expandedList(poolSize, defaultSize);
        poolSize = newSize;
        list.setPoolSize(poolSize);
        System.out.println("Memory pool expanded to be " + poolSize
                + " bytes.");
    }

    /**
     * find the best fit index
     * @param size is the input size
     * @return the best index for the given size in the pool
     * return -1 if there is no place found
     */
    private int bestfit(int size) {
        
        return list.bestfitP(size);
    }
    
    /**
     * remove a handle
     * @param theHandle is the input handle
     * remove the stored bytes in the pool
     */
    public void remove(Handle theHandle) {
        byte[] buffer = new byte[2];
        bufferP.writeOut(buffer, 2, theHandle.getValue());
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        int length = byteBuffer.getShort(); 
        list.replaceForRemove(theHandle.getValue(), length);
    }

    
    /**
     * returns the byte array
     * @param input is the input handle
     * @return a string 
     */
    public byte[] get(Handle input) {
        
        
        byte[] buffer = new byte[2];
        bufferP.writeOut(buffer, 2, input.getValue());
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        int length = byteBuffer.getShort();
        
        byte[] output = new byte[length];
        bufferP.writeOut(output, length, input.getValue() + 2);      
        return output;
    }
    
    /**
     * get size
     * @return the pool size
     */
    public int size() {
        
        return poolSize;
    }
    
    /**
     * get integer array of sizes
     * @return an array of sizes
     */
    public int[] retrieveSizeTable() {
        
        return expandedSize;
    }
}
