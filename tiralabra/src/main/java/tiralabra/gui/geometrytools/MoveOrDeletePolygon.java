
package tiralabra.gui.geometrytools;

import java.awt.event.MouseEvent;
import tiralabra.VertexContainer;
import tiralabra.datastructures.Point;
import tiralabra.datastructures.Vertex;
import tiralabra.gui.GraphicInterface;
import tiralabra.gui.MouseInput;

/**
 * Tool for moving or deleting entire polygons.
 * @author Pavel
 */
public class MoveOrDeletePolygon extends MouseInput {

    public MoveOrDeletePolygon(VertexContainer p, GraphicInterface gui)
    {
        super(p, gui);
        System.out.println("Reposition/Delete polygon mode. Drag left button to reposition polygon.\n"
                + "Middle button to copy polygon.\n"
                + "Right button to delete polygon.");
    }
    @Override
    public void mouseReleased(MouseEvent e)
    {
        super.mouseReleased(e);
        if (e.getButton() == MouseEvent.BUTTON1)
            movePolygon(draggedFromVertex);
        else if (e.getButton() == MouseEvent.BUTTON2)
            copyPolygon(draggedFromVertex);
        else if (e.getButton() == MouseEvent.BUTTON3)
            deletePolygon(draggedToVertex);
        vertices.buildGraph();
        gui.repaint();
    }
/**
 * Moves a polygon.
 * @param v Point of a polygon to be moved.
 */
    public void movePolygon(Vertex v)
    {
        copyPolygon(v);
        deletePolygon(v);
    }
/**
 * Copies a polygon at a mouse cursor location.
 * @param v Point of a polygon to be copied.
 */
    public void copyPolygon(Vertex v)
    {
        if (v == null)  return;
        if (v.getClass() != Point.class)    return;
        Point p = (Point)v;
        double xDiff = draggedToX - draggedFromX;
        double yDiff = draggedToY - draggedFromY;
        Point q = p;
        Point prev = null;
        Point begin = null;
        do {
            Point next = vertices.addPoint(q.X() + xDiff, q. Y() + yDiff);
            if (prev != null)
            {
                prev.setRight(next);
                next.setLeft(prev);
            }
            else    begin = next;
            prev = next;
            q = q.getRight();
        } while (q != p);
        prev.setRight(begin);
        begin.setLeft(prev);
    }
/**
 * Deletes a polygon.
 * @param v Point of a polygon to be deleted.
 */
    public void deletePolygon(Vertex v)
    {
        if (v == null)  return;
        if (v.getClass() != Point.class)    return;
        Point p = (Point)v;
        Point q = p;
        do {
            vertices.removeVertex(q);
            q = q.getRight();
        } while (q != p);
    }
}
