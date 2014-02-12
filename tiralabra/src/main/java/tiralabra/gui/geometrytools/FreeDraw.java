
package tiralabra.gui.geometrytools;

import java.awt.event.MouseEvent;
import tiralabra.VertexContainer;
import tiralabra.gui.GraphicInterface;
import tiralabra.gui.MouseInput;
import tiralabra.util.Const;

/**
 *
 * @author Pavel
 */
public class FreeDraw extends MouseInput {
    private boolean startedDrawing;

    public FreeDraw(VertexContainer p, GraphicInterface gui) {
        super(p, gui);
        startedDrawing = false;
    }
    @Override
    public void mousePressed(MouseEvent e)
    {
        super.mousePressed(e);
        if (e.getButton() == MouseEvent.BUTTON1)
            startedDrawing = true;
    }
    @Override
    public void mouseReleased(MouseEvent e)
    {
        super.mouseReleased(e);
        if (e.getButton() == MouseEvent.BUTTON1)
            startedDrawing = false;
    }
    @Override
    public void mouseDragged(MouseEvent e)
    {
        if (startedDrawing)
        {
            addSpline(e.getX(), e.getY());
            gui.repaint();
        }
    }
    public void addSpline(int x, int y)
    {
        for (double i = 0; i < 2 * Math.PI; i += 2 * Math.PI / Const.circlePrecision)
            points.addVertex(x + Const.brushWidth * Math.cos(i), y + Const.brushWidth * Math.sin(i));
    }

}
