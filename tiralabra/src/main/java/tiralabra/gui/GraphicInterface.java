
package tiralabra.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import tiralabra.VertexContainer;
import tiralabra.algorithms.AngleElimination;
import tiralabra.algorithms.Dijkstra;
import tiralabra.datastructures.LinkedList;
import tiralabra.datastructures.Point;
import tiralabra.datastructures.Tree;
import tiralabra.datastructures.TreeMap;
import tiralabra.datastructures.Vertex;
import tiralabra.util.Const;
import tiralabra.util.VertexComparator;

/**
 * Plain graphic interface to visualise the going-ons of the program
 * and to let player interact with the program.
 * @author Pavel
 */
public class GraphicInterface extends JPanel implements Runnable {
    private JFrame frame;
    private VertexContainer vertices;
    private MouseInput currentTool;
    private ToolSwitcher toolswitcher;
    private Vertex traceSrc;
/**
 * Constructor.
 * @param vertices VertexContainer object.
 */
    public GraphicInterface(VertexContainer vertices)
    {
        super();
        toolswitcher = new ToolSwitcher(vertices, this);
        this.vertices = vertices;
    }
/**
 * Changes geometry manipulation tool.
 * @param tool new tool.
 */
    public void switchTool(MouseInput tool)
    {
        removeMouseListener(currentTool);
        removeMouseMotionListener(currentTool);
        if (currentTool != null)   currentTool.close();
        currentTool = tool;
        addMouseMotionListener(currentTool);
        addMouseListener(currentTool);
        repaint();
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
        LinkedList<Vertex> vlist = vertices.getVertices().toLinkedList();
        while (vlist.hasNext())
            drawVertex(g, vlist.getNext());
        currentTool.drawInputSpecific(g);
        drawShortestPath(g);
    }
/**
 * Draws the shortest path which was selected by buildPath().
 * @param g Graphics object.
 */
    public void drawShortestPath(Graphics g)
    {
        TreeMap<Vertex, Vertex> previousPoint = 
                Dijkstra.getShortestPaths(vertices.endA, vertices.endB, vertices.getVertices().toLinkedList());
        if (previousPoint == null)  return;
        
        TreeMap<Vertex, Vertex> shortestPath = new TreeMap(new VertexComparator());
        Vertex next = vertices.endB;
        while (next != null)
        {
            Vertex q = previousPoint.get(next);
            if (q != null)
                drawEdge(g, Color.blue, q, next);
            shortestPath.put(next, q);
            next = q;
        }
        
        while (!previousPoint.isEmpty())
        {
            Vertex p = (Vertex)previousPoint.getMin();
            if (p != null)
            {
                Vertex q = previousPoint.get(p);
                if (q != shortestPath.get(p))
                    drawEdge(g, Color.LIGHT_GRAY, p, q);
                previousPoint.remove(p);
            }
        }
    }
/**
 * Draws certain vertex and its connections with other vertices.
 * @param g Graphics object.
 * @param vertex Vertex that is drawn.
 */
    public void drawVertex(Graphics g, Vertex vertex)
    {
        LinkedList<Vertex> list = vertex.getAdjacents().toLinkedList();
        while (list.hasNext())
            drawEdge(g, Color.white, vertex, list.getNext());
        //if (vertex != vlist.endA && vertex != vlist.endB)   return;
        g.setColor(currentTool.chooseColorByPoint(vertex));
        g.fillOval((int)vertex.X() - Const.pointWidth / 2, 
            (int)vertex.Y() - Const.pointWidth / 2,
            Const.pointWidth, Const.pointWidth);
        if (vertex.getClass() == Point.class)
        {
            Point p = (Point)vertex;
            drawEdge(g, Color.PINK, p, p.getLeft());
            drawEdge(g, Color.PINK, p, p.getRight());
            //g.setColor(Color.cyan);
            //g.drawLine((int)p.X(), (int)p.Y(), p.angleMarker()[0], p.angleMarker()[1]);
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
        Tree<Point> used = new Tree<>(new VertexComparator());
        LinkedList<Vertex> vlist = vertices.getVertices().toLinkedList();
        while (vlist.hasNext())
        {
            Vertex v = vlist.getNext();
            if (v.getClass() != Point.class)    continue;
            Point p = (Point)v;
            if (used.contains(p)) continue;
            
            LinkedList<Integer> x = new LinkedList<>();
            LinkedList<Integer> y = new LinkedList<>();
            if (!retrieveCoordinatesFromPolygon(p, x, y, used))
                continue;
            
            g.setColor((vertices.shapeIsWall(p)) ? Color.PINK : Color.WHITE);
            g.fillPolygon( convertLinkedListToArray(x),
                            convertLinkedListToArray(y),
                            x.size());
        }
    }
/**
 * Converts LinkedList data to an array.
 * @param list LinkedList object
 * @return array that contains all data that LinkedList contains.
 */
    public int[] convertLinkedListToArray(LinkedList<Integer> list)
    {
        int[] result = new int[list.size()];
        for (int i = 0; list.hasNext(); i++) result[i] = list.getNext();
        return result;
    }
/**
 * Collects coordinates from all vertices in a polygon.
 * @param first A point in the polygon from which all other vertices are accessed.
 * @param x LinkedList of X coordinates.
 * @param y LinkedList of Y coordinates.
 * @param used Set of vertices that were already processed.
 * @return true if coordinates were retrieved correctly.
 */
    public boolean retrieveCoordinatesFromPolygon(Point first, LinkedList<Integer> x, LinkedList<Integer> y, Tree<Point> used)
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
