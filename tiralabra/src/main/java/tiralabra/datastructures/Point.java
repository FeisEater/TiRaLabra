
package tiralabra.datastructures;

/**
 * Point in geometry. Connected with another point, forms a line
 * which separates a wall and non-wall part of the geometry.
 * Extends vertex class, but is not always a vertex in the graph.
 * Has a left and right neighbour, which are chained to form a polygon.
 * @author Pavel
 */
public class Point extends Vertex {
    private Point leftNeighbour;
    private Point rightNeighbour;
    public Point(double x, double y)
    {
        super(x,y);
    }
    public Point getLeft()    {return leftNeighbour;}
    public Point getRight()    {return rightNeighbour;}
    public void setLeft(Point p)    {leftNeighbour = p;}
    public void setRight(Point p)    {rightNeighbour = p;}
/**
 * 
 * @return angle value between the non-wall part of the point.
 */
    public double getAngle()
    {
        if (leftNeighbour == null || rightNeighbour == null)
            return -1024;
        double leftAngle = getDirection(leftNeighbour);
        double rightAngle = getDirection(rightNeighbour);
        if (rightAngle < leftAngle) rightAngle += Math.PI * 2;
        return rightAngle - leftAngle;
    }
/**
 * Checks if should be considered as a vertex in the graph.
 * Non reflex angles are not vertices in the graph.
 * @return true if has reflex angle.
 */
    @Override
    public boolean isVertex()
    {
        return getAngle() > Math.PI && getAngle() < 2 * Math.PI;
    }
/**
 * Coordinate for angle marker, used for visualisation purposes.
 * @return [0] - x coordinate. [1] - y coordinate.
 */
    public int[] angleMarker()
    {
        int[] coord = {(int)X(),(int)Y()};
        if (leftNeighbour == null || rightNeighbour == null)    return coord;
        double middleAngle = getMiddleDirection();
        coord[0] = (int)(X() + Math.cos(middleAngle) * 32);
        coord[1] = (int)(Y() + Math.sin(middleAngle) * 32);
        return coord;
    }
/**
 * 
 * @return middle direction between the angle.
 */
    public double getMiddleDirection()
    {
        if (leftNeighbour == null || rightNeighbour == null)    return -1024;
        double leftAngle = getDirection(leftNeighbour);
        double rightAngle = getDirection(rightNeighbour);
        if (rightAngle < leftAngle) rightAngle += Math.PI * 2;
        return (leftAngle + rightAngle) / 2;
    }
}
