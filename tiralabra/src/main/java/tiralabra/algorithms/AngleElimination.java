
package tiralabra.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import tiralabra.datastructures.Heap;
import tiralabra.datastructures.Point;
import tiralabra.datastructures.Vertex;

/**
 * Tracing algorithm from the point of view of a vertex.
 * @author Pavel
 */
public class AngleElimination {
    private static Heap<AngleInterval> intervals = new Heap<>(15, new DirectionComparator());
/**
 * Finds all vertices that are unobstructed from a specified vertex's
 * point of view.
 * @param src Vertex from which algorithm is tracing.
 * @param vertices A set of all vertices in the program
 * @return Set of vertices that are unobstructed from src.
 */
    public static List<Vertex> findUnobstructedPoints(Vertex src, List<Vertex> vertices)
    {
        intervals.clear(15);
        if (src == null)  return null;
        findIntervals(src, vertices);
        return getUnobstructedVertices(src, vertices);
    }
/**
 * Checks if a specified vertex is obstructed.
 * @param test Specified vertex.
 * @return true if vertex is obstructed.
 */
    public static boolean isObstructed(Vertex test)
    {
        for (int i = 0; i < intervals.size(); i++)
        {
            AngleInterval ai = (AngleInterval)intervals.getArray()[i];
            if (ai.vertexIsObstructed(test))
                return true;
        }
        return false;
    }
/**
 * Finds all polygon edges and divides field into sectors, where each
 * line fills the width of the sector.
 * @param src Vertex, which becomes a center of the sectors.
 * @param vertices A set of all vertices in the program
 */
    private static void findIntervals(Vertex src, List<Vertex> vertices)
    {
//if src is Point, wall part of the angle should be a sector where everything
//is obstructed, no matter how close the other vertex is.
        if (src.getClass() == Point.class)
            intervals.insert(new AngleInterval((Point)src));
        
        for (Vertex v : vertices)
        {
            if (src == v || v.getClass() != Point.class)
                continue;
            Point q = (Point)v;
            if (src == q || q.getRight() == null || q.getRight() == src)
                continue;
            AngleInterval ai = new AngleInterval(src,q);
            intervals.insert(ai);
        }
        //Collections.sort(intervals);
    }
/**
 * Removes redundant sectors and overlapping.
 */
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
/**
 * Finds unobstructed vertices based on generated sectors.
 * @param src Vertex from which algorithm is tracing.
 * @param vertices A set of all vertices in the program
 * @return set of unobstructed vertices.
 */
    private static List<Vertex> getUnobstructedVertices(Vertex src, List<Vertex> vertices)
    {
        List<Vertex> unobstructed = new ArrayList<>();
        if (src.getClass() == Point.class)
            addNeighbours((Point)src, unobstructed);
        for (Vertex q : vertices)
        {
            if (src == q || !q.isVertex()) continue;
            if (!isObstructed(q))
                unobstructed.add(q);
        }
        return unobstructed;
    }
/**
 * Adds point's neighbours to the list of unobstructed vertices if they
 * are fit to be vertices.
 * @param src Point, whose neighbours will be added.
 * @param unobstructed List of unobstructed vertices.
 */
    private static void addNeighbours(Point src, List<Vertex> unobstructed)
    {
        if (src.getLeft().isVertex())
            unobstructed.add(src.getLeft());
        if (src.getRight().isVertex())
            unobstructed.add(src.getRight());
    }
/**
 * Interval of angles, which forms a sector. Each edge if the sector
 * has a value, which is a distance from the point that caused the
 * creation of the sector. Anything beyond that distance is obstructed.
 */
    private static class AngleInterval implements Comparable
    {
        public double leftAngle;
        public double rightAngle;
        public double leftDist;
        public double rightDist;
        private Vertex src;
/**
 * Constructor. Creates a sector based on a line.
 * @param source Vertex from which algorithm is tracing.
 * @param p One point of the line. The other point is p.getRight()
 */
        public AngleInterval(Vertex source, Point p)
        {
            Point[] directions = findLeftAndRightAngles(source, p);
            setAttributes(source, directions[0], directions[1]);
        }
/**
 * Constructor. Creates a sector for the angle of point's non-wall part.
 * Max distance is set to 0, that way everything is obstructed in that
 * angle interval.
 * @param src Point from which algorithm is tracing.
 */
        public AngleInterval(Point src)
        {
            setAttributes(src, src.getRight(), src.getLeft());
            leftDist = 0;
            rightDist = 0;
        }
/**
 * Initializes class attributes.
 * @param src Vertex from which algorithm is tracing.
 * @param leftest Left point of the line that caused the sector to be created.
 * @param rightest Right point of the line that caused the sector to be created.
 */
        public void setAttributes(Vertex src, Point leftest, Point rightest)
        {
            this.src = src;
            if (leftest != null)
            {
                leftAngle = src.getDirection(leftest);
                leftDist = src.getDistance(leftest);
            }
            if (rightest != null)
            {
                rightAngle = src.getDirection(rightest);
                rightDist = src.getDistance(rightest);
            }
        }
/**
 * Decides which of the two points of the line is considered the left
 * and right part of the sector.
 * @param source Vertex from which algorithm is tracing.
 * @param p One point of the line. The other point is p.getRight()
 * @return [0] - Point that is considered to be the left part of the sector.
 *          [1] - Point that is considered to be the right part of the sector.
 */
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
//If line crosses the part where direction calculation jumps the cycle,
//switch sides.
            //System.out.println(source + ", " + leftest + ", " + rightest + ": " + Math.abs(source.getDirection(leftest) - source.getDirection(rightest)));

