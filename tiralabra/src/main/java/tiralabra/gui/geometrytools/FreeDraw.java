
package tiralabra.gui.geometrytools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import tiralabra.VertexContainer;
import tiralabra.algorithms.Dijkstra;
import tiralabra.datastructures.Point;
import tiralabra.datastructures.Stack;
import tiralabra.datastructures.TreeMap;
import tiralabra.datastructures.Vertex;
import tiralabra.gui.GraphicInterface;
import tiralabra.gui.MouseInput;
import tiralabra.util.Const;
import tiralabra.util.Tools;
import tiralabra.util.VertexComparator;

/**
 *
 * @author Pavel
 */
public class FreeDraw extends MouseInput {
    private final double circleStep = 2 * Math.PI / Const.circlePrecision;
    private boolean startedDrawing;
    private Spline last;
    private Vertex endA;
    private Vertex endB;
    private boolean toggleEnd;
    private TreeMap<Vertex, Vertex> previousPoint;

    public FreeDraw(VertexContainer p, GraphicInterface gui) {
        super(p, gui);
        startedDrawing = false;
        toggleEnd = false;
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
        else if (e.getButton() == MouseEvent.BUTTON2)
        {
            points.getVertices().clear();
        }
        else if (e.getButton() == MouseEvent.BUTTON3)
        {
            toggleEnd = !toggleEnd;
            if (toggleEnd)
            {
                points.removeVertex(endA);
                endA = points.addVertex(e.getX(), e.getY());
            }
            else
            {
                points.removeVertex(endB);
                endB = points.addVertex(e.getX(), e.getY());
            }
            points.buildGraph();
            buildPath();
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
        Point p = null;
        Point begin = null;
        Stack<Point> stack = new Stack<>();
        while (s.prev != null)
        {
            next = s;
            s = s.prev;
            if (s.prev != null)
            {
                Point q = points.addPoint(s.x + Const.brushWidth * Math.cos(s.angle + Math.PI / 2),
                        s.y + Const.brushWidth * Math.sin(s.angle + Math.PI / 2));
                stack.add(points.addPoint(s.x + Const.brushWidth * Math.cos(s.angle - Math.PI / 2),
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
            p = constructCircle(s, Math.PI + getAngle(next), p);
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
        p = constructCircle(last, getAngle(last), p);
        begin.setLeft(p);
        p.setRight(begin);
        last = null;
        points.buildGraph();
    }
    private Point constructCircle(Spline s, double ignoredAngle, Point p)
    {
        if (ignoredAngle < -Math.PI)   ignoredAngle += Math.PI * 2;
        else if (ignoredAngle > Math.PI)   ignoredAngle -= Math.PI * 2;
        double left = ignoredAngle - Math.PI / 2 - circleStep / 2;
        if (left < -Math.PI)    left += Math.PI * 2;
        double right = ignoredAngle + Math.PI / 2 + circleStep / 2;
        if (right > Math.PI)    right -= Math.PI * 2;
        boolean looped = (left < right);
        for (double i = left; !looped || i <= right; i += circleStep)
        {
            if (i > Math.PI)
            {
                looped = true;
                i -= Math.PI * 2;
            }
            Point q = points.addPoint(s.x + Const.brushWidth * Math.cos(i), s.y + Const.brushWidth * Math.sin(i));
            if (p != null)
            {
                p.setRight(q);
                q.setLeft(p);
            }
            p = q;
        }
        return p;
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
        drawShortestPath(g);
    }
    /**
     * Finds the shortest path via mouse drag.
     */
    public void buildPath()
    {
        previousPoint = Dijkstra.getShortestPaths(endA, points.getVertices().toLinkedList());
    }
/**
 * Draws the shortest path which was selected by buildPath().
 * @param g Graphics object.
 */
    public void drawShortestPath(Graphics g)
    {
        if (previousPoint == null)  return;
        
        Vertex next = endB;
        while (next != null)
        {
            Vertex q = previousPoint.get(next);
            if (q != null)
                gui.drawEdge(g, Color.blue, q, next);
            next = q;
        }
    }
    @Override
    public Color chooseColorByPoint(Vertex vertex)
    {
        if (vertex == endA) return Color.MAGENTA;
        if (vertex == endB) return Color.red;
        return super.chooseColorByPoint(vertex);
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
