
package tiralabra.gui.geometrytools;

import java.awt.Color;
import java.awt.event.MouseEvent;
import tiralabra.VertexContainer;
import tiralabra.datastructures.Vertex;
import tiralabra.gui.GraphicInterface;
import tiralabra.gui.MouseInput;

/**
 * Mouse input tool that lets user position vertices and build the graph
 * freely by connecting vertices by hand.
 * @author Pavel
 */
public class BuildGraph extends MouseInput {
/** Vertex that waits to be connected with other vertex in a graph. */
    private Vertex edgeend;
    public BuildGraph(VertexContainer p, GraphicInterface gui)
        {super(p, gui);}
    @Override
    public void mouseReleased(MouseEvent e)
    {
        super.mouseReleased(e);
        if (e.getButton() == MouseEvent.BUTTON1)
            addVertex(draggedToX, draggedToY);
        else if (e.getButton() == MouseEvent.BUTTON2)
            removeVertex(draggedToVertex);
        else if (e.getButton() == MouseEvent.BUTTON3)
            joinPoints(draggedToVertex);
        gui.repaint();
    }
/**
 * Adds a vertex.
 * @param x X coordinate where vertex is created.
 * @param y Y coordinate where vertex is created.
 */
    public void addVertex(int x, int y)
    {
        Vertex p = vertices.addVertex(x,y);
        edgeend = null;
    }
/**
 * Removes given vertex.
 * @param point given vertex.
 */
    public void removeVertex(Vertex point)
    {
        vertices.removeVertex(point);
    }
/**
 * Selects vertex to connect with other vertex. If one was already selected,
 * adds a connection or removes it if they were already connected.
 * @param point Chosen vertex.
 */
    public void joinPoints(Vertex point)
    {
        if (point == null)  return;
        
        if (edgeend == null)
            edgeend = point;
        else
        {
            vertices.toggleEdge(point, edgeend);
            edgeend = null;
        }
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
        if (point == edgeend)
            return Color.magenta;
        return super.chooseColorByPoint(point);
    }

}
