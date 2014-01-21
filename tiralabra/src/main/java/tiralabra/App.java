package tiralabra;

import tiralabra.gui.GraphicInterface;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import tiralabra.datastructures.Point;
import tiralabra.util.Tools;

/**
 * Hello world!
 *
 */
public class App 
{
    public static List<Point> points = new ArrayList<>();

    public static void main( String[] args )
    {
        SwingUtilities.invokeLater(new GraphicInterface());
    }
    public static Point addPoint(int x, int y)
    {
        Point point = new Point((double)x, (double)y);
        points.add(point);
        return point;
    }
    public static void removePoint(Point point)
    {
        if (point == null)  return;
        point.removeAllAdjacents();
        points.remove(point);
    }
    public static void toggleEdge(Point p1, Point p2)
    {
        if (p1.getAdjacents().contains(p2))
            p1.removeAdjacent(p2);
        else
        {
            p1.addAdjacent(p2);
            //p2.setRight(p1);
            //p1.setLeft(p2);
        }
    }
    public static void buildGraph()
    {
        for (Point p : points)
        {
            if (!p.isVertex())  continue;
            if (p.getLeft().isVertex())   p.addAdjacent(p.getLeft());
            if (p.getRight().isVertex())   p.addAdjacent(p.getRight());
            for (Point q : points)
            {
                if (p == q || !q.isVertex()) continue;
                if (!isBetween(p, p.getDirection(p.getLeft()), p.getDirection(p.getRight()), q) && 
                    !isBetween(q, q.getDirection(q.getLeft()), q.getDirection(q.getRight()), p))
                    p.addAdjacent(q);
            }
        }
    }
    public static boolean isBetween(Point source, double leftAngle, double rightAngle, Point other)
    {
        if (leftAngle >= rightAngle)
        {
            if (source.getDirection(other) < leftAngle && source.getDirection(other) > rightAngle)
                return true;
        }
        else
        {
            if (source.getDirection(other) > rightAngle || source.getDirection(other) < leftAngle)
                return true;
        }
        return false;
    }
    public static Point findMiddle(Point begin)
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
    public static void setShapeMode(Point begin)
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
