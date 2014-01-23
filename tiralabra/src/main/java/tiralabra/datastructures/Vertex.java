
package tiralabra.datastructures;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Pavel
 */
public class Vertex {
    private double x;
    private double y;
    private Set<Vertex> adjacent;
    public Vertex(double x, double y)
    {
        this.x = x;
        this.y = y;
        adjacent = new HashSet<>();
    }
    public double X()   {return x;}
    public double Y()   {return y;}
    public void addAdjacent(Vertex ver)
    {
        if (ver == this)  return;
        adjacent.add(ver);
        ver.adjacent.add(this);
    }
    public void removeAdjacent(Vertex ver)
    {
        if (ver == this)  return;
        adjacent.remove(ver);
        ver.adjacent.remove(this);
    }
    public Set<Vertex> getAdjacents()
    {
        return adjacent;
    }
    public void removeAllAdjacents()
    {
        for (Vertex v : adjacent)
            v.adjacent.remove(this);
        adjacent.clear();
    }
    public double getDirection(Vertex ver)
    {
        return Math.atan2(ver.y - y, ver.x - x);
    }
    public boolean isVertex()
    {
        return true;
    }
    public boolean hasPointBetween(double leftAngle, double rightAngle, Vertex other)
    {
        if (leftAngle >= rightAngle)
        {
            if (getDirection(other) < leftAngle && getDirection(other) > rightAngle)
                return true;
        }
        else
        {
            if (getDirection(other) > rightAngle || getDirection(other) < leftAngle)
                return true;
        }
        return false;
    }
    public String toString()
    {
        return "" + x + " " + y;
    }
}
