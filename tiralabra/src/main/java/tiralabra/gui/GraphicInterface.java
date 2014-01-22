
package tiralabra.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import tiralabra.PointContainer;
import tiralabra.datastructures.Point;
import tiralabra.gui.geometrytools.BuildGraph;
import tiralabra.gui.geometrytools.ChainPolygon;
import tiralabra.util.Const;

/**
 *
 * @author Pavel
 */
public class GraphicInterface extends JPanel implements Runnable {
    private JFrame frame;
    private PointContainer points;
    private MouseInput currentTool;
    public GraphicInterface(PointContainer points)
    {
        super();
        currentTool = new ChainPolygon(points, this);
        addMouseListener(currentTool);
        this.points = points;
    }
    @Override
    public void run()
    {
        frame = new JFrame("tiralabra");
        frame.setPreferredSize(new Dimension(640, 480));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
    }
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        fillPolygon(g);
        for (Point p : points.getPoints())
            drawPoint(g, p);
        currentTool.drawInputSpecific(g);
    }
    public void drawPoint(Graphics g, Point point)
    {
        for (Point adj : point.getAdjacents())
            drawEdge(g, Color.red, point, adj);
        g.setColor(currentTool.chooseColorByPoint(point));
        g.fillOval((int)point.X() - Const.pointWidth / 2, 
            (int)point.Y() - Const.pointWidth / 2,
            Const.pointWidth, Const.pointWidth);
        drawEdge(g, Color.black, point, point.getLeft());
        drawEdge(g, Color.black, point, point.getRight());
        g.setColor(Color.cyan);
        g.drawLine((int)point.X(), (int)point.Y(), point.angleMarker()[0], point.angleMarker()[1]);
    }
    public void drawEdge(Graphics g, Color c, Point p1, Point p2)
    {
        if (p1 == null || p2 == null)   return;
        g.setColor(c);
        g.drawLine((int)p1.X(), (int)p1.Y(), (int)p2.X(), (int)p2.Y());
    }
    public void fillPolygon(Graphics g)
    {
        Set<Point> used = new HashSet<>();
        for (Point p : points.getPoints())
        {
            if (used.contains(p)) continue;
            
            List<Integer> x = new ArrayList<>();
            List<Integer> y = new ArrayList<>();
            if (!retrieveCoordinatesFromPolygon(p, x, y, used))
                continue;
            
            g.setColor(Color.PINK);
            g.fillPolygon( convertArrayListToArray(x),
                            convertArrayListToArray(y),
                            x.size());
        }
    }
    public int[] convertArrayListToArray(List<Integer> list)
    {
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) result[i] = list.get(i);
        return result;
    }
    public boolean retrieveCoordinatesFromPolygon(Point first, List<Integer> x, List<Integer> y, Set<Point> used)
    {
        Point q = first;
        do
        {
            if (q == null)
                return false;
            x.add((int)q.X());
            y.add((int)q.Y());
            used.add(q);
            q = q.getRight();
        } while (first != q);
        return true;
    }

}
