
package tiralabra.datastructures;

import java.util.Comparator;

/**
 * A map of key-value pairs stored in a search tree.
 * @author Pavel
 * @param <K> Key object's class.
 * @param <V> Value object's class.
 */
public class TreeMap<K, V> extends Tree {
    public TreeMap(Comparator comp) {super(comp);}
/**
 * Element of the treemap.
 */
    protected class MapNode extends Tree.Node
    {
        public MapNode(K k, V v)
        {
            super(k);
            value = v;
        }
        public V value;
    }
/**
 * Retrieves element by the key.
 * @param key Key of the element.
 * @return Value of the element.
 */
    public V get(K key)
    {
        MapNode n = (MapNode)find(key);
        if (n == null)  return null;
        return n.value;
    }
/**
 * Adds a new key-value pair to the map. If map has an element of the
 * given key, changes that element's value.
 * @param key Key of the element.
 * @param value Value of the element.
 */
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
