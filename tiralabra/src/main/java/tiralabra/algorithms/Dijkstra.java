
package tiralabra.algorithms;

import java.util.Comparator;
import tiralabra.datastructures.Heap;
import tiralabra.datastructures.LinkedList;
import tiralabra.datastructures.Vertex;
import tiralabra.datastructures.Point;
import tiralabra.datastructures.TreeMap;
import tiralabra.util.VertexComparator;

/**
 * Dijkstra's algorithm for finding the shortest path in a graph.
 * @author Pavel
 */
public class Dijkstra {
    private static TreeMap<Vertex, Double> shortestPaths = new TreeMap<>(new VertexComparator());
    private static TreeMap<Vertex, Vertex> previousVertex = new TreeMap<>(new VertexComparator());
    private static Vertex e;
/**
 * Runs the algorithm.
 * @param begin Beginning vertex from which the path is generated.
 * @param end Destination vertex in the path. If set to null, algorithm
 *          will produce the same result, however the A* heuristic
 *          will not be used, which may slow down the algorithm.
 * @param vertices Set of all vertices in the graph.
 * @return Map, which tells for each vertex which is its previous vertex
 *          that leads to the shortest path. To find the shortest path,
 *          find destination vertex on the map and call the value recursively
 *          until you get the source vertex. Note that this is the reverse order.
 */
    public static TreeMap<Vertex, Vertex> getShortestPaths(Vertex begin, Vertex end, LinkedList<Vertex> vertices)
    {
        e = end;
        Heap<Vertex> heap = initialize(begin, vertices);
        while (!heap.isEmpty())
        {
            if (heap.peek() == end) break;
            relaxEdgesOnNextInHeap(heap);
        }
        return previousVertex;
    }
/**
 * Initializes attributes for the algorithm.
 * @param begin Beginning vertex from which the path is generated.
 * @param originalPoints Set of all vertices in the graph.
 * @return Heap of vertices, which have estimated path lengths. Source vertex's
 *          value is 0, other vertices' is Double.MAX_VALUE
 */
    private static Heap<Vertex> initialize(Vertex begin, LinkedList<Vertex> vertices)
    {
        shortestPaths.clear();
        previousVertex.clear();
        Heap<Vertex> dijkstraVertices = 
            new Heap<>(15, new DijkstraComparator(), new TreeMap(new VertexComparator()));
        while (vertices.hasNext())
        {
            Vertex p = vertices.getNext();
            double d = (p == begin) ? 0 : Double.MAX_VALUE;
            shortestPaths.put(p, d);
            dijkstraVertices.insert(p);
        }
        return dijkstraVertices;
    }
/**
 * Takes the next vertex in the heap and re-estimates its path length.
 * @param vertices Heap of vertices, which have estimated path lengths.
 * @param end End destination of the path.
 */
    private static void relaxEdgesOnNextInHeap(Heap<Vertex> vertices)
    {
        Vertex p = vertices.pop();
        LinkedList<Vertex> list = p.getAdjacents().toLinkedList();
        while (list.hasNext())
        {
            Vertex adj = list.getNext();
                        
            if (shortestPaths.get(p) + p.getDistance(adj) >= shortestPaths.get(adj))
                continue;

            shortestPaths.put(adj, shortestPaths.get(p) + p.getDistance(adj));
            vertices.valueChanged(adj);
            previousVertex.put(adj, p);
        }
    }
/**
 * Calculates an A* heuristic to speed up Dijkstra Algorithm's performance.
 * @param p Graph vertex currently being processed.
 * @param end End destination of the path.
 * @return Chebyshev distance between p and end.
 */
    private static double aStarHeuristic(Vertex p)
    {
        if (e == null)    return 0;
        return Math.max(Math.abs(p.X() - e.X()), Math.abs(p.Y() - e.Y()));
    }
/**
 * Comparator for the vertex heap. Vertex with shortest path length estimate
 * is first on the heap.
 */
    private static class DijkstraComparator implements Comparator
    {
        @Override
        public int compare(Object o1, Object o2)
        {
            if ((o1.getClass() != Vertex.class && o1.getClass() != Point.class) ||
                (o2.getClass() != Vertex.class && o2.getClass() != Point.class))
                return 0;
            Vertex v1 = (Vertex)o1;
            Vertex v2 = (Vertex)o2;
            return (shortestPaths.get(v1) + aStarHeuristic(v1) < 
                    shortestPaths.get(v2) + aStarHeuristic(v2)) ? -1 : 1;
        }
    }
}
