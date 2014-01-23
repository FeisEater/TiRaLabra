
package tiralabra.algorithms;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import tiralabra.datastructures.Vertex;
import tiralabra.datastructures.Point;
import tiralabra.util.Tools;

/**
 *
 * @author Pavel
 */
public class Dijkstra {
    private static Map<Vertex, Double> shortestPaths = new HashMap<>();
    private static Map<Vertex, Vertex> previousPoint = new HashMap<>();
    public static Map<Vertex, Vertex> getShortestPaths(Vertex begin, List<Vertex> originalPoints)
    {
        PriorityQueue<Vertex> points = initialize(begin, originalPoints);
        while (!points.isEmpty())
            relaxEdgesOnNextInHeap(points);
        return previousPoint;
    }
    private static PriorityQueue<Vertex> initialize(Vertex begin, List<Vertex> points)
    {
        shortestPaths.clear();
        previousPoint.clear();
        PriorityQueue<Vertex> dijkstrapoints = new PriorityQueue<>(11, new pointComparator());
        for (Vertex p : points)
        {
            double d = (p == begin) ? 0 : Double.MAX_VALUE;
            shortestPaths.put(p, d);
            dijkstrapoints.add(p);
        }
        return dijkstrapoints;
    }
    private static void relaxEdgesOnNextInHeap(PriorityQueue<Vertex> points)
    {
        Vertex p = points.poll();
        for (Vertex adj : p.getAdjacents())
        {
            if (!points.contains(adj))  continue;
             
            if (shortestPaths.get(p) + Tools.distance(p, adj) >= shortestPaths.get(adj))
                continue;

            points.remove(adj);
            shortestPaths.put(adj, shortestPaths.get(p) + Tools.distance(p, adj));
            points.add(adj);
            previousPoint.put(adj, p);
        }
    }
    private static class pointComparator implements Comparator
    {
        @Override
        public int compare(Object o1, Object o2)
        {
            if ((o1.getClass() != Vertex.class && o1.getClass() != Point.class) ||
                (o2.getClass() != Vertex.class && o2.getClass() != Point.class))
                return 0;
            return (int)(shortestPaths.get((Vertex)o1) - shortestPaths.get((Vertex)o2));
        }
    }
}
