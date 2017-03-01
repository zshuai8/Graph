import java.io.IOException;

/**
 * This project is about use a memory manager to store
 * a collection of artists and songs, and use a graph
 * data structure to print out some statistics 
 */

/**
 * The class containing the main method.
 *
 * @author zshuai8, @author jiaheng1
 * @version {2016.12.04}
 */

// On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.

public class GraphProject {
    private static int hashSize;
    private static int numOfBuffer;
    private static int bufferSize;
    private static String cmdFile;
    private static String memFile;
    private static String fileName;
    /**
     * main method
     * @param args
     *            Command line parameters
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        
        beginReadingInput(args);
        new Parser(memFile, numOfBuffer, bufferSize,
                hashSize, cmdFile, fileName);
    }
    
    /**
     * parser for command line
     * @param input
     *            is the command lines parsing command lines
     * @return true if parser successfully parsed the input
     * return null else.
     */
    public static boolean beginReadingInput(String[] input) {
 
        if (input.length < 6) {

            System.out.println("Not enough input," 
                    + " please support with correct amount of inputs");
            return false;
        }
        memFile = input[0];
        cmdFile = input[4];
        fileName = input[5];
        try {

            numOfBuffer = Integer.parseInt(input[1]);
        }
        catch (NumberFormatException nfe) {

            System.out.println(
                    "The second argument must be an integer.");
            return false;
        }

        try {

            bufferSize = Integer.parseInt(input[2]);
        }
        catch (NumberFormatException nfe) {

            System.out.println(
                    "The third argument must be an integer.");
            return false;
        }
        
        try {

            hashSize = Integer.parseInt(input[3]);
        }
        catch (NumberFormatException nfe) {

            System.out.println(
                    "The fourth argument must be an integer.");
            return false;
        }
        
        
        return true;
    }
}