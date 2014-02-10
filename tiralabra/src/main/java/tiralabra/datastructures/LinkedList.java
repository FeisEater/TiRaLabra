
package tiralabra.datastructures;

/**
 * Iterable list of elements. List is acyclic and can't be iterated
 * in the other direction.
 * @author Pavel
 * @param <E> Class of list's elements.
 */
public class LinkedList<E> {
/**
 * Element in the list.
 */
    private class Node
    {
        private Node next;
        private E value;
        public Node(E e)   {value = e;}
        public E getValue()   {return value;}
        public Node getNext()   {return next;}
        public void setNext(Node n)   {next = n;}
    }
    private Node head;
    private Node tail;
    private Node current;
    private int size;
    public LinkedList() {size = 0;}
/**
 * Adds an element to the list.
 * @param e Given element.
 */
    public void add(E e)
    {
        Node u = new Node(e);
        if (tail != null)   tail.setNext(u);
        tail = u;
        if (isEmpty())
        {
            head = tail;
            current = head;
        }
        size++;
    }
/**
 * Resets the pointer for iterating. After calling this method
 * getNext will return the first element in the list.
 */
    public void reset()
    {
        current = head;
    }
/**
 * Returns next element in the iterating process.
 * @return next element.
 */
    public E getNext()
    {
        if (!hasNext())    return null;
        E result = current.getValue();
        current = current.getNext();
        return result;
    }
/**
 * 
 * @return true if iterating the list hasn't reached its conclusion.
 */
    public boolean hasNext()
    {
        return current != null;
    }
/**
 * Removes all elements in the list.
 */
    public void clear()
    {
        head = null;
        tail = null;
        current = null;
        size = 0;
    }
/**
 * 
 * @return true if list has no elements in it.
 */
    public boolean isEmpty()
    {
        return head == null;
    }
/**
 * 
 * @return Amount of elements in the list.
 */
    public int size()   {return size;}
    @Override
    public String toString()
    {
        Node last = current;
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
