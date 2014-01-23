
package tiralabra.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import tiralabra.VertexContainer;
import tiralabra.datastructures.Vertex;
import tiralabra.util.Const;

/**
 *
 * @author Pavel
 */
public class MouseInput implements MouseListener
{
    protected VertexContainer points;
    protected int draggedFromX;
    protected int draggedFromY;
    protected Vertex draggedFromPoint;
    protected int draggedToX;
    protected int draggedToY;
    protected Vertex draggedToPoint;
    protected GraphicInterface gui;
    public MouseInput(VertexContainer p, GraphicInterface gui)
    {
        points = p;
        this.gui = gui;
    }
    public Vertex choosePoint(int x, int y)
    {
        Vertex bestPoint = null;
        double bestDist = Const.pointWidth;
        for (Vertex p : points.getVertices())
        {
            double dist = Math.sqrt(Math.pow(p.X() - x, 2) + 
                    Math.pow(p.Y() - y, 2));
            if (dist <= bestDist)
            {
                bestDist = dist;
                bestPoint = p;
            }
        }
        return bestPoint;
    }
    public Vertex choosePoint(MouseEvent e)
    {
        return choosePoint(e.getX(), e.getY());
    }
    public void drawInputSpecific(Graphics g) {}
    public Color chooseColorByPoint(Vertex point)
    {
        if (point == draggedFromPoint)
            return Color.orange;
        if (point == draggedToPoint)
            return Color.green;
        if (point.isVertex())
            return Color.BLUE;
        return Color.black;
    }
    @Override
    public void mousePressed(MouseEvent e)
    {
        draggedToPoint = null;
        draggedFromX = e.getX();
        draggedFromY = e.getY();
        draggedFromPoint = choosePoint(e);
        gui.repaint();
    }
    @Override
    public void mouseReleased(MouseEvent e)
    {
        draggedToX = e.getX();
        draggedToY = e.getY();
        draggedToPoint = choosePoint(e);
        gui.repaint();
    }
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}