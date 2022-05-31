package pathfinder.junitTests;

import graph.DirectedGraph;
import org.junit.Before;
import org.junit.Test;
import pathfinder.DijkstraPathfinder;

import static org.junit.Assert.assertEquals;

public class TestDijkstraPathfinder
{
    private DirectedGraph<String, Double> graph;

    @Before
    public void initDijkstraPathfinder()
    {
        graph = new DirectedGraph<>();
        graph.addNode("Canterlot");
        graph.addNode("Ponyville");
        graph.addNode("Manehattan");
        graph.addNode("Everfree Forest");
        graph.addEdge("Ponyville", "Canterlot", 1.337);
        graph.addEdge("Ponyville", "Manehattan", 7355.608);
    }

    @Test
    public void testGetShortestDistanceNoPath()
    {
        assertEquals(null, DijkstraPathfinder.getShortestDistance(graph, "Canterlot",
                "Ponyville"));
        assertEquals(null, DijkstraPathfinder.getShortestDistance(graph, "Manehattan",
                "Ponyville"));
    }

    @Test
    public void testGetPathNoValidPath()
    {
        // make sure getShortestDistance method returns null when no valid paths
        // exist from the source node to destination node.
        assertEquals(null, DijkstraPathfinder.getShortestDistance(graph, "Canterlot",
                "Ponyville"));
        assertEquals(null, DijkstraPathfinder.getShortestDistance(graph, "Manehattan",
                "Ponyville"));
        assertEquals(null, DijkstraPathfinder.getShortestDistance(graph, "Everfree Forest",
                "Ponyville"));
    }
}
