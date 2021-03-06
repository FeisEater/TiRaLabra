
package tiralabra.datastructures;

/**
 * Queue that works with 'first in first out' principle.
 * @author Pavel
 * @param <E> Class of queue's elements.
 */
public class Queue<E> {
/**
 * Element of the queue.
 */
    private class Node
    {
/** Pointer to the next element in the queue. */
        private Node next;
        private E value;
        public Node(E e)   {value = e;}
        public E getValue()   {return value;}
        public Node getNext()   {return next;}
        public void setNext(Node n)   {next = n;}
    }
/** First element in the queue. */
    private Node head;
/** Last element in the queue. */
    private Node tail;
/** Size of the queue. */
    private int size;
    public Queue()  {size = 0;}
/**
 * Enqueues an element to the end of the queue.
 * @param e given element.
 */
    public void enqueue(E e)
    {
        if (e == null)  return;
        Node u = new Node(e);
        if (tail != null)   tail.setNext(u);
        tail = u;
        if (isEmpty())
            head = tail;
        size++;
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
        size--;
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
        size = 0;
    }
/**
 * 
 * @return true if no elements in the queue.
 */
    public boolean isEmpty()
    {
        return head == null;
    }
/**
 * Converts queue into a linkedlist.
 * @return LinkedList containing all data that is in the queue.
 */
    public LinkedList<E> toLinkedList()
    {
        LinkedList<E> result = new LinkedList<>();
        Node n = head;
        while (n != null)
        {
            result.add(n.value);
            n = n.next;
        }
        return result;
    }
/**
 * 
 * @return size of the queue.
 */
    public int size()   {return size;}
}
