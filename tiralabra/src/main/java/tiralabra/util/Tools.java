
package tiralabra.util;

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
}
