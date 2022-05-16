package graph;

import java.util.*;

/**
 * Represents a mutable, directed labelled graph where a set of nodes of type N can be connected
 * via directed edges labelled with type E (each edge connecting a parent node to a child node), and there
 * exist no two edges in the graph between any two parent and child nodes such that the edges
 * have the same label.
 */
public class DirectedGraph<N, E>
{
    public static final boolean DEBUG = false;

    // Representation Invariant (RI): nodes != null, adjList != null,
    //                                nodes has no nulls, adjList has no mappings to null sets or null Edges
    //                                in the sets, adjList has a mapping to a set of Edges for each node in nodes,
    //                                and numEdges is the total number of Edges in adjList.
    //
    // Abstraction Function:
    //      AF(this) = a directed graph with the HashSet set of nodes "nodes" and
    //                 an adjacency list "adjList" represented with a HashMap mapping
    //                 each source node to the set of outbound edges from that
    //                 source node.
    //
    private Set<N> nodes;
    private Map<N, Set<Edge<N, E>>> adjList;
    private int numEdges;

    /**
     * Checks the representation invariant of this DirectedGraph to ensure rep invariant
     * holds before and after this DirectedGraph is mutated.
     */
    private void checkRep()
    {
        assert nodes != null;
        assert adjList != null;

        if (DEBUG)
        {
            // run runtime-intensive tests if debugging
            int actualEdgeCount = 0;

            for (N sourceNode : nodes)
            {
                assert sourceNode != null;
                assert adjList.containsKey(sourceNode);
                assert adjList.get(sourceNode) != null;

                for (Edge e : adjList.get(sourceNode))
                {
                    assert e != null;
                    actualEdgeCount++;
                }
            }

            assert actualEdgeCount == numEdges;
        }
    }

    /**
     * Instantiates an empty graph with no nodes and no edges.
     */
    public DirectedGraph()
    {
        nodes = new HashSet<>();
        adjList = new HashMap<>();
        numEdges = 0;
        checkRep();
    }

    /**
     * Adds a given node, targetNode, to the graph.
     * If node targetNode already exists in the graph, throws
     * IllegalArgumentException
     *
     * @param targetNode the node to add to the graph.
     * @throws IllegalArgumentException if the node targetNode
     *          already exists in this graph.
     * @spec.requires targetNode != null and contains only ASCII characters.
     * @spec.effects targetNode is added to this graph.
     */
    public void addNode(N targetNode)
    {
        checkRep();

        if (nodes.contains(targetNode))
            throw new IllegalArgumentException(String.format("Node %s already exists in graph!", targetNode));

        nodes.add(targetNode);
        adjList.put(targetNode, new HashSet<>());
        checkRep();
    }

    /**
     * Returns whether a given node targetNode exists in the graph.
     *
     * @param targetNode the node to check the existence of in the graph.
     * @spec.requires targetNode != null and contains only ASCII characters.
     * @return true iff the node targetNode exists in this graph.
     */
    public boolean hasNode(N targetNode)
    {
        return nodes.contains(targetNode);
    }

    /**
     * Returns a list of the all nodes in the graph.
     * If the graph has no nodes, returns an empty list.
     *
     * @return a list of all nodes in the graph,
     *         or an empty list if the graph has no nodes.
     */
    public List<N> getNodes()
    {
        List<N> res = new ArrayList<>();

        for (N node : nodes)
            res.add(node);

        return res;
    }

    /**
     * Returns a set of the child nodes of node sourceNode in the graph.
     *
     * @param sourceNode the node in the Graph to
     *                 return a list of child nodes of.
     * @spec.requires sourceNode != null and contains only ASCII characters.
     * @throws IllegalArgumentException if the graph has no node sourceNode
     * @return a set of the child nodes of the node sourceNode.
     */
    @SuppressWarnings("unchecked")
    public Set<N> getChildNodes(N sourceNode)
    {
        if (!nodes.contains(sourceNode))
            throw new IllegalArgumentException(String.format("Node %s doesn't exist in graph!", sourceNode));

        Set<N> resSet = new HashSet<>();

        for (Edge e : adjList.get(sourceNode))
            resSet.add((N) e.getDestNode());

        return resSet;
    }

