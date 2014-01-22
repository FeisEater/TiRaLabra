package tiralabra;

import tiralabra.gui.GraphicInterface;
import javax.swing.SwingUtilities;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        PointContainer container = new PointContainer();
        SwingUtilities.invokeLater(new GraphicInterface(container));
    }
}
