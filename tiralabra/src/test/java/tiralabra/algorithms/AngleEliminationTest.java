package tiralabra.algorithms;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import tiralabra.VertexContainer;
import tiralabra.datastructures.LinkedList;
import tiralabra.datastructures.Point;
import tiralabra.datastructures.Tree;
import tiralabra.datastructures.Vertex;
import tiralabra.util.VertexComparator;

/**
 *
 * @author Pavel
 */
public class AngleEliminationTest {
    
    public AngleEliminationTest() {
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

    public void addTriangle(Point p1, Point p2, Point p3)
    {
        p1.setLeft(p3);
        p2.setLeft(p1);
        p3.setLeft(p2);
        p3.setRight(p1);
        p1.setRight(p2);
        p2.setRight(p3);
    }
    @Test
    public void findsCorrectUnobstructedPoints()
    {
        VertexContainer vc = new VertexContainer();
        Vertex src = vc.addVertex(0, 0);
        Point p1 = vc.addPoint(-1, -4);
        Point p2 = vc.addPoint(2, -1);
        Point p3 = vc.addPoint(3, 3);
        Point p4 = vc.addPoint(-4, -3);
        Point p5 = vc.addPoint(-4, 3);
        addTriangle(p1, vc.addPoint(2, -4), p2);
        addTriangle(vc.addPoint(3, -3), vc.addPoint(5, 0), p3);
        addTriangle(vc.addPoint(-7, 5), vc.addPoint(7, 5), vc.addPoint(0, 7));
        addTriangle(p4, p5, vc.addPoint(-7, 0));
        addTriangle(vc.addPoint(-8, 2), vc.addPoint(-9, 3), vc.addPoint(-9, 1));
        addTriangle(vc.addPoint(-8, -3), vc.addPoint(-8, -1), vc.addPoint(-9, -2));
        LinkedList<Vertex> list =
            AngleElimination.findUnobstructedPoints(src, vc.getVertices().toLinkedList(), null);
        Tree<Vertex> tree = new Tree<>(new VertexComparator());
        while (list.hasNext())
            tree.add(list.getNext());
        assertTrue(tree.contains(p1) && tree.contains(p2) &&
                tree.contains(p3) && tree.contains(p4) && tree.contains(p5));
    }
}
