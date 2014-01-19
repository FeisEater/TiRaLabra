
package tiralabra.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import tiralabra.App;
import tiralabra.algorithms.Dijkstra;
import tiralabra.datastructures.Point;
import tiralabra.util.Const;

/**
 *
 * @author Pavel
 */
public class GraphicInterface extends JPanel implements Runnable {
    private JFrame frame;
    private Point edgeend;
    private Point dragfrom;
    private Point dragto;
    private Point begin;
    private Point prev;
    private Map<Point, Point> previousPoint;
    public GraphicInterface()
    {
        super();
        addMouseListener(new MouseInput(this));
    }
    public void startDrag(Point point)
    {
        dragfrom = point;
        dragto = null;
        repaint();
    }
    public void stopDrag(Point point)
    {
        dragto = point;
        previousPoint = Dijkstra.getShortestPaths(dragfrom, App.points);
    }
    public void addPoint(int x, int y)
    {
        Point p = App.addPoint(x,y);
        if (begin == null)  begin = p;
        else
        {
            p.setLeft(prev);
            prev.setRight(p);
        }
        prev = p;
        edgeend = null;
        dragfrom = null;
        dragto = null;
    }
    public void removePoint(Point point)
    {
        App.removePoint(point);
    }
    public void addEdge(Point point)
    {
        begin.setLeft(prev);
        prev.setRight(begin);
        begin = null;
        prev = null;
        App.buildGraph();
/*        if (point == null)  return;
        
        dragfrom = null;
        dragto = null;
        if (edgeend == null)
            edgeend = point;
        else
        {
            App.toggleEdge(point, edgeend);
            edgeend = null;
        }*/
    }
    public boolean wasntDragged(Point point)
    {
        return dragfrom == point;
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
        for (Point p : App.points)
            drawPoint(g, p);
        drawShortestPath(g);
    }
    public void drawPoint(Graphics g, Point point)
    {
        for (Point adj : point.getAdjacents())
            drawEdge(g, Color.red, point, adj);
        g.setColor(chooseColorByPoint(point));
        g.fillOval((int)point.X() - Const.pointWidth / 2, 
            (int)point.Y() - Const.pointWidth / 2,
            Const.pointWidth, Const.pointWidth);
        drawEdge(g, Color.black, point, point.getLeft());
        drawEdge(g, Color.black, point, point.getRight());
        g.setColor(Color.cyan);
        g.drawLine((int)point.X(), (int)point.Y(), point.angleMarker()[0], point.angleMarker()[1]);
    }
    public Color chooseColorByPoint(Point point)
    {
        if (point == edgeend)
            return Color.magenta;
        if (point == dragfrom)
            return Color.orange;
        if (point == dragto)
            return Color.green;
        if (point.isVertex())
            return Color.BLUE;
        return Color.black;
    }
    public void drawEdge(Graphics g, Color c, Point p1, Point p2)
    {
        if (p1 == null || p2 == null)   return;
        g.setColor(c);
        g.drawLine((int)p1.X(), (int)p1.Y(), (int)p2.X(), (int)p2.Y());
    }
    public void drawShortestPath(Graphics g)
    {
        Point next = dragto;
        while (next != null)
        {
            Point prev = previousPoint.get(next);
            if (prev != null)
                drawEdge(g, Color.green, prev, next);
            next = prev;
        }
    }
}
