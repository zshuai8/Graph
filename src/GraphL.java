/**
 * @author zshuai8, @author jiaheng1
 * @version {2016/11/23}
 */
public class GraphL {
    
    private class ParPtrTree {
        private int[] array;     // Node array

        /**
         * constructor for parent pointer tree
         * @param size is the initial size of the tree
         */
        public ParPtrTree(int size) {
            
            array = new int[size]; // Create node array
            for (int i = 0; i < size; i++) {
              
                array[i] = -1;       // Each node is its own root to start
            }
        }

        /**
         * find the root of the current node
         * @param x is the index of the node 
         * @return the root of x
         */
        public int find(int x)
        {
            if (array[x] == -1) {
                
                return x;
            }
            array[x] = find(array[x]);
            return array[x];
        }

        /**
         * merge two subtrees if they are different
         * @param a is the first tree
         * @param b is the second tree
         */
        public void union(int a, int b)
        {
            int x = find(a);
            int y = find(b);
            if (x == y) {
                
                return;
            }
            array[x] = y;
        }
    }
    
    private Vertex[] vertexL;
    private int vertexCount;
    private LRUList<Integer> freeblocks;
    private int currentIndex;
    private final static int MAX = 10000;
    private ParPtrTree tree;
    
    /**
     * constructor for the adjacency list 
     * calls init() to initialize 
     */
    public GraphL() {
        
        init();
    }
    
    /**
     * get the next available index for vertex
     * @return the index of the next vertex
     */
    public int getNextIndex() {
        

        if (!freeblocks.isEmpty()) {

            return freeblocks.removeEnd();   
        }
        
        int temp = currentIndex;
        currentIndex++;
        return temp;
    }
  
