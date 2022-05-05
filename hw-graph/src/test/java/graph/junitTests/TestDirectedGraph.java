package graph.junitTests;

import graph.DirectedGraph;
import graph.Edge;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestDirectedGraph
{
    private DirectedGraph g0; // empty graph
    private DirectedGraph g1; // graph w/ 1 node
    private DirectedGraph g2; // graph w/ 2 nodes
    private DirectedGraph g3; // graph w/ 3 nodes

    @Before
    public void createSampleGraphs()
    {
        g0 = new DirectedGraph();
        g1 = new DirectedGraph();
        g2 = new DirectedGraph();
        g3 = new DirectedGraph();

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
        DirectedGraph g = new DirectedGraph();
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
        assertTrue(g0.getNodes().contains("ALPHA"));

        g0.addNode("BRAVO");
        assertEquals(2, g0.getNodeCount());
        assertTrue(g0.hasNode("ALPHA"));
        assertTrue(g0.hasNode("BRAVO"));
        assertTrue(g0.getNodes().contains("ALPHA"));
        assertTrue(g0.getNodes().contains("BRAVO"));

        g0.addNode("CHARLIE");
        assertEquals(3, g0.getNodeCount());
        assertTrue(g0.hasNode("ALPHA"));
        assertTrue(g0.hasNode("BRAVO"));
        assertTrue(g0.hasNode("CHARLIE"));
        assertTrue(g0.getNodes().contains("ALPHA"));
        assertTrue(g0.getNodes().contains("BRAVO"));
        assertTrue(g0.getNodes().contains("CHARLIE"));
    }

    @Test
    public void testAddNodesTo1NodeGraph()
    {
        g1.addNode("BRAVO");
        assertEquals(2, g1.getNodeCount());
        assertTrue(g1.hasNode("ALPHA"));
        assertTrue(g1.hasNode("BRAVO"));
        assertTrue(g1.getNodes().contains("ALPHA"));
        assertTrue(g1.getNodes().contains("BRAVO"));

        g1.addNode("CHARLIE");
        assertEquals(3, g1.getNodeCount());
        assertTrue(g1.hasNode("ALPHA"));
        assertTrue(g1.hasNode("BRAVO"));
        assertTrue(g1.hasNode("CHARLIE"));
        assertTrue(g1.getNodes().contains("ALPHA"));
        assertTrue(g1.getNodes().contains("BRAVO"));
        assertTrue(g1.getNodes().contains("CHARLIE"));

        g1.addNode("DELTA");
        assertEquals(4, g1.getNodeCount());
        assertTrue(g1.hasNode("ALPHA"));
        assertTrue(g1.hasNode("BRAVO"));
        assertTrue(g1.hasNode("CHARLIE"));
        assertTrue(g1.hasNode("DELTA"));
        assertTrue(g1.getNodes().contains("ALPHA"));
        assertTrue(g1.getNodes().contains("BRAVO"));
        assertTrue(g1.getNodes().contains("CHARLIE"));
        assertTrue(g1.getNodes().contains("DELTA"));
    }

    @Test
    public void testAddNodesTo2NodeGraph()
    {
        g2.addNode("CHARLIE");
        assertEquals(3, g2.getNodeCount());
        assertTrue(g2.hasNode("ALPHA"));
        assertTrue(g2.hasNode("BRAVO"));
        assertTrue(g2.hasNode("CHARLIE"));
        assertTrue(g2.getNodes().contains("ALPHA"));
        assertTrue(g2.getNodes().contains("BRAVO"));
        assertTrue(g2.getNodes().contains("CHARLIE"));

        g2.addNode("DELTA");
        assertEquals(4, g2.getNodeCount());
        assertTrue(g2.hasNode("ALPHA"));
        assertTrue(g2.hasNode("BRAVO"));
        assertTrue(g2.hasNode("CHARLIE"));
        assertTrue(g2.hasNode("DELTA"));
        assertTrue(g2.getNodes().contains("ALPHA"));
        assertTrue(g2.getNodes().contains("BRAVO"));
        assertTrue(g2.getNodes().contains("CHARLIE"));
        assertTrue(g2.getNodes().contains("DELTA"));

        g2.addNode("ECHO");
        assertEquals(5, g2.getNodeCount());
        assertTrue(g2.hasNode("ALPHA"));
        assertTrue(g2.hasNode("BRAVO"));
        assertTrue(g2.hasNode("CHARLIE"));
        assertTrue(g2.hasNode("DELTA"));
        assertTrue(g2.hasNode("ECHO"));
        assertTrue(g2.getNodes().contains("ALPHA"));
        assertTrue(g2.getNodes().contains("BRAVO"));
        assertTrue(g2.getNodes().contains("CHARLIE"));
        assertTrue(g2.getNodes().contains("DELTA"));
        assertTrue(g2.getNodes().contains("ECHO"));
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

    @Test
    public void testListNodesInEmptyGraph()
    {
        assertTrue(g0.getNodes().isEmpty());
        assertEquals(0, g0.getNodes().size());
    }

    @Test
    public void testListNodesIn1NodeGraph()
    {
        assertFalse(g1.getNodes().isEmpty());
        assertEquals(1, g1.getNodes().size());
        assertTrue(g1.hasNode("ALPHA"));
        assertTrue(g1.getNodes().contains("ALPHA"));
    }

    @Test
    public void testListNodesIn2NodeGraph()
    {
        assertFalse(g2.getNodes().isEmpty());
        assertEquals(2, g2.getNodes().size());
        assertTrue(g2.hasNode("ALPHA"));
        assertTrue(g2.hasNode("BRAVO"));
        assertTrue(g2.getNodes().contains("ALPHA"));
        assertTrue(g2.getNodes().contains("BRAVO"));
    }

    @Test
    public void testListNodesIn3NodeGraph()
    {
        assertFalse(g3.getNodes().isEmpty());
        assertEquals(3, g3.getNodes().size());
        assertTrue(g3.hasNode("ALPHA"));
        assertTrue(g3.hasNode("BRAVO"));
        assertTrue(g3.hasNode("CHARLIE"));
        assertTrue(g3.getNodes().contains("ALPHA"));
        assertTrue(g3.getNodes().contains("BRAVO"));
        assertTrue(g3.getNodes().contains("CHARLIE"));
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
        g3.addEdge("ALPHA", "CHARLIE", "ac1");
        g3.addEdge("ALPHA", "CHARLIE", "ac1");
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

    // Edge class tests below
    @Test(expected = IllegalArgumentException.class)
    public void testNullSrcNodeEdgeConstructorThrowsException()
    {
        Edge e = new Edge(null, "", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullDestNodeEdgeConstructorThrowsException()
    {
        Edge e = new Edge("", null, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLabelEdgeConstructorThrowsException()
    {
        Edge e = new Edge("", "", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSrcDestNodeEdgeConstructorThrowsException()
    {
        Edge e = new Edge(null, null, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullDestLabelNodeEdgeConstructorThrowsException()
    {
        Edge e = new Edge("", null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSrcLabelEdgeConstructorThrowsException()
    {
        Edge e = new Edge(null, "", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullAllParamsConstructorThrowsException()
    {
        Edge e = new Edge(null, null, null);
    }

    private Edge edge;
    private Edge edgeDup;
    private Edge edge2;
    private Edge edge2Dup;

    @Before
    public void initializeValidEdges()
    {
        edge = new Edge("mySrc", "myDest", "myLabel");
        edgeDup = new Edge("mySrc", "myDest", "myLabel");
        edge2 = new Edge("mySrc2", "myDest2", "myLabel2");
        edge2Dup = new Edge("mySrc2", "myDest2", "myLabel2");
    }

    @Test
    public void testGetSourceNode()
    {
        assertEquals("mySrc", edge.getSourceNode());
        assertEquals("mySrc", edgeDup.getSourceNode());
        assertEquals("mySrc2", edge2.getSourceNode());
        assertEquals("mySrc2", edge2Dup.getSourceNode());
    }

    @Test
    public void testGetDestNode()
    {
        assertEquals("myDest", edge.getDestNode());
        assertEquals("myDest", edgeDup.getDestNode());
        assertEquals("myDest2", edge2.getDestNode());
        assertEquals("myDest2", edge2Dup.getDestNode());
    }

    @Test
    public void testGetEdgeLabel()
    {
        assertEquals("myLabel", edge.getLabel());
        assertEquals("myLabel", edgeDup.getLabel());
        assertEquals("myLabel2", edge2.getLabel());
        assertEquals("myLabel2", edge2Dup.getLabel());
    }

    @Test
    public void testEdgeEquals()
    {
        // test symmetry of equals
        assertTrue(edge.equals(edgeDup));
        assertTrue(edgeDup.equals(edge));
        // test equals is consistent.
        assertEquals(edge, edgeDup);
        Edge nonEqualEdge = new Edge("notMySrc", "notMyDest", "notMyLabel");
        // test symmetry of equals when false
        assertFalse(edge.equals(nonEqualEdge));
        assertFalse(nonEqualEdge.equals(edge));
        nonEqualEdge = new Edge("mySrc", "notMyDest", "notMyLabel");
        // test symmetry of equals when false
        assertFalse(edge.equals(nonEqualEdge));
        assertFalse(nonEqualEdge.equals(edge));
        nonEqualEdge = new Edge("notMySrc", "myDest", "notMyLabel");
        // test symmetry of equals when false
        assertFalse(edge.equals(nonEqualEdge));
        assertFalse(nonEqualEdge.equals(edge));
        nonEqualEdge = new Edge("notMySrc", "notMyDest", "myLabel");
        // test symmetry of equals when false
        assertFalse(edge.equals(nonEqualEdge));
        assertFalse(nonEqualEdge.equals(edge));
        nonEqualEdge = new Edge("mySrc", "myDest", "notMyLabel");
        // test symmetry of equals when false
        assertFalse(edge.equals(nonEqualEdge));
        assertFalse(nonEqualEdge.equals(edge));
        nonEqualEdge = new Edge("mySrc", "notMyDest", "myLabel");
        // test symmetry of equals when false
        assertFalse(edge.equals(nonEqualEdge));
        assertFalse(nonEqualEdge.equals(edge));
        nonEqualEdge = new Edge("notMySrc", "myDest", "myLabel");
        // test symmetry of equals when false
        assertFalse(edge.equals(nonEqualEdge));
        assertFalse(nonEqualEdge.equals(edge));
    }

    @Test
    public void testEdgeHashCode()
    {
        // test if edge2 == edge2Dup, then edge2.hashCode() == edge2.hashCode()
        assertEquals(edge, edgeDup);
        assertEquals(edge.hashCode(), edgeDup.hashCode());
        // test hashCode is consistent
        assertEquals(edge.hashCode(), edgeDup.hashCode());

        // test if edge2 == edge2Dup, then edge2.hashCode() == edge2.hashCode()
        assertEquals(edge2, edge2Dup);
        assertEquals(edge2.hashCode(), edge2Dup.hashCode());
        // test hashCode is consistent, again.
        assertEquals(edge2.hashCode(), edge2Dup.hashCode());

        assertNotEquals(edge.hashCode(), edge2.hashCode());
        // test if edge.hashCode != edge2.hashCode, then edge != edge2
        //  (as that is the contrapositive of the implication that:
        //      if edge == edge2, then edge.hashCode == edge2.hashCode .)
        assertNotEquals(edge, edge2);
    }
}
