package graph.junitTests;

import graph.Graph;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestGraph
{
    private Graph g0; // empty graph
    private Graph g1; // graph w/ 1 node
    private Graph g2; // graph w/ 2 nodes
    private Graph g3; // graph w/ 3 nodes

    @Before
    public void createSampleGraphs()
    {
        g0 = new Graph();
        g1 = new Graph();
        g2 = new Graph();
        g3 = new Graph();

        g1.addNode("ALPHA");

        g2.addNode("ALPHA");
        g2.addNode("BRAVO");

        g3.addNode("ALPHA");
        g3.addNode("BRAVO");
        g3.addNode("CHARLIE");
    }

    @Test
    public void testCreateEmptyGraph()
    {
        Graph g = new Graph();
        assertEquals(0, g.getNodeCount());
        assertEquals(0, g.getEdgeCount());
        assertTrue(g.getNodes().isEmpty());
    }

    @Test
    public void testAddNodesToEmptyGraph()
    {
        g0.addNode("ALPHA");
        assertEquals(1, g0.getNodeCount());
        assertTrue(g0.hasNode("ALPHA"));

        g0.addNode("BRAVO");
        assertEquals(2, g0.getNodeCount());
        assertTrue(g0.hasNode("ALPHA"));
        assertTrue(g0.hasNode("BRAVO"));

        g0.addNode("CHARLIE");
        assertEquals(3, g0.getNodeCount());
        assertTrue(g0.hasNode("ALPHA"));
        assertTrue(g0.hasNode("BRAVO"));
        assertTrue(g0.hasNode("CHARLIE"));
    }

    @Test
    public void testAddNodesTo1NodeGraph()
    {
        g1.addNode("BRAVO");
        assertEquals(2, g1.getNodeCount());
        assertTrue(g1.hasNode("ALPHA"));
        assertTrue(g1.hasNode("BRAVO"));

        g1.addNode("CHARLIE");
        assertEquals(3, g1.getNodeCount());
        assertTrue(g1.hasNode("ALPHA"));
        assertTrue(g1.hasNode("BRAVO"));
        assertTrue(g1.hasNode("CHARLIE"));

        g1.addNode("DELTA");
        assertEquals(4, g1.getNodeCount());
        assertTrue(g1.hasNode("ALPHA"));
        assertTrue(g1.hasNode("BRAVO"));
        assertTrue(g1.hasNode("CHARLIE"));
        assertTrue(g1.hasNode("DELTA"));
    }

    @Test
    public void testAddNodesTo2NodeGraph()
    {
        g2.addNode("CHARLIE");
        assertEquals(3, g2.getNodeCount());
        assertTrue(g2.hasNode("ALPHA"));
        assertTrue(g2.hasNode("BRAVO"));
        assertTrue(g2.hasNode("CHARLIE"));

        g2.addNode("DELTA");
        assertEquals(4, g2.getNodeCount());
        assertTrue(g2.hasNode("ALPHA"));
        assertTrue(g2.hasNode("BRAVO"));
        assertTrue(g2.hasNode("CHARLIE"));
        assertTrue(g2.hasNode("DELTA"));

        g2.addNode("ECHO");
        assertEquals(5, g2.getNodeCount());
        assertTrue(g2.hasNode("ALPHA"));
        assertTrue(g2.hasNode("BRAVO"));
        assertTrue(g2.hasNode("CHARLIE"));
        assertTrue(g2.hasNode("DELTA"));
        assertTrue(g2.hasNode("ECHO"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateNodeTo1NodeGraph()
    {
        g1.addNode("ALPHA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateNodeTo2NodeGraph()
    {
        g2.addNode("ALPHA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEdgesToEmptyGraph()
    {
        // test adding edges to empty graph to ensure IllegalArgumentException thrown.
        g0.addEdge("ALPHA", "ALPHA", "aa1");
    }

    @Test
    public void testAddEdgesTo1NodeGraph()
    {
        g1.addEdge("ALPHA", "ALPHA", "aa1");
        assertEquals(1, g1.getEdgeCount());
        assertTrue(g1.hasEdge("ALPHA", "ALPHA", "aa1"));
        assertTrue(g1.getChildNodes("ALPHA").contains("ALPHA"));
        assertTrue(g1.getEdges("ALPHA", "ALPHA").contains("aa1"));

        g1.addEdge("ALPHA", "ALPHA", "aa2");
        assertEquals(2, g1.getEdgeCount());
        assertTrue(g1.hasEdge("ALPHA", "ALPHA", "aa1"));
        assertTrue(g1.hasEdge("ALPHA", "ALPHA", "aa2"));
        assertTrue(g1.getChildNodes("ALPHA").contains("ALPHA"));
        assertTrue(g1.getEdges("ALPHA", "ALPHA").contains("aa1"));
        assertTrue(g1.getEdges("ALPHA", "ALPHA").contains("aa2"));

        g1.addEdge("ALPHA", "ALPHA", "aa3");
        assertEquals(3, g1.getEdgeCount());
        assertTrue(g1.hasEdge("ALPHA", "ALPHA", "aa1"));
        assertTrue(g1.hasEdge("ALPHA", "ALPHA", "aa2"));
        assertTrue(g1.hasEdge("ALPHA", "ALPHA", "aa3"));
        assertTrue(g1.getChildNodes("ALPHA").contains("ALPHA"));
        assertTrue(g1.getEdges("ALPHA", "ALPHA").contains("aa1"));
        assertTrue(g1.getEdges("ALPHA", "ALPHA").contains("aa2"));
        assertTrue(g1.getEdges("ALPHA", "ALPHA").contains("aa3"));
    }

    @Test
    public void testAddEdgesTo2NodeGraph()
    {
        g2.addEdge("ALPHA", "ALPHA", "aa1");
        assertEquals(1, g2.getEdgeCount());
        assertTrue(g2.hasEdge("ALPHA", "ALPHA", "aa1"));
        assertTrue(g2.getChildNodes("ALPHA").contains("ALPHA"));
        assertTrue(g2.getEdges("ALPHA", "ALPHA").contains("aa1"));

        g2.addEdge("ALPHA", "BRAVO", "ab1");
        assertEquals(2, g2.getEdgeCount());
        assertTrue(g2.hasEdge("ALPHA", "ALPHA", "aa1"));
        assertTrue(g2.hasEdge("ALPHA", "BRAVO", "ab1"));
        assertTrue(g2.getChildNodes("ALPHA").contains("ALPHA"));
        assertTrue(g2.getChildNodes("ALPHA").contains("BRAVO"));
        assertTrue(g2.getEdges("ALPHA", "ALPHA").contains("aa1"));
        assertTrue(g2.getEdges("ALPHA", "BRAVO").contains("ab1"));

        g2.addEdge("BRAVO", "ALPHA", "ba1");
        assertEquals(3, g2.getEdgeCount());
        assertTrue(g2.hasEdge("ALPHA", "ALPHA", "aa1"));
        assertTrue(g2.hasEdge("ALPHA", "BRAVO", "ab1"));
        assertTrue(g2.hasEdge("BRAVO", "ALPHA", "ba1"));
        assertTrue(g2.getChildNodes("ALPHA").contains("ALPHA"));
        assertTrue(g2.getChildNodes("ALPHA").contains("BRAVO"));
        assertTrue(g2.getChildNodes("BRAVO").contains("ALPHA"));
        assertTrue(g2.getEdges("ALPHA", "ALPHA").contains("aa1"));
        assertTrue(g2.getEdges("ALPHA", "BRAVO").contains("ab1"));
        assertTrue(g2.getEdges("BRAVO", "ALPHA").contains("ba1"));
    }

    @Test
    public void testAddEdgesTo3NodeGraph()
    {
        g3.addEdge("ALPHA", "ALPHA", "aa1");
        assertEquals(1, g3.getEdgeCount());
        assertTrue(g3.hasEdge("ALPHA", "ALPHA", "aa1"));
        assertTrue(g3.getChildNodes("ALPHA").contains("ALPHA"));
        assertTrue(g3.getEdges("ALPHA", "ALPHA").contains("aa1"));

        g3.addEdge("ALPHA", "BRAVO", "ab1");
        assertEquals(2, g3.getEdgeCount());
        assertTrue(g3.hasEdge("ALPHA", "ALPHA", "aa1"));
        assertTrue(g3.hasEdge("ALPHA", "BRAVO", "ab1"));
        assertTrue(g3.getChildNodes("ALPHA").contains("ALPHA"));
        assertTrue(g3.getChildNodes("ALPHA").contains("BRAVO"));
        assertTrue(g3.getEdges("ALPHA", "ALPHA").contains("aa1"));
        assertTrue(g3.getEdges("ALPHA", "BRAVO").contains("ab1"));

        g3.addEdge("ALPHA", "CHARLIE", "ac1");
        assertEquals(3, g3.getEdgeCount());
        assertTrue(g3.hasEdge("ALPHA", "ALPHA", "aa1"));
        assertTrue(g3.hasEdge("ALPHA", "BRAVO", "ab1"));
        assertTrue(g3.hasEdge("ALPHA", "CHARLIE", "ac1"));
        assertTrue(g3.getChildNodes("ALPHA").contains("ALPHA"));
        assertTrue(g3.getChildNodes("ALPHA").contains("BRAVO"));
        assertTrue(g3.getChildNodes("ALPHA").contains("CHARLIE"));
        assertTrue(g3.getEdges("ALPHA", "ALPHA").contains("aa1"));
        assertTrue(g3.getEdges("ALPHA", "BRAVO").contains("ab1"));
        assertTrue(g3.getEdges("ALPHA", "CHARLIE").contains("ac1"));

        g3.addEdge("BRAVO", "CHARLIE", "bc1");
        assertEquals(4, g3.getEdgeCount());
        assertTrue(g3.hasEdge("ALPHA", "ALPHA", "aa1"));
        assertTrue(g3.hasEdge("ALPHA", "BRAVO", "ab1"));
        assertTrue(g3.hasEdge("ALPHA", "CHARLIE", "ac1"));
        assertTrue(g3.hasEdge("BRAVO", "CHARLIE", "bc1"));
        assertTrue(g3.getChildNodes("ALPHA").contains("ALPHA"));
        assertTrue(g3.getChildNodes("ALPHA").contains("BRAVO"));
        assertTrue(g3.getChildNodes("ALPHA").contains("CHARLIE"));
        assertTrue(g3.getChildNodes("BRAVO").contains("CHARLIE"));
        assertTrue(g3.getEdges("ALPHA", "ALPHA").contains("aa1"));
        assertTrue(g3.getEdges("ALPHA", "BRAVO").contains("ab1"));
        assertTrue(g3.getEdges("ALPHA", "CHARLIE").contains("ac1"));
        assertTrue(g3.getEdges("BRAVO", "CHARLIE").contains("bc1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateEdgeTo1NodeGraph()
    {
        g1.addEdge("ALPHA", "ALPHA", "aa1");
        g1.addEdge("ALPHA", "ALPHA", "aa1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateEdgeTo2NodeGraph()
    {
        g2.addEdge("ALPHA", "BRAVO", "ab1");
        g2.addEdge("ALPHA", "BRAVO", "ab1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateEdgeTo3NodeGraph()
    {
        g2.addEdge("ALPHA", "CHARLIE", "ac1");
        g2.addEdge("ALPHA", "CHARLIE", "ac1");
    }

    @Test
    public void testCyclicPathsAllowedOn2NodeGraph()
    {
        g2.addEdge("ALPHA", "BRAVO", "ab");
        g2.addEdge("BRAVO", "ALPHA", "ab");
        assertTrue(g2.getChildNodes("ALPHA").contains("BRAVO"));
        assertTrue(g2.getChildNodes("BRAVO").contains("ALPHA"));
    }

    @Test
    public void testCyclicPathsAllowedOn3NodeGraph()
    {
        g3.addEdge("ALPHA", "BRAVO", "ab");
        g3.addEdge("BRAVO", "CHARLIE", "bc");
        g3.addEdge("CHARLIE", "ALPHA", "ca");
        assertTrue(g3.getChildNodes("ALPHA").contains("BRAVO"));
        assertTrue(g3.getChildNodes("BRAVO").contains("CHARLIE"));
        assertTrue(g3.getChildNodes("CHARLIE").contains("ALPHA"));
    }

    @Test
    public void testReflexiveEdgeAllowedOn1NodeGraph()
    {
        g1.addEdge("ALPHA", "ALPHA", "aa");
        assertTrue(g1.hasEdge("ALPHA", "ALPHA", "aa"));
        assertTrue(g1.getEdges("ALPHA", "ALPHA").contains("aa"));
        assertTrue(g1.getChildNodes("ALPHA").contains("ALPHA"));
    }

    @Test
    public void testReflexiveEdgeAllowedOn2NodeGraph()
    {
        g2.addEdge("ALPHA", "ALPHA", "aa");
        g2.addEdge("BRAVO", "BRAVO", "bb");
        assertTrue(g2.hasEdge("ALPHA", "ALPHA", "aa"));
        assertTrue(g2.getEdges("ALPHA", "ALPHA").contains("aa"));
        assertTrue(g2.getChildNodes("ALPHA").contains("ALPHA"));
        assertTrue(g2.hasEdge("BRAVO", "BRAVO", "bb"));
        assertTrue(g2.getEdges("BRAVO", "BRAVO").contains("bb"));
        assertTrue(g2.getChildNodes("BRAVO").contains("BRAVO"));
    }
}
