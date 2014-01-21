
package tiralabra.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        if (begin != null && begin != null)
        {
            begin.setLeft(prev);
            prev.setRight(begin);
        }
        App.setShapeMode(begin);
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
        fillPolygon(g);
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
            Point q = previousPoint.get(next);
            if (q != null)
                drawEdge(g, Color.green, q, next);
            next = q;
        }
    }
    public void fillPolygon(Graphics g)
    {
        Set<Point> used = new HashSet<>();
        for (Point p : App.points)
        {
            if (used.contains(p))   continue;
            
            List<Integer> x = new ArrayList<>();
            List<Integer> y = new ArrayList<>();
            if (!retrieveCoordinatesFromPolygon(p, x, y, used))
                continue;
            
            g.setColor(Color.PINK);
            g.fillPolygon(  convertArrayListToArray(x),
                            convertArrayListToArray(y),
                            x.size());
        }
    }
    public int[] convertArrayListToArray(List<Integer> list)
    {
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++)  result[i] = list.get(i);
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
        }   while (first != q);
        return true;
    }
}
