package tiralabra;

import tiralabra.gui.GraphicInterface;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import tiralabra.datastructures.Point;

/**
 * Hello world!
 *
 */
public class App 
{
    public static List<Point> points = new ArrayList<>();

    public static void main( String[] args )
    {
        SwingUtilities.invokeLater(new GraphicInterface());
    }
    public static void addPoint(int x, int y)
    {
        points.add(new Point((double)x, (double)y));
    }
    public static void removePoint(Point point)
    {
        if (point == null)  return;
        point.removeAllAdjacents();
        points.remove(point);
    }
    public static void toggleEdge(Point p1, Point p2)
    {
        if (p1.getAdjacents().contains(p2))
            p1.removeAdjacent(p2);
        else
            p1.addAdjacent(p2);
    }
}
