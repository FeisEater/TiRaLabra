package tiralabra;

import tiralabra.gui.GraphicInterface;
import javax.swing.SwingUtilities;
import tiralabra.algorithms.AngleElimination;
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
    }
}
