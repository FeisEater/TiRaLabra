
package tiralabra.datastructures;

import java.util.HashSet;
import java.util.Set;

/**
 * Vertex in a graph.
 * @author Pavel
 */
public class Vertex {
    private double x;
    private double y;
    private Set<Vertex> adjacent;
/**
 * Constructor.
 * @param x X coordinate at which vertex is created.
 * @param y Y coordinate at which vertex is created.
 */
    public Vertex(double x, double y)
    {
        this.x = x;
        this.y = y;
        adjacent = new HashSet<>();
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
    public Set<Vertex> getAdjacents()
    {
        return adjacent;
    }
/**
 * Removes all connections with other vertices.
 */
    public void removeAllAdjacents()
    {
        for (Vertex v : adjacent)
            v.adjacent.remove(this);
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
 * If Vertex class is not extended, is always considered as vertex in the graph.
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
        return hasAngleBetween(leftAngle, rightAngle, getDirection(other));
    }
/**
 * Checks if specified angle is between the specified directions from
 * this Vertex's point of view.
 * @param leftAngle Specified vertex should be to the right of this direction.
 * @param rightAngle Specified vertex should be to the left of this direction.
 * @param testAngle Specified angle
 * @return true if testAngle is between the angles.
 */
    public boolean hasAngleBetween(double leftAngle, double rightAngle, double testAngle)
    {
        if (leftAngle >= rightAngle)
        {
            if (testAngle < leftAngle && testAngle > rightAngle)
                return true;
        }
        else
        {
            if (testAngle > rightAngle || testAngle < leftAngle)
                return true;
        }
        return false;
    }
    @Override
    public String toString()
    {
        return "" + x + " " + y;
    }
}
