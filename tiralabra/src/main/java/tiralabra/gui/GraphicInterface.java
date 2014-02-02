
package tiralabra.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import tiralabra.VertexContainer;
import tiralabra.algorithms.AngleElimination;
import tiralabra.datastructures.LinkedList;
import tiralabra.datastructures.Point;
import tiralabra.datastructures.Tree;
import tiralabra.datastructures.Vertex;
import tiralabra.gui.geometrytools.BuildGraph;
import tiralabra.gui.geometrytools.ChainPolygon;
import tiralabra.util.Const;

/**
 * Plain graphic interface to visualise the going-ons of the program
 * and to let player interact with the program.
 * @author Pavel
 */
public class GraphicInterface extends JPanel implements Runnable {
    private JFrame frame;
    private VertexContainer points;
    private MouseInput currentTool;
    private Tree<Integer> tree;
/**
 * Constructor.
 * @param points VertexContainer object.
 */
    public GraphicInterface(VertexContainer points)
    {
        super();
        currentTool = new ChainPolygon(points, this);
        addMouseListener(currentTool);
        this.points = points;
        tree = new Tree<>(new Comparator() {
            public int compare(Object o1, Object o2)
            {
                if (o1.getClass() != Integer.class || o2.getClass() != Integer.class)
                    return 0;
                int i1 = (Integer)o1;
                int i2 = (Integer)o2;
                return i1-i2;
            }
        });
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
        fillUnobstructedArea(g, 8);
        LinkedList<Vertex> vertices = points.getVertices().toLinkedList();
        while (vertices.hasNext())
            drawPoint(g, vertices.getNext());
        currentTool.drawInputSpecific(g);   
        //points.getVertices().drawTree(g);
    }
    public Tree<Integer> getTree()
    {
        return tree;
    }
/**
 * Draws certain vertex and its connections with other vertices.
 * @param g Graphics object.
 * @param point Vertex that is drawn.
 */
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
    }
/**
 * Draws a connection between two specified vertices.
 * @param g Graphics object.
 * @param c Color of the line.
 * @param p1 Specified vertex
 * @param p2 Specified vertex
 */
    public void drawEdge(Graphics g, Color c, Vertex p1, Vertex p2)
    {
        if (p1 == null || p2 == null)   return;
        g.setColor(c);
        g.drawLine((int)p1.X(), (int)p1.Y(), (int)p2.X(), (int)p2.Y());
    }
/**
 * Draws a polygon.
 * @param g Graphics object.
 */
    public void fillPolygon(Graphics g)
    {
        Set<Point> used = new HashSet<>();
        LinkedList<Vertex> vertices = points.getVertices().toLinkedList();
        while (vertices.hasNext())
        {
            Vertex v = vertices.getNext();
            if (v.getClass() != Point.class)    continue;
            Point p = (Point)v;
            if (used.contains(p)) continue;
            
            LinkedList<Integer> x = new LinkedList<>();
            LinkedList<Integer> y = new LinkedList<>();
            if (!retrieveCoordinatesFromPolygon(p, x, y, used))
                continue;
            
            g.setColor((points.shapeIsWall(p)) ? Color.PINK : Color.WHITE);
            g.fillPolygon( convertLinkedListToArray(x),
                            convertLinkedListToArray(y),
                            x.size());
        }
    }
/**
 * Converts arrayList data to an array.
 * @param list ArrayList object
 * @return array that contains all data that ArrayList contains.
 */
    public int[] convertLinkedListToArray(LinkedList<Integer> list)
    {
        int[] result = new int[list.size()];
        for (int i = 0; list.hasNext(); i++) result[i] = list.getNext();
        return result;
    }
/**
 * Collects coordinates from all points in a polygon.
 * @param first A point in the polygon from which all other points are accessed.
 * @param x ArrayList of X coordinates.
 * @param y ArrayList of Y coordinates.
 * @param used Set of points that were already processed.
 * @return true if coordinates were retrieved correctly.
 */
    public boolean retrieveCoordinatesFromPolygon(Point first, LinkedList<Integer> x, LinkedList<Integer> y, Set<Point> used)
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
/**
 * Visualises the area that is unobstructed from the point of view
 * of the point that was the last one that called the tracing algorithm.
 * @param g Graphics object.
 * @param pixelsize Size of the pixel.
 */
    public void fillUnobstructedArea(Graphics g, int pixelsize)
    {
        g.setColor(Color.green);
        for (int i = 0; i < frame.getWidth(); i += pixelsize)
            for (int j = 0; j < frame.getHeight(); j += pixelsize)
                if (!AngleElimination.isObstructed(new Vertex(i+pixelsize/2, j+pixelsize/2)))
                    g.fillRect(i, j, pixelsize, pixelsize);
    }
}
