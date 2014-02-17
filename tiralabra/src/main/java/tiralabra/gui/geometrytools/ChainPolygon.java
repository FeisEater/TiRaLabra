
package tiralabra.gui.geometrytools;

import java.awt.event.MouseEvent;
import tiralabra.VertexContainer;
import tiralabra.datastructures.Point;
import tiralabra.datastructures.Vertex;
import tiralabra.gui.GraphicInterface;
import tiralabra.gui.MouseInput;

/**
 * Mouse input tool that lets user chain points together to form
 * polygons.
 * @author Pavel
 */
public class ChainPolygon extends MouseInput {
/** First point placed for creating a polygon. */
    private Point begin;
/** Last point that was placed for creating a polygon. */
    private Point prev;
/** if true, polygons will be walls. */
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
        Vertex chosenPoint = chooseVertex(e);
        if (e.getButton() == MouseEvent.BUTTON1)
            addPoint(draggedToX, draggedToY);
        else if (e.getButton() == MouseEvent.BUTTON2)
            toggleWallMode(chosenPoint);
        else if (e.getButton() == MouseEvent.BUTTON3)
            closeLoop();
        vertices.buildGraph();
        gui.repaint();
    }
/**
 * Adds a point and chains it to previously created point.
 * @param x X coordinate where point is created.
 * @param y Y coordinate where point is created.
 */
    public void addPoint(int x, int y)
    {
        Point p = vertices.addPoint(x,y);
        if (begin == null)  begin = p;
        else
        {
            p.setLeft(prev);
            prev.setRight(p);
        }
        prev = p;
        draggedToVertex = null;
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
        vertices.setShapeMode(begin, wallmode);
        begin = null;
        prev = null;
    }
    @Override
    public void close()
    {
        closeLoop();
    }
}
