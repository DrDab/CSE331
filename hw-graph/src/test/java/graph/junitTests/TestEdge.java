package graph.junitTests;

import graph.Edge;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestEdge
{
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
        assertEquals(edge, edgeDup);
        // test equals is consistent.
        assertEquals(edge, edgeDup);
        Edge nonEqualEdge = new Edge("notMySrc", "notMyDest", "notMyLabel");
        assertNotEquals(edge, nonEqualEdge);
        nonEqualEdge = new Edge("mySrc", "notMyDest", "notMyLabel");
        assertNotEquals(edge, nonEqualEdge);
        nonEqualEdge = new Edge("notMySrc", "myDest", "notMyLabel");
        assertNotEquals(edge, nonEqualEdge);
        nonEqualEdge = new Edge("notMySrc", "notMyDest", "myLabel");
        assertNotEquals(edge, nonEqualEdge);
        nonEqualEdge = new Edge("mySrc", "myDest", "notMyLabel");
        assertNotEquals(edge, nonEqualEdge);
        nonEqualEdge = new Edge("mySrc", "notMyDest", "myLabel");
        assertNotEquals(edge, nonEqualEdge);
        nonEqualEdge = new Edge("notMySrc", "myDest", "myLabel");
        assertNotEquals(edge, nonEqualEdge);
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
