
package tiralabra;

import java.util.ArrayList;
import java.util.List;
import tiralabra.algorithms.AngleElimination;
import tiralabra.datastructures.Point;
import tiralabra.datastructures.Vertex;
import tiralabra.util.Tools;

/**
 *
 * @author Pavel
 */
public class VertexContainer {
    private List<Vertex> vertices = new ArrayList<>();
    public List<Vertex> getVertices()  {return vertices;}
    public Vertex addVertex(int x, int y)
    {
        Vertex vertex = new Vertex((double)x, (double)y);
        vertices.add(vertex);
        return vertex;
    }
    public Point addPoint(int x, int y)
    {
        Point point = new Point((double)x, (double)y);
        vertices.add(point);
        return point;
    }
    public void removeVertex(Vertex vertex)
    {
        if (vertex == null)  return;
        vertex.removeAllAdjacents();
        vertices.remove(vertex);
    }
    public void toggleEdge(Vertex v1, Vertex v2)
    {
        if (v1.getAdjacents().contains(v2))
            v1.removeAdjacent(v2);
        else
            v1.addAdjacent(v2);
    }
    public void buildGraph()
    {
        for (Vertex v : vertices)
            v.removeAllAdjacents();
        for (Vertex v : vertices)
        {
            if (!v.isVertex())  continue;
            AngleElimination.findUnobstructedPoints(v, vertices);
        }
    }
    public void setShapeMode(Point begin, boolean wall)
    {
        if (begin == null)  return;
        if (shapeIsWall(begin) == wall) return;
        invertShape(begin);
    }
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
        
        return Tools.round(anglesum) != Tools.round((pointsum - 2) * Math.PI);
    }
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
