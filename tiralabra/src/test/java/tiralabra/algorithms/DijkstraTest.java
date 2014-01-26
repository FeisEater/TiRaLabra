/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tiralabra.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import tiralabra.datastructures.Vertex;

/**
 *
 * @author Pavel
 */
public class DijkstraTest {
    
    public DijkstraTest() {
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
    public void findsShortestPath()
    {
        List<Vertex> vertices = new ArrayList<>();
        Vertex v1 = new Vertex(0,0);
        Vertex v2 = new Vertex(1,-1);
        Vertex v3 = new Vertex(1,1);
        Vertex v4 = new Vertex(5,0);
        vertices.add(v1);
        vertices.add(v2);
        vertices.add(v3);
        vertices.add(v4);
        v1.addAdjacent(v2);
        v1.addAdjacent(v3);
        v4.addAdjacent(v2);
        v4.addAdjacent(v3);

        Map<Vertex, Vertex> paths = Dijkstra.getShortestPaths(v2, vertices);
        assertTrue(""+paths, paths.get(v3) == v1 && paths.get(v1) == v2);
    }
}
