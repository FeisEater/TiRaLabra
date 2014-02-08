
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
        add(new MapNode(key, value));
    }
}
