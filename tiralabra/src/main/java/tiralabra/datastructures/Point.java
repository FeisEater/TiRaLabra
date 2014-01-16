
package tiralabra.datastructures;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Pavel
 */
public class Point {
    private double x;
    private double y;
    private Set<Point> adjacent;
    private int i;
    public Point(double x, double y, int i)
    {
        this.x = x;
        this.y = y;
        this.i = i;
        adjacent = new HashSet<>();
    }
    public double X()   {return x;}
    public double Y()   {return y;}
    public void addAdjacent(Point point)
    {
        if (point == this)  return;
        adjacent.add(point);
        point.adjacent.add(this);
    }
    public void removeAdjacent(Point point)
    {
        if (point == this)  return;
        adjacent.remove(point);
        point.adjacent.remove(this);
    }
    public Set<Point> getAdjacents()
    {
        return adjacent;
    }
    public String toString()
    {
        return "" + i;
    }
}
