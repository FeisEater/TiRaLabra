
package tiralabra.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import tiralabra.datastructures.Point;
import tiralabra.datastructures.Vertex;
import tiralabra.util.Tools;

/**
 *
 * @author Pavel
 */
public class AngleElimination {
    private static List<AngleInterval> intervals = new ArrayList<AngleInterval>();
    public static void findUnobstructedPoints(Vertex p, List<Vertex> points)
    {
        intervals.clear();
        if (p == null)  return;
        if (p.getClass() == Point.class)
            intervals.add(new AngleInterval((Point)p));
        for (Vertex v : points)
        {
            if (p == v || v.getClass() != Point.class)
                continue;
            Point q = (Point)v;
            if (p == q || q.getRight() == null || q.getRight() == p)
                continue;
            intervals.add(new AngleInterval(p,q));
        }
        Collections.sort(intervals);
        /*List<AngleInterval> curIntervals = new ArrayList<>();
        for (AngleInterval ai : intervals)
        {
            for (AngleInterval ai2 : curIntervals)
            {
                if (ai2.rightAngle )
            }
            curIntervals.add(ai);
        }
        System.out.println(intervals);*/
        if (p.getClass() == Point.class)
        {
            Point point = (Point)p;
            if (point.getLeft().isVertex())
                point.addAdjacent(point.getLeft());
            if (point.getRight().isVertex())
                point.addAdjacent(point.getRight());
        }
        for (Vertex q : points)
        {
            if (p == q || !q.isVertex()) continue;
            if (isObstructed(q))    continue;
            p.addAdjacent(q);
        }
    }
    public static boolean isObstructed(Vertex test)
    {
        for (AngleInterval i : intervals)
        {
            if (i.isBetween(test))  return true;
        }
        return false;
    }
    private static class AngleInterval implements Comparable
    {
        public double leftAngle;
        public double rightAngle;
        private double leftDist;
        private double rightDist;
        private Vertex src;
        public AngleInterval(Vertex src, Point p)
        {
            this.src = src;
            Point leftest;
            Point rightest;
            if (src.getDirection(p) < src.getDirection(p.getRight()))
            {
                leftest = p;
                rightest = p.getRight();
            }
            else
            {
                leftest = p.getRight();
                rightest = p;
            }
            if ((p.Y() - src.Y()) * (p.getRight().Y() - src.Y()) < 0 && 
                    src.X() > p.X() + 
                    Math.cos(p.getDirection(p.getRight())) *
                    Math.abs(src.Y() - p.Y()))
            {
                Point q = leftest;
                leftest = rightest;
                rightest = q;
            }
            leftAngle = src.getDirection(leftest);
            rightAngle = src.getDirection(rightest);
            leftDist = Tools.distance(src, leftest);
            rightDist = Tools.distance(src, rightest);
        }
        public AngleInterval(Point src)
        {
            this.src = src;
            leftAngle = src.getDirection(src.getRight());
            rightAngle = src.getDirection(src.getLeft());
            leftDist = 0;
            rightDist = 0;
        }
        public boolean isBetween(Vertex test)
        {
            if (src.hasPointBetween(rightAngle, leftAngle, test))
            {
                /*double right = (rightAngle < leftAngle) ? rightAngle + Math.PI * 2 : rightAngle;
                double dir = (src.getDirection(test) < leftAngle) ? src.getDirection(test) + Math.PI * 2 : src.getDirection(test);
                double angleRatio = (dir - leftAngle) / (right - leftAngle);
                double radius = leftDist + angleRatio * (rightDist - leftDist);
                */
                if (leftDist <= 0 && rightDist <= 0)    return true;
                double x1 = leftDist * Math.cos(leftAngle);
                double y1 = leftDist * Math.sin(leftAngle);
                double x2 = rightDist * Math.cos(rightAngle);
                double y2 = rightDist * Math.sin(rightAngle);
                double testAngle = src.getDirection(test);
                double radius;
                if (x2 - x1 > y2 - y1)
                {
                    double coeff = (y2-y1)/(x2-x1);
                    radius = (y1 - x1*coeff)/(Math.sin(testAngle) - coeff * Math.cos(testAngle));
                }
                else
                {
                    double coeff = (x2-x1)/(y2-y1);
                    radius = (x1 - y1*coeff)/(Math.cos(testAngle) - coeff * Math.sin(testAngle));                    
                }
                if (Tools.distance(src, test) > radius)   return true;
            }
            return false;
        }
        @Override
        public int compareTo(Object o)
        {
            if (o.getClass() != this.getClass()) return 0;
            AngleInterval a = (AngleInterval)o;
            return (a.leftAngle > this.leftAngle) ? -1 : 1;
        }
    }
}
