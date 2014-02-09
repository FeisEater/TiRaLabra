
package tiralabra.gui.geometrytools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import tiralabra.VertexContainer;
import tiralabra.algorithms.Dijkstra;
import tiralabra.datastructures.TreeMap;
import tiralabra.datastructures.Vertex;
import tiralabra.gui.GraphicInterface;
import tiralabra.gui.MouseInput;

/**
 * Mouse input tool that lets user position vertices and build the graph
 * freely by connecting vertices by hand.
 * @author Pavel
 */
public class BuildGraph extends MouseInput {
    private Vertex edgeend;
    private TreeMap<Vertex, Vertex> previousPoint;
    public BuildGraph(VertexContainer p, GraphicInterface gui)
        {super(p, gui);}
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
                joinPoints(chosenPoint);
            else
                buildPath();
        }
        gui.repaint();
    }
    /**
     * Finds the shortest path via mouse drag.
     */
    public void buildPath()
    {
        previousPoint = Dijkstra.getShortestPaths(draggedFromPoint, points.getVertices().toLinkedList());
    }
/**
 * Adds a vertex.
 * @param x X coordinate where vertex is created.
 * @param y Y coordinate where vertex is created.
 */
    public void addPoint(int x, int y)
    {
        Vertex p = points.addVertex(x,y);
        edgeend = null;
    }
/**
 * Removes given vertex.
 * @param point given vertex.
 */
    public void removePoint(Vertex point)
    {
        points.removeVertex(point);
    }
/**
 * Selects vertex to connect with other vertex. If one was already selected,
 * adds a connection or removes it if they were already connected.
 * @param point Chosen vertex.
 */
    public void joinPoints(Vertex point)
    {
        if (point == null)  return;
        
        if (edgeend == null)
            edgeend = point;
        else
        {
            points.toggleEdge(point, edgeend);
            edgeend = null;
        }
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
 * Decides what color should the vertex be represented as.
 * Points that await to be connected with other vertex are colored magenta.
 * @param point Specific vertex
 * @return Color of the vertex.
 */
    @Override
    public Color chooseColorByPoint(Vertex point)
    {
        if (point == edgeend)
            return Color.magenta;
        return super.chooseColorByPoint(point);
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
