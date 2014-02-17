
package tiralabra.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputListener;
import tiralabra.VertexContainer;
import tiralabra.datastructures.LinkedList;
import tiralabra.datastructures.Vertex;
import tiralabra.util.Const;

/**
 * Handles mouse input. Can be extended to implement differrent commands.
 * @author Pavel
 */
public class MouseInput implements MouseInputListener
{
    protected VertexContainer vertices;
/** X coordinate where mouse was held down. */
    protected int draggedFromX;
/** Y coordinate where mouse was held down. */
    protected int draggedFromY;
/** Vertex where mouse was held down. */
    protected Vertex draggedFromVertex;
/** X coordinate where mouse was released. */
    protected int draggedToX;
/** Y coordinate where mouse was released. */
    protected int draggedToY;
/** Vertex where mouse was released. */
    protected Vertex draggedToVertex;
    protected GraphicInterface gui;
    /**
     * Constructor.
     * @param p All vertices in the program.
     * @param gui GraphicInterface object.
     */
    public MouseInput(VertexContainer p, GraphicInterface gui)
    {
        vertices = p;
        this.gui = gui;
    }
    /**
     * Finds the vertex at specified coordinates.
     * @param x X coordinate
     * @param y Y coordinate
     * @return Vertex that is located in given coordinates.
     */
    public Vertex chooseVertex(int x, int y)
    {
        Vertex bestPoint = null;
        double bestDist = Const.pointWidth;
        LinkedList<Vertex> list = vertices.getVertices().toLinkedList();
        while (list.hasNext())
        {
            Vertex p = list.getNext();
            double dist = Math.sqrt(Math.pow(p.X() - x, 2) + 
                    Math.pow(p.Y() - y, 2));
            if (dist <= bestDist)
            {
                bestDist = dist;
                bestPoint = p;
            }
        }
        return bestPoint;
    }
/**
 * Overriden method that handles the tool being switched out of.
 */
    public void close() {}
/**
 * Finds a vertex at mouse's coordinates.
 * @param e MouseEvent object.
 * @return Vertex that is located at mouse's coordinates.
 */
    public Vertex chooseVertex(MouseEvent e)
    {
        return chooseVertex(e.getX(), e.getY());
    }
/**
 * Draws information specific to mouse tool mode.
 * @param g Graphics object.
 */
    public void drawInputSpecific(Graphics g) {}
/**
 * Decides what color should the vertex be represented as.
 * @param vertex Specific vertex
 * @return Color of the vertex.
 */
    public Color chooseColorByPoint(Vertex vertex)
    {
        if (vertex == vertices.endA)
            return Color.MAGENTA;
        if (vertex == vertices.endB)
            return Color.red;
        if (vertex == draggedFromVertex)
            return Color.orange;
        if (vertex == draggedToVertex)
            return Color.green;
        if (vertex.isVertex())
            return Color.BLACK;
        return Color.gray;
    }
    @Override
    public void mousePressed(MouseEvent e)
    {
        draggedToVertex = null;
        draggedFromX = e.getX();
        draggedFromY = e.getY();
        draggedFromVertex = chooseVertex(e);
        gui.repaint();
    }
    @Override
    public void mouseReleased(MouseEvent e)
    {
        draggedToX = e.getX();
        draggedToY = e.getY();
        draggedToVertex = chooseVertex(e);
        gui.repaint();
    }
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e)  {}
    @Override
    public void mouseMoved(MouseEvent e) {}
}