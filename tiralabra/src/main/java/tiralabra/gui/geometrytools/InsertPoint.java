
package tiralabra.gui.geometrytools;

import java.awt.Color;
import java.awt.event.MouseEvent;
import tiralabra.VertexContainer;
import tiralabra.datastructures.Point;
import tiralabra.datastructures.Vertex;
import tiralabra.gui.GraphicInterface;
import tiralabra.gui.MouseInput;

/**
 *
 * @author Pavel
 */
public class InsertPoint extends MouseInput {
    private Point leftPoint;
    private Point rightPoint;
    public InsertPoint(VertexContainer p, GraphicInterface gui)
    {
        super(p, gui);
        System.out.println("Inserts or deletes points inbetween polygon.\n"
                + "Left button to choose two points to insert a point in between them.\n"
                + "Right button to choose two points to remove all points between them.");
    }
    @Override
    public void mouseReleased(MouseEvent e)
    {
        super.mouseReleased(e);
        if (leftPoint != null && rightPoint != null)
        {
            removePointsInBetween();
            Point p = points.addPoint(e.getX(), e.getY());
            leftPoint.setRight(p);
            p.setLeft(leftPoint);
            p.setRight(rightPoint);
            rightPoint.setLeft(p);
            leftPoint = null;
            rightPoint = null;
        }
        else
            choosePoints(draggedFromPoint, e);
    }
    public void choosePoints(Vertex v, MouseEvent e)
    {
        if (v == null)  return;
        if (v.getClass() != Point.class)    return;
        Point p = (Point)v;
        if (leftPoint == null)  leftPoint = p;
        else
        {
            rightPoint = p;
            if (e.getButton() == MouseEvent.BUTTON3)
            {
                removePointsInBetween();
                leftPoint = null;
                rightPoint = null;
            }
        }
    }
    public void removePointsInBetween()
    {
        Point q = leftPoint.getRight();
        while (q != rightPoint)
        {
            points.removeVertex(q);
            q = q.getRight();
        }
        leftPoint.setRight(rightPoint);
        rightPoint.setLeft(leftPoint);
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
        if (point == leftPoint)
            return Color.green;
        if (point == rightPoint)
            return Color.yellow;
        return super.chooseColorByPoint(point);
    }

}
