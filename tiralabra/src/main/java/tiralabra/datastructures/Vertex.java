
package tiralabra.datastructures;

import tiralabra.util.Tools;
import tiralabra.util.VertexComparator;

/**
 * Vertex in a graph.
 * @author Pavel
 */
public class Vertex {
    private double x;
    private double y;
    private Tree<Vertex> adjacent;
/**
 * Constructor.
 * @param x X coordinate at which vertex is created.
 * @param y Y coordinate at which vertex is created.
 */
    public Vertex(double x, double y)
    {
        this.x = x;
        this.y = y;
        adjacent = new Tree<>(new VertexComparator());
    }
    public double X()   {return x;}
    public double Y()   {return y;}
/**
 * Joins specified vertex with this vertex in a graph.
 * @param ver Specified vertex.
 */
    public void addAdjacent(Vertex ver)
    {
        if (ver == this)  return;
        adjacent.add(ver);
        ver.adjacent.add(this);
    }
/**
 * Removes the connection with the specified vertex if there ever
 * was a connection.
 * @param ver Specified vertex.
 */
    public void removeAdjacent(Vertex ver)
    {
        if (ver == this)  return;
        adjacent.remove(ver);
        ver.adjacent.remove(this);
    }
/**
 * 
 * @return Set of vertices that are connected with this vertex.
 */
    public Tree<Vertex> getAdjacents()
    {
        return adjacent;
    }
/**
 * Removes all connections with other vertices.
 */
    public void removeAllAdjacents()
    {
        LinkedList<Vertex> list = adjacent.toLinkedList();
        while (list.hasNext())
            list.getNext().adjacent.remove(this);
        adjacent.clear();
    }
/**
 * 
 * @param ver Specified vertex.
 * @return direction from this vertex to ver.
 */
    public double getDirection(Vertex ver)
    {
        return Math.atan2(ver.y - y, ver.x - x);
    }
/**
 * Calculates distance between this and other vertex.
 * @param ver Other vertex.
 * @return distance between two vertices.
 */
    public double getDistance(Vertex ver)
    {
        return Math.sqrt(Math.pow(ver.X() - x, 2) +
                Math.pow(ver.Y() - y, 2));
    }

/**
 * Checks if should be considered as a vertex in the graph.
 * If Vertex class is not extended, it's always considered as vertex in the graph.
 * @return always true.
 */
    public boolean isVertex()
    {
        return true;
    }
/**
 * Checks if specified vertex is between the specified directions from
 * this Vertex's point of view.
 * @param leftAngle Specified vertex should be to the right of this direction.
 * @param rightAngle Specified vertex should be to the left of this direction.
 * @param other Specified vertex.
 * @return true if vertex was between the angles.
 */
    public boolean hasPointBetween(double leftAngle, double rightAngle, Vertex other)
    {
        return Tools.hasAngleBetween(leftAngle, rightAngle, getDirection(other));
    }
    @Override
    public String toString()
    {
        return "" + x + " " + y;
    }

}
