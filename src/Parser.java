import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * A parser class which parses input arguments
 * @author zshuai8, @author jiaheng1
 * @version 9/4/2016
 *
 */
public class Parser {
    
    private Hash artistHash;
    private Hash songHash;
    private MemoryManager pool; 
    private GraphL graph;
    
    /**
     * 
     * @param inputHashSize is the input hashtable size
     * @param inputFileName is the input file name
     * @param inputMemFile is the memory file for use
     * @param inputNumOfBuffer is the number of buffers
     * @param inputBufferSize is the size of the buffer
     * @param inputCmdFile is the command file to read from
     * @throws IOException 
     */
    public Parser(String inputMemFile, int inputNumOfBuffer,
            int inputBufferSize, int inputHashSize, String inputCmdFile, 
            String inputFileName) throws IOException {
        
        artistHash = new Hash(inputHashSize);
        songHash = new Hash(inputHashSize);
        BufferPool bufferP = new BufferPool(inputMemFile, inputNumOfBuffer,
                inputBufferSize);
        pool = new MemoryManager(inputBufferSize, bufferP);
        artistHash.getPool(pool);
        songHash.getPool(pool);
        graph = new GraphL();
        
        final long start = System.currentTimeMillis();
        beginParsingByLine(inputCmdFile);
        bufferP.flushAll();
        final long end = System.currentTimeMillis();
        int time = (int) (end - start);
        
        PrintWriter writer = new PrintWriter(
                new BufferedWriter(new FileWriter(inputFileName, true)));
        writer.println("Cache Hits: " + bufferP.getCounter());
        writer.println("Disk Reads: " + bufferP.discRead());
        writer.println("Disk Writes: " + bufferP.discWrite());
        writer.println("Time is " + time);
        writer.close();  
    }


    /**
     * parser
     * @param filename
     *            is the input file parsing the input file
     * @return true if parsing is successful, else return false
     */
    public boolean beginParsingByLine(String filename) {

        try {

            Scanner sc = new Scanner(new File(filename));
            Scanner scancmd;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                scancmd = new Scanner(line);
                String cmd = scancmd.next();
                String type;

                if (cmd.equals("insert")) {

                    scancmd.useDelimiter("<SEP>");
                    String artist = scancmd.next().replaceFirst(" ", "");
                    String song = scancmd.next();
                    insert(song, artist);
                        
                }
                else if (cmd.equals("remove")) {
                    
                    type = scancmd.next();
                    String token = scancmd.nextLine().replaceFirst(" ", "");
                    remove(type, token);
                        
                }
                else if (cmd.equals("print")) {
                    
                    type = scancmd.next();
                    print(type);       
                }
                else {
                    
                    System.out.println("Unrecognized input");
                }
            }
            sc.close();
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * we print out results accordinly to the input song and artist
     * @param inputSong is the inputsong
     * @param inputArtist is the input artist
     */
    public void insert(String inputSong, String inputArtist) {
        
        String song = inputSong;
        String artist = inputArtist;
        int artistIndex = artistHash.contain(artist);
        
        if (artistIndex < 0) {
            
            artistIndex = graph.getNextIndex();
            artistHash.insert(inputArtist, "Artist hash", artistIndex);
            graph.addVertex(new Vertex(artistIndex, null, null));
            System.out.println("|" + artist
                    + "| is added to the Artist database.");  
            
        }
        else {
            
            
            artistIndex = artistHash.
                    retrieveIndex(artistHash.contain(inputArtist));
            System.out.println("|" + artist
                    + "| duplicates a record "
                    + "already in the Artist database.");
        }
        
        int songIndex = songHash.contain(song);

        if (songIndex < 0) {
            
            songIndex = graph.getNextIndex();
            songHash.insert(inputSong, "Song hash", songIndex);
            graph.addVertex(new Vertex(songIndex, null, null));
            System.out.println("|" + song
                    + "| is added to the Song database.");
        }
        else {
           
            songIndex = songHash.
                    retrieveIndex(songHash.contain(inputSong));
            System.out.println("|" + song 
                            + "| duplicates a record "
                            + "already in the Song database.");
        }
        
        graph.addEdge(artistIndex, songIndex);
        graph.addEdge(songIndex, artistIndex);
    }
    
    
    /**
     * we remove an element of a hashtable accordingly to the input and command
     * @param command is the input command
     * @param input is the input name
     */
    public void remove(String command, String input) {
        String type = command;
        String removed = input;
        
        if (type.equals("artist")) {

            int artistIndex = artistHash.remove(removed);
            if (artistIndex >= 0) {
                
                System.out.println("|" + removed + "| is removed from "
                    + "the artist database.");
                graph.removeVertex(artistIndex);
            }
            else {

                System.out.println("|" + removed + "| does not exist "
                        + "in the artist database.");
            }
        }
        else if (type.equals("song")) {
            
            int songIndex = songHash.remove(removed);
            if (songIndex >= 0) {

                System.out.println("|" + removed + "| is removed "
                        + "from the song database.");
                graph.removeVertex(songIndex);
            }
            else {

                System.out.println("|" + removed + "| does not exist "
                        + "in the song database.");
            }
        }
        else {
            
            System.out.println(
                    "Error bad remove type " + removed);
        }
    }
    
    /**
     * method for dealing with print command
     * @param input is the input
     * there are three different options we can print
     * artists, songs and blocks
     */
    public void print(String input) {
        
        String printed = input;
        
        if (printed.equals("song")) {
                
            if (songHash.getElements() > 0) {
                
                String[] songTable = 
                        songHash.storeElements();
                int[] songIndex = 
                        songHash.storeIndex();
              
                for (int i = 0; 
                        i < songHash.getElements(); 
                        i++) {
                   
                    System.out.println(
                            "|" + songTable[i] 
                            + "| " + songIndex[i]);
                }
            }
            System.out.println("total songs: " 
                    + songHash.getElements());
        }
        else if (printed.equals("artist")) {
                
            if (artistHash.getElements() > 0) {
                
                String[] artistTable = 
                        artistHash.storeElements();
                int[] artistIndex = 
                        artistHash.storeIndex();
                
                for (int i = 0; 
                        i < artistHash.getElements(); 
                        i++) {
                    
                    System.out.println(
                            "|" + artistTable[i] 
                            + "| " + artistIndex[i]);
                }
            }
            System.out.println("total artists: " 
                    + artistHash.getElements());
        }              
        else if (printed.equals("blocks")) {
                
            System.out.println(
                    new String(pool.getList().toString()));         
                
        }
        else if (printed.equals("graph")) {
            
            graph.print();         
                
        }
        else {
            
            System.out.println(
                    "Error bad print type " + printed);
        }
    }
}

