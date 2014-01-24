
package tiralabra;

import java.util.ArrayList;
import java.util.List;
import tiralabra.algorithms.AngleElimination;
import tiralabra.datastructures.Point;
import tiralabra.datastructures.Vertex;
import tiralabra.util.Tools;

/**
 * Contains all vertices and points created in the program.
 * @author Pavel
 */
public class VertexContainer {
    private List<Vertex> vertices = new ArrayList<>();
    public List<Vertex> getVertices()  {return vertices;}
/**
 * Creates a vertex at a specified position.
 * @param x X coordinate where vertex is created.
 * @param y Y coordinate where vertex is created.
 * @return pointer to the newly created vertex.
 */
    public Vertex addVertex(int x, int y)
    {
        Vertex vertex = new Vertex((double)x, (double)y);
        vertices.add(vertex);
        return vertex;
    }
/**
 * Creates a point at a specified position.
 * @param x X coordinate where point is created.
 * @param y Y coordinate where point is created.
 * @return pointer to the newly created point.
 */
    public Point addPoint(int x, int y)
    {
        Point point = new Point((double)x, (double)y);
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
        for (Vertex v : vertices)
            v.removeAllAdjacents();
        for (Vertex v : vertices)
        {
            if (!v.isVertex())  continue;
            for (Vertex w : AngleElimination.findUnobstructedPoints(v, vertices))
                v.addAdjacent(w);
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
        return Tools.round(anglesum) != Tools.round((pointsum - 2) * Math.PI);
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