
package tiralabra.datastructures;

/**
 * Stack that works with 'first in last out' principle.
 * @author Pavel
 */
public class Stack<E> {
/**
 * Element of the stack.
 */
    private class Node
    {
/** Pointer to the previous element in the stack. */
        private Node prev;
        private E value;
        public Node(E e)
        {
            value = e;
        }
    }
/** Element last added to the stack. */
    private Node top;
/**
 * Adds an element on top of the stack.
 * @param e given element.
 */
    public void add(E e)
    {
        Node n = top;
        top = new Node(e);
        if (n != null)
            top.prev = n;
    }
/**
 * Retrieves and removes an element on top of the stack.
 * @return element on top of the stack.
 */
    public E pop()
    {
        if (isEmpty())  return null;
        E result = top.value;
        top = top.prev;
        return result;
    }
/**
 * 
 * @return true if no elements in the stack.
 */
    public boolean isEmpty()
    {
        return top == null;
    }
}
