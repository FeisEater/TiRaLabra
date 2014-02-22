
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
import tiralabra.util.StatWriter;
import tiralabra.util.Tools;

/**
 * Tracing algorithm from the point of view of a vertex.
 * @author Pavel
 */
public class AngleElimination {
    private static Vertex src;
    private static LinkedList<AngleInterval> list;
/**
 * Finds all vertices that are unobstructed from a specified vertex's
 * point of view.
 * @param v Vertex from which algorithm is tracing.
 * @param vertices A set of all vertices in the program
 * @param writer Writer that stores results of flattenIntervals() method.
 * @return Set of vertices that are unobstructed from src.
 */
    public static LinkedList<Vertex> findUnobstructedPoints(Vertex v, LinkedList<Vertex> vertices, StatWriter writer)
    {
        if (v == null)  return null;
        src = v;
        Heap<AngleInterval> allIntervals = findIntervals(vertices);
        list = flattenIntervals(allIntervals, writer);
        return getUnobstructedVertices(vertices);
    }
/**
 * Visualizes generated angle intervals for debugging purposes.
 * @param g Graphics object.
 */
    public static void visualize(Graphics g)
    {
        if (list == null)   return;
        list.reset();
        g.setColor(Color.red);
        while (list.hasNext())
        {
            AngleInterval ai = list.getNext();
            if ((int)ai.leftDist == 0 && (int)ai.rightDist == 0)
                continue;
            g.drawLine((int)(src.X() + Math.cos(ai.leftAngle) * ai.leftDist),
                    (int)(src.Y() + Math.sin(ai.leftAngle) * ai.leftDist),
                    (int)(src.X() + Math.cos(ai.rightAngle) * ai.rightDist),
                    (int)(src.Y() + Math.sin(ai.rightAngle) * ai.rightDist));
        }
    }
/**
 * Checks if a specified vertex is obstructed.
 * @param test Specified vertex.
 * @return true if vertex is obstructed.
 */
    public static boolean isObstructed(Vertex test)
    {
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
 * @param vertices A set of all vertices in the program
 */
    private static Heap<AngleInterval> findIntervals(LinkedList<Vertex> vertices)
    {
        Heap<AngleInterval> intervals = new Heap<>(15, new DirectionComparator());
//if src is Point, wall part of the angle should be a sector where everything
//is obstructed, no matter how close the other vertex is.
        if (src.getClass() == Point.class)
            intervals.insert(new AngleInterval());
        
        while (vertices.hasNext())
        {
            Vertex v = vertices.getNext();
            if (src == v || v.getClass() != Point.class)
                continue;
            Point q = (Point)v;
            if (src == q || q.getRight() == null || q.getRight() == src)
                continue;
            AngleInterval ai = new AngleInterval(q);
            intervals.insert(ai);
        }
        vertices.reset();
        return intervals;
    }
/**
 * Removes redundant sectors and overlapping.
 * @param startAngles Heap of angles in an order where lowest leftAngle is on top.
 * @param writer Writer object for storing stats of this algorithm.
 * @return 
 */
    private static LinkedList<AngleInterval> flattenIntervals(Heap<AngleInterval> startAngles, StatWriter writer)
    {
        int opt = startAngles.size();   //Original amount of sectors is stored for stats collecting.
        Queue<AngleInterval> flat = new Queue<>();  //This will be returned.
        Heap<AngleInterval> endAngles = new Heap<>(15, new EndDirectionComparator());   //This holds sectors in between which lastAngle lies.
        Tree<AngleInterval> distances = new Tree<>(new DistanceComparator());   //Same as endAngles but with different comparatot, telling which sector is closer to src.
        double lastAngle = -Math.PI;    //Angle marker which makes a full rotation from -PI to +PI.
        
        while (!sectorsAreExhausted(startAngles, endAngles))
        {
            AngleInterval newAngle = startAngles.peek();
            while (sectorsRightAngleBeforeLastAngle(endAngles, lastAngle))
                distances.remove(endAngles.pop());
            if (sectorsAreExhausted(startAngles, endAngles))    break;
            AngleInterval oldAngle = endAngles.peek();
            
            if (newAngleBeforeOldAngle(newAngle, oldAngle))
            {
                if (endAngles.isEmpty())
                    lastAngle = newAngle.leftAngle;
                else if (nextSectorIsClosest(newAngle, distances, lastAngle))
                {
                    flat.enqueue(createSectorBeforeNewAngle(lastAngle, distances.getMin(), newAngle));
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
                    flat.enqueue(closeOldSector(lastAngle, oldAngle));
                    lastAngle = oldAngle.rightAngle;
                }
                overWriteFirstSectorsAfterLoop(oldAngle, flat);
                endAngles.pop();
            }
        }
        
        writeStats(writer, opt, flat.size());
        return flat.toLinkedList();
    }
/**
 * Checks if endAngles.peek()'s right angle is before lastAngle even on loop jump.
 * @param endAngles Heap of angles left angles of which are before lastAngle.
 * @param lastAngle Current angle marker of flattenIntervals algorithm.
 * @return true if endAngles.peek()'s right angle is before lastAngle.
 */
    private static boolean sectorsRightAngleBeforeLastAngle(Heap<AngleInterval> endAngles, double lastAngle)
    {
        return !endAngles.isEmpty() && lastAngle > endAngles.peek().rightAngle && 
                (endAngles.peek().leftAngle < endAngles.peek().rightAngle || 
                    lastAngle < endAngles.peek().leftAngle);
    }
/**
 * 
 * @param startAngles Sectors that are yet to be passed by lastAngle.
 * @param endAngles Sectors that were passed by lastAngle.
 * @return true if both heaps of sectors are exhausted.
 */
    private static boolean sectorsAreExhausted(Heap<AngleInterval> startAngles, Heap<AngleInterval> endAngles)
    {
        return startAngles.isEmpty() && endAngles.isEmpty();
    }
/**
 * 
 * @param newAngle
 * @param oldAngle
 * @return true if newAngle opens before oldAngle closes.
 */
    private static boolean newAngleBeforeOldAngle(AngleInterval newAngle, AngleInterval oldAngle)
    {
        return newAngle != null &&
            (oldAngle == null || 
                ((oldAngle.leftAngle < oldAngle.rightAngle && newAngle.leftAngle < oldAngle.rightAngle) ||
                    oldAngle.leftAngle > oldAngle.rightAngle));
    }
/**
 * Overwrites beginning of flattened sectors by the leftovers of the loop jump.
 * @param oldAngle  Angle that passes the loop jump.
 * @param flat Queue of flattened sectors.
 */
    private static void overWriteFirstSectorsAfterLoop(AngleInterval oldAngle, Queue<AngleInterval> flat)
    {
        //got nullpointer
        while (canOverWriteFirstSectorsAfterLoop(oldAngle, flat))
        {
            if (flat.peek().rightAngle > oldAngle.rightAngle)
            {
                double newDist = flat.peek().distanceFromLine(oldAngle.rightDist);
                flat.peek().leftAngle = oldAngle.rightAngle;
                flat.peek().leftDist = newDist;
                flat.enqueue(flat.dequeue());
            }
            else    flat.dequeue();
        }
    }
/**
 * Checks if beginning of sectors can be overwritten by sectors that pass the loop jump.
 * @param oldAngle Angle that passes the loop jump.
 * @param flat Queue of flattened sectors.
 * @return true if beginning of sectors can be overwritten by sectors that pass the loop jump.
 */
    private static boolean canOverWriteFirstSectorsAfterLoop(AngleInterval oldAngle, Queue<AngleInterval> flat)
    {
        return oldAngle.leftAngle > oldAngle.rightAngle && 
                oldAngle.distanceFromLine(flat.peek().leftAngle) < flat.peek().leftDist && 
                flat.peek().leftAngle < oldAngle.rightAngle;
    }
/**
 * 
 * @param newAngle Checked sector.
 * @param distances Sectors that are due process.
 * @param lastAngle Angle marker.
 * @return true if newAngle is closer to src than any sector stored in distances-Tree.
 */
    private static boolean nextSectorIsClosest(AngleInterval newAngle, Tree<AngleInterval> distances, double lastAngle)
    {
        Comparator comp = new DistanceComparator();
        return comp.compare(newAngle, distances.getMin()) < 0 &&
                lastAngle != newAngle.leftAngle;
    }
/**
 * 
 * @param lastAngle Angle marker.
 * @param closestSector Closest stored sector besides newAngle.
 * @param newAngle
 * @return Appropriate sector that bridges the gap before marking lastAngle as newAngle.leftAngle
 */
    private static AngleInterval createSectorBeforeNewAngle(double lastAngle, AngleInterval closestSector, AngleInterval newAngle)
    {
        return new AngleInterval(lastAngle, closestSector.distanceFromLine(lastAngle),
                    newAngle.leftAngle, closestSector.distanceFromLine(newAngle.leftAngle));
    }
/**
 * 
 * @param lastAngle Angle marker.
 * @param oldAngle  Sector that is closed off.
 * @return Appropriate sector that ends where oldAngle ends.
 */
    private static AngleInterval closeOldSector(double lastAngle, AngleInterval oldAngle)
    {
        return new AngleInterval(lastAngle, oldAngle.distanceFromLine(lastAngle),
                        oldAngle.rightAngle, oldAngle.rightDist);
    }
/**
 * Writes statistics of flattenIntervals performance.
 * @param writer Writer object.
 * @param originalSize Amount of sectors before optimization.
 * @param reducedSize   Amount of sectors after optimization.
 */
    private static void writeStats(StatWriter writer, int originalSize, int reducedSize)
    {
        try {
            writer.writeAngleEliminations(originalSize, reducedSize);
        } catch (Throwable e)   {}
    }
/**
 * Finds unobstructed vertices based on generated sectors.
 * @param vertices A set of all vertices in the program
 * @return set of unobstructed vertices.
 */
    private static LinkedList<Vertex> getUnobstructedVertices(LinkedList<Vertex> vertices)
    {
        LinkedList<Vertex> unobstructed = new LinkedList<>();
        addNeighbours(unobstructed);
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
 * @param unobstructed List of unobstructed vertices.
 */
    private static void addNeighbours(LinkedList<Vertex> unobstructed)
    {
        if (src.getClass() != Point.class) return;
        Point p = (Point)src;
        if (p.getLeft().isVertex())
            unobstructed.add(p.getLeft());
        if (p.getRight().isVertex())
            unobstructed.add(p.getRight());
    }
/**
 * Interval of angles, which forms a sector. Each edge of the sector
 * has a value, which is a distance from the vertex that caused the
 * creation of the sector. Anything beyond that distance is obstructed.
 */
    private static class AngleInterval
    {
        public double leftAngle;
        public double rightAngle;
        public double leftDist;
        public double rightDist;
/**
 * Constructor. Creates a sector based on specific attributes.
 * @param la Left-most angle of the sector.
 * @param ld Maximum distance of the left-most angle.
 * @param ra Right-most angle of the sector.
 * @param rd Maximum distance of the right-most angle.
*/
        public AngleInterval(double la, double ld, double ra, double rd)
        {
            leftAngle = la;
            rightAngle = ra;
            leftDist = ld;
            rightDist = rd;
        }
/**
 * Constructor. Creates a sector based on a line.
 * @param p One point of the line. The other point is p.getRight()
 */
        public AngleInterval(Point p)
        {
            Point[] directions = findLeftAndRightAngles(p);
            setAttributes(directions[0], directions[1]);
        }
/**
 * Constructor. Creates a sector for the angle of point's wall part.
 * Max distance is set to 0, that way everything is obstructed in that
 * angle interval. src must be implementing a Point class.
 */
        public AngleInterval()
        {
            if (src.getClass() != Point.class)  return;
            Point p = (Point)src;
            setAttributes(p.getRight(), p.getLeft());
            leftDist = 0;
            rightDist = 0;
        }
/**
 * Initializes class attributes.
 * @param leftest Left point of the line that caused the sector to be created.
 * @param rightest Right point of the line that caused the sector to be created.
 */
        public void setAttributes(Point leftest, Point rightest)
        {
            if (leftest != null)
            {
                leftAngle = Tools.round(src.getDirection(leftest), 10000);
                leftDist = src.getDistance(leftest);
            }
            if (rightest != null)
            {
                rightAngle = Tools.round(src.getDirection(rightest), 10000);
                rightDist = src.getDistance(rightest);
            }
        }
/**
 * Decides which of the two points of the line is considered the left
 * and right part of the sector.
 * @param p One point of the line. The other point is p.getRight()
 * @return [0] - Point that is considered to be the left part of the sector.
 *          [1] - Point that is considered to be the right part of the sector.
 */
        public Point[] findLeftAndRightAngles(Point p)
        {
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
//If line crosses the part where direction calculation jumps the cycle,
//switch sides.
            if (anglesCrossoverDirectionLoop(p))
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
 * Current implementation only supports straight lines.
 * @param p One point of the line. The other point is p.getRight()
 * @return True if line is causing a cycle jump.
 */
        public boolean anglesCrossoverDirectionLoop(Point p)
        {
//Difference in angles between two points can't be more than 180 degrees.
//If this is detected, it's interperted as if line crosses the loop jump point.
            return (Math.abs(src.getDirection(p) - src.getDirection(p.getRight())) > Math.PI);
        }
/**
 * Checks if specified vertex is obstructed based on this sector.
 * @param test Certain vertex.
 * @return true if this sector obstructs the vertex.
 */
        public boolean vertexIsObstructed(Vertex test)
        {
            if (src.hasPointBetween(leftAngle, rightAngle, test))
            {
                if (leftDist <= 0 && rightDist <= 0)    return true;
                if (src.getDistance(test) > distanceFromLine(src.getDirection(test)) + 0.01)
                    return true;
            }
            return false;
        }
/**
 * Calculates the distance from the line that caused the sector to be
 * created at a specified direction.
 * @param testAngle Direction at which the line is crossed.
 * @return Distance from source vertex to the line crosspoint at given direction.
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
 * Comparator class for placing sectors in a tree based on their distance.
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
            if (Tools.hasAngleBetween(ai1.leftAngle, ai1.rightAngle, ai2.leftAngle))
            {
                if (ai2.leftDist < ai1.distanceFromLine(ai2.leftAngle))
                    return 1;
                return -1;
            }
            if (Tools.hasAngleBetween(ai2.leftAngle, ai2.rightAngle, ai1.leftAngle))
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
