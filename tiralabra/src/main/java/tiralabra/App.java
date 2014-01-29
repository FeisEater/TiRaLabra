package tiralabra;

import java.util.Comparator;
import tiralabra.gui.GraphicInterface;
import javax.swing.SwingUtilities;
import tiralabra.datastructures.Tree;

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
        
        Tree<Integer> t = new Tree<>(new Comparator() {
            public int compare(Object o1, Object o2)
            {
                if (o1.getClass() != Integer.class || o2.getClass() != Integer.class)
                    return 0;
                int i1 = (Integer)o1;
                int i2 = (Integer)o2;
                return i1-i2;
            }
        });
        for (int i = 0; i < 10; i++)
        {
            t.add((int)(Math.random() * 1000));
        }
        System.out.println(t);
    }
}
