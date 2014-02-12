
package tiralabra.datastructures;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import tiralabra.util.Tools;

/**
 *
 * @author Pavel
 */
public class VertexTest {
    
    public VertexTest() {
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
    public void connectsTwoVertices()
    {
        Vertex v1 = new Vertex(1,0);
        Vertex v2 = new Vertex(0,1);
        v1.addAdjacent(v2);
        assertTrue(v1.getAdjacents().contains(v2) &&
                v2.getAdjacents().contains(v1));
    }
    @Test
    public void cantConnectVertexWithItself()
    {
        Vertex v1 = new Vertex(0,0);
        v1.addAdjacent(v1);
        assertTrue(v1.getAdjacents().isEmpty());
    }
    @Test
    public void removesConnection()
    {
        Vertex v1 = new Vertex(1,0);
        Vertex v2 = new Vertex(0,1);
        v1.addAdjacent(v2);
        v2.removeAdjacent(v1);
        assertTrue(v1.getAdjacents().isEmpty() &&
                v2.getAdjacents().isEmpty());
    }
    @Test
    public void removesAllConnections()
    {
        Vertex v = new Vertex(0,0);
        for (int i = 1; i < 1000; i++)
            v.addAdjacent(new Vertex(i,i));
        v.removeAllAdjacents();
        assertTrue(v.getAdjacents().isEmpty());
    }
    @Test
    public void calculatesDistance()
    {
        Vertex v1 = new Vertex(0,0);
        Vertex v2 = new Vertex(3,4);
        assertTrue(Tools.round(v1.getDistance(v2), 100) == 5 &&
                Tools.round(v2.getDistance(v1), 100) == 5);
    }
    @Test
    public void calculatesDirection()
    {
        Vertex v1 = new Vertex(0,0);
        Vertex v2 = new Vertex(1,1);
        assertTrue(Tools.round(v1.getDirection(v2), 100) == Tools.round(Math.PI / 4, 100) &&
                Tools.round(v2.getDirection(v1), 100) == Tools.round(-3 * Math.PI / 4, 100));
    }
    @Test
    public void pointIsBetweenAngles()
    {
        Vertex src = new Vertex(0,0);
        Vertex v = new Vertex(1,0);
        Vertex left = new Vertex(1, 1);
        Vertex right = new Vertex(1, -1);
        assertTrue(src.hasPointBetween(src.getDirection(left), src.getDirection(right), v));
    }
    @Test
    public void pointIsBetweenAnglesAtLoopJump()
    {
        Vertex src = new Vertex(0,0);
        Vertex v = new Vertex(-1,0);
        Vertex left = new Vertex(-1, -1);
        Vertex right = new Vertex(-1, 1);
        assertTrue(src.hasPointBetween(src.getDirection(left), src.getDirection(right), v));
    }
    @Test
    public void pointIsNotBetweenAngles()
    {
        Vertex src = new Vertex(0,0);
        Vertex v = new Vertex(-1,0);
        Vertex left = new Vertex(1, 1);
        Vertex right = new Vertex(1, -1);
        assertTrue(!src.hasPointBetween(src.getDirection(left), src.getDirection(right), v));
    }
    @Test
    public void pointIsNotBetweenAnglesAtLoopJump()
    {
        Vertex src = new Vertex(0,0);
        Vertex v = new Vertex(1,0);
        Vertex left = new Vertex(-1, -1);
        Vertex right = new Vertex(-1, 1);
        assertTrue(!src.hasPointBetween(src.getDirection(left), src.getDirection(right), v));
    }
}
