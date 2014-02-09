
package tiralabra.algorithms;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Comparator;
import tiralabra.datastructures.Heap;
import tiralabra.datastructures.LinkedList;
import tiralabra.datastructures.Point;
import tiralabra.datastructures.Queue;
import tiralabra.datastructures.Tree;
import tiralabra.datastructures.Vertex;

/**
 * Tracing algorithm from the point of view of a vertex.
 * @author Pavel
 */
public class AngleElimination {
    private static LinkedList<AngleInterval> list;
/**
 * Finds all vertices that are unobstructed from a specified vertex's
 * point of view.
 * @param src Vertex from which algorithm is tracing.
 * @param vertices A set of all vertices in the program
 * @return Set of vertices that are unobstructed from src.
 */
    public static LinkedList<Vertex> findUnobstructedPoints(Vertex src, LinkedList<Vertex> vertices)
    {
        if (src == null)  return null;
        Heap<AngleInterval> allIntervals = findIntervals(src, vertices);
        list = flattenIntervals(src, allIntervals);
        return getUnobstructedVertices(src, vertices);
    }
    public static void visualize(Graphics g)
    {
        if (list == null)   return;
        list.reset();
        g.setColor(Color.black);
        while (list.hasNext())
        {
            AngleInterval ai = list.getNext();
            if ((int)ai.leftDist == 0 && (int)ai.rightDist == 0)
                continue;
            g.drawLine((int)(ai.src.X() + Math.cos(ai.leftAngle) * ai.leftDist),
                    (int)(ai.src.Y() + Math.sin(ai.leftAngle) * ai.leftDist),
                    (int)(ai.src.X() + Math.cos(ai.rightAngle) * ai.rightDist),
                    (int)(ai.src.Y() + Math.sin(ai.rightAngle) * ai.rightDist));
        }
    }
/**
 * Checks if a specified vertex is obstructed.
 * @param test Specified vertex.
 * @return true if vertex is obstructed.
 */
    public static boolean isObstructed(Vertex test)
    {
        //System.out.println(list);
        if (list == null)   return true;
        list.reset();
        while (list.hasNext())
        {
            AngleInterval ai = list.getNext();
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
    private static Heap<AngleInterval> findIntervals(Vertex src, LinkedList<Vertex> vertices)
    {
        Heap<AngleInterval> intervals = new Heap<>(15, new DirectionComparator());
//if src is Point, wall part of the angle should be a sector where everything
//is obstructed, no matter how close the other vertex is.
        if (src.getClass() == Point.class)
            intervals.insert(new AngleInterval((Point)src));
        
        while (vertices.hasNext())
        {
            Vertex v = vertices.getNext();
            if (src == v || v.getClass() != Point.class)
                continue;
            Point q = (Point)v;
            if (src == q || q.getRight() == null || q.getRight() == src)
                continue;
            AngleInterval ai = new AngleInterval(src,q);
            intervals.insert(ai);
        }
        vertices.reset();
        return intervals;
    }
/**
 * Removes redundant sectors and overlapping.
 */
    private static LinkedList<AngleInterval> flattenIntervals(Vertex src, Heap<AngleInterval> startAngles)
    {
        int opt = startAngles.size();
        Queue<AngleInterval> flat = new Queue<>();
//Uncomment this if the code won't work!
        //while (!startAngles.isEmpty())
        //    flat.add(startAngles.pop());
//Now, ladies and gentlemen, give it up for the shittiest code in the world!
        Heap<AngleInterval> endAngles = new Heap<>(15, new EndDirectionComparator());
        Tree<AngleInterval> distances = new Tree<>(new DistanceComparator());
        double lastAngle = -Math.PI;
        while (!startAngles.isEmpty() || !endAngles.isEmpty())
        {
            AngleInterval newAngle = startAngles.peek();
            while (!endAngles.isEmpty() && lastAngle > endAngles.peek().rightAngle && endAngles.peek().leftAngle < endAngles.peek().rightAngle)
                distances.remove(endAngles.pop());
            if (startAngles.isEmpty() && endAngles.isEmpty())    break;
            AngleInterval oldAngle = endAngles.peek();
            Comparator comp = new DistanceComparator();
            if (newAngle != null && (oldAngle == null || newAngle.leftAngle < oldAngle.rightAngle))
            {
                if (endAngles.isEmpty())
                    lastAngle = newAngle.leftAngle;
                else if (comp.compare(newAngle, distances.getMin()) < 0 && lastAngle != newAngle.leftAngle)
                {
                    flat.enqueue(new AngleInterval(src, lastAngle, distances.getMin().distanceFromLine(lastAngle),
                        newAngle.leftAngle, distances.getMin().distanceFromLine(newAngle.leftAngle)));
                    lastAngle = newAngle.leftAngle;
                }
                distances.add(newAngle);
                endAngles.insert(newAngle);
                startAngles.pop();
            }
            else
            {
                boolean wasClosest = (oldAngle == distances.getMin());
                distances.remove(oldAngle);
                if (wasClosest && lastAngle != oldAngle.rightAngle)
                {
                    flat.enqueue(new AngleInterval(src, lastAngle, oldAngle.distanceFromLine(lastAngle),
                        oldAngle.rightAngle, oldAngle.rightDist));
                    lastAngle = oldAngle.rightAngle;
                }
                //if (oldAngle.leftAngle > oldAngle.rightAngle && oldAngle.distanceFromLine(flat.peek().leftAngle) < flat.peek().leftDist)
                //    flat.dequeue();
                endAngles.pop();
            }
        }
        LinkedList<AngleInterval> result = new LinkedList<>();
        while (!flat.isEmpty())
            result.add(flat.dequeue());
        return result;
    }
/**
 * Finds unobstructed vertices based on generated sectors.
 * @param src Vertex from which algorithm is tracing.
 * @param vertices A set of all vertices in the program
 * @return set of unobstructed vertices.
 */
    private static LinkedList<Vertex> getUnobstructedVertices(Vertex src, LinkedList<Vertex> vertices)
    {
        LinkedList<Vertex> unobstructed = new LinkedList<>();
        if (src.getClass() == Point.class)
            addNeighbours((Point)src, unobstructed);
        while (vertices.hasNext())
        {
            Vertex q = vertices.getNext();
            if (src == q || !q.isVertex()) continue;
            if (!isObstructed(q))
                unobstructed.add(q);
        }
        vertices.reset();
        return unobstructed;
    }
/**
 * Adds point's neighbours to the list of unobstructed vertices if they
 * are fit to be vertices.
 * @param src Point, whose neighbours will be added.
 * @param unobstructed List of unobstructed vertices.
 */
    private static void addNeighbours(Point src, LinkedList<Vertex> unobstructed)
    {
        if (src.getLeft().isVertex())
            unobstructed.add(src.getLeft());
        if (src.getRight().isVertex())
            unobstructed.add(src.getRight());
    }
/**
 * Interval of angles, which forms a sector. Each edge of the sector
 * has a value, which is a distance from the point that caused the
 * creation of the sector. Anything beyond that distance is obstructed.
 */
    private static class AngleInterval
    {
        public double leftAngle;
        public double rightAngle;
        public double leftDist;
        public double rightDist;
        private Vertex src;
        public boolean inverse;
/**
 * Constructor. Creates a sector based on a line.
 * @param source Vertex from which algorithm is tracing.
 * @param p One point of the line. The other point is p.getRight()
 */
        public AngleInterval(Vertex source, double la, double ld, double ra, double rd)
        {
            leftAngle = la;
            rightAngle = ra;
            leftDist = ld;
            rightDist = rd;
            src = source;
            inverse = false;
        }
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
            inverse = false;
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
                inverse = true;
            }
            else
            {
                leftest = p.getRight();
                rightest = p;
                inverse = false;
            }
//If line crosses the part where direction calculation jumps the cycle,
//switch sides.
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
            if (leftDist <= 0 && rightDist <= 0)    return 0;
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
        public String toString()
        {
            return "" + (int)(180 * leftAngle / Math.PI) + "->" + (int)leftDist + ": " + (int)(180 * rightAngle / Math.PI) + "->" + (int)rightDist;
        }
    }
/**
 * Comparator class for placing sectors in a heap based on their starting direction.
 */
    private static class DirectionComparator implements Comparator
    {
        @Override
        public int compare(Object o1, Object o2)
        {
            if (o1.getClass() != AngleInterval.class || o2.getClass() != AngleInterval.class)
                return 0;
            AngleInterval ai1 = (AngleInterval)o1;
            AngleInterval ai2 = (AngleInterval)o2;
            if (ai1.leftAngle == ai2.leftAngle)
            {
                if (ai2.rightDist < ai1.distanceFromLine(ai2.rightAngle))
                    return 1;
                return -1;
            }
            return (ai1.leftAngle < ai2.leftAngle) ? -1 : 1;
        }
    }
/**
 * Comparator class for placing sectors in a heap based on their ending direction.
 */
    private static class EndDirectionComparator implements Comparator
    {
        @Override
        public int compare(Object o1, Object o2)
        {
            if (o1.getClass() != AngleInterval.class || o2.getClass() != AngleInterval.class)
                return 0;
            AngleInterval ai1 = (AngleInterval)o1;
            AngleInterval ai2 = (AngleInterval)o2;
            if (ai1.rightAngle == ai2.rightAngle)
            {
                if (ai2.leftDist < ai1.distanceFromLine(ai2.leftAngle))
                    return 1;
                return -1;
            }
            boolean b1 = (ai1.rightAngle < ai1.leftAngle);
            boolean b2 = (ai2.rightAngle < ai2.leftAngle);
            if (b1 == !b2)
                return (ai1.rightAngle < ai2.rightAngle) ? 1 : -1;
            return (ai1.rightAngle < ai2.rightAngle) ? -1 : 1;
        }
    }
/**
 * Comparator class for placing sectors in a heap based on their distance.
 */
    private static class DistanceComparator implements Comparator
    {
        @Override
        public int compare(Object o1, Object o2)
        {
            if (o1.getClass() != AngleInterval.class || o2.getClass() != AngleInterval.class)
                return 0;
            AngleInterval ai1 = (AngleInterval)o1;
            AngleInterval ai2 = (AngleInterval)o2;
            boolean b1 = (ai1.rightAngle < ai1.leftAngle);
            boolean b2 = (ai2.rightAngle < ai2.leftAngle);
            boolean b3 = (b1 == b2) ? (ai2.rightAngle < ai1.rightAngle) : (ai2.rightAngle > ai1.rightAngle);
            if (ai1.leftAngle == ai2.leftAngle && ai1.leftDist == ai2.leftDist)
            {
                if (b3)
                {
                    if (ai2.rightDist < ai1.distanceFromLine(ai2.rightAngle))
                        return 1;
                    return -1;
                }
                else
                {
                    if (ai1.rightDist < ai2.distanceFromLine(ai1.rightAngle))
                        return -1;
                    return 1;
                }
            }
            if (ai1.src.hasAngleBetween(ai1.rightAngle, ai1.leftAngle, ai2.leftAngle))
            {
                if (ai2.leftDist < ai1.distanceFromLine(ai2.leftAngle))
                    return 1;
                return -1;
            }
            if (ai2.src.hasAngleBetween(ai2.rightAngle, ai2.leftAngle, ai1.leftAngle))
            {
                if (ai1.leftDist < ai2.distanceFromLine(ai1.leftAngle))
                    return -1;
                return 1;
            }
            if (ai1.leftAngle == ai2.rightAngle && ai1.leftDist == ai2.rightDist)
                return 1;
            if (ai1.rightAngle == ai2.leftAngle && ai1.rightDist == ai2.leftDist)
                return -1;
            if (ai1.leftAngle == ai2.leftAngle)
                return (ai1.leftDist < ai2.leftDist) ? -1 : 1;
            return 0;
        }
    }
}
