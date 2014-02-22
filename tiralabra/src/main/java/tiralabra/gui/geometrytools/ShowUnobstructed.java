
package tiralabra.gui.geometrytools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import tiralabra.VertexContainer;
import tiralabra.algorithms.AngleElimination;
import tiralabra.datastructures.Vertex;
import tiralabra.gui.GraphicInterface;
import tiralabra.gui.MouseInput;

/**
 * Trace mode. Vertex can be chosen, after which the area that is accessible from
 * that vertex is colored.
 * @author Pavel
 */
public class ShowUnobstructed extends MouseInput {

    public ShowUnobstructed(VertexContainer p, GraphicInterface gui) {
        super(p, gui);
        System.out.println("Trace mode. Left button to choose a vertex from which trace is visualized.");
    }
    @Override
    public void mouseReleased(MouseEvent e)
    {
        super.mouseReleased(e);
        if (e.getButton() == MouseEvent.BUTTON1)
            AngleElimination.findUnobstructedPoints(draggedToVertex, vertices.getVertices().toLinkedList(), null);
    }
    @Override
    public void drawInputSpecific(Graphics g)
    {
        fillUnobstructedArea(g, 4);
        AngleElimination.visualize(g);
    }
/**
 * Visualises the area that is unobstructed from the point of view
 * of the point that was the last one that called the tracing algorithm.
 * @param g Graphics object.
 * @param pixelsize Size of the pixel.
 */
    public void fillUnobstructedArea(Graphics g, int pixelsize)
    {
        g.setColor(Color.green);
        for (int i = 0; i < gui.getWidth(); i += pixelsize)
            for (int j = 0; j < gui.getHeight(); j += pixelsize)
                if (!AngleElimination.isObstructed(new Vertex(i+pixelsize/2, j+pixelsize/2)))
                    g.fillRect(i, j, pixelsize, pixelsize);
    }

}
