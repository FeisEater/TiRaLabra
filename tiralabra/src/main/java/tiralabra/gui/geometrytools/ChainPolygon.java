
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
public class ChainPolygon extends MouseInput {
    private Point begin;
    private Point prev;
    private Map<Point, Point> previousPoint;
    public ChainPolygon(PointContainer p, GraphicInterface gui)   {super(p, gui);}
    @Override
    public void mouseReleased(MouseEvent e)
    {
        super.mouseReleased(e);
        Point chosenPoint = choosePoint(e);
        if (e.getButton() == MouseEvent.BUTTON1)
            addPoint(e.getX(), e.getY());
        else if (e.getButton() == MouseEvent.BUTTON2)
            removePoint(chosenPoint);
        else if (e.getButton() == MouseEvent.BUTTON3)
        {
            if (draggedFromPoint == chosenPoint)
                closeLoop();
            else
                buildPath();
        }
        gui.repaint();
    }
    public void buildPath()
    {
        previousPoint = Dijkstra.getShortestPaths(draggedFromPoint, points.getPoints());
    }
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
    public void removePoint(Point point)
    {
        points.removePoint(point);
        draggedToPoint = null;
    }
    public void closeLoop()
    {
        if (begin != null && prev != null)
        {
            begin.setLeft(prev);
            prev.setRight(begin);
        }
        points.setShapeMode(begin);
        begin = null;
        prev = null;
        points.buildGraph();
    }
    @Override
    public void drawInputSpecific(Graphics g)
    {
        drawShortestPath(g);
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
