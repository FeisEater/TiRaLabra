
package tiralabra;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import tiralabra.datastructures.Point;

/**
 *
 * @author Pavel
 */
public class GraphicInterface extends JPanel implements Runnable {
    private JFrame frame;
    private Point edgeend;
    private Point dragfrom;
    private Point dragto;
    private class mouseInput implements MouseListener
    {
        @Override
        public void mouseClicked(MouseEvent e) {}
        @Override
        public void mousePressed(MouseEvent e)
        {
            dragfrom = null;
            dragto = null;
            Point best = null;
            double dist = 8;
            for (Point p : App.points)
            {
                if (Math.sqrt(Math.pow(p.X() - e.getX(), 2) +
                    Math.pow(p.Y() - e.getY(), 2)) <= dist)
                {
                    dist = Math.sqrt(Math.pow(p.X() - e.getX(), 2) +
                        Math.pow(p.Y() - e.getY(), 2));
                    best = p;
                }
            }
            if (best != null)
                dragfrom = best;
            repaint();
        }
        @Override
        public void mouseReleased(MouseEvent e)
        {
            if (e.getButton() == MouseEvent.BUTTON1)
            {
                App.addPoint(e.getX(), e.getY());
                edgeend = null;
                dragfrom = null;
                dragto = null;
            }
            else if (e.getButton() == MouseEvent.BUTTON3)
            {
                Point best = null;
                double dist = 8;
                for (Point p : App.points)
                {
                    if (Math.sqrt(Math.pow(p.X() - e.getX(), 2) +
                        Math.pow(p.Y() - e.getY(), 2)) <= dist)
                    {
                        dist = Math.sqrt(Math.pow(p.X() - e.getX(), 2) +
                            Math.pow(p.Y() - e.getY(), 2));
                        best = p;
                    }
                }
                if (best != null)
                {
                    if (dragfrom == best)
                    {
                        dragfrom = null;
                        dragto = null;
                        if (edgeend == null)
                            edgeend = best;
                        else
                        {
                            App.toggleEdge(best, edgeend);
                            edgeend = null;
                        }
                    }
                    else
                    {
                        dragto = best;
                        App.dijkstra(dragfrom);
                    }
                }
            }
            repaint();
        }
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
    }
    public boolean created = false;
    public GraphicInterface()
    {
        super();
        addMouseListener(new mouseInput());
    }
    @Override
    public void run()
    {
        frame = new JFrame("tiralabra");
        frame.setPreferredSize(new Dimension(640, 480));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
        created = true;
    }
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        for (Point p : App.points)
        {
            g.setColor(Color.blue);
            if (p == edgeend)
                g.setColor(Color.magenta);
            if (p == dragfrom)
                g.setColor(Color.orange);
            if (p == dragto)
                g.setColor(Color.green);
            g.fillOval((int)p.X() - 4, (int)p.Y() - 4, 8, 8);
            g.setColor(Color.red);
            for (Point adj : p.getAdjacents())
                g.drawLine((int)p.X(), (int)p.Y(), (int)adj.X(), (int)adj.Y());
        }
        g.setColor(Color.green);
        Point next = dragto;
        while (next != null)
        {
            Point prev = App.previousPoint.get(next);
            if (prev != null)
            {
                g.drawLine((int)next.X(), (int)next.Y(), 
                    (int)prev.X(), (int)prev.Y());
            }
            System.out.println(prev + " -> " + next);
            next = prev;
        }
    }
}
