
package tiralabra.datastructures;

/**
 *
 * @author Pavel
 */
public class Queue<E> {
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
    public void enqueue(E e)
    {
        Unit u = new Unit(e);
        if (tail != null)   tail.setNext(u);
        tail = u;
        if (isEmpty())
            head = tail;
    }
    public E dequeue()
    {
        if (isEmpty())  return null;
        E result = head.getValue();
        head = head.getNext();
        if (isEmpty())  tail = null;
        return result;
    }
    public void clear()
    {
        head = null;
        tail = null;
    }
    public boolean isEmpty()
    {
        return head == null;
    }
}
