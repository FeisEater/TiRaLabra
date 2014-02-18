
package tiralabra.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pavel
 */
public class ToolsTest {
    
    public ToolsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void roundsCorrectly1()
    {
        assertTrue(Tools.round(1.12345678, 100) == 1.12);
    }
    @Test
    public void roundsCorrectly2()
    {
        assertTrue(Tools.round(1.129999345, 100) == 1.13);
    }
    @Test
    public void roundsCorrectly3()
    {
        assertTrue(Tools.round(1.12, 100) == 1.12);
    }
    @Test
    public void roundsCorrectly4()
    {
        assertTrue(Tools.round(1.1, 100) == 1.1);
    }
    @Test
    public void roundsCorrectly5()
    {
        assertTrue(Tools.round(1, 100) == 1);
    }
    @Test
    public void roundsCorrectly6()
    {
        assertTrue(Tools.round(2.9999999945, 100) == 3);
    }
    @Test
    public void roundsCorrectly7()
    {
        assertTrue(Tools.round(1.12345678, 10000) == 1.1235);
    }
    @Test
    public void roundsCorrectly8()
    {
        assertTrue(Tools.round(1.129999345, 10000) == 1.1300);
    }
    @Test
    public void roundsCorrectly9()
    {
        assertTrue(Tools.round(1.1256, 10000) == 1.1256);
    }
    @Test
    public void roundsCorrectly10()
    {
        assertTrue(Tools.round(1.1, 10000) == 1.1);
    }
    @Test
    public void roundsCorrectly11()
    {
        assertTrue(Tools.round(1, 10000) == 1);
    }
    @Test
    public void roundsCorrectly12()
    {
        assertTrue(Tools.round(2.9999999945, 10000) == 3);
    }
    @Test
    public void directionIsBetweenAngles()
    {
        assertTrue(Tools.hasAngleBetween(-Math.PI / 4, Math.PI / 4, 0));
    }
    @Test
    public void directionIsBetweenAnglesAtLoopJump()
    {
        assertTrue(Tools.hasAngleBetween(3 * Math.PI / 4, -3 * Math.PI / 4, Math.PI));
    }
    @Test
    public void directionIsNotBetweenAngles()
    {
        assertTrue(!Tools.hasAngleBetween(-Math.PI / 4, Math.PI / 4, -Math.PI));
    }
    @Test
    public void directionIsNotBetweenAnglesAtLoopJump()
    {
        assertTrue(!Tools.hasAngleBetween(3 * Math.PI / 4, -3 * Math.PI / 4, 0));
    }

}
