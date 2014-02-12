
package tiralabra.util;

/**
 * Miscelanneous small scale algorithms used throughout the program.
 * @author Pavel
 */
public class Tools {
/**
 * Rounds the value to a certain precision.
 * @param a Value that needs to be round off.
 * @param tol Precision to which value is rounded off. Value is inverted,
 *          if tol = 100 then 2.0946 is rounded to 2.09.
 * @return Rounded off value of a.
 */
    public static double round(double a, double tol)
    {
        double i = Math.floor(a);
        double d = a - i;
        
        d *= tol;
        d = Math.round(d);
        d /= tol;
        
        return i + d;
    }
}
