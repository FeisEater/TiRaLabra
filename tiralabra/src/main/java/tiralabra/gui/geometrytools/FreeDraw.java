
package tiralabra.gui.geometrytools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import tiralabra.VertexContainer;
import tiralabra.datastructures.Point;
import tiralabra.datastructures.Stack;
import tiralabra.gui.GraphicInterface;
import tiralabra.gui.MouseInput;
import tiralabra.util.Const;
import tiralabra.util.Tools;

/**
 *
 * @author Pavel
 */
public class FreeDraw extends MouseInput {
    private final double circleStep = 2 * Math.PI / Const.circlePrecision;
    private boolean startedDrawing;
    private Spline last;
    
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
        {
            startedDrawing = false;
            constructPolygon();
        }
        else
            points.getVertices().clear();
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
        if (last != null && Math.sqrt(Math.pow(x - last.x, 2) + Math.pow(y - last.y, 2)) < Const.brushWidth)
            return;
        Spline s = new Spline(x, y, last);
        if (last != null && last.prev != null)
        {
            last.angle = (getAngle(s) + getAngle(last)) / 2;
            System.out.println((last.angle * 180 / Math.PI));
        }
        last = s;
        if (isStraightAngle())
            last.prev = last.prev.prev;
    }
    public boolean isStraightAngle()
    {
        if (last.prev == null || last.prev.prev == null)
            return false;
        if (last.x == last.prev.x && last.y == last.prev.y)
            return true;
        return getAngle(last) == getAngle(last.prev);
    }
    private double getAngle(Spline s)
    {
        return Tools.round(Math.atan2(s.y - s.prev.y, s.x - s.prev.x), 10000);
    }
    public void constructPolygon()
    {
        if (last == null)   return;
        Spline s = last;
        Spline next = null;
        constructCircle(s, getAngle(s));
        while (s.prev.prev != null)
        {
            next = s;
            s = s.prev;
            points.addPoint(s.x + Const.brushWidth * Math.cos(s.angle + Math.PI / 2),
                    s.y + Const.brushWidth * Math.sin(s.angle + Math.PI / 2));
            points.addPoint(s.x + Const.brushWidth * Math.cos(s.angle - Math.PI / 2),
                    s.y + Const.brushWidth * Math.sin(s.angle - Math.PI / 2));
        }
        if (next != null)
            constructCircle(s, Math.PI + getAngle(next));
/*        Spline next = null;
        Stack<Point> otherSide = new Stack<>();
        while (s != null)
        {
            Point begin = null;
            Point p = null;
            for (double i = -Math.PI; i < Math.PI; i += circleStep)
            {
                if (s.prev != null)
                {
                    p = null;
                    double angle = getAngle(s);
                    double left = angle - Math.PI / 2 - circleStep / 2;
                    if (left < -Math.PI)    left += Math.PI * 2;
                    double right = angle + Math.PI / 2 + circleStep / 2;
                    if (right > Math.PI)    right -= Math.PI * 2;
                    if (!Tools.hasAngleBetween(left, right, i))
                        continue;
                }
                if (next != null)
                {
                    p = null;
                    double angle = Math.PI + getAngle(next);
                    if (angle < -Math.PI)   angle += Math.PI * 2;
                    else if (angle > Math.PI)   angle -= Math.PI * 2;
                    double left = angle - Math.PI / 2 - circleStep / 2;
                    if (left < -Math.PI)    left += Math.PI * 2;
                    double right = angle + Math.PI / 2 + circleStep / 2;
                    if (right > Math.PI)    right -= Math.PI * 2;
                    if (!Tools.hasAngleBetween(left, right, i))
                        continue;
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
            next = s;
            s = s.prev;
        }*/
        last = null;
        //points.buildGraph();
    }
    private void constructCircle(Spline s, double ignoredAngle)
    {
        for (double i = -Math.PI; i < Math.PI; i += circleStep)
        {
            if (ignoredAngle < -Math.PI)   ignoredAngle += Math.PI * 2;
            else if (ignoredAngle > Math.PI)   ignoredAngle -= Math.PI * 2;
            double left = ignoredAngle - Math.PI / 2 - circleStep / 2;
            if (left < -Math.PI)    left += Math.PI * 2;
            double right = ignoredAngle + Math.PI / 2 + circleStep / 2;
            if (right > Math.PI)    right -= Math.PI * 2;
            if (!Tools.hasAngleBetween(left, right, i))
                continue;
            points.addPoint(s.x + Const.brushWidth * Math.cos(i), s.y + Const.brushWidth * Math.sin(i));
        }
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
        private double angle;
        public Spline(int ix, int iy, Spline iprev)
        {
            x = ix;
            y = iy;
            prev = iprev;
            angle = -1024;
        }
        public String toString()
        {
            return "" + x + ", " + y;
        }
    }
}
