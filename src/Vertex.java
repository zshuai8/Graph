/**
 * @author zshuai8
 * @version 2016/11/30
 *
 */
public class Vertex {
    
    private int index;
    private boolean removed;
    private Vertex prevV;
    private Vertex nextV;
    
    /**
     * constructor for the vertex
     * @param inpIndex is the input index
     * @param prev is the previous vertex
     * @param next is the next vertex
     */
    public Vertex(int inpIndex, Vertex prev, Vertex next) {
        
        index = inpIndex;
        prevV = prev;
        nextV = next;
        removed = false;
    }
    
    /**
     * get the index
     * @return the index of the vertex
     */
    public int getIndex() {
        
        return index;
    }
    
    /**
     * sets the index of the vertex
     * @param input is the index to be set
     */
    public void setIndex(int input) {
        
        index = input;
    }
    
    /**
     * get the next vertex
     * @return the handle
     */
    public Vertex next() {
        
        return nextV;
    }
    
    /**
     * get the previous vertex
     * @return the handle
     */
    public Vertex prev() {
        
        return prevV;
    }
    
    /**
     * set the next vertex
     * @param input is the next vertex to be set
     */
    public void setNext(Vertex input) {
        
        nextV = input;
    }
    
    /**
     * set the previous vertex
     * @param input is the previous index to be set
     */
    public void setPrev(Vertex input) {
        
        prevV = input;
    }
    
    /**
     * check if the vertex is removed
     * @return the value of removed
     */
    public boolean removed() {
        
        return removed;
    }
    
    /**
     * set value for removed
     * @param input is the input boolean
     */
    public void setRemoved(boolean input) {
        
        removed = input;
    }
}
