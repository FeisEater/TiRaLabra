
package tiralabra;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import tiralabra.datastructures.Point;
import tiralabra.datastructures.Tree;
import tiralabra.datastructures.Vertex;
import tiralabra.util.VertexComparator;

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
        Vertex test = vc.getVertices().toLinkedList().getNext();
        assertTrue(v.X() == 3 && v.Y() == 5 && 
                test.X() == 3 &&
                test.Y() == 5);
    }
    @Test
    public void pointIsAdded()
    {
        Point p = vc.addPoint(3, 5);
        Vertex test = vc.getVertices().toLinkedList().getNext();
        assertTrue(p.X() == 3 && p.Y() == 5 &&
                test.X() == 3 &&
                test.Y() == 5);
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
        Vertex v1 = vc.addVertex(3, 5);
        Vertex v2 = vc.addVertex(5, 3);
        vc.toggleEdge(v1, v2);
        boolean b1 = v1.getAdjacents().contains(v2) && v2.getAdjacents().contains(v1);
        vc.toggleEdge(v2, v1);
        boolean b2 = v1.getAdjacents().isEmpty() && v2.getAdjacents().isEmpty();
        assertTrue(b1 && b2);
    }
    @Test
    public void edgeIsToggled2()
    {
        Vertex v1 = vc.addVertex(3, 5);
        Vertex v2 = vc.addVertex(5, 3);
        for (int i = 0; i < 1000; i++)
        {
            vc.toggleEdge(v1, v2);
            boolean b = (i%2==0);
            if (v1.getAdjacents().isEmpty() == b)   assertTrue(false);
        }
        assertTrue(true);
    }
    @Test
    public void buildsGraph()
    {
        Point p1 = vc.addPoint(0,0);
        Point p2 = vc.addPoint(1,-1);
        Point p3 = vc.addPoint(1,1);
        Point p4 = vc.addPoint(1.5, 0.0);
        Point p5 = vc.addPoint(4,-1);
        Point p6 = vc.addPoint(4,1);
        p1.setRight(p2);
        p2.setLeft(p1);
        p3.setRight(p1);
        p1.setLeft(p3);
        p2.setRight(p3);
        p3.setLeft(p2);
        
        p4.setRight(p5);
        p5.setLeft(p4);
        p6.setRight(p4);
        p4.setLeft(p6);
        p5.setRight(p6);
        p6.setLeft(p5);
        
        vc.buildGraph();

        Tree<Vertex> testadj1 = new Tree<>(new VertexComparator());
        Tree<Vertex> testadj2 = new Tree<>(new VertexComparator());
        Tree<Vertex> testadj3 = new Tree<>(new VertexComparator());
        Tree<Vertex> testadj4 = new Tree<>(new VertexComparator());
        Tree<Vertex> testadj5 = new Tree<>(new VertexComparator());
        Tree<Vertex> testadj6 = new Tree<>(new VertexComparator());
        testadj1.add(p2);
        testadj1.add(p3);
        //testadj1.add(p5);   //remove this
        //testadj1.add(p6);   //remove this
        testadj2.add(p1);
        testadj2.add(p5);
        testadj2.add(p4);
        testadj2.add(p3);
        testadj3.add(p1);
        testadj3.add(p6);
        testadj3.add(p4);
        testadj3.add(p2);
        testadj4.add(p2);
        testadj4.add(p3);
        testadj4.add(p5);
        testadj4.add(p6);
        testadj5.add(p2);
        testadj5.add(p4);
        testadj5.add(p6);
        //testadj5.add(p1);   //remove this
        testadj6.add(p5);
        testadj6.add(p4);
        testadj6.add(p3);
        //testadj6.add(p1);   //remove this
        
        assertTrue(p1.getAdjacents().equals(testadj1) &&
                p2.getAdjacents().equals(testadj2) &&
                p3.getAdjacents().equals(testadj3) &&
                p4.getAdjacents().equals(testadj4) &&
                p5.getAdjacents().equals(testadj5) &&
                p6.getAdjacents().equals(testadj6));
    }
    @Test
    public void recognisesWall()
    {
        Point p1 = vc.addPoint(0,0);
        Point p2 = vc.addPoint(1,-1);
        Point p3 = vc.addPoint(1,1);
        p1.setRight(p2);
        p2.setLeft(p1);
        p3.setRight(p1);
        p1.setLeft(p3);
        p2.setRight(p3);
        p3.setLeft(p2);
        assertTrue(vc.shapeIsWall(p1) && vc.shapeIsWall(p2) && vc.shapeIsWall(p3));
    }
    @Test
    public void recognisesNonWall()
    {
        Point p1 = vc.addPoint(0,0);
        Point p2 = vc.addPoint(1,-1);
        Point p3 = vc.addPoint(1,1);
        p1.setLeft(p2);
        p2.setRight(p1);
        p3.setLeft(p1);
        p1.setRight(p3);
        p2.setLeft(p3);
        p3.setRight(p2);
        assertTrue(!vc.shapeIsWall(p1) && !vc.shapeIsWall(p2) && !vc.shapeIsWall(p3));
    }
    @Test
    public void invertsShape()
    {
        Point p1 = vc.addPoint(0,0);
        Point p2 = vc.addPoint(1,-1);
        Point p3 = vc.addPoint(1,1);
        p1.setLeft(p2);
        p2.setRight(p1);
        p3.setLeft(p1);
        p1.setRight(p3);
        p2.setLeft(p3);
        p3.setRight(p2);
        vc.invertShape(p1);
        assertTrue(vc.shapeIsWall(p1) && vc.shapeIsWall(p2) && vc.shapeIsWall(p3));
    }
}
