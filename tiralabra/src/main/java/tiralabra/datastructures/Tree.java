
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
        public boolean isRed;
        public Unit(E e)
        {
            value = e;
            isRed = true;
        }
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
            tobeAdded.isRed = false;
            return;
        }
        findPlacement(tobeAdded);
        balanceOnInsertion(tobeAdded);
    }
    private void findPlacement(Unit tobeAdded)
    {
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
    private void balanceOnInsertion(Unit u)
    {
        while (true)
        {
            if (u == root)
            {
                u.isRed = false;
                return;
            }
            if (!u.parent.isRed)    return;
            Unit a = getAunt(u);
            if (a != null && a.isRed)
            {
                u.parent.isRed = false;
                a.isRed = false;
                getGrandParent(u).isRed = true;
                u = getGrandParent(u);
            }
            else
            {
                Unit g = getGrandParent(u);
                if (g == null)  return;
                if (u == u.parent.right && u.parent == g.left)
                {
                    rotateLeft(u.parent);
                    u = u.left;
                }
                else if (u == u.parent.left && u.parent == g.right)
                {
                    rotateRight(u.parent);
                    u = u.right;
                }
                u.parent.isRed = false;
                g.isRed = true;
                if (u == u.parent.left)
                    rotateRight(g);
                else    rotateLeft(g);
                return;
            }
        }
    }
    private void rotateLeft(Unit u)
    {
        int hookType;
        if (root == u)   hookType = 0;
        else if (u == u.parent.left)    hookType = -1;
        else    hookType = 1;
        Unit nodesRight = u.right;
        Unit rightsLeft = nodesRight.left;
        nodesRight.left = u;
        if (hookType == 0)  root = nodesRight;
        else
        {
            nodesRight.parent = u.parent;
            if (hookType == -1) u.parent.left = nodesRight;
            else    u.parent.right = nodesRight;
        }
        u.right = rightsLeft;
        if (rightsLeft != null)   rightsLeft.parent = u;
        u.parent = nodesRight;
    }
    private void rotateRight(Unit u)
    {
        int hookType;
        if (root == u)   hookType = 0;
        else if (u == u.parent.left)    hookType = -1;
        else    hookType = 1;
        Unit nodesLeft = u.left;
        Unit leftsRight = nodesLeft.right;
        nodesLeft.right = u;
        if (hookType == 0)  root = nodesLeft;
        else
        {
            nodesLeft.parent = u.parent;
            if (hookType == -1) u.parent.left = nodesLeft;
            else    u.parent.right = nodesLeft;
        }
        u.left = leftsRight;
        if (leftsRight != null)   leftsRight.parent = u;
        u.parent = nodesLeft;
    }
    private Unit getGrandParent(Unit u)
    {
        if (u.parent == null)   return null;
        return u.parent.parent;
    }
    private Unit getAunt(Unit u)
    {
        Unit g = getGrandParent(u);
        if (g == null)  return null;
        if (u.parent == g.left) return g.right;
        return g.left;
    }
    public void remove(E e)
    {
        Unit u = find(e);
        if (u == null)  return;
        
        if (u.left == null && u.right == null)
            removeNodeWithoutChildren(u);
        else if (u.left != null && u.right != null)
            removeNodeWithTwoChildren(u);
        else    removeNodeWithOneChild(u);
    }
    private void removeNodeWithoutChildren(Unit u)
    {
        if (root == u)
        {
            root = null;
            return;
        }
        if (u.parent.left == u) u.parent.left = null;
        else    u.parent.right = null;
        u.parent = null;
    }
    private void removeNodeWithTwoChildren(Unit u)
    {
        Unit n = getNext(u);
        u.value = n.value;
        if (n.parent == u)  u.right = null;
        else    n.parent.left = null;
        n.parent = null;
    }
    private void removeNodeWithOneChild(Unit u)
    {
        Unit n = (u.right == null) ? u.left : u.right;
        if (u == root)
        {
            root = n;
            u.left = null;
            u.right = null;
        }
        else
        {
            if (u.parent.left == u) u.parent.left = n;
            else    u.parent.right = n;
        }
        u.parent = null;
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
    public LinkedList<E> toLinkedList()
    {
        LinkedList<E> result = new LinkedList<>();
        Queue<Unit> q = new Queue<>();
        q.enqueue(root);
        while (!q.isEmpty())
        {
            Unit u = q.dequeue();
            result.add(u.value);
            if (u.left != null)   q.enqueue(u.left);
            if (u.right != null)   q.enqueue(u.right);
        }
        return result;
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
            if (u.isRed)    result += "r";
            result += u.value + " ";
            if (u.left != null)   q.enqueue(u.left);
            if (u.right != null)   q.enqueue(u.right);
            if (firstOnLevel == null)   firstOnLevel = (u.left == null) ? u.right : u.left;
        }
        return result;
    }
}
