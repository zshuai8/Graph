/**
 * index class for the handle
 * @author zshuai8 @author jiaheng1
 * @version {2016.12.1}
 */
public class Indexer {
    private Handle handle;
    private int vertexIndex;
    
    /** 
     * constructor for the indexer
     * @param inputhHandle is the handle that the index refer to
     * @param indexV is the index for the vertex
     */
    public Indexer(Handle inputhHandle, int indexV) {
        
        handle = inputhHandle;
        vertexIndex = indexV;
    }
    
    /**
     * getter method
     * @return handle index
     */
    public Handle getHI() {
        
        return handle;
    }
    
    /**
     * getter method
     * @return the index of the vertex
     */
    public int getVI() {
        
        return vertexIndex;
    }
    
    /**
     * sets the vertex index
     * @param input is the vertex index to set
     */
    public void setVI(int input) {
        
        vertexIndex = input;
    }
}
