package tiralabra;

import tiralabra.gui.GraphicInterface;
import javax.swing.SwingUtilities;

/**
 * Starting point of the program.
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        VertexContainer container = new VertexContainer();
        SwingUtilities.invokeLater(new GraphicInterface(container));
    }
}
