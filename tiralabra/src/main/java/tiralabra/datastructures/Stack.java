
package tiralabra.datastructures;

/**
 *
 * @author Pavel
 */
public class Stack<E> {
    private class Node
    {
        private Node prev;
        private E value;
        public Node(E e)
        {
            value = e;
        }
    }
    private Node top;
    public void add(E e)
    {
        Node n = top;
        top = new Node(e);
        if (n != null)
            top.prev = n;
    }
    public E pop()
    {
        E result = top.value;
        top = top.prev;
        return result;
    }
    public boolean isEmpty()
    {
        return top == null;
    }
}
