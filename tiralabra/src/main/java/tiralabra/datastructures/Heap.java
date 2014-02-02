
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
    public void clear(int initialSize)
    {
        array = new Object[initialSize];
        heapSize = 0;
    }
    public E pop()
    {
        E result = (E)array[0];
        switchNodes(0, heapSize - 1);
        heapSize--;
        heapify(0);
        return result;
    }
    public void insert(E e)
    {
        int newPlace = heapSize;
        while (newPlace > 0 &&
                comparator.compare(e, array[getParent(newPlace)]) < 0)
        {
            array[newPlace] = array[getParent(newPlace)];
            newPlace = getParent(newPlace);
        }
        array[newPlace] = e;
        heapSize++;
        if (heapSize >= array.length)
            increaseArray();
    }
    public void valueChanged(int changeAt)
    {
        while (changeAt > 0 &&
                comparator.compare(array[changeAt], array[getParent(changeAt)]) < 0)
        {
            switchNodes(changeAt, getParent(changeAt));
            changeAt = getParent(changeAt);
        }
        heapify(changeAt);
    }
    public void changeValue(E oldV, E newV)
    {
        int i = findValue(oldV);
        array[i] = newV;
        valueChanged(i);
    }
    public int findValue(E e)
    {
        for (int i = 0; i < heapSize; i++)
            if (e == array[i])  return i;
/*        int i = 0;
        while (getLeft(i) < heapSize && comparator.compare(e, array[getLeft(i)]) > 0)
            i = getLeft(i);
        while (i < heapSize)
        {
            if (e == array[i])
                return i;
            i++;
        }*/
        return -1;
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
                if (comparator.compare(left, right) < 0)    min = getLeft(cur);
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
    private void switchNodes(int i1, int i2)
    {
        Object o = array[i1];
        array[i1] = array[i2];
        array[i2] = o;
    }
    public int size()   {return heapSize;}
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
