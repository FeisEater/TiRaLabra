
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
import tiralabra.datastructures.Vertex;
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
        for (Vertex p : points.getPoints())
            drawPoint(g, p);
        /*int pw = 8;
        g.setColor(Color.green);
        for (int i = 0; i < frame.getWidth(); i += pw)
            for (int j = 0; j < frame.getHeight(); j += pw)
                if (!AngleElimination.isObstructed(new Point(i+pw/2, j+pw/2)))
                    g.fillRect(i, j, pw, pw);*/
        currentTool.drawInputSpecific(g);        
    }
    public void drawPoint(Graphics g, Vertex point)
    {
        for (Vertex adj : point.getAdjacents())
            drawEdge(g, Color.red, point, adj);
        g.setColor(currentTool.chooseColorByPoint(point));
        g.fillOval((int)point.X() - Const.pointWidth / 2, 
            (int)point.Y() - Const.pointWidth / 2,
            Const.pointWidth, Const.pointWidth);
        if (point.getClass() == Point.class)
        {
            Point p = (Point)point;
            drawEdge(g, Color.black, p, p.getLeft());
            drawEdge(g, Color.black, p, p.getRight());
            g.setColor(Color.cyan);
            g.drawLine((int)p.X(), (int)p.Y(), p.angleMarker()[0], p.angleMarker()[1]);
        }
        /*if (point.getRight() != null)
        {
            g.setColor(Color.red);
            double dir = point.getDirection(point.getRight());
            int[] xPoints = {(int)(point.getRight().X() - Math.cos(dir) * 16 - Math.cos(dir + Math.PI/2) * 8),
                            (int)point.getRight().X(),
                            (int)(point.getRight().X() - Math.cos(dir) * 16 + Math.cos(dir + Math.PI/2) * 8)};
            int[] yPoints = {(int)(point.getRight().Y() - Math.sin(dir) * 16 - Math.sin(dir + Math.PI/2) * 8),
                            (int)point.getRight().Y(),
                            (int)(point.getRight().Y() - Math.sin(dir) * 16 + Math.sin(dir + Math.PI/2) * 8)};
            g.fillPolygon(xPoints, yPoints, 3);
        }*/
    }
    public void drawEdge(Graphics g, Color c, Vertex p1, Vertex p2)
    {
        if (p1 == null || p2 == null)   return;
        g.setColor(c);
        g.drawLine((int)p1.X(), (int)p1.Y(), (int)p2.X(), (int)p2.Y());
    }
    public void fillPolygon(Graphics g)
    {
        Set<Point> used = new HashSet<>();
        for (Vertex v : points.getPoints())
        {
            if (v.getClass() != Point.class)    continue;
            Point p = (Point)v;
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
