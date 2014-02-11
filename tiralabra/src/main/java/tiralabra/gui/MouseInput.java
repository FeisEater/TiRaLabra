
package tiralabra.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
    protected VertexContainer points;
    protected int draggedFromX;
    protected int draggedFromY;
    protected Vertex draggedFromPoint;
    protected int draggedToX;
    protected int draggedToY;
    protected Vertex draggedToPoint;
    protected GraphicInterface gui;
    /**
     * Constructor.
     * @param p
     * @param gui 
     */
    public MouseInput(VertexContainer p, GraphicInterface gui)
    {
        points = p;
        this.gui = gui;
    }
    /**
     * Finds the vertex at specified coordinates.
     * @param x X coordinate
     * @param y Y coordinate
     * @return Vertex that is located in given coordinates.
     */
    public Vertex choosePoint(int x, int y)
    {
        Vertex bestPoint = null;
        double bestDist = Const.pointWidth;
        LinkedList<Vertex> vertices = points.getVertices().toLinkedList();
        while (vertices.hasNext())
        {
            Vertex p = vertices.getNext();
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
 * Finds a vertex at mouse's coordinates.
 * @param e MouseEvent object.
 * @return Vertex that is located in mouse's coordinates.
 */
    public Vertex choosePoint(MouseEvent e)
    {
        return choosePoint(e.getX(), e.getY());
    }
/**
 * Draws information specific to mouse tool mode.
 * @param g Graphics object.
 */
    public void drawInputSpecific(Graphics g) {}
/**
 * Decides what color should the vertex be represented as.
 * @param point Specific vertex
 * @return Color of the vertex.
 */
    public Color chooseColorByPoint(Vertex point)
    {
        if (point == draggedFromPoint)
            return Color.orange;
        if (point == draggedToPoint)
            return Color.green;
        if (point.isVertex())
            return Color.BLUE;
        return Color.black;
    }
    @Override
    public void mousePressed(MouseEvent e)
    {
        draggedToPoint = null;
        draggedFromX = e.getX();
        draggedFromY = e.getY();
        draggedFromPoint = choosePoint(e);
        gui.repaint();
    }
    @Override
    public void mouseReleased(MouseEvent e)
    {
        draggedToX = e.getX();
        draggedToY = e.getY();
        draggedToPoint = choosePoint(e);
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