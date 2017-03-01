import student.TestCase;

/**
 * @author zshuai8 @author jiaheng1
 * @version 12/01/2016
 *
 */
public class GraphLTest extends TestCase {
    
    /**
     * test basic methods of graph class
     */
    public void testGraphBasic() {
        GraphL graph = new GraphL();
        graph.print();
        assertFuzzyEquals(systemOut().getHistory(),
                "there are 0 connected components\n"
                + "the largest connected component has 0 elements\n"
                + "the diameter of the largest component is 0");
        int index = graph.getNextIndex();
        assertEquals(index, 0);
        graph.addVertex(new Vertex(index, null, null));
        int index1 = graph.getNextIndex();
        assertEquals(index1, 1);
        graph.addVertex(new Vertex(index1, null, null));
        assertFalse(graph.isNeighbors(index, index1));
        graph.addEdge(index, index1);
        graph.addEdge(index1, index);
        assertTrue(graph.isNeighbors(index, index1));
        systemOut().clearHistory();
        graph.print();
        assertFuzzyEquals(systemOut().getHistory(),
                "there are 1 connected components\n"
                + "the largest connected component has 2 elements\n"
                + "the diameter of the largest component is 1");
        graph.removeEdge(index, index1);
        graph.removeEdge(index1, index);
        systemOut().clearHistory();
        graph.print();
        assertFuzzyEquals(systemOut().getHistory(),
                "there are 2 connected components\n"
                + "the largest connected component has 1 elements\n"
                + "the diameter of the largest component is 0");
        graph.removeVertex(index);
        assertFalse(graph.isNeighbors(index, index1));
        systemOut().clearHistory();
        graph.print();
        assertFuzzyEquals(systemOut().getHistory(),
                "there are 1 connected components\n"
                + "the largest connected component has 1 elements\n"
                + "the diameter of the largest component is 0"); 
    }
    
    /**
     * test complicated insert and remove
     */
    public void testInsert() {
        GraphL graph = new GraphL();
        int index = graph.getNextIndex();
        assertEquals(index, 0);
        Vertex v = new Vertex(index, null, null);
        int index1 = graph.getNextIndex();
        assertEquals(index1, 1);
        Vertex v1 = new Vertex(index1, null, null);
        int index2 = graph.getNextIndex();
        assertEquals(index2, 2);
        Vertex v2 = new Vertex(index2, null, null);
        int index3 = graph.getNextIndex();
        assertEquals(index3, 3);
        Vertex v3 = new Vertex(index3, null, null);
        int index4 = graph.getNextIndex();
        assertEquals(index4, 4);
        Vertex v4 = new Vertex(index4, null, null);
        graph.addVertex(v);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addEdge(index, index);
        graph.addEdge(index, index1);
        graph.addEdge(index1, index);
        graph.addEdge(index1, index2);
        graph.addEdge(index2, index1);

        graph.addEdge(index3, index4);
        graph.addEdge(index4, index3);
        graph.addEdge(index2, index4);
        graph.addEdge(index4, index2);

     
        graph.print();
        assertFuzzyEquals(systemOut().getHistory(), 
                "there are 1 connected components\n"
                + "the largest connected component has 5 elements\n"
                + "the diameter of the largest component is 4");
        graph.removeVertex(index);
        graph.removeVertex(index2);
        graph.removeEdge(index2, index);
        graph.removeEdge(index, index2);
        int index5 = graph.getNextIndex();
        assertEquals(index5, 0);
        Vertex v5 = new Vertex(index5, v3, v4);
        graph.addVertex(v5);
        graph.addEdge(index3, index4);
        graph.addEdge(index4, index3);
        graph.addEdge(index4, index5);
        graph.addEdge(index5, index4);
        systemOut().clearHistory();
        graph.print();
        assertFuzzyEquals(systemOut().getHistory(), 
                "there are 2 connected components\n"
                + "the largest connected component has 3 elements\n"
                + "the diameter of the largest component is 2");
        graph.addEdge(0, 1);
    }
}
