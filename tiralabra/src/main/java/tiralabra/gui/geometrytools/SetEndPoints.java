
package tiralabra.gui.geometrytools;

import java.awt.event.MouseEvent;
import tiralabra.VertexContainer;
import tiralabra.gui.GraphicInterface;
import tiralabra.gui.MouseInput;

/**
 * Sets A and B points for building the shortest path between them.
 * @author Pavel
 */
public class SetEndPoints extends MouseInput {

    public SetEndPoints(VertexContainer p, GraphicInterface gui)
    {
        super(p, gui);
        System.out.println("Set path end points. Left and right buttons to place an end point.");
    }
    @Override
    public void mouseReleased(MouseEvent e)
    {
        super.mouseReleased(e);
        if (e.getButton() == MouseEvent.BUTTON1)
        {
            vertices.removeVertex(vertices.endA);
            vertices.endA = vertices.addVertex(draggedToX, draggedToY);
        }
        else if (e.getButton() == MouseEvent.BUTTON3)
        {
            vertices.removeVertex(vertices.endB);
            vertices.endB = vertices.addVertex(draggedToX, draggedToY);
        }
        vertices.buildGraph();
        gui.repaint();
    }

}