            if (anglesCrossoverDirectionLoop(source, p))
            {
                Point q = leftest;
                leftest = rightest;
                rightest = q;
            }
            Point[] result = {leftest, rightest};
            return result;
        }
/**
 * Checks if the line crosses the loop jump of the direction calculation.
 * Calculated angle value jumps from -PI to +PI in the left direction.
 * This complicates deciding which of sector's two sides is left or right side.
 * @param source Vertex from which algortihm is tracing.
 * @param p One point of the line. The other point is p.getRight()
 * @return True if line is causing a cycle jump.
 */
        public boolean anglesCrossoverDirectionLoop(Vertex source, Point p)
        {
//Difference in angles between two points can't be more than 180 degrees.
//If this is detected, it's interperted as if line crosses the loop jump point.
            return (Math.abs(source.getDirection(p) - source.getDirection(p.getRight())) > Math.PI);
/*            return (p.Y() - source.Y()) * (p.getRight().Y() - source.Y()) < 0 && //Points of the line are on the opposite sides of the crosspoint Y coordinate.
                    source.X() > p.X() + 
                    Math.cos(p.getDirection(p.getRight())) *
                    Math.abs(source.Y() - p.Y()); //Checks if source's X coordinate is on the right of the crosspoint.
*/
        }
/**
 * Checks if specified vertex is obstructed based on this sector.
 * @param test Certain vertex.
 * @return true if this sector obstructs the vertex.
 */
        public boolean vertexIsObstructed(Vertex test)
        {
            if (src.hasPointBetween(rightAngle, leftAngle, test))
            {
                if (leftDist <= 0 && rightDist <= 0)    return true;
                if (src.getDistance(test) > distanceFromLine(src.getDirection(test)))
                    return true;
            }
            return false;
        }
/**
 * Calculates the distance from the line that caused the sector to be
 * created at a specified direction.
 * @param testAngle Direction at which the line is crossed.
 * @return Distance from source point to the line crosspoint at given direction.
 */
        public double distanceFromLine(double testAngle)
        {
            double x1 = leftDist * Math.cos(leftAngle);
            double y1 = leftDist * Math.sin(leftAngle);
            double x2 = rightDist * Math.cos(rightAngle);
            double y2 = rightDist * Math.sin(rightAngle);
//Method uses a formula for drawing a straight line in polar coordinates.
            if (Math.abs(x2 - x1) > Math.abs(y2 - y1))
            {
                double coeff = (y2-y1)/(x2-x1);
                return (y1 - x1*coeff)/(Math.sin(testAngle) - coeff * Math.cos(testAngle));
            }
//Other formula is used in case coefficent's denominator is close to zero.
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
        public String toString()
        {
            return "" + src + ": " + (int)(180 * leftAngle / Math.PI) + "->" + (int)leftDist + ", " + (int)(180 * rightAngle / Math.PI) + "->" + (int)rightDist;
        }
    }
    private static class DirectionComparator implements Comparator
    {
        @Override
        public int compare(Object o1, Object o2)
        {
            if (o1.getClass() != AngleInterval.class || o2.getClass() != AngleInterval.class)
                return 0;
            AngleInterval ai1 = (AngleInterval)o1;
            AngleInterval ai2 = (AngleInterval)o2;
            return (int)(ai1.leftAngle - ai2.leftAngle);
        }
    }
}
