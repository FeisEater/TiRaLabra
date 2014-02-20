
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
 * Tool that lets user form polygons as if user used a thick brush
 * to draw shapes. Some restrictions apply in this implementation.
 * @author Pavel
 */
public class FreeDraw extends MouseInput {
    private final double circleStep = 2 * Math.PI / Const.circlePrecision;
/** true if mouse is held. */
    private boolean startedDrawing;
/** last position where mouse cursor was located. */
    private Spline last;

    public FreeDraw(VertexContainer p, GraphicInterface gui) {
        super(p, gui);
        startedDrawing = false;
        System.out.println("Free draw mode: Hold the left button to draw a polygon.");
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
        vertices.buildGraph();
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
/**
 * Marks location as a part of a polygon.
 * @param x X coordinate.
 * @param y Y coordinate.
 */
    public void addSpline(int x, int y)
    {
        if (last != null && Math.sqrt(Math.pow(x - last.x, 2) + Math.pow(y - last.y, 2)) < Const.brushWidth)
            return;
        Spline s = new Spline(x, y, last);
        if (last != null && last.prev != null)
        {
            if (Math.abs(getAngle(s) - getAngle(last)) > 1.5 * Math.PI)
                last.angle = (getAngle(s) - getAngle(last)) / 2;
            else    last.angle = (getAngle(s) + getAngle(last)) / 2;
        }
        last = s;
        if (isStraightAngle())
            last.prev = last.prev.prev;
    }
/**
 * Checks if previous spline was a straight angle. If so, the spline is
 * redundant and shouldn't be accounted for.
 * @return true if previous spline forms a straight angle.
 */
    public boolean isStraightAngle()
    {
        if (last.prev == null || last.prev.prev == null)
            return false;
        if (last.x == last.prev.x && last.y == last.prev.y)
            return true;
        return Math.abs(getAngle(last) - getAngle(last.prev)) < Const.fdAngleTolerance;
    }
/**
 * Calculates an angle the spline forms with its previous spline.
 * @param s Given spline.
 * @return Absolute direction between s and s.prev
 */
    private double getAngle(Spline s)
    {
        return Tools.round(Math.atan2(s.y - s.prev.y, s.x - s.prev.x), 10000);
    }
/**
 * Constructs a polygon from marked splines and resets tool for the
 * next brush stroke.
 */
    public void constructPolygon()
    {
        if (last == null)   return;
        Spline s = last;
        Spline next = null;
        Point p = null;
        Point begin = null;
        Stack<Point> stack = new Stack<>();
        while (s.prev != null)
        {
            next = s;
            s = s.prev;
            if (s.prev != null)
            {
                Point q = vertices.addPoint(s.x + Const.brushWidth * Math.cos(s.angle + Math.PI / 2),
                        s.y + Const.brushWidth * Math.sin(s.angle + Math.PI / 2));
                stack.add(vertices.addPoint(s.x + Const.brushWidth * Math.cos(s.angle - Math.PI / 2),
                        s.y + Const.brushWidth * Math.sin(s.angle - Math.PI / 2)));
                if (p != null)
                {
                    p.setRight(q);
                    q.setLeft(p);
                }
                else    begin = q;
                p = q;
            }
        }
        if (next != null)
        {
            Point[] ar = constructCircle(s, Math.PI + getAngle(next), p);
            p = ar[0];
            if (begin == null)  begin = ar[1];
        }
        while (!stack.isEmpty())
        {
            Point q = stack.pop();
            if (p != null)
            {
                p.setRight(q);
                q.setLeft(p);
            }
            p = q;
        }
        p = constructCircle(last, getAngle(last), p)[0];
        begin.setLeft(p);
        p.setRight(begin);
        last = null;
        vertices.buildGraph();
    }
/**
 * Forms a circle at a given spline.
 * @param s Given spline.
 * @param ignoredAngle Angle for which points shouldn't be formed.
 * @param p Previous point that was chained for a polygon.
 * @return [0] - Next point that should be chained for the polygon.
 *          [1] - First point constructed for the circle. [1] will
 *          be null if p isn't null.
 */
    private Point[] constructCircle(Spline s, double ignoredAngle, Point p)
    {
        if (ignoredAngle < -Math.PI)   ignoredAngle += Math.PI * 2;
        else if (ignoredAngle > Math.PI)   ignoredAngle -= Math.PI * 2;
        double left = ignoredAngle - Math.PI / 2 - circleStep / 2;
        if (left < -Math.PI)    left += Math.PI * 2;
        double right = ignoredAngle + Math.PI / 2 + circleStep / 2;
        if (right > Math.PI)    right -= Math.PI * 2;
        boolean looped = (left < right);
        Point begin = null;
        for (double i = left; !looped || i <= right; i += circleStep)
        {
            if (i > Math.PI)
            {
                looped = true;
                i -= Math.PI * 2;
            }
            Point q = vertices.addPoint(s.x + Const.brushWidth * Math.cos(i), s.y + Const.brushWidth * Math.sin(i));
            if (p != null)
            {
                p.setRight(q);
                q.setLeft(p);
            }
            else    begin = q;
            p = q;
        }
        Point[] result = {p, begin};
        return result;
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
/**
 * Marked position that will be represented as a brush stroke.
 */
    private class Spline
    {
        private int x;
        private int y;
/** Previous spline that was marked. */
        private Spline prev;
/** Angle between previous and next spline. */
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
