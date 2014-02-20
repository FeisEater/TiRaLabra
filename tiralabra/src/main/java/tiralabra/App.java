package tiralabra;

import tiralabra.gui.GraphicInterface;
import javax.swing.SwingUtilities;
import tiralabra.gui.Histagram;

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
        //Runnable g = new Histagram();
        SwingUtilities.invokeLater(g);
    }

}
