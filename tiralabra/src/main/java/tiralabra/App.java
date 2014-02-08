package tiralabra;

import java.util.Comparator;
import java.util.Scanner;
import tiralabra.gui.GraphicInterface;
import javax.swing.SwingUtilities;
import tiralabra.datastructures.Heap;
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
        TreeMap<Integer, String> t = new TreeMap<>(new Comparator() {
            public int compare(Object o1, Object o2)
            {
                if (o1.getClass() != Integer.class || o2.getClass() != Integer.class)
                    return 0;
                int i1 = (Integer)o1;
                int i2 = (Integer)o2;
                return i1-i2;
            }
        });
        t.put(1, "one");
        t.put(2, "two");
        t.put(3, "three");
        System.out.println(t.get(0));
    }
}
