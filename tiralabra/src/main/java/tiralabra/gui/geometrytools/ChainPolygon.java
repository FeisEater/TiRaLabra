
package tiralabra.gui.geometrytools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import tiralabra.VertexContainer;
import tiralabra.algorithms.AngleElimination;
import tiralabra.algorithms.Dijkstra;
import tiralabra.datastructures.Point;
import tiralabra.datastructures.TreeMap;
import tiralabra.datastructures.Vertex;
import tiralabra.gui.GraphicInterface;
import tiralabra.gui.MouseInput;

/**
 * Mouse input tool that lets user chain points together to form
 * polygons.
 * @author Pavel
 */
public class ChainPolygon extends MouseInput {
    private Point begin;
    private Point prev;
    private boolean wallmode;
    public ChainPolygon(VertexContainer p, GraphicInterface gui)
    {
        super(p, gui);
        wallmode = true;
        System.out.println("Chain Polygon mode: Left button to place and string points together.\n"
                + "Middle button to toggle wall mode.\n"
                + "Right button to close the polygon of placed points.");
    }
    @Override
    public void mouseReleased(MouseEvent e)
    {
        super.mouseReleased(e);
        Vertex chosenPoint = choosePoint(e);
        if (e.getButton() == MouseEvent.BUTTON1)
            addPoint(draggedToX, draggedToY);
        else if (e.getButton() == MouseEvent.BUTTON2)
            toggleWallMode(chosenPoint);
        else if (e.getButton() == MouseEvent.BUTTON3)
        {
            if (begin == null)
                traceAround(e.getX(), e.getY(), chosenPoint);
            else
                closeLoop();
        }
        gui.repaint();
    }
    public void traceAround(int x, int y, Vertex point)
    {
        AngleElimination.findUnobstructedPoints(point, points.getVertices().toLinkedList());
    }
/**
 * Adds a point and chains it to previously created point.
 * @param x X coordinate where point is created.
 * @param y Y coordinate where point is created.
 */
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
/**
 * Removes given vertex.
 * @param point given vertex.
 */
    public void toggleWallMode(Vertex point)
    {
        wallmode = !wallmode;
    }
/**
 * Closes the polygon loop by chaining firstly created point
 * with previously created point.
 */
    public void closeLoop()
    {
        if (begin != null && prev != null)
        {
            begin.setLeft(prev);
            prev.setRight(begin);
        }
        points.setShapeMode(begin, wallmode);
        begin = null;
        prev = null;
        points.buildGraph();
    }
    @Override
    public void close()
    {
        closeLoop();
    }
}
