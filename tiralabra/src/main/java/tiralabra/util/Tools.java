
package tiralabra.util;

import tiralabra.datastructures.Vertex;

/**
 *
 * @author Pavel
 */
public class Tools {
/**
 * Rounds the value to a certain precision.
 * @param a Value that needs to be round off.
 * @return Rounded off value of a.
 */
    public static double round(double a)
    {
        double i = Math.floor(a);
        double d = a - i;
        
        d *= Const.roundPrecision;
        d = Math.round(d);
        d /= Const.roundPrecision;
        
        return i + d;
    }
    public static double distance(Vertex p1, Vertex p2)
    {
        return Math.sqrt(Math.pow(p1.X() - p2.X(), 2) +
                Math.pow(p1.Y() - p2.Y(), 2));
    }
}
