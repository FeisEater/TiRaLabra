
package tiralabra.util;

import java.util.Comparator;
import tiralabra.datastructures.Point;
import tiralabra.datastructures.Vertex;

/**
 * Comparator class for placing vertices in a data structure.
 * @author Pavel
 */
public class VertexComparator implements Comparator
{
    @Override
    public int compare(Object o1, Object o2)
    {
        if ((o1.getClass() != Vertex.class && o1.getClass() != Point.class) ||
            (o2.getClass() != Vertex.class && o2.getClass() != Point.class))
            return 0;
        Vertex v1 = (Vertex)o1;
        Vertex v2 = (Vertex)o2;
        if (v1.X() == v2.X())   return (v1.Y() < v2.Y()) ? -1: 1;
        return (v1.X() < v2.X()) ? -1: 1;
    }
}
