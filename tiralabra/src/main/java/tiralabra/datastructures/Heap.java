
package tiralabra.datastructures;

import java.util.Comparator;

/**
 * Binary heap. Contains elements in a binary tree, where every node
 * has to be bigger than both its children (assuming the comparator
 * defines the heap as maximum heap). Elements are stored in an array,
 * and all pointers are accessed via array indexes. Optionally keeps elements
 * in a treemap to retrieve indexes by element's pointer.
 * @author Pavel
 * @param <E> Class of stored elements.
 */
public class Heap<E> {
    private Object[] array;
    private int heapSize;
    private Comparator comparator;
/** Map that retrieves an index by element's pointer. */
    private TreeMap<E, Integer> indexes;
/**
 * Constructor
 * @param initialSize Initial size of heap's array.
 * @param comp Comparator class used in the heap.
 */
    public Heap(int initialSize, Comparator comp)
    {
        array = new Object[initialSize];
        heapSize = 0;
        comparator = comp;
    }
/**
 * Constructor
 * @param initialSize Initial size of heap's array.
 * @param comp Comparator class used in the heap.
 * @param ind TreeMap for retrieving indexes based on an element.
 *      Must use a different comparator class than heap's comparator.
 */
    public Heap(int initialSize, Comparator comp, TreeMap<E, Integer> ind)
    {
        this(initialSize, comp);
        indexes = ind;
    }
/**
 * Removes all elements in the heap.
 * @param initialSize new size of the array.
 */
    public void clear(int initialSize)
    {
        array = new Object[initialSize];
        heapSize = 0;
        if (indexes != null)    indexes.clear();
    }
/**
 * Returns and deletes the first element of the heap.
 * @return First element of the heap.
 */
    public E pop()
    {
        if (isEmpty())  return null;
        E result = (E)array[0];
        switchNodes(0, heapSize - 1);
        heapSize--;
        heapify(0);
        return result;
    }
/**
 * Returns top element in the heap without making changes to the heap.
 * @return top element in the heap.
 */
    public E peek()
    {
        if (isEmpty())  return null;
        return (E)array[0];
    }
/**
 * Adds an element to the heap.
 * @param e Object that is inserted.
 */
    public void insert(E e)
    {
        int newPlace = heapSize;
        while (newPlace > 0 &&
                comparator.compare(e, array[getParent(newPlace)]) < 0)
        {
            array[newPlace] = array[getParent(newPlace)];
            if (indexes != null)
                indexes.put((E)array[newPlace], newPlace);
            newPlace = getParent(newPlace);
        }
        array[newPlace] = e;
        if (indexes != null)
            indexes.put(e, newPlace);
        heapSize++;
        if (heapSize >= array.length)
            increaseArray();
    }
/**
 * Notifies the heap of an element that has changed its value,
 * taking care that heap's structure is not violated.
 * @param changedValue Element of the heap that has changed its value.
 */
    public void valueChanged(E changedValue)
    {
        int changeAt = indexes.get(changedValue);
        while (changeAt > 0 &&
                comparator.compare(array[changeAt], array[getParent(changeAt)]) < 0)
        {
            switchNodes(changeAt, getParent(changeAt));
            changeAt = getParent(changeAt);
        }
        heapify(changeAt);
    }
/**
 * Doubles the size of the array.
 */
    private void increaseArray()
    {
        Object[] newArray = new Object[array.length * 2];
        for (int i = 0; i < array.length; i++)
            newArray[i] = array[i];
        array = newArray;
    }
/**
 * Finds given element's left child.
 * @param cur Index of the given element.
 * @return Index of the left child of the given element.
 */
    private int getLeft(int cur)
    {
        return (cur + 1) * 2 - 1;
    }
/**
 * Finds given element's right child.
 * @param cur Index of the given element.
 * @return Index of the right child of the given element.
 */
    private int getRight(int cur)
    {
        return (cur + 1) * 2;
    }
/**
 * Finds given element's parent.
 * @param cur Index of the given element.
 * @return Index of given element's parent.
 */
    private int getParent(int cur)
    {
        return (int)Math.floor((cur+1) / 2) - 1;
    }
/**
 * Recovers heap structure downwards starting from the given node.
 * @param cur Index of a given element.
 */
    private void heapify(int cur)
    {
        while (getRight(cur) < heapSize)
        {
            E left = (E)array[getLeft(cur)];
            E right = (E)array[getRight(cur)];
            if (comparator.compare(array[cur], left) > 0 ||
                comparator.compare(array[cur], right) > 0)
            {
                int min;
                if (comparator.compare(left, right) < 0)
                    min = getLeft(cur);
                else    min = getRight(cur);
                
                switchNodes(cur, min);
                cur = min;
            }
            else    return;
        }
        if (getLeft(cur) < heapSize && 
                comparator.compare(array[cur], array[getLeft(cur)]) > 0)
            switchNodes(cur, getLeft(cur));
    }
/**
 * Switches values between two nodes.
 * @param i1 Index of an element.
 * @param i2 Index of an element.
 */
    private void switchNodes(int i1, int i2)
    {
        Object o = array[i1];
        array[i1] = array[i2];
        array[i2] = o;
        if (indexes != null)
        {
            indexes.put((E)array[i1], i1);
            indexes.put((E)array[i2], i2);
        }
    }
/**
 * 
 * @return Amount of elements in the heap.
 */
    public int size()   {return heapSize;}
/**
 * 
 * @return Heap is empty.
 */
    public boolean isEmpty()    {return heapSize == 0;}
    public Object[] getArray()  {return array;}
    @Override
    public String toString()
    {
        String result = "";
        for (int i = 0; i < heapSize; i++)
        {
            if (isBase2(i+1) && i != 0) result += "\n";
            result += array[i] + ", ";
        }
        result += "\n---";
        return result;
    }
/**
 * 
 * @param i Positive integer.
 * @return true if i is a number in base 2 
 * (i = 2^n where n is some positive integer)
 */
    private boolean isBase2(int i)
    {
        while (i > 1)
        {
            if (i%2 != 0)   return false;
            i /= 2;
        }
        return i == 1;
    }
}
