package tiralabra;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import javax.swing.SwingUtilities;
import tiralabra.datastructures.Point;

/**
 * Hello world!
 *
 */
public class App 
{
    private static class pointComparator implements Comparator
    {
        @Override
        public int compare(Object o1, Object o2)
        {
            if (o1.getClass() != Point.class ||
                o2.getClass() != Point.class)    return 0;
            return compare(shortestPaths.get(o1), shortestPaths.get(o2));
        }
    }
    public static List<Point> points = new ArrayList<>();
    private static PriorityQueue<Point> dijkstrapoints = new PriorityQueue<>(100, new pointComparator());
    private static Map<Point, Double> shortestPaths = new HashMap<>();
    public static Map<Point, Point> previousPoint = new HashMap<>();
    private static GraphicInterface gui = new GraphicInterface();
    private static int i = 0;

    public static void main( String[] args )
    {
        SwingUtilities.invokeLater(gui);
        while (!gui.created) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                System.out.println("Piirtoalustaa ei ole vielä luotu.");
            }
        }
    }
    public static void addPoint(int x, int y)
    {
        points.add(new Point((double)x, (double)y, i++));
    }
    public static void toggleEdge(Point p1, Point p2)
    {
        if (p1.getAdjacents().contains(p2))
            p1.removeAdjacent(p2);
        else
            p1.addAdjacent(p2);
    }
    public static double distance(Point p1, Point p2)
    {
        return Math.sqrt(Math.pow(p1.X() - p2.X(), 2) +
                Math.pow(p1.Y() - p2.Y(), 2));
    }
    public static void dijkstra(Point begin)
    {
        shortestPaths.clear();
        previousPoint.clear();
        for (Point p : points)
        {
            double d = (p == begin) ? 0 : Double.MAX_VALUE;
            shortestPaths.put(p, d);
            dijkstrapoints.add(p);
        }
        while (!dijkstrapoints.isEmpty())
        {
            Point p = dijkstrapoints.poll();
            System.out.println("--- took " + p + " with " + shortestPaths.get(p));
            for (Point adj : p.getAdjacents())
            {
                if (dijkstrapoints.contains(adj) && 
                        distance(p, adj) < shortestPaths.get(adj))
                {
                    shortestPaths.put(adj, distance(p, adj));
                    PriorityQueue<Point> keko = new PriorityQueue<>();
                    for (Point q : dijkstrapoints)
                        keko.add(q);
                    dijkstrapoints = keko;
                    previousPoint.put(adj, p);
                }
                for (Point q : dijkstrapoints)
                    System.out.print(" " + shortestPaths.get(q));
                System.out.println("");
            }
        }
        System.out.println(previousPoint);
    }
}
