package tiralabra;

import tiralabra.gui.GraphicInterface;
import javax.swing.SwingUtilities;
import tiralabra.datastructures.Point;

/**
 * Starting point of the program.
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        VertexContainer vc = new VertexContainer();
        SwingUtilities.invokeLater(new GraphicInterface(vc));
        /*
        Point p1 = vc.addPoint(100,200);
        Point p2 = vc.addPoint(200,100);
        Point p3 = vc.addPoint(200,300);
        Point p4 = vc.addPoint(250,200);
        Point p5 = vc.addPoint(500,100);
        Point p6 = vc.addPoint(500,300);
        p1.setRight(p2);
        p2.setLeft(p1);
        p3.setRight(p1);
        p1.setLeft(p3);
        p2.setRight(p3);
        p3.setLeft(p2);
        
        p4.setRight(p5);
        p5.setLeft(p4);
        p6.setRight(p4);
        p4.setLeft(p6);
        p5.setRight(p6);
        p6.setLeft(p5);
        
        vc.buildGraph();
*/
    }
}
