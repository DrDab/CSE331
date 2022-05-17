package pathfinder;

import graph.DirectedGraph;
import pathfinder.datastructures.Path;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class DijkstraPathfinder<T>
{
    private DirectedGraph<T, Double> graph;

    public DijkstraPathfinder(DirectedGraph<T, Double> graph)
    {
        this.graph = graph;
    }

    private Double getShortestDistance(T sourceNode, T destNode)
    {
        Double shortest = null;
        for (double distance : graph.getEdges(sourceNode, destNode))
        {
            shortest = shortest == null ? distance : Math.min(distance, shortest);
        }

        return shortest;
    }

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

            if (minDest.equals(sourceNode))
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
