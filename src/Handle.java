/**
 * @author zshuai8, @author jiaheng1
 * @version 2016/9/4
 * Handle class stores the position of stored string
 */
public class Handle {
    
    private int position;
    private boolean tombstoneBoolean;
    
    /**
     * constructor for the handle
     * @param input is the input int
     */
    public Handle(int input) {
         
        position = input;
        tombstoneBoolean = false;
    }
    
    /**
     * gets the value stored
     * @return the value stored in handle
     */
    public int getValue() {
        
        return position;
    }
    
    /**
     * sets the tombstone value
     * @param input is the boolean value we want the tombestone to be
     */
    public void setTombstone(boolean input) {
        
        tombstoneBoolean = input;
    }
    
    /**
     * gets the tombstone value
     * @return if tombstone exists
     */
    public boolean getTombstone() {
        
        return tombstoneBoolean;
    }
}
