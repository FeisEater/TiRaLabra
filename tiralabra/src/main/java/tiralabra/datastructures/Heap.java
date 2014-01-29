
package tiralabra.datastructures;

import java.util.Comparator;

/**
 *
 * @author Pavel
 */
public class Heap<E> {
    private Object[] array;
    private int heapSize;
    private Comparator comparator;
    public Heap(int initialSize, Comparator comp)
    {
        array = new Object[initialSize];
        heapSize = 0;
        comparator = comp;
    }
    public E pop()
    {
        E result = (E)array[0];
        return result;
    }
    public void insert(E e)
    {
        array[heapSize] = e;
        heapSize++;
        if (heapSize >= array.length)
            increaseArray();
    }
    public void increaseArray()
    {
        Object[] newArray = new Object[array.length * 2];
        for (int i = 0; i < array.length; i++)
            newArray[i] = array[i];
        array = newArray;
    }
    private int getLeft(int cur)
    {
        return (cur + 1) * 2 - 1;
    }
    private int getRight(int cur)
    {
        return (cur + 1) * 2;
    }
    private int getParent(int cur)
    {
        return (int)Math.floor((cur+1) / 2) - 1;
    }
}
