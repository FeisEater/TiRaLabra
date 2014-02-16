
package tiralabra.gui.geometrytools;

import java.awt.event.MouseEvent;
import tiralabra.VertexContainer;
import tiralabra.datastructures.Vertex;
import tiralabra.gui.GraphicInterface;
import tiralabra.gui.MouseInput;

/**
 *
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
            points.removeVertex(points.endA);
            points.endA = points.addVertex(draggedToX, draggedToY);
        }
        else if (e.getButton() == MouseEvent.BUTTON3)
        {
            points.removeVertex(points.endB);
            points.endB = points.addVertex(draggedToX, draggedToY);
        }
        points.buildGraph();
        gui.repaint();
    }

}
