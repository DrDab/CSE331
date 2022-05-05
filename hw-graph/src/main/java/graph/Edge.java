package graph;

/**
 * Represents an immutable edge in a graph from a given source node to
 * a given destination node and with a given label.
 */
public class Edge
{
    /**
     * @param sourceNode the name of the node the edge starts from
     * @param destNode the name of the node the edge terminates on
     * @param label the label of the edge from node sourceNode to destNode
     * @spec.requires sourceNode, destNode, label != null
     * @throws IllegalArgumentException if sourceNode == null or destNode == null or label == null
     */
    public Edge(String sourceNode, String destNode, String label) throws IllegalArgumentException
    {
        throw new RuntimeException("Constructor not yet implemented!");
    }

    /**
     * Returns the name of the node the edge originates from.
     *
     * @return the name of the node the edge originates from.
     */
    public String getSourceNode()
    {
        throw new RuntimeException("Method not yet implemented!");
    }

    /**
     * Returns the name of the node the edge terminates on.
     *
     * @return the name of the node the edge terminates on.
     */
    public String getDestNode()
    {
        throw new RuntimeException("Method not yet implemented!");
    }

    /**
     * Returns the label of the edge.
     *
     * @return the label of the edge.
     */
    public String getLabel()
    {
        throw new RuntimeException("Method not yet implemented!");
    }

    @Override
    public int hashCode()
    {
        throw new RuntimeException("Method not yet implemented!");
    }

    @Override
    public boolean equals(Object o)
    {
        throw new RuntimeException("Method not yet implemented!");
    }
}
