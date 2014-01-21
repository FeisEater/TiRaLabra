
package tiralabra.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import tiralabra.App;
import tiralabra.datastructures.Point;
import tiralabra.util.Const;

/**
 *
 * @author Pavel
 */
public class MouseInput implements MouseListener
{
    private GraphicInterface gui;
    public MouseInput(GraphicInterface gui)
    {
        this.gui = gui;
    }
    public Point choosePoint(int x, int y)
    {
        Point bestPoint = null;
        double bestDist = Const.pointWidth;
        for (Point p : App.points)
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
    @Override
    public void mousePressed(MouseEvent e)
    {
        Point chosenPoint = choosePoint(e.getX(), e.getY());
        gui.startDrag(chosenPoint);
    }
    @Override
    public void mouseReleased(MouseEvent e)
    {
        Point chosenPoint = choosePoint(e.getX(), e.getY());
        
        if (e.getButton() == MouseEvent.BUTTON1)
            gui.addPoint(e.getX(), e.getY());
        
        else if (e.getButton() == MouseEvent.BUTTON2)
            gui.removePoint(chosenPoint);
        
        else if (e.getButton() == MouseEvent.BUTTON3)
        {
            if (gui.wasntDragged(chosenPoint))
                gui.addEdge(chosenPoint);
            else
                gui.stopDrag(chosenPoint);
        }
        gui.repaint();
    }
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}