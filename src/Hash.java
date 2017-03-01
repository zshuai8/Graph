import java.io.IOException;

/**
 * Hashtable stores handles which store the position 
 * of stored string in the memory pool
 *
 * @author CS3114 staff
 * @version August 27, 2016
 */


public class Hash
{
    /**
     * @author zshuai8 @author jiaheng1
     * @version 2016.9.12
     * memory pool
     */
    MemoryManager pool;
    private Indexer[] hashTable;
    private int tableSize;
    private int elements = 0;
    
    /**
     * Create a new Hash object.
     *
     */
    public Hash()
    {
        
        hashTable = new Indexer[10];
        tableSize = 10;
    }
    
    /**
     * Create a new Hash object.
     * this takes a parameter to determine the size of the hashtable
     * @param size is the input size
     */
    public Hash(int size)
    {
        
        hashTable = new Indexer[size];
        tableSize = size;
    }
    
    /**
     * set the memory manager to use
     * @param inputPool is the size of the input pool
     */
    public void getPool(MemoryManager inputPool) {
        
        pool = inputPool;
    }
    
    /**
     * insert a handle
     * @param input is the input string client wants to store
     * @param type is the type of the data stored
     * @param index is the index of the indexer 
     * @return handle if insertion is successful, otherwise return null
     * @throws IOException 
     */
    public Handle insert(String input, String type, int index) {
        
        if (contain(input) != -1) {
            return null;
        }
        
        if (elements + 1 > (tableSize / 2)) {
            
            rehashTable(tableSize * 2);
            System.out.println(type + " table size doubled.");
        }
        
        Handle newHandle = pool.insert(input.getBytes(), input.length());
        int initialIndex = h(input, tableSize);
        int position = 0;
        
        while (hashTable[(initialIndex 
                + position * position) % tableSize] != null) {
            
            if (hashTable[(initialIndex 
                    + position * position) %
                          tableSize].getHI().getTombstone()) {
                
                break;
            }
            
            position++;        
        }
        
        hashTable[(initialIndex + position * position)
                  % tableSize] =  new Indexer(newHandle, index);
        elements++;
        
        return newHandle;
    }
    
    /**
     * remove a handle
     * @param input is the input string which is needed to be stored
     * @return true if removal is successful otherwise return false
     * @throws IOException 
     */
    public int remove(String input) {
        
        int index = contain(input);
        
        if (index != -1) {
            
            Indexer newHandle = hashTable[index];
            hashTable[index].getHI().setTombstone(true);
            elements--;
            pool.remove(hashTable[index].getHI());
            
            return newHandle.getVI();
        }
        else {
            
            return -1;
        }
    }
    
    /**
     * rehash the hash table with or without the same size
     * store the values in the old array first, 
     * then rehash them into the new array
     * percondition: 
     * @param size must be bigger or equal than the current tableSize
     */
    private void rehashTable(int size) {
        
        Indexer[] elementsInOldArray = new Indexer[elements];
        int currentIndex = 0;
        elements = 0;
        
        for (int i = 0; i < tableSize; i++) {
            
            if ((hashTable[i] != null) && 
                    !(hashTable[i].getHI().getTombstone())) {
                
                elementsInOldArray[currentIndex] = hashTable[i];
                currentIndex++;
            }
        }
        
        tableSize = size;
        hashTable = new Indexer[tableSize];
        
        for (int j = 0; j < elementsInOldArray.length; j++) {
            
            String inputString = new 
                    String(pool.get(elementsInOldArray[j].getHI()));
            int initialIndex = h(inputString, tableSize);
            int position = 0;       
            
            while (hashTable[(initialIndex 
                    + position * position) % tableSize] != null) {
                
                
                position++;        
            }
            
            hashTable[(initialIndex + position *
                    position) % tableSize] =  elementsInOldArray[j];
            elements++;
        }
    }
    
    /**
     * check if it contains the string input
     * @param input is the input string we check for existence
     * @return the index of the string or -1 means not exist
     */
    public int contain(String input) {
        
        int currentPosition = h(input, tableSize); 
        int testProbe = 0;
        
        while (hashTable[(currentPosition 
                + testProbe * testProbe) % tableSize] != null) {
                  
            if (hashTable[(currentPosition 
                    + testProbe * testProbe)
                          % tableSize].getHI().getTombstone()) {
                
                testProbe++;
                continue;
            }
            
            if (input.equals(new String(pool.get((hashTable[(currentPosition 
                    + testProbe * testProbe) % tableSize].getHI()))))) {
                
                return (currentPosition 
                        + testProbe * testProbe) % tableSize;
            }
            testProbe++; 
        }
        
        return -1;
    }
    
    /**
     * get the number of elements 
     * @return the number of elements in the table
     */
    public int getElements() {
        
        return elements;
    }
    
    /**
     * get the size of the table
     * @return the size of the hashtable
     */
    public int getSize() {
        
        return tableSize;
    }
    
    /**
     * Compute the hash function. Uses the "sfold" method from the OpenDSA
     * module on hash functions
     *
     * @param s
     *            The string that we are hashing
     * @param m
     *            The size of the hash table
     * @return The home slot for that string
     */
    private int h(String s, int m)
    {
        int intLength = s.length() / 4;
        long sum = 0;
        for (int j = 0; j < intLength; j++)
        {
            char[] c = s.substring(j * 4, (j * 4) + 4).toCharArray();
            long mult = 1;
            for (int k = 0; k < c.length; k++)
            {
                sum += c[k] * mult;
                mult *= 256;
            }
        }

        char[] c = s.substring(intLength * 4).toCharArray();
        long mult = 1;
        for (int k = 0; k < c.length; k++)
        {
            sum += c[k] * mult;
            mult *= 256;
        }

        return (int)(Math.abs(sum) % m);
    }
    
    /**
     * wipe out elements inside the hashtable
     */
    public void dump() {
        
        hashTable = new Indexer[tableSize];
        elements = 0;
        
    }
    
    /**
     * get the string list of stored elements
     * @return the stored string for the use of printing out element
     */
    public String[] storeElements() {
        
        String[] stringArray = new String[elements];
        int[] index = new int[elements];
        int arrayCounter = 0;
        
        for (int i = 0; i < tableSize; i++) {
            
            if ((hashTable[i] != null) &&
                    !(hashTable[i].getHI().getTombstone())) {
                
                stringArray[arrayCounter] = new 
                        String(pool.get(hashTable[i].getHI()));
                index[arrayCounter] = i;
                arrayCounter++;
            }
        }
        
        return stringArray;
    }
    
    /**
     * get the integer array of the stored index
     * @return the index of stored string
     */
    public int[] storeIndex() {
        
        int[] index = new int[elements];
        int arrayCounter = 0;
        
        for (int i = 0; i < tableSize; i++) {
            
            if ((hashTable[i] != null) &&
                    !(hashTable[i].getHI().getTombstone())) {
                
                index[arrayCounter] = i;
                arrayCounter++;
            }
        }
        
        return index;
    }
    
    /**
     * get the index of the handle
     * @param index is the index of a handle
     * @return a handle in given index
     */
    public int retrieveIndex(int index) {
        
        return hashTable[index].getVI();
    }
}
