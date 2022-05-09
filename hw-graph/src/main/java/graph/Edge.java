package graph;

/**
 * Represents an immutable edge in a graph from a given source node to
 * a given destination node and with a given label.
 */
public class Edge
{
    // Representation Invariant (RI): sourceNode, destNode, label != null.
    // Abstraction Function:
    //      AF(this) = an Edge of a directed graph with source node name sourceNode,
    //                 destination node name destNode, and label label.
    private String sourceNode;
    private String destNode;
    private String label;

    /**
     * @param sourceNode the name of the node the edge starts from
     * @param destNode the name of the node the edge terminates on
     * @param label the label of the edge from node sourceNode to destNode
     * @throws IllegalArgumentException if sourceNode or destNode or label != null
     */
    public Edge(String sourceNode, String destNode, String label)
    {
        if (sourceNode == null || destNode == null || label == null)
        {
            throw new IllegalArgumentException("sourceNode, destNode, and label must be non-null!");
        }

        this.sourceNode = sourceNode;
        this.destNode = destNode;
        this.label = label;
    }

    /**
     * Returns the name of the node the edge originates from.
     *
     * @return the name of the node the edge originates from.
     */
    public String getSourceNode()
    {
        return sourceNode;
    }

    /**
     * Returns the name of the node the edge terminates on.
     *
     * @return the name of the node the edge terminates on.
     */
    public String getDestNode()
    {
        return destNode;
    }

    /**
     * Returns the label of the edge.
     *
     * @return the label of the edge.
     */
    public String getLabel()
    {
        return label;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return sourceNode.hashCode() ^ destNode.hashCode() ^ label.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o)
    {
        if (o == null)
            return false;

        if (!(o instanceof Edge))
            return false;

        Edge e2 = (Edge) o;
        return sourceNode.equals(e2.sourceNode) && destNode.equals(e2.destNode)
                && label.equals(e2.label);
    }
}
