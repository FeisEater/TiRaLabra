package tiralabra.algorithms;

import org.junit.Test;
import static org.junit.Assert.*;
import tiralabra.datastructures.Tree;
import tiralabra.datastructures.TreeMap;
import tiralabra.datastructures.Vertex;
import tiralabra.util.VertexComparator;

/**
 *
 * @author Pavel
 */
public class DijkstraTest {
    
    @Test
    public void findsShortestPath()
    {
        Tree<Vertex> vertices = new Tree<>(new VertexComparator());
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

        TreeMap<Vertex, Vertex> paths = Dijkstra.getShortestPaths(v2, v3, vertices.toLinkedList());
        assertTrue(paths.get(v3) == v1 && paths.get(v1) == v2);
    }
    @Test
    public void findsShortestPath2()
    {
        Tree<Vertex> vertices = new Tree<>(new VertexComparator());
        Vertex v1 = new Vertex(0,0);
        Vertex v2 = new Vertex(1,0);
        Vertex v3 = new Vertex(1,1);
        Vertex v4 = new Vertex(2,1);
        Vertex v5 = new Vertex(2,2);
        vertices.add(v1);
        vertices.add(v2);
        vertices.add(v3);
        vertices.add(v4);
        vertices.add(v5);
        v1.addAdjacent(v2);
        v2.addAdjacent(v3);
        v3.addAdjacent(v4);
        v4.addAdjacent(v5);
        v1.addAdjacent(v5);

        TreeMap<Vertex, Vertex> paths = Dijkstra.getShortestPaths(v1, v5, vertices.toLinkedList());
        assertTrue(paths.get(v5) == v1);
    }
    @Test
    public void findsShortestPath3()
    {
        Tree<Vertex> vertices = new Tree<>(new VertexComparator());
        Vertex v1 = new Vertex(0,0);
        Vertex v2 = new Vertex(1,0);
        Vertex v3 = new Vertex(1,1);
        Vertex v4 = new Vertex(2,1);
        Vertex v5 = new Vertex(2,2);
        Vertex v6 = new Vertex(0,3);
        vertices.add(v1);
        vertices.add(v2);
        vertices.add(v3);
        vertices.add(v4);
        vertices.add(v5);
        vertices.add(v6);
        v1.addAdjacent(v2);
        v2.addAdjacent(v3);
        v3.addAdjacent(v4);
        v4.addAdjacent(v5);
        v1.addAdjacent(v6);
        v6.addAdjacent(v5);

        TreeMap<Vertex, Vertex> paths = Dijkstra.getShortestPaths(v1, v5, vertices.toLinkedList());
        assertTrue(paths.get(v5) == v4 && paths.get(v4) == v3 && paths.get(v3) == v2 && paths.get(v2) == v1);
    }
}
