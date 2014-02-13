
package tiralabra.gui.geometrytools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import tiralabra.VertexContainer;
import tiralabra.datastructures.Point;
import tiralabra.gui.GraphicInterface;
import tiralabra.gui.MouseInput;
import tiralabra.util.Const;
import tiralabra.util.Tools;

/**
 *
 * @author Pavel
 */
public class FreeDraw extends MouseInput {
    private boolean startedDrawing;
    private Spline last;
    
    public FreeDraw(VertexContainer p, GraphicInterface gui) {
        super(p, gui);
        startedDrawing = false;
        addSpline(100, 200);
        addSpline(200, 201);
        constructPolygon();
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
        {
            startedDrawing = false;
            constructPolygon();
        }
        gui.repaint();
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
        last = new Spline(x, y, last);
        if (isStraightAngle())
            last.prev = last.prev.prev;
    }
    public boolean isStraightAngle()
    {
        if (last.prev == null || last.prev.prev == null)
            return false;
        if (last.x == last.prev.x && last.y == last.prev.y)
            return true;
        System.out.println((getAngle(last) * 180 / Math.PI) + ", " + (getAngle(last.prev) * 180 / Math.PI));
        return getAngle(last) == getAngle(last.prev);
    }
    private double getAngle(Spline s)
    {
        return Tools.round(Math.atan2(s.y - s.prev.y, s.x - s.prev.x), 10000);
    }
    public void constructPolygon()
    {
        Spline s = last;
        while (s != null)
        {
            Point begin = null;
            Point p = null;
            for (double i = -Math.PI; i < Math.PI; i += 2 * Math.PI / Const.circlePrecision)
            {
                if (s.prev != null)
                {
                    double angle = getAngle(s);
                }
                Point q = points.addPoint(s.x + Const.brushWidth * Math.cos(i), s.y + Const.brushWidth * Math.sin(i));
                if (p != null)
                {
                    p.setRight(q);
                    q.setLeft(p);
                }
                else    begin = q;
                p = q;
            }
            //begin.setLeft(p);
            //p.setRight(begin);
            //points.setShapeMode(p, true);
            s = s.prev;
        }
        last = null;
        //points.buildGraph();
    }
    @Override
    public void drawInputSpecific(Graphics g)
    {
        g.setColor(Color.pink);
        Spline s = last;
        while (s != null)
        {
            g.drawOval((int)(s.x - Const.brushWidth),
                    (int)(s.y - Const.brushWidth),
                    (int)Const.brushWidth * 2, (int)Const.brushWidth * 2);
            if (s.prev != null)
                g.drawLine(s.x, s.y, s.prev.x, s.prev.y);
            s = s.prev;
        }
    }
    private class Spline
    {
        private int x;
        private int y;
        private Spline prev;
        public Spline(int ix, int iy, Spline iprev)
        {
            x = ix;
            y = iy;
            prev = iprev;
        }
        public String toString()
        {
            return "" + x + ", " + y;
        }
    }
}
