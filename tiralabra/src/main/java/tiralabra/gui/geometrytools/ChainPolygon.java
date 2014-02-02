
package tiralabra.gui.geometrytools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.Map;
import tiralabra.VertexContainer;
import tiralabra.algorithms.AngleElimination;
import tiralabra.algorithms.Dijkstra;
import tiralabra.datastructures.Point;
import tiralabra.datastructures.Vertex;
import tiralabra.gui.GraphicInterface;
import tiralabra.gui.MouseInput;

/**
 * Mouse input tool that lets user chain points together to form
 * polygons.
 * @author Pavel
 */
public class ChainPolygon extends MouseInput {
    private Point begin;
    private Point prev;
    private Map<Vertex, Vertex> previousPoint;
    private boolean wallmode;
    public ChainPolygon(VertexContainer p, GraphicInterface gui)
    {
        super(p, gui);
        wallmode = true;
    }
    @Override
    public void mouseReleased(MouseEvent e)
    {
        super.mouseReleased(e);
        Vertex chosenPoint = choosePoint(e);
        if (e.getButton() == MouseEvent.BUTTON1)
            addPoint(draggedToX, draggedToY);
        else if (e.getButton() == MouseEvent.BUTTON2)
            removePoint(chosenPoint);
        else if (e.getButton() == MouseEvent.BUTTON3)
        {
            if (draggedFromPoint == chosenPoint)
            {
                if (begin == null)
                    traceAround(e.getX(), e.getY(), chosenPoint);
                else
                    closeLoop();
            }
            else
                buildPath();
        }
        gui.repaint();
    }
    public void traceAround(int x, int y, Vertex point)
    {
        //points.addVertex(x, y);
        //points.buildGraph();
        //AngleElimination.findUnobstructedPoints(point, points.getVertices());
    }
    /**
     * Finds the shortest path via mouse drag.
     */
    public void buildPath()
    {
        previousPoint = Dijkstra.getShortestPaths(draggedFromPoint, points.getVertices().toLinkedList());
    }
/**
 * Adds a point and chains it to previously created point.
 * @param x X coordinate where point is created.
 * @param y Y coordinate where point is created.
 */
    public void addPoint(int x, int y)
    {
        Point p = points.addPoint(x,y);
        if (begin == null)  begin = p;
        else
        {
            p.setLeft(prev);
            prev.setRight(p);
        }
        prev = p;
        draggedToPoint = null;
    }
/**
 * Removes given vertex.
 * @param point given vertex.
 */
    public void removePoint(Vertex point)
    {
        points.removeVertex(point);
        draggedToPoint = null;
        if (point == null)
            wallmode = !wallmode;
    }
/**
 * Closes the polygon loop by chaining firstly created point
 * with previously created point.
 */
    public void closeLoop()
    {
        if (begin != null && prev != null)
        {
            begin.setLeft(prev);
            prev.setRight(begin);
        }
        points.setShapeMode(begin, wallmode);
        begin = null;
        prev = null;
        points.buildGraph();
    }
/**
 * Draws information specific to mouse tool mode.
 * Draws the shortest path which was selected by buildPath()
 * @param g Graphics object.
 */
    @Override
    public void drawInputSpecific(Graphics g)
    {
        drawShortestPath(g);
    }
/**
 * Draws the shortest path which was selected by buildPath().
 * @param g Graphics object.
 */
    public void drawShortestPath(Graphics g)
    {
        if (previousPoint == null)  return;
        
        Vertex next = draggedToPoint;
        while (next != null)
        {
            Vertex q = previousPoint.get(next);
            if (q != null)
                gui.drawEdge(g, Color.green, q, next);
            next = q;
        }
    }

}
