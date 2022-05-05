package graph;

import java.util.List;

/**
 * Represents a mutable, directed labelled graph where a set of labelled nodes can be connected
 * via directed, labelled edges (each edge connecting a parent node to a child node), and each edge
 * has a unique name and there exists no two edges between any two parent and child nodes in the graph
 * such that the edges have the same label.
 */
public class DirectedGraph
{

    /**
     * Instantiates an empty Graph with no nodes and no edges.
     */
    public DirectedGraph()
    {
        throw new RuntimeException("Constructor not yet implemented!");
    }

    /**
     * Adds a node with the given name, name, to the Graph.
     * If a node with the given name already exists in the Graph, throws
     * IllegalArgumentException
     *
     * @param name the name of the node to add to the graph.
     * @throws IllegalArgumentException if a node with the given name
     *          already exists in the Graph.
     * @spec.requires name != null and contains only ASCII characters.
     * @spec.effects a node is added to the Graph with the name given.
     */
    public void addNode(String name) throws IllegalArgumentException
    {
        throw new RuntimeException("Method not yet implemented!");
    }

    /**
     * Returns whether a node with the given name, name, exists in the Graph.
     * If name is null, throws IllegalArgumentException.
     *
     * @param name the name of the node to check the existence of in the Graph.
     * @spec.requires name != null and contains only ASCII characters.
     * @return true iff a node exists in the graph with the name given
     */
    public boolean hasNode(String name)
    {
        throw new RuntimeException("Method not yet implemented!");
    }

    /**
     * Returns a list of the names of all nodes in the Graph.
     * If the Graph has no nodes, returns an empty list.
     *
     * @return a list of the names of all nodes in the Graph,
     *         or an empty list if the Graph has no nodes.
     */
    public List<String> getNodes()
    {
        throw new RuntimeException("Method not yet implemented!");
    }

    /**
     * Returns a list of the names of the child nodes of a node
     * with the given name nodeName in the Graph.
     *
     * @param nodeName the name of the node in the Graph to
     *                 return a list of child nodes of.
     * @spec.requires nodeName != null and contains only ASCII characters.
     * @throws IllegalArgumentException if the Graph has no node with the name nodeName.
     * @return a list of the names of the child nodes of the node
     *          with the given name.
     */
    public List<String> getChildNodes(String nodeName) throws IllegalArgumentException
    {
        throw new RuntimeException("Method not yet implemented!");
    }

    /**
     * Returns the number of nodes in the Graph.
     *
     * @return the number of nodes in the Graph.
     */
    public int getNodeCount()
    {
        throw new RuntimeException("Method not yet implemented!");
    }

    /**
     * Adds a directed edge with the given String label, label, to the Graph
     * from the node named n1 to the node named n2. If node n1 or node n2
     * doesn't exist, or there exists an edge from node n1 to node n2 with
     * the given label, then this method throws IllegalArgumentException.
     *
     * @param n1 the name of the node the edge originates from
     * @param n2 the name of the node the edge terminates on.
     * @param label the label of the edge.
     * @spec.requires n1, n2, label != null, and there exist nodes
     *                with names n1, n2 in this Graph, and no edge in this Graph
     *                starting from n1 and ending on n2 has the label, label.
     * @spec.effects an edge with start node n1, destination node n2, and
     *               label named label is added to this Graph.
     * @throws IllegalArgumentException if nodes with names n1, n2 don't exist in this Graph
     *         or Graph already has an edge from node n1 to node n2 with the given label.
     */
    public void addEdge(String n1, String n2, String label) throws IllegalArgumentException
    {
        throw new RuntimeException("Method not yet implemented!");
    }

    /**
     * Returns whether an edge exists in the Graph that starts from node n1,
     * ends on node n2, and has the label, label.
     *
     * @param n1 the name of the node the edge originates from
     * @param n2 the name of the node the edge terminates on.
     * @param label the label of the edge to check the existence of.
     * @spec.requires n1, n2, label != null
     * @return whether an edge exists in the Graph that starts from node n1,
     *         ends on node n2, and has the label, label.
     */
    public boolean hasEdge(String n1, String n2, String label)
    {
        throw new RuntimeException("Method not yet implemented!");
    }

    /**
     * Returns the list of edge names that start from node n1 and end
     * on node n2. If nodes n1 or n2 do not exist in the Graph,
     * this method will throw an IllegalArgumentException.
     *
     * @param n1 the name of the node the edges originate from
     * @param n2 the name of the node the edges terminate on.
     * @spec.requires n1, n2 != null and there exist nodes n1, n2 in this Graph.
     * @return the list of edge names that start from node n1 and end
     *         on node n2.
     */
    public List<String> getEdges(String n1, String n2) throws IllegalArgumentException
    {
        throw new RuntimeException("Method not yet implemented!");
    }

    /**
     * Returns the number of edges in the Graph.
     *
     * @return the number of edges in the Graph.
     */
    public int getEdgeCount()
    {
        throw new RuntimeException("Method not yet implemented!");
    }

}
