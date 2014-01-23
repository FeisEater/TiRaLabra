
package tiralabra.datastructures;

/**
 *
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
    public double getAngle()
    {
        if (leftNeighbour == null || rightNeighbour == null)
            return -1024;
        double leftAngle = getDirection(leftNeighbour);
        double rightAngle = getDirection(rightNeighbour);
        if (rightAngle < leftAngle) rightAngle += Math.PI * 2;
        return rightAngle - leftAngle;
    }
    @Override
    public boolean isVertex()
    {
        return getAngle() > Math.PI && getAngle() < 2 * Math.PI;
    }
    public int[] angleMarker()
    {
        int[] coord = {(int)X(),(int)Y()};
        if (leftNeighbour == null || rightNeighbour == null)    return coord;
        double middleAngle = getMiddleDirection();
        coord[0] = (int)(X() + Math.cos(middleAngle) * 32);
        coord[1] = (int)(Y() + Math.sin(middleAngle) * 32);
        return coord;
    }
    public double getMiddleDirection()
    {
        if (leftNeighbour == null || rightNeighbour == null)    return -1024;
        double leftAngle = getDirection(leftNeighbour);
        double rightAngle = getDirection(rightNeighbour);
        if (rightAngle < leftAngle) rightAngle += Math.PI * 2;
        return (leftAngle + rightAngle) / 2;
    }
}
