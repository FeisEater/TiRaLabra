
package tiralabra;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import tiralabra.datastructures.Point;
import tiralabra.datastructures.Vertex;

/**
 *
 * @author Pavel
 */
public class VertexContainerTest {
    private VertexContainer vc;
    public VertexContainerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        vc = new VertexContainer();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void vertexIsAdded()
    {
        Vertex v = vc.addVertex(3, 5);
        assertTrue(v.X() == 3 && v.Y() == 5 && 
                vc.getVertices().get(0).X() == 3 &&
                vc.getVertices().get(0).Y() == 5);
    }
    @Test
    public void pointIsAdded()
    {
        Point p = vc.addPoint(3, 5);
        assertTrue(p.X() == 3 && p.Y() == 5 &&
                vc.getVertices().get(0).X() == 3 &&
                vc.getVertices().get(0).Y() == 5);
    }
    @Test
    public void vertexIsRemoved()
    {
        Vertex v = vc.addVertex(3, 5);
        vc.removeVertex(v);
        assertTrue(vc.getVertices().isEmpty());
    }
    @Test
    public void pointIsRemoved()
    {
        Point p = vc.addPoint(3, 5);
        vc.removeVertex(p);
        assertTrue(vc.getVertices().isEmpty());
    }
    @Test
    public void edgeIsToggled()
    {
        Vertex v1 = vc.addPoint(3, 5);
        Vertex v2 = vc.addPoint(5, 3);
        vc.toggleEdge(v1, v2);
        boolean b1 = v1.getAdjacents().contains(v2) && v2.getAdjacents().contains(v1);
        vc.toggleEdge(v2, v1);
        boolean b2 = v1.getAdjacents().isEmpty() && v2.getAdjacents().isEmpty();
        assertTrue(b1 && b2);
    }
    @Test
    public void edgeIsToggled2()
    {
        Vertex v1 = vc.addPoint(3, 5);
        Vertex v2 = vc.addPoint(5, 3);
        for (int i = 0; i < 1000; i++)
        {
            vc.toggleEdge(v1, v2);
            boolean b = (i%2==0);
            if (v1.getAdjacents().isEmpty() == b)   assertTrue(false);
        }
        assertTrue(true);
    }
}
