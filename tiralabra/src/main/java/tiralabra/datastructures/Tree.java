
package tiralabra.datastructures;

import java.util.Comparator;

/**
 *
 * @author Pavel
 */
public class Tree<E> {
    private class Unit
    {
        public Unit left;
        public Unit right;
        public Unit parent;
        public E value;
        public Unit(E e)   {value = e;}
    }
    private Comparator comparator;
    private Unit root;
    public Tree(Comparator comp)
    {
        comparator = comp;
    }
    public void add(E e)
    {
        Unit tobeAdded = new Unit(e);
        if (root == null)
        {
            root = tobeAdded;
            return;
        }
        Unit u = root;
        while (true)
        {
            if (comparator.compare(tobeAdded.value, u.value) < 0)
            {
                if (u.left == null)
                {
                    u.left = tobeAdded;
                    tobeAdded.parent = u;
                    break;
                }
                else    u = u.left;
            }
            else
            {
                if (u.right == null)
                {
                    u.right = tobeAdded;
                    tobeAdded.parent = u;
                    break;
                }
                else    u = u.right;
            }
        }
    }
    public void remove(E e)
    {
        Unit u = find(e);
        if (u == null)  return;
        if (u.left == null && u.right == null)
        {
            if (root == u)
            {
                root = null;
                return;
            }
            if (u.parent.left == u) u.parent.left = null;
            else    u.parent.right = null;
            u.parent = null;
            return;
        }
        if (u.left != null && u.right != null)
        {
            Unit n = getNext(u);
            u.value = n.value;
            if (n.parent == u)  u.right = null;
            else    n.parent.left = null;
            n.parent = null;
            return;
        }
        if (u.left != null)
        {
            if (u == root)
            {
                root = u.left;
                u.left = null;
            }
            else
            {
                if (u.parent.left == u) u.parent.left = u.left;
                else    u.parent.right = u.left;
            }
            u.parent = null;
        }
        if (u.right != null)
        {
            if (u == root)
            {
                root = u.right;
                u.right = null;
            }
            else
            {
                if (u.parent.left == u) u.parent.left = u.right;
                else    u.parent.right = u.right;
            }
            u.parent = null;
        }
    }
    private Unit getNext(Unit u)
    {
        if (u.right == null)
        {
            Unit n = u;
            do {
                u = n;
                n = n.parent;
            } while (u == n.right);
            return n;
        }
        Unit n = u.right;
        while (n.left != null)
            n = n.left;
        return n;
    }
    private Unit find(E e)
    {
        Unit u = root;
        while (u.value != e)
        {
            if (comparator.compare(e, u.value) < 0)
                u = u.left;
            else    u = u.right;
        }
        return u;
    }
    @Override
    public String toString()
    {
        String result = "";
        Queue<Unit> q = new Queue<>();
        q.enqueue(root);
        Unit firstOnLevel = null;
        while (!q.isEmpty())
        {
            Unit u = q.dequeue();
            if (u == firstOnLevel)
            {
                firstOnLevel = null;
                result += "\n";
            }
            if (u != root)  result += u.parent.value + "->";
            result += u.value + " ";
            if (u.left != null)   q.enqueue(u.left);
            if (u.right != null)   q.enqueue(u.right);
            if (firstOnLevel == null)   firstOnLevel = (u.left == null) ? u.right : u.left;
        }
        return result;
    }
}
