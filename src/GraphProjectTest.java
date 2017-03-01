import student.TestCase;

/**
 * @author zshuai8, @author jiaheng1
 * @version {2016.11.19}
 */
public class GraphProjectTest extends TestCase {

    /**
     * test main method.
     */
    public void testGInit() throws Exception {
        GraphProject gph = new GraphProject();
        assertNotNull(gph);
        String[] str = {"test.bin", "10", 
            "32", "10", "input.txt", "statSample.txt"};
        try {
            GraphProject.main(str);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        String[] str1 = {"test.bin", "10", 
            "32", "10", "input.txt"};
        try {
            GraphProject.main(str1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        String[] str2 = {"test.bin", "hello", 
            "32", "10", "input.txt", "statSample.txt"};
        try {
            GraphProject.main(str2);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        String[] str3 = {"test.bin", "10", 
            "hello", "10", "input.txt", "statSample.txt"};
        try {
            GraphProject.main(str3);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        String[] str4 = {"test.bin", "10", 
            "32", "hello", "input.txt", "statSample.txt"};
        try {
            GraphProject.main(str4);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
            
            
    }
}
