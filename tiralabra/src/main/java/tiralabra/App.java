package tiralabra;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import tiralabra.datastructures.Point;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
/*        Map<Point, Double> shortestPaths = new HashMap<>();
        Map<Point, Point> previousPoint = new HashMap<>();
        PriorityQueue<Point> points = new PriorityQueue<>();
        while (!points.isEmpty())
        {
            Point p = points.poll();
            for (Point adj : p.getAdjacents())
            {
                if (distance(p, adj) < shortestPaths.get(adj))
                {
                    shortestPaths.put(adj, distance(p, adj));
                    previousPoint.put(adj, p);
                }
                if (!points.contains(adj))   points.add(adj);
            }
        }*/
    }
    public static double distance(Point p1, Point p2)
    {
        return Math.sqrt(Math.pow(p1.X() - p2.X(), 2) +
                Math.pow(p1.Y() - p2.Y(), 2));
    }
}
