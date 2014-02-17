
package tiralabra.gui.geometrytools;

import java.awt.Color;
import java.awt.event.MouseEvent;
import tiralabra.VertexContainer;
import tiralabra.datastructures.Point;
import tiralabra.datastructures.Vertex;
import tiralabra.gui.GraphicInterface;
import tiralabra.gui.MouseInput;

/**
 * Tool for inserting a point between two points.
 * @author Pavel
 */
public class InsertPoint extends MouseInput {
/** Chosen left Point. */
    private Point leftPoint;
/** Chosen right Point. */
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
            insertPoint(draggedToX, draggedToY);
        else
            choosePoints(draggedToVertex, e);
        vertices.buildGraph();
        gui.repaint();
    }
/**
 * Inserts a point between chosen left and right points.
 * @param x X position where point is inserted.
 * @param y Y position where point is inserted.
 */
    public void insertPoint(int x, int y)
    {
        removePointsInBetween();
        Point p = vertices.addPoint(x, y);
        leftPoint.setRight(p);
        p.setLeft(leftPoint);
        p.setRight(rightPoint);
        rightPoint.setLeft(p);
        leftPoint = null;
        rightPoint = null;
    }
/**
 * Choose a left Point, if already chose, choose a right Point.
 * @param v Vertex to be chosen.
 * @param e MouseEvent. If right button, points between left and right
 *      points are removed and third mouse click is not prompted.
 */
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
/**
 * Removes all points between chosen left and right points.
 */
    public void removePointsInBetween()
    {
        Point q = leftPoint.getRight();
        while (q != rightPoint)
        {
            vertices.removeVertex(q);
            q = q.getRight();
        }
        leftPoint.setRight(rightPoint);
        rightPoint.setLeft(leftPoint);
    }
/**
 * Decides what color should the vertex be represented as.
 * Left Point is green, right Point is yellow.
 * @param vertex Specific vertex
 * @return Color of the vertex.
 */
    @Override
    public Color chooseColorByPoint(Vertex vertex)
    {
        if (vertex == leftPoint)
            return Color.green;
        if (vertex == rightPoint)
            return Color.yellow;
        return super.chooseColorByPoint(vertex);
    }

}
