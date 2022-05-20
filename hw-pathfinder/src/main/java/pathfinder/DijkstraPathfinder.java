package pathfinder;

import graph.DirectedGraph;
import pathfinder.datastructures.Path;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * A pathfinding utility that takes in a DirectedGraph of immutable node type T connected
 * by edges of non-negative Doubles representing the cost to travel from a given node to another, and
 * uses Dijkstra's shortest-path algorithm to find the shortest path from a given source
 * node in the graph to a given destination node.
 */
public class DijkstraPathfinder<T>
{
    // This class does not represent an ADT.

    // the graph on which to use pathfinding, where the edges are non-negative
    // Double costs to travel between nodes.
    private DirectedGraph<T, Double> graph;

    /**
     * Instantiates a DijkstraPathFinder for the given graph mapping immutable nodes
     * of type T to edges of non-negative doubles representing the distance from
     * one node to another.
     *
     * @param graph, the graph mapping nodes of type T to double edges of distance
     *               between nodes.
     * @spec.requires graph != null and has no edges &lt; 0
     */
    public DijkstraPathfinder(DirectedGraph<T, Double> graph)
    {
        this.graph = graph;
    }

    /**
     * Gets the shortest distance in the graph between
     * a given source node and a given destination node. Returns null if
     * there are no paths between the source and destination node.
     *
     * @param sourceNode, the source node to get the distance from
     * @param destNode, the destination node to get the distance to
     * @spec.requires sourceNode, destNode != null and exist in the graph
     * @return the shortest distance between sourceNode and destNode, or null
     *         if there are no paths from sourceNode to destNode.
     */
    public Double getShortestDistance(T sourceNode, T destNode)
    {
        Double shortest = null;

        for (double distance : graph.getEdges(sourceNode, destNode))
            shortest = shortest == null ? distance : Math.min(distance, shortest);

        return shortest;
    }

    /**
     * Uses Dijkstra's algorithm to return the shortest path from sourceNode
     * to destNode, or null if no path from sourceNode to destNode exists.
     *
     * @param sourceNode, the source node to get the shortest path from.
     * @param destNode, the destination node to get the distance to
     * @spec.requires sourceNode, destNode != null and exist in the graph
     * @return the shortest path from sourceNode to destNode in the graph, or
     *         null if no path from sourceNode to destNode exists.
     */
    public Path<T> getShortestPath(T sourceNode, T destNode)
    {
        PriorityQueue<Path<T>> active = new PriorityQueue<>(
                new Comparator<Path<T>>() {
            @Override
            public int compare(Path<T> a, Path<T> b) {
                return Double.compare(a.getCost(), b.getCost());
            }
        });

        Set<T> finished = new HashSet<>();

        active.add(new Path<>(sourceNode));

        while (!active.isEmpty())
        {
            Path<T> minPath = active.remove();
            T minDest = minPath.getEnd();

            if (minDest.equals(destNode))
                return minPath;

            if (finished.contains(minDest))
                continue;

            Set<T> childNodes = graph.getChildNodes(minDest);

            for (T child : childNodes)
            {
                if (!finished.contains(child))
                {
                    Path<T> newPath = minPath.extend(child, getShortestDistance(minDest, child));
                    active.add(newPath);
                }
            }

            finished.add(minDest);
        }

        return null;
    }
}
