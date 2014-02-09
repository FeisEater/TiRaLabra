
package tiralabra.datastructures;

import java.util.Comparator;

/**
 *
 * @author Pavel
 */
public class TreeMap<K, V> extends Tree {
    public TreeMap(Comparator comp) {super(comp);}
    protected class MapNode extends Tree.Node
    {
        public MapNode(K k, V v)
        {
            super(k);
            value = v;
        }
        public V value;
    }
    public V get(K key)
    {
        MapNode n = (MapNode)find(key);
        if (n == null)  return null;
        return n.value;
    }
    public void put(K key, V value)
    {
        if (contains(key))
        {
            MapNode mn = (MapNode)find(key);
            mn.value = value;
        }
        else
            add(new MapNode(key, value));
    }
    @Override
    public String toString()
    {
        String result = "[";
        Queue<MapNode> q = new Queue<>();
        q.enqueue((MapNode)root);
        while (!q.isEmpty())
        {
            MapNode n = q.dequeue();
            if (n.left != null) q.enqueue((MapNode)n.left);
            if (n.right != null) q.enqueue((MapNode)n.right);
            result += n.key + "=" + n.value + ", ";
        }
        return result + "]";
    }
}
