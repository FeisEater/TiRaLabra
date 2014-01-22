
package tiralabra.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import tiralabra.datastructures.Point;
import tiralabra.util.Tools;

/**
 *
 * @author Pavel
 */
public class AngleElimination {
    private static List<AngleInterval> intervals = new ArrayList<AngleInterval>();
    public static void findUnobstructedPoints(Point p, List<Point> points)
    {
        intervals.clear();
        intervals.add(new AngleInterval(p));
        for (Point q : points)
        {
            if (p == q || q.getRight() == null || q.getRight() == p)
                continue;
            intervals.add(new AngleInterval(p,q));
        }
        Collections.sort(intervals);
        System.out.println(intervals);
        /*if (p.getLeft().isVertex())   p.addAdjacent(p.getLeft());
        if (p.getRight().isVertex())   p.addAdjacent(p.getRight());
        for (Point q : points)
        {
            if (p == q || !q.isVertex()) continue;
            if (!p.hasPointBetween(p.getDirection(p.getLeft()), p.getDirection(p.getRight()), q) && 
                !q.hasPointBetween(q.getDirection(q.getLeft()), q.getDirection(q.getRight()), p))
                p.addAdjacent(q);
        }*/
    }
    public static boolean isObstructed(double x, double y)
    {
        for (AngleInterval i : intervals)
        {
            if (i.isBetween(x, y))  return true;
        }
        return false;
    }
    private static class AngleInterval implements Comparable
    {
        private double leftAngle;
        private double rightAngle;
        private double leftDist;
        private double rightDist;
        private Point src;
        public AngleInterval(Point src, Point p)
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
        public boolean isBetween(double x, double y)
        {
            return src.hasPointBetween(rightAngle, leftAngle, new Point(x,y));
        }
        private int toDegree(double a)  {return (int)(180 * a / Math.PI);}
        public String toString()
        {
            return "" + toDegree(leftAngle);// + "-> " + (int)leftDist + " : " + 
                    //toDegree(rightAngle) + "-> " + (int)rightDist;
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
