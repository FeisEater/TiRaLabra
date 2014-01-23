
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
    private static List<AngleInterval> intervals = new ArrayList<>();
    public static void findUnobstructedPoints(Vertex src, List<Vertex> vertices)
    {
        intervals.clear();
        if (src == null)  return;
        findIntervals(src, vertices);
        buildGraph(src, vertices);
    }
    public static boolean isObstructed(Vertex test)
    {
        for (AngleInterval i : intervals)
            if (i.vertexIsObstructed(test))  return true;
        return false;
    }
    private static void findIntervals(Vertex src, List<Vertex> vertices)
    {
        if (src.getClass() == Point.class)
            intervals.add(new AngleInterval((Point)src));
        
        for (Vertex v : vertices)
        {
            if (src == v || v.getClass() != Point.class)
                continue;
            Point q = (Point)v;
            if (src == q || q.getRight() == null || q.getRight() == src)
                continue;
            intervals.add(new AngleInterval(src,q));
        }
        Collections.sort(intervals);
    }
    private static void flattenIntervals()
    {
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
    }
    private static void buildGraph(Vertex src, List<Vertex> vertices)
    {
        if (src.getClass() == Point.class)
            connectWithNeighbours((Point)src);
        for (Vertex q : vertices)
        {
            if (src == q || !q.isVertex()) continue;
            if (!isObstructed(q))
                src.addAdjacent(q);
        }
    }
    private static void connectWithNeighbours(Point src)
    {
        if (src.getLeft().isVertex())
            src.addAdjacent(src.getLeft());
        if (src.getRight().isVertex())
            src.addAdjacent(src.getRight());
    }
    private static class AngleInterval implements Comparable
    {
        private double leftAngle;
        private double rightAngle;
        private double leftDist;
        private double rightDist;
        private Vertex src;
        public AngleInterval(Vertex source, Point p)
        {
            Point[] directions = findLeftAndRightAngles(source, p);
            setAttributes(source, directions[0], directions[1]);
        }
        public AngleInterval(Point src)
        {
            setAttributes(src, src.getRight(), src.getLeft());
            leftDist = 0;
            rightDist = 0;
        }
        public void setAttributes(Vertex src, Point leftest, Point rightest)
        {
            this.src = src;
            if (leftest != null)
            {
                leftAngle = src.getDirection(leftest);
                leftDist = Tools.distance(src, leftest);
            }
            if (rightest != null)
            {
                rightAngle = src.getDirection(rightest);
                rightDist = Tools.distance(src, rightest);
            }
        }
        public Point[] findLeftAndRightAngles(Vertex source, Point p)
        {
            Point leftest;
            Point rightest;
            if (source.getDirection(p) < source.getDirection(p.getRight()))
            {
                leftest = p;
                rightest = p.getRight();
            }
            else
            {
                leftest = p.getRight();
                rightest = p;
            }
            if (anglesCrossoverDirectionLoop(source, p))
            {
                Point q = leftest;
                leftest = rightest;
                rightest = q;
            }
            Point[] result = {leftest, rightest};
            return result;
        }
        public boolean anglesCrossoverDirectionLoop(Vertex source, Point p)
        {
            return (p.Y() - source.Y()) * (p.getRight().Y() - source.Y()) < 0 && 
                    source.X() > p.X() + 
                    Math.cos(p.getDirection(p.getRight())) *
                    Math.abs(source.Y() - p.Y());
        }
        public boolean vertexIsObstructed(Vertex test)
        {
            if (src.hasPointBetween(rightAngle, leftAngle, test))
            {
                if (leftDist <= 0 && rightDist <= 0)    return true;
                if (Tools.distance(src, test) > distanceFromLine(src.getDirection(test)))
                    return true;
            }
            return false;
        }
        public double distanceFromLine(double testAngle)
        {
            double x1 = leftDist * Math.cos(leftAngle);
            double y1 = leftDist * Math.sin(leftAngle);
            double x2 = rightDist * Math.cos(rightAngle);
            double y2 = rightDist * Math.sin(rightAngle);
            if (x2 - x1 > y2 - y1)
            {
                double coeff = (y2-y1)/(x2-x1);
                return (y1 - x1*coeff)/(Math.sin(testAngle) - coeff * Math.cos(testAngle));
            }
            double coeff = (x2-x1)/(y2-y1);
            return (x1 - y1*coeff)/(Math.cos(testAngle) - coeff * Math.sin(testAngle));                    
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
