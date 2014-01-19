
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
    private Point leftNeighbour;
    private Point rightNeighbour;
    public Point(double x, double y)
    {
        this.x = x;
        this.y = y;
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
    public void removeAllAdjacents()
    {
        for (Point p : adjacent)
            p.adjacent.remove(this);
        adjacent.clear();
    }
    public Point getLeft()    {return leftNeighbour;}
    public Point getRight()    {return rightNeighbour;}
    public void setLeft(Point p)    {leftNeighbour = p;}
    public void setRight(Point p)    {rightNeighbour = p;}
    public double getAngle()
    {
        if (leftNeighbour == null || rightNeighbour == null)
            return -1024;
        double leftAngle = getDirection(leftNeighbour);
        double rightAngle = getDirection(rightNeighbour);
        if (rightAngle < leftAngle) rightAngle += Math.PI * 2;
        return rightAngle - leftAngle;
    }
    public double getDirection(Point point)
    {
        return Math.atan2(point.y - y, point.x - x);
    }
    public boolean isVertex()
    {
        return getAngle() > Math.PI && getAngle() < 2 * Math.PI;
    }
    public int[] angleMarker()
    {
        int[] coord = {(int)x,(int)y};
        if (leftNeighbour == null || rightNeighbour == null)    return coord;
        double leftAngle = getDirection(leftNeighbour);
        double rightAngle = getDirection(rightNeighbour);
        if (rightAngle < leftAngle) rightAngle += Math.PI * 2;
        double middleAngle = (leftAngle + rightAngle) / 2;
        coord[0] = (int)(x + Math.cos(middleAngle) * 32);
        coord[1] = (int)(y + Math.sin(middleAngle) * 32);
        return coord;
    }
}
