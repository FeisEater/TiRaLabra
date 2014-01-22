
package tiralabra.gui.geometrytools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.Map;
import tiralabra.PointContainer;
import tiralabra.algorithms.Dijkstra;
import tiralabra.datastructures.Point;
import tiralabra.gui.GraphicInterface;
import tiralabra.gui.MouseInput;

/**
 *
 * @author Pavel
 */
public class BuildGraph extends MouseInput {
    private Point edgeend;
    private Map<Point, Point> previousPoint;
    public BuildGraph(PointContainer p, GraphicInterface gui)   {super(p, gui);}
    @Override
    public void mouseReleased(MouseEvent e)
    {
        Point chosenPoint = choosePoint(e);
        if (e.getButton() == MouseEvent.BUTTON1)
            addPoint(e.getX(), e.getY());
        else if (e.getButton() == MouseEvent.BUTTON2)
            removePoint(chosenPoint);
        else if (e.getButton() == MouseEvent.BUTTON3)
        {
            if (draggedFromPoint == chosenPoint)
                joinPoints(chosenPoint);
            else
                buildPath();
        }
        super.mouseReleased(e);
    }
    public void buildPath()
    {
        previousPoint = Dijkstra.getShortestPaths(draggedFromPoint, points.getPoints());
    }
    public void addPoint(int x, int y)
    {
        Point p = points.addPoint(x,y);
        edgeend = null;
    }
    public void removePoint(Point point)
    {
        points.removePoint(point);
    }
    public void joinPoints(Point point)
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
    @Override
    public void drawInputSpecific(Graphics g)
    {
        drawShortestPath(g);
    }
    @Override
    public Color chooseColorByPoint(Point point)
    {
        if (point == edgeend)
            return Color.magenta;
        return super.chooseColorByPoint(point);
    }
    public void drawShortestPath(Graphics g)
    {
        if (previousPoint == null)  return;

        Point next = draggedToPoint;
        while (next != null)
        {
            Point q = previousPoint.get(next);
            if (q != null)
                gui.drawEdge(g, Color.green, q, next);
            next = q;
        }
    }

}
