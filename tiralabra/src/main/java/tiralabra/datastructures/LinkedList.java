
package tiralabra.datastructures;

/**
 *
 * @author Pavel
 */
public class LinkedList {
    private class Unit
    {
        private Unit next;
        private Object value;
        public Unit(Object o)   {value = o;}
        public Object getValue()   {return value;}
        public Unit getNext()   {return next;}
        public void setNext(Unit n)   {next = n;}
    }
    private Unit head;
    private Unit tail;
    private Unit current;
    public void add(Object o)
    {
        Unit u = new Unit(o);
        if (tail != null)   tail.setNext(u);
        tail = u;
        if (head == null)   head = tail;
    }
    public void reset()
    {
        current = head;
    }
    public Object getNext()
    {
        Object result = current.getValue();
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
    }
    public String toString()
    {
        reset();
        String result = "[";
        while (hasNext())
        {
            if (current == head) result += getNext().toString();
            else    result += ", " + getNext().toString();
        }
        result += "]";
        return result;
    }
}
