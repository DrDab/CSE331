package graph.junitTests;

import graph.Graph;
import org.junit.Test;
import org.junit.BeforeClass;

import java.util.List;

import static org.junit.Assert.*;

public class TestGraph
{
    @Test
    public void testCreateEmptyGraph()
    {
        Graph g = new Graph();
        assertEquals(0, g.getNodeCount());
        assertEquals(0, g.getEdgeCount());
        assertTrue(g.getNodes().isEmpty());
    }

    private static final String[] ALPHABETICAL_NODES = new String[]{
            "ALPHA",
            "BRAVO",
            "CHARLIE",
            "DELTA",
            "ECHO",
            "FOXTROT",
            "GOLF",
            "HOTEL",
            "INDIA",
            "JULIET",
            "KILO"
    };

    @Test
    public void testAddNodesToGraph()
    {
        Graph g = new Graph();

        g.addNode("ALPHA");

        assertEquals(1, g.getNodeCount());
        assertTrue(g.hasNode("ALPHA"));

        g.addNode("BRAVO");

        assertEquals(2, g.getNodeCount());
        assertTrue(g.hasNode("BRAVO"));

        g.addNode("CHARLIE");
        g.addNode("DELTA");

        assertEquals(4, g.getNodeCount());
        assertTrue(g.hasNode("CHARLIE"));
        assertTrue(g.hasNode("DELTA"));

        g.addNode("ECHO");
        g.addNode("FOXTROT");
        g.addNode("GOLF");

        assertEquals(7, g.getNodeCount());
        assertTrue(g.hasNode("ECHO"));
        assertTrue(g.hasNode("FOXTROT"));
        assertTrue(g.hasNode("GOLF"));

        g.addNode("HOTEL");
        g.addNode("INDIA");
        g.addNode("JULIET");
        g.addNode("KILO");

        assertEquals(11, g.getNodeCount());
        assertTrue(g.hasNode("HOTEL"));
        assertTrue(g.hasNode("INDIA"));
        assertTrue(g.hasNode("JULIET"));
        assertTrue(g.hasNode("KILO"));

        List<String> nodes = g.getNodes();

        for (String expectedNode : ALPHABETICAL_NODES)
        {
            assertTrue(g.hasNode(expectedNode));
            assertTrue(nodes.contains(expectedNode));
        }
    }

    @Test
    public void testAddEdgesToGraph()
    {
        Graph g = new Graph();

        g.addNode("ALPHA");
        g.addNode("BRAVO");
        g.addNode("CHARLIE");

        assertEquals(0, g.getEdgeCount());
        assertFalse(g.hasEdge("ALPHA", "BRAVO", "ab1"));

        g.addEdge("ALPHA", "BRAVO", "ab1");

        assertEquals(1, g.getEdgeCount());
        assertTrue(g.hasEdge("ALPHA", "BRAVO", "ab1"));
        assertFalse(g.hasEdge("BRAVO", "ALPHA", "ab1"));

        g.addEdge("ALPHA", "BRAVO", "ab2");
        g.addEdge("ALPHA", "BRAVO", "ab3");

        assertEquals(3, g.getEdgeCount());
        assertTrue(g.hasEdge("ALPHA", "BRAVO", "ab2"));
        assertTrue(g.hasEdge("ALPHA", "BRAVO", "ab3"));

        assertFalse(g.hasEdge("BRAVO", "ALPHA", "ba1"));
        assertFalse(g.hasEdge("BRAVO", "ALPHA", "ba2"));

        g.addEdge("BRAVO", "ALPHA", "ba1");
        g.addEdge("BRAVO", "ALPHA", "ba2");

        assertEquals(5, g.getEdgeCount());
        assertTrue(g.hasEdge("BRAVO", "ALPHA", "ba1"));
        assertTrue(g.hasEdge("BRAVO", "ALPHA", "ba2"));

        g.addEdge("ALPHA", "CHARLIE", "ac1");

        assertEquals(6, g.getEdgeCount());
        assertTrue(g.hasEdge("ALPHA", "CHARLIE", "ac1"));
        assertTrue(g.hasEdge("CHARLIE", "ALPHA", "ac1"));

        // test reflexive edge
        g.addEdge("ALPHA", "ALPHA", "reflexiveAA");
        assertEquals(7, g.getEdgeCount());
        assertTrue(g.hasEdge("ALPHA", "ALPHA", "reflexiveAA"));
    }

    @Test
    public void testRenameNodesFromGraph()
    {
        Graph g = new Graph();

        g.addNode("ALPHA");
        g.addNode("BRAVO");
        g.addNode("CHARLIE");
        g.addNode("Dubs");
        g.addNode("Harry");

        g.addEdge("ALPHA", "Dubs", "applePen");
        g.addEdge("ALPHA", "BRAVO", "bananaBoat");
        g.addEdge("Harry", "Dubs", "huskies");

        g.renameNode("ALPHA", "Brody");
        g.renameNode("BRAVO", "Elan");
        g.renameNode("CHARLIE", "Logan");
        g.renameNode("Dubs", "Doki");
        g.renameNode("Harry", "Harry"); // test reflexive re-naming changes nothing

        assertFalse(g.hasNode("ALPHA"));
        assertTrue(g.hasNode("Brody"));

        assertFalse(g.hasNode("BRAVO"));
        assertTrue(g.hasNode("Elan"));

        assertFalse(g.hasNode("CHARLIE"));
        assertTrue(g.hasNode("Logan"));

        assertFalse(g.hasNode("Dubs"));
        assertTrue(g.hasNode("Doki"));

        assertTrue(g.hasNode("Harry"));

        assertFalse(g.hasEdge("ALPHA", "Dubs", "applePen"));
        assertTrue(g.hasEdge("Brody", "Dubs", "applePen"));

        assertFalse(g.hasEdge("ALPHA", "BRAVO", "bananaBoat"));
        assertTrue(g.hasEdge("Brody", "Elan", "bananaBoat"));

        assertFalse(g.hasEdge("Harry", "Dubs", "huskies"));
        assertTrue(g.hasEdge("Harry", "Doki", "huskies"));
    }

