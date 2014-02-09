package tiralabra;

import java.util.Comparator;
import java.util.Scanner;
import tiralabra.gui.GraphicInterface;
import javax.swing.SwingUtilities;
import tiralabra.datastructures.Heap;
import tiralabra.datastructures.Point;
import tiralabra.datastructures.Tree;
import tiralabra.datastructures.TreeMap;

/**
 * Starting point of the program.
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        VertexContainer vc = new VertexContainer();
        GraphicInterface g = new GraphicInterface(vc);
        SwingUtilities.invokeLater(g);
    }
}
