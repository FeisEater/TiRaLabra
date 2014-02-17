package tiralabra.gui.geometrytools;

import java.awt.Color;
import java.awt.event.MouseEvent;
import tiralabra.VertexContainer;
import tiralabra.datastructures.Point;
import tiralabra.datastructures.Vertex;
import tiralabra.gui.GraphicInterface;
import tiralabra.gui.MouseInput;

/**
 * Tool for joining two separate polygons.
 * @author Pavel
 */
public class JoinPolygons extends MouseInput {
/** Left point of polygon #1. */
    private Point firstLeft;
/** Right point of polygon #1. */
    private Point firstRight;
/** Left point of polygon #2. */
    private Point lastLeft;
/** Right point of polygon #2. */
    private Point lastRight;
    public JoinPolygons(VertexContainer p, GraphicInterface gui)
    {
        super(p, gui);
        System.out.println("Join polygons mode. Left and right buttons to choose two points of different polygons to connect.");
    }
    @Override
    public void mouseReleased(MouseEvent e)
    {
        super.mouseReleased(e);
        choosePoints(draggedToVertex, e);
        vertices.buildGraph();
        gui.repaint();
    }
/**
 * Chooses points for merging.
 * @param v Chosen vertex.
 * @param e MouseEvent. On left button choose points from polygon #1.
 *      On right button choose points from polygon #2.
 */
    public void choosePoints(Vertex v, MouseEvent e)
    {
        if (v == null)  return;
        if (v.getClass() != Point.class)    return;
        Point p = (Point)v;
        if (e.getButton() == MouseEvent.BUTTON1)
        {
            if (firstLeft == null)
                firstLeft = p;
            else    firstRight = p;
        }
        else if (e.getButton() == MouseEvent.BUTTON3)
        {
            if (lastLeft == null)
                lastLeft = p;
            else    lastRight = p;
        }
        if (firstLeft != null && firstRight != null && lastLeft != null && lastRight != null)
            joinPolygons();
    }
/**
 * Merges polygons at chosen points.
 */
    public void joinPolygons()
    {
        Point p = firstLeft.getRight();
        while (p != firstRight)
        {
            vertices.removeVertex(p);
            p = p.getRight();
        }
        p = lastLeft.getRight();
        while (p != lastRight)
        {
            vertices.removeVertex(p);
            p = p.getRight();
        }
        firstLeft.setRight(lastRight);
        lastRight.setLeft(firstLeft);
        firstRight.setLeft(lastLeft);
        lastLeft.setRight(firstRight);
        firstLeft = null;
        firstRight = null;
        lastLeft = null;
        lastRight = null;
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
        if (point == firstLeft || point == firstRight)
            return Color.green;
        if (point == lastLeft || point == lastRight)
            return Color.yellow;
        return super.chooseColorByPoint(point);
    }

}
