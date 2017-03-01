import java.io.IOException;

import student.TestCase;

/**
 * @author zshuai8, @author jiaheng1
 * @version 2016/8/28
 */
public class ParserTest extends TestCase {

    /**
     * check basic methods of parser class
     * @throws IOException 
     */
    public void testParser() throws IOException {
        
        Parser p = new Parser("test.bin", 10, 
                32, 10, "input.txt", "statSample.txt"); 
        assertNotNull(p);
    } 
}
