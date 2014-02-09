
package tiralabra.datastructures;

/**
 * Queue that works with 'first in first out' principle.
 * @author Pavel
 */
public class Queue<E> {
/**
 * Element of the queue.
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
/**
 * Enqueues an element to the end of the queue.
 * @param e given element.
 */
    public void enqueue(E e)
    {
        Node u = new Node(e);
        if (tail != null)   tail.setNext(u);
        tail = u;
        if (isEmpty())
            head = tail;
    }
/**
 * Dequeues the first element in the queue.
 * @return first element of the queue.
 */
    public E dequeue()
    {
        if (isEmpty())  return null;
        E result = head.getValue();
        head = head.getNext();
        if (isEmpty())  tail = null;
        return result;
    }
/**
 * Retrieves the first element in a queue without making changes in the queue.
 * @return first element of the queue.
 */
    public E peek()
    {
        if (isEmpty())  return null;
        E result = head.getValue();
        return result;
    }
/**
 * Removes all elements in the queue.
 */
    public void clear()
    {
        head = null;
        tail = null;
    }
/**
 * 
 * @return true if no elements in the queue.
 */
    public boolean isEmpty()
    {
        return head == null;
    }
}
