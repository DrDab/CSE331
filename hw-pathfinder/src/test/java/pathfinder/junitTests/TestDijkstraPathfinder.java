package pathfinder.junitTests;

import graph.DirectedGraph;
import org.junit.Before;
import org.junit.Test;
import pathfinder.DijkstraPathfinder;

import static org.junit.Assert.assertEquals;

public class TestDijkstraPathfinder
{
    private DijkstraPathfinder<String> pFinder;

    @Before
    public void initDijkstraPathfinder()
    {
        DirectedGraph<String, Double> graph = new DirectedGraph<>();
        graph.addNode("Canterlot");
        graph.addNode("Ponyville");
        graph.addNode("Manehattan");
        graph.addNode("Everfree Forest");
        graph.addEdge("Ponyville", "Canterlot", 1.337);
        graph.addEdge("Ponyville", "Manehattan", 7355.608);
        pFinder = new DijkstraPathfinder<>(graph);
    }

    @Test
    public void testGetShortestDistanceNoPath()
    {
        assertEquals(null, pFinder.getShortestDistance("Canterlot",
                "Ponyville"));
        assertEquals(null, pFinder.getShortestDistance("Manehattan",
                "Ponyville"));
    }

    @Test
    public void testGetPathNoValidPath()
    {
        // make sure getShortestDistance method returns null when no valid paths
        // exist from the source node to destination node.
        assertEquals(null, pFinder.getShortestDistance("Canterlot",
                "Ponyville"));
        assertEquals(null, pFinder.getShortestDistance("Manehattan",
                "Ponyville"));
        assertEquals(null, pFinder.getShortestDistance("Everfree Forest",
                "Ponyville"));
    }
}
