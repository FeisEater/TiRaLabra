
package tiralabra.algorithms;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import tiralabra.datastructures.Vertex;
import tiralabra.datastructures.Point;

/**
 * Dijkstra's algorithm for finding the shortest path in a graph.
 * @author Pavel
 */
public class Dijkstra {
    private static Map<Vertex, Double> shortestPaths = new HashMap<>();
    private static Map<Vertex, Vertex> previousPoint = new HashMap<>();
/**
 * Runs the algorithm.
 * @param begin Beginning vertex from which the path is generated.
 * @param originalPoints Set of all vertices in the graph.
 * @return Map, which tells for each vertex which is its previous vertex
 *          that leads to the shortest path. To find the shortest path,
 *          find destination vertex on the map and call the value recursively
 *          until you get the source vertex. Note that this is the reverse order.
 */
    public static Map<Vertex, Vertex> getShortestPaths(Vertex begin, List<Vertex> originalPoints)
    {
        PriorityQueue<Vertex> points = initialize(begin, originalPoints);
        while (!points.isEmpty())
            relaxEdgesOnNextInHeap(points);
        return previousPoint;
    }
/**
 * Initializes attributes for the algorithm.
 * @param begin Beginning vertex from which the path is generated.
 * @param originalPoints Set of all vertices in the graph.
 * @return Heap of vertices, which have estimated path lengths. Source vertex's
 *          value is 0, other vertices' is Double.MAX_VALUE
 */
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
/**
 * Takes the next vertex in the heap and re-estimates its path length.
 * @param points Heap of vertices, which have estimated path lengths.
 */
    private static void relaxEdgesOnNextInHeap(PriorityQueue<Vertex> points)
    {
        Vertex p = points.poll();
        for (Vertex adj : p.getAdjacents())
        {
            if (!points.contains(adj))  continue;
             
            if (shortestPaths.get(p) + p.getDistance(adj) >= shortestPaths.get(adj))
                continue;

            points.remove(adj);
            shortestPaths.put(adj, shortestPaths.get(p) + p.getDistance(adj));
            points.add(adj);
            previousPoint.put(adj, p);
        }
    }
/**
 * Comparator for the vertex heap. Vertex with shortest path length estimate
 * is first on the heap.
 */
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
