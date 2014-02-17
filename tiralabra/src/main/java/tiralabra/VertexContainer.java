
package tiralabra;

import tiralabra.algorithms.AngleElimination;
import tiralabra.datastructures.LinkedList;
import tiralabra.datastructures.Point;
import tiralabra.datastructures.Tree;
import tiralabra.datastructures.Vertex;
import tiralabra.util.Tools;
import tiralabra.util.VertexComparator;

/**
 * Contains all vertices and points created in the program.
 * @author Pavel
 */
public class VertexContainer {
/** All vertices created in the program. */
    private Tree<Vertex> vertices = new Tree<>(new VertexComparator());
    public Tree<Vertex> getVertices()  {return vertices;}
        
/** A point of the generated path. */
    public Vertex endA;
/** B point of the generated path. */
    public Vertex endB;
    
/**
 * Creates a vertex at a specified position.
 * @param x X coordinate where vertex is created.
 * @param y Y coordinate where vertex is created.
 * @return pointer to the newly created vertex.
 */
    public Vertex addVertex(double x, double y)
    {
        Vertex vertex = new Vertex(x, y);
        vertices.add(vertex);
        return vertex;
    }
/**
 * Creates a point at a specified position.
 * @param x X coordinate where point is created.
 * @param y Y coordinate where point is created.
 * @return pointer to the newly created point.
 */
    public Point addPoint(double x, double y)
    {
        Point point = new Point(x, y);
        vertices.add(point);
        return point;
    }
/**
 * Removes a vertex or point.
 * @param vertex vertex or point that will get removed.
 */
    public void removeVertex(Vertex vertex)
    {
        if (vertex == null)  return;
        vertex.removeAllAdjacents();
        vertices.remove(vertex);
    }
/**
 * Joins specified two vertices in a graph if there is no connection
 * between them. Removes the connection if it exists prior.
 * @param v1 certain vertex or point
 * @param v2 certain vertex or point
 */
    public void toggleEdge(Vertex v1, Vertex v2)
    {
        if (v1.getAdjacents().contains(v2))
            v1.removeAdjacent(v2);
        else
            v1.addAdjacent(v2);
    }
/**
 * Rebuilds a graph for all the existing vertices. All vertices
 * are joined with other vertices if the joining edge isn't obstructed.
 */
    public void buildGraph()
    {
        LinkedList<Vertex> vlist = vertices.toLinkedList();
        while (vlist.hasNext())
            vlist.getNext().removeAllAdjacents();
        vlist.reset();
        while (vlist.hasNext())
        {
            Vertex v = vlist.getNext();
            if (!v.isVertex())  continue;
            LinkedList<Vertex> graphList =
                AngleElimination.findUnobstructedPoints(v, vertices.toLinkedList());
            while (graphList.hasNext())
                v.addAdjacent(graphList.getNext());
        }
    }
/**
 * Configures polygon to act as a wall or non-wall.
 * @param begin Any point in the polygon, from which all other
 *              points in the polygon will be accessed.
 * @param wall if true, polygon is configured as a wall. Otherwise non-wall.
 */
    public void setShapeMode(Point begin, boolean wall)
    {
        if (begin == null)  return;
        if (shapeIsWall(begin) == wall) return;
        invertShape(begin);
    }
/**
 * Checks if a polygon acts like a wall.
 * @param begin Any point in the polygon, from which all other
 *              points in the polygon will be accessed.
 * @return true if a polygon acts like a wall.
 */
    public boolean shapeIsWall(Point begin)
    {
        if (begin == null)  return true;
        Point point = begin;
        double anglesum = 0;
        int pointsum = 0;
        do
        {
            pointsum++;
            anglesum += point.getAngle();
            point = point.getRight();
        }   while (point != begin);
//The check is performed by utilising one law of geometry:
//  Sum of angles in a polygon is equal to: (n-2) * 180 degrees
//  where n is amount of sides (ie amount of points)
//A wall polygon will not abide the law, because all its corners
//are inverted compared to non-wall polygon.
        return Tools.round(anglesum, 10000) != Tools.round((pointsum - 2) * Math.PI, 10000);
    }
/**
 * Inverts all angles in a polygon. Sets a new angle for all
 * corners in a polygon from a to 360 - a, where a is the value
 * of an angle in degrees.
 * @param begin Any point in the polygon, from which all other
 *              points in the polygon will be accessed.
 */
    public void invertShape(Point begin)
    {
        if (begin == null)  return;
        Point point = begin;
        do
        {
            Point q = point.getLeft();
            point.setLeft(point.getRight());
            point.setRight(q);
            point = point.getRight();
        }   while (point != begin);
    }
}
