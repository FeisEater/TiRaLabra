
package tiralabra.datastructures;

import org.junit.Test;
import static org.junit.Assert.*;
import tiralabra.util.Tools;

/**
 *
 * @author Pavel
 */
public class PointTest {
    
    @Test
    public void angleIsCalculatedCorrectly()
    {
        Point center = new Point(0,0);
        for (double d = -Math.PI; d <= 3 * Math.PI; d += 0.1)
        {
            center.setLeft(new Point(32 * Math.cos(d), 32 * Math.sin(d)));
            center.setRight(new Point(32 * Math.cos(d + Math.PI / 2), 32 * Math.sin(d + Math.PI / 2)));
            double angle = Tools.round(center.getAngle(), 100);
            if (angle != Tools.round(Math.PI / 2, 100))  assertTrue(false);
        }
        assertTrue(true);
    }
    @Test
    public void pointIsVertexIfItsAngleIsReflex()
    {
        Point center = new Point(0,0);
        Point left = new Point(1, -1);
        Point right = new Point(1, 1);
        center.setLeft(left);
        center.setRight(right);
        boolean b1 = !center.isVertex();
        center.setLeft(right);
        center.setRight(left);
        boolean b2 = center.isVertex();
        assertTrue(b1 && b2);
    }
}