    /**
     * Returns the number of nodes in the Graph.
     *
     * @return the number of nodes in the Graph.
     */
    public int getNodeCount()
    {
        return nodes.size();
    }

    /**
     * Adds a directed edge with the given label, label, to the Graph
     * from the node sourceNode to the node destNode. If node sourceNode or destNode
     * don't exist in this graph, or there already exists an edge from sourceNode to destNode with
     * the given label, then this method throws IllegalArgumentException.
     *
     * @param sourceNode the node the edge originates from
     * @param destNode the node the edge terminates on.
     * @param label the label of the edge.
     * @spec.requires sourceNode, destNode, label != null.
     * @spec.effects an edge with start node sourceNode, destination node destNode, and
     *               given label is added to this Graph.
     * @throws IllegalArgumentException if nodes sourceNode, destNode don't exist in this Graph
     *         or Graph already has an edge from sourceNode to destNode with the given label.
     */
    @SuppressWarnings("unchecked")
    public void addEdge(N sourceNode, N destNode, E label)
    {
        checkRep();

        if (!nodes.contains(sourceNode) || !nodes.contains(destNode))
        {
            String nodesStr = (!nodes.contains(sourceNode) && !nodes.contains(destNode)) ?
                    String.format("s %s and %s", sourceNode, destNode) :
                    (!nodes.contains(sourceNode)) ? " " + sourceNode : " " + destNode;
            throw new IllegalArgumentException(
                    String.format("Node%s must exist in the graph!", nodesStr));
        }

        Set<Edge<N, E>> edges = adjList.get(sourceNode);
        Edge e = new Edge(sourceNode, destNode, label);

        if (edges.contains(e))
            throw new IllegalArgumentException(
                    String.format("An edge from node %s to %s with label %s already exists!",
                            sourceNode, destNode, label));

        adjList.get(sourceNode).add(e);
        numEdges++;
        checkRep();
    }

    /**
     * Returns whether an edge exists in the Graph that starts from node sourceNode,
     * ends on node destNode, and has the label, label.
     *
     * @param sourceNode the node the edge originates from
     * @param destNode the node the edge terminates on.
     * @param label the label of the edge to check the existence of.
     * @spec.requires n1, n2, label != null
     * @return whether an edge exists in the Graph that starts from node sourceNode,
     *         ends on node destNode, and has the label, label.
     */
    public boolean hasEdge(N sourceNode, N destNode, E label)
    {
        if (!nodes.contains(sourceNode) || !nodes.contains(destNode))
            return false;

        return adjList.get(sourceNode).contains(new Edge<N, E>(sourceNode, destNode, label));
    }

    /**
     * Returns the list of edges that start from node sourceNode and end
     * on node destNode. If nodes sourceNode or destNode do not exist in the Graph,
     * this method will throw an IllegalArgumentException.
     *
     * @param sourceNode the node the edges originate from
     * @param destNode the node the edges terminate on.
     * @spec.requires sourceNode, destNode != null
     * @throws IllegalArgumentException if nodes sourceNode, destNode don't exist in this Graph.
     * @return the list of edges that start from node sourceNode and end
     *         on node destNode.
     */
    @SuppressWarnings("unchecked")
    public List<E> getEdges(N sourceNode, N destNode) throws IllegalArgumentException
    {
        if (!nodes.contains(sourceNode) || !nodes.contains(destNode))
        {
            String nodesStr = (!nodes.contains(sourceNode) && !nodes.contains(destNode)) ?
                    String.format("s %s and %s", sourceNode, destNode) :
                    (!nodes.contains(sourceNode)) ? " " + sourceNode : " " + destNode;
            throw new IllegalArgumentException(
                    String.format("Node%s must exist in the graph!", nodesStr));
        }

        List<E> res = new ArrayList<>();

        for (Edge e : adjList.get(sourceNode))
            if (e.getDestNode().equals(destNode))
                res.add((E) e.getLabel());

        return res;
    }

    /**
     * Returns the number of edges in the Graph.
     *
     * @return the number of edges in the Graph.
     */
    public int getEdgeCount()
    {
        return numEdges;
    }

}
