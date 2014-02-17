
package tiralabra.gui.geometrytools;

import java.awt.event.MouseEvent;
import tiralabra.VertexContainer;
import tiralabra.datastructures.Point;
import tiralabra.datastructures.Vertex;
import tiralabra.gui.GraphicInterface;
import tiralabra.gui.MouseInput;

/**
 * Tool for moving or deleting a point of a polygon.
 * @author Pavel
 */
public class MoveOrDeletePoint extends MouseInput {
    public MoveOrDeletePoint(VertexContainer p, GraphicInterface gui)
    {
        super(p, gui);
        System.out.println("Reposition/Delete point mode. Drag left button to move point.\n"
                + "Middle button to invert polygon (wait, what is this doing here?)\n"
                + "Right button to delete point from the polygon.");
    }
    @Override
    public void mouseReleased(MouseEvent e)
    {
        super.mouseReleased(e);
        if (e.getButton() == MouseEvent.BUTTON1)
            movePoint(draggedFromVertex);
        else if (e.getButton() == MouseEvent.BUTTON2 && draggedToVertex.getClass() == Point.class)
            vertices.invertShape((Point)draggedToVertex);
        else if (e.getButton() == MouseEvent.BUTTON3)
            deletePoint(draggedToVertex);
        vertices.buildGraph();
        gui.repaint();
    }
/**
 * Moves a point within the polygon.
 * @param v dragged Point.
 */
    public void movePoint(Vertex v)
    {
        if (v == null)  return;
        if (v.getClass() != Point.class)    return;
        Point p = (Point)v;
        double xDiff = draggedToX - draggedFromX;
        double yDiff = draggedToY - draggedFromY;
        Point q = vertices.addPoint(p.X() + xDiff, p.Y() + yDiff);
        q.setRight(p.getRight());
        p.getRight().setLeft(q);
        q.setLeft(p.getLeft());
        p.getLeft().setRight(q);
        vertices.removeVertex(p);
    }
/**
 * Deletes a polygon while maintaining the polygon.
 * @param v deleted Point.
 */
    public void deletePoint(Vertex v)
    {
        if (v == null)  return;
        if (v.getClass() != Point.class)    return;
        Point p = (Point)v;
        p.getLeft().setRight(p.getRight());
        p.getRight().setLeft(p.getLeft());
        vertices.removeVertex(p);
    }

}
