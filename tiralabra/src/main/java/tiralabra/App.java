package tiralabra;

import java.util.Comparator;
import tiralabra.gui.GraphicInterface;
import javax.swing.SwingUtilities;
import tiralabra.datastructures.Heap;
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
        
        Heap<Integer> t = new Heap<>(8, new Comparator() {
            public int compare(Object o1, Object o2)
            {
                if (o1.getClass() != Integer.class || o2.getClass() != Integer.class)
                    return 0;
                int i1 = (Integer)o1;
                int i2 = (Integer)o2;
                return i1-i2;
            }
        });
        /*for (int i = 0; i < 10; i++)
        {
            t.insert((int)(Math.random() * 1000));
        }*/
        //System.out.println(t.toLinkedList());
        Integer i1 = new Integer(10);
        Integer i2 = new Integer(2);
        t.insert(i1);
        t.insert(i2);
        t.changeValue(i1, new Integer(1));
        System.out.println(t);
        /*while (!t.isEmpty())
            System.out.print(t.pop() + " ");
        System.out.println("");*/
    }
}
