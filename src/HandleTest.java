import student.TestCase;

/**
 * @author zshuai8, @author jiaheng1
 * @version 2016/8/28
 */
public class HandleTest extends TestCase {

    /**
     * test basic methods of handle class
     */
    public void testHandle() {
        
        Handle handle = new Handle(10);
        assertEquals(handle.getValue(), 10);
    }
}
