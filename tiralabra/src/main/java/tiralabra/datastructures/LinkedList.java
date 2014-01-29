
package tiralabra.datastructures;

/**
 *
 * @author Pavel
 */
public class LinkedList<E> {
    private class Unit
    {
        private Unit next;
        private E value;
        public Unit(E e)   {value = e;}
        public E getValue()   {return value;}
        public Unit getNext()   {return next;}
        public void setNext(Unit n)   {next = n;}
    }
    private Unit head;
    private Unit tail;
    private Unit current;
    private int size;
    public LinkedList() {size = 0;}
    public void add(E e)
    {
        Unit u = new Unit(e);
        if (tail != null)   tail.setNext(u);
        tail = u;
        if (isEmpty())
        {
            head = tail;
            current = head;
        }
        size++;
    }
    public void reset()
    {
        current = head;
    }
    public E getNext()
    {
        if (!hasNext())    return null;
        E result = current.getValue();
        current = current.getNext();
        return result;
    }
    public boolean hasNext()
    {
        return current != null;
    }
    public void clear()
    {
        head = null;
        tail = null;
        current = null;
        size = 0;
    }
    public boolean isEmpty()
    {
        return head == null;
    }
    public int size()   {return size;}
    @Override
    public String toString()
    {
        Unit last = current;
        reset();
        String result = "[";
        while (hasNext())
        {
            if (current == head) result += getNext().toString();
            else    result += ", " + getNext().toString();
        }
        result += "]";
        current = last;
        return result;
    }
}