    @Test
    public void testDeleteNodesFromGraph()
    {
        Graph g = new Graph();
        g.addNode("ALPHA");
        g.addNode("BRAVO");
        g.addNode("CHARLIE");
        g.addNode("Dubs");
        g.addNode("Harry");

        g.addEdge("ALPHA", "Dubs", "applePen");
        g.addEdge("ALPHA", "BRAVO", "bananaBoat");
        g.addEdge("Harry", "Dubs", "huskies");

        g.deleteNode("ALPHA");

        assertFalse(g.hasNode("ALPHA"));
        assertFalse(g.hasEdge("ALPHA", "Dubs", "applePen"));
        assertFalse(g.hasEdge("ALPHA", "BRAVO", "bananaBoat"));

        g.deleteNode("Dubs");
        assertFalse(g.hasEdge("Harry", "Dubs", "huskies"));
    }

    @Test
    public void testEdgeOperations()
    {
        Graph g = new Graph();
        g.addNode("ALPHA");
        g.addNode("BRAVO");
        g.addNode("CHARLIE");
        g.addNode("Dubs");
        g.addNode("Harry");

        g.addEdge("ALPHA", "Dubs", "applePen");
        g.addEdge("ALPHA", "BRAVO", "bananaBoat");
        g.addEdge("Harry", "Dubs", "huskies");

        List<String> alphaChildNodes = g.getChildNodes("ALPHA");
        assertTrue(alphaChildNodes.contains("Dubs"));
        assertTrue(alphaChildNodes.contains("BRAVO"));
        assertFalse(alphaChildNodes.contains("CHARLIE"));

        List<String> harryChildNodes = g.getChildNodes("Harry");
        assertTrue(alphaChildNodes.contains("Dubs"));
        assertFalse(alphaChildNodes.contains("BRAVO"));
        assertFalse(alphaChildNodes.contains("CHARLIE"));

        List<String> alphaDubsEdges = g.getEdges("ALPHA", "Dubs");
        assertTrue(alphaDubsEdges.contains("applePen"));
        assertFalse(alphaDubsEdges.contains("bananaBoat"));

        List<String> harryDubsEdges = g.getEdges("Harry", "Dubs");
        assertTrue(harryDubsEdges.contains("huskies"));
        assertFalse(alphaDubsEdges.contains("bananaBoat"));

        List<String> alphaBravoEdges = g.getEdges("ALPHA", "BRAVO");
        assertTrue(alphaBravoEdges.contains("bananaBoat"));
        assertFalse(alphaChildNodes.contains("applePen"));
        assertFalse(alphaChildNodes.contains("huskies"));
    }

    @Test
    public void testRenamingEdges()
    {
        Graph g = new Graph();
        g.addNode("ALPHA");
        g.addNode("BRAVO");
        g.addNode("CHARLIE");
        g.addNode("Dubs");
        g.addNode("Harry");

        g.addEdge("ALPHA", "Dubs", "applePen1");
        g.addEdge("ALPHA", "BRAVO", "bananaBoat1");
        g.addEdge("Harry", "Dubs", "huskies1");

        g.renameEdge("ALPHA", "Dubs", "applePen1", "applePen");
        g.renameEdge("ALPHA", "BRAVO", "bananaBoat1", "bananaBoat");
        g.renameEdge("Harry", "Dubs", "huskies1", "huskies");

        List<String> alphaChildNodes = g.getChildNodes("ALPHA");
        assertTrue(alphaChildNodes.contains("Dubs"));
        assertTrue(alphaChildNodes.contains("BRAVO"));
        assertFalse(alphaChildNodes.contains("CHARLIE"));

        List<String> harryChildNodes = g.getChildNodes("Harry");
        assertTrue(alphaChildNodes.contains("Dubs"));
        assertFalse(alphaChildNodes.contains("BRAVO"));
        assertFalse(alphaChildNodes.contains("CHARLIE"));

        List<String> alphaDubsEdges = g.getEdges("ALPHA", "Dubs");
        assertTrue(alphaDubsEdges.contains("applePen"));
        assertFalse(alphaDubsEdges.contains("bananaBoat"));

        List<String> harryDubsEdges = g.getEdges("Harry", "Dubs");
        assertTrue(harryDubsEdges.contains("huskies"));
        assertFalse(alphaDubsEdges.contains("bananaBoat"));

        List<String> alphaBravoEdges = g.getEdges("ALPHA", "BRAVO");
        assertTrue(alphaBravoEdges.contains("bananaBoat"));
        assertFalse(alphaChildNodes.contains("applePen"));
        assertFalse(alphaChildNodes.contains("huskies"));
    }
}