    /**
     * floyd's algorithm to calculate all-pairs shortest paths
     * @param d is the matrix containing all the vertexs to vertexs
     * @param length is the length between two vertexs 
     */
    public void floyd(int[][] d, int length) {
        
        
        for (int k = 0; k < length; k++) {
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < length; j++) {
                    if ((d[i][k] != Integer.MAX_VALUE) &&
                        (d[k][j] != Integer.MAX_VALUE) &&
                        (d[i][j] > (d[i][k] + d[k][j]))) {
                        d[i][j] = d[i][k] + d[k][j];
                    }
                }
            }
        }
    }
    
    /**
     * finds the max value in a matrix
     * @param array is the matrix 
     * @return the max value in the array
     */
    private int findMax(int[][] array) {
        
        int max = array[0][0];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j] > max) {
                    max = array[i][j]; 
                }
            }
        }
        return max;
    }
    
    /**
     * initialize the adjacency list
     */
    public void init() {
        
        vertexL = new Vertex[MAX];
        vertexCount = 0;
        freeblocks = new LRUList<Integer>(MAX);
        currentIndex = 0;
    }
    
    /**
     * finds the link in vertexOne's neighbor list that preceeds the 
     * one with vertexTwo  
     * @param vertexOne is the first vertex
     * @param vertexTwo is the second vertex
     * @return the vertex that preceeds with vertexTwo 
     * and is a neighbor of vertexOne
     */
    private Vertex findV(int vertexOne, int vertexTwo) {

        Vertex curr = vertexL[vertexOne];
        
        while ((curr.next() != null) && (curr.next().getIndex() != vertexTwo)) {
            curr = curr.next();
        }
        if ((curr.next() != null) && curr.next().getIndex() == vertexTwo) {
            curr = curr.next();
        }
        return curr;
    }
    
    /**
     * add an edge
     * @param vertexOne is the starting vertex
     * @param vertexTwo is the ending vertex
     */
    public void addEdge(int vertexOne, int vertexTwo) {
        
        Vertex curr = findV(vertexOne, vertexTwo);
        if (curr.getIndex() == vertexTwo) {
            return;
        }
        else {
            curr.setNext(new Vertex(vertexTwo, curr, curr.next()));
        }
    }
    
    /**
     * remove a vertex
     * @param vertexOne is the starting vertex
     */
    public void removeVertex(int vertexOne) {
        
        Vertex curr = vertexL[vertexOne];
        Vertex temp = curr.next();
        while (temp != null) {
            
            removeEdge(temp.getIndex(), vertexOne);
            temp = temp.next();
        }

        curr.setNext(null);
        curr.setPrev(null);
        curr.setRemoved(true);
        freeblocks.add(vertexOne);
        vertexCount--;
    }
    
    /**
     * remove an edge
     * @param vertexOne is the starting vertex
     * @param vertexTwo is the ending vertex
     */
    public void removeEdge(int vertexOne, int vertexTwo) {
        
        Vertex curr = findV(vertexOne, vertexTwo);
        if (curr.getIndex() != vertexTwo) {
            return;
        }

        if (curr.next() != null) {
            
            curr.next().setPrev(curr.prev());
        }
        curr.prev().setNext(curr.next());
    }
    
    /**
     * add a vertex
     * @param vertex is the vertex to add
     */
    public void addVertex(Vertex vertex) {
        
        vertexL[vertex.getIndex()] = vertex;
        vertexCount++;
    }

    /**
     * compute how many connected components is inside the tree
     */
    public void computeTree() {
        
        tree = new ParPtrTree(currentIndex);
        for (int i = 0; i < currentIndex; i++) {
            Vertex curr = vertexL[i];

            while (curr.next() != null && !curr.next().removed()) {
                
                curr = curr.next();
                tree.union(i, curr.getIndex());
            }
        }
    }
    
    /**
     * check if two vertex are neighbors
     * @param vertexOne is the first vertex
     * @param vertexTwo is the second vertex
     * @return true if they are neighbors, false otherwise
     */
    public boolean isNeighbors(int vertexOne, int vertexTwo) {

        Vertex vertex = findV(vertexOne, vertexTwo);
        return vertex.getIndex() == vertexTwo;
    }
    
    /**
     * print out status
     */
    public void print() {
        
        computeTree();
        int counter = 0;
        int[] values = new int[vertexCount];
        int[] nodesSum = new int[currentIndex];
        for (int i = 0; i < currentIndex; i++) {
            
            if (tree.array[i] == -1 && !vertexL[i].removed()) {
                
                values[counter] = i;
                nodesSum[i]++;
                counter++;
            }
            else if (!vertexL[i].removed()) {
                
                nodesSum[tree.find(i)]++;
            }
        }

        System.out.println("There are " + counter + " connected components");
        if (vertexCount == 0) {
            System.out.println("The largest connected component has "
                    + 0 + " elements");
            System.out.println("The diameter of the largest component is "
                    + 0);
            return;
        }
        
        int currIndex = 0;
        int maxIndex = values[currIndex];
        int maxValue = nodesSum[values[currIndex]];
        for (int i = 0; i < counter - 1; i++) {
            
            if (maxValue >= nodesSum[values[currIndex + 1]]) {
                
                currIndex++;
                continue;
            }
            else {
                
                maxValue = nodesSum[values[currIndex + 1]];
                maxIndex = values[currIndex + 1];
                currIndex++;
            }
        }
        
        int[] nodesIndex = new int[maxValue];
        counter = 0;
        for (int i = 0; i < currentIndex; i++) {
            
            if (tree.find(i) == maxIndex) {
                
                nodesIndex[counter] = i;
                counter++;
                
                if (counter == maxValue) {
                    break;
                }
            }
        }
        
        int[][] input = new int[maxValue][maxValue]; 
        System.out.println("The largest connected component has "
                + maxValue + " elements");
        
        for (int i = 0; i < maxValue; i++) {
            for (int j = 0; j < maxValue; j++) {
                
                if (i == j) {
                    
                    input[i][j] = 0;
                }
                else if (isNeighbors(nodesIndex[i], nodesIndex[j])) {
                    input[i][j] = 1;
                }
                else {
                    input[i][j] = Integer.MAX_VALUE;
                }
            }
        }
        floyd(input, maxValue);
        System.out.println("The diameter of the largest component is "
                + findMax(input));
    }
}