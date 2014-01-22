
package tiralabra;

import java.util.ArrayList;
import java.util.List;
import tiralabra.algorithms.AngleElimination;
import tiralabra.datastructures.Point;
import tiralabra.util.Tools;

/**
 *
 * @author Pavel
 */
public class PointContainer {
    private List<Point> points = new ArrayList<>();
    public List<Point> getPoints()  {return points;}
    public Point addPoint(int x, int y)
    {
        Point point = new Point((double)x, (double)y);
        points.add(point);
        return point;
    }
    public void removePoint(Point point)
    {
        if (point == null)  return;
        point.removeAllAdjacents();
        points.remove(point);
    }
    public void toggleEdge(Point p1, Point p2)
    {
        if (p1.getAdjacents().contains(p2))
            p1.removeAdjacent(p2);
        else
            p1.addAdjacent(p2);
    }
    public void buildGraph()
    {
        for (Point p : points)
        {
            if (!p.isVertex())  continue;
            AngleElimination.findUnobstructedPoints(p, points);
        }
    }
    public Point findMiddle(Point begin)
    {
        if (begin == null)  return null;
        Point point = begin;
        int size = 0;
        double sumx = 0;
        double sumy = 0;
        do
        {
            size++;
            sumx += point.X();
            sumy += point.Y();
            point = point.getRight();
        }   while (point != begin);
        return new Point(sumx / size, sumy / size);
    }
    public void setShapeMode(Point begin)
    {
        if (begin == null)  return;
        Point point = begin;
        double anglesum = 0;
        int pointsum = 0;
        do
        {
            pointsum++;
            anglesum += point.getAngle();
            point = point.getRight();
        }   while (point != begin);
        if (Tools.round(anglesum) != Tools.round((pointsum - 2) * Math.PI))
            return;
        do
        {
            Point q = point.getLeft();
            point.setLeft(point.getRight());
            point.setRight(q);
            point = point.getRight();
        }   while (point != begin);
    }

}
