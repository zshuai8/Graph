import java.io.IOException;
import java.io.RandomAccessFile;


/**
 * @author zshuai8, @author jiaheng1
 * @version 2016.10.16
 *
 */
public class BufferPool implements BufferPoolADT {
    private RandomAccessFile file;
    private int bufferSize;
    private LRUList<Buffer> list;
    private int discR = 0;
    private int discW = 0;
    private int counter = 0;
    
    /**
     * BufferPool constructor
     * initialize the pool size
     * @param src is the source string
     * @param size is the size of the bufferpool
     * @param inputBufferSize is the size of the buffer
     */
    public BufferPool(String src, int size, int inputBufferSize) {
        
        list = new LRUList<Buffer>(size);
        try {
            file = new RandomAccessFile(src, "rw");
            bufferSize = inputBufferSize;
            for (int i = 0; i < size; i++) {
                
                Buffer buf = new Buffer(0, new byte[inputBufferSize]);
                list.add(buf);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        } 
    }
    
    /**
     *  Copy "sz" bytes from "space" to position "pos" in the buffered storage
     *  @param space is the source array 
     *  @param sz is the size of bytes space wants to copy 
     *  @param index is the byte position in the bufferpool space wants to copy
     * @throws IOException 
     */
    public void writeIn(byte[] space, int sz, int index) {
        
        int pos = index;
        int length = sz + pos % bufferSize - bufferSize;
        Buffer buf = search(pos);
        if (length > 0) {
            length = bufferSize - pos % bufferSize;
            System.arraycopy(space, 0, buf.getByte(), pos % bufferSize, 
                    length);
            buf.setDirty(true);
            int i = 0;
            while (sz - (bufferSize - pos % bufferSize) - 
                    bufferSize * (i + 1) > 0) {
                i++;
                buf = search(pos + i * bufferSize); 
                System.arraycopy(space, length + bufferSize * (i - 1), 
                        buf.getByte(), 0, bufferSize);
                buf.setDirty(true); 
            }
            buf = search(pos + (i + 1) * bufferSize);
            System.arraycopy(space, length + bufferSize * i, buf.getByte(),
                    0, sz - length - bufferSize * i);
            
        }
        else {
            
            System.arraycopy(space, 0, buf.getByte(), pos % bufferSize, 
                    sz);
        }
        buf.setDirty(true);
        counter++;  
        
    }
    
    /**
     *  Copy "sz" bytes from position "pos" of the buffered storage to "space"
     *  @param space is the destination byte array 
     *  @param sz is the size of bytes bufferpool wants to copy 
     *  @param index is the byte position in the array bufferpool wants to copy
     * @throws IOException 
     */
    public void writeOut(byte[] space, int sz, int index) {
        
        int pos = index;
        int length = sz + pos % bufferSize - bufferSize;
        Buffer buf = search(pos);
        if (length > 0) {
            length = bufferSize - pos % bufferSize;
            System.arraycopy(buf.getByte(), pos % bufferSize, space, 0, 
                    length);
            int i = 0;
            while (sz - (bufferSize - pos % bufferSize)
                    - bufferSize * (i + 1) > 0) {
               
                i++;
                buf = search(pos + i * bufferSize); 
                System.arraycopy(buf.getByte(), 0, space,
                        length + bufferSize * (i - 1), bufferSize);
            }
            buf = search(pos + (i + 1) * bufferSize);
            System.arraycopy(buf.getByte(), 0, space,
                    length + bufferSize * i, sz - 
                    length - bufferSize * i);
            
        }
        else {
            System.arraycopy(buf.getByte(), pos % bufferSize, space, 0, 
                    sz);
        }
        counter++;   
    }
    
    /**
     * search Buffer by its index
     * @param index is the search index
     * @return the buffer with the search index
     * @throws IOException 
     */
    public Buffer search(int index) {
        
        for (int i = 0; i < list.size(); i++) {
            Buffer newBuffer = list.get(i);
            if (newBuffer.getIndex() == index / bufferSize) {
                return newBuffer;
            }
        }
        
        byte[] newByte = new byte[bufferSize];
        try {
            if (index < file.length()) {
                
                file.seek((index / bufferSize) * bufferSize);
                file.read(newByte);
                discR++;
            }
            Buffer newBuf = new Buffer(index / bufferSize, newByte);
            Buffer removed = list.removeEnd();
            if (removed.getDirty()) {
                
                file.seek(removed.getIndex() * bufferSize);
                file.write(removed.getByte());
                discW++;
            }
            list.add(newBuf);
            return newBuf;
        }
        catch (IOException e) {
            
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * flushes all the dirty bit back to the memory
     * @throws IOException 
     * 
     */
    public void flushAll() throws IOException {
        
        Buffer cur;
        for (int i = 0; i < list.size(); i++) {
            cur = list.get(i);
            if (cur.getDirty()) {
                file.seek(cur.getIndex() * bufferSize);
                file.write(cur.getByte());
                discW++;
            }
        }
    }
    
    /**
     * get the number of disc reads
     * @return number of disk reads
     */
    public int discRead() {
        return discR;
    }
    
    /**
     * get the number of disc writes
     * @return number of disk writes
     */
    public int discWrite() {
        return discW;
    }
    
    /**
     * get the number of cache hits
     * @return number of cache hits
     */
    public int getCounter() {
        return counter;
    }
}