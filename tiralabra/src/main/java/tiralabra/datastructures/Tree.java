
package tiralabra.datastructures;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
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
        root.parent = null;
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
    private Unit getSibling(Unit u)
    {
        if (u.parent == null)   return null;
        if (u.parent.left == u) return u.parent.right;
        return u.parent.left;
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
        if (root != null)   root.parent = null;
    }
    private void removeNodeWithoutChildren(Unit u)
    {
        if (root == u)
        {
            root = null;
            return;
        }
        if (u.parent.left == u) u.parent.left = null;
        else if (u.parent.right == u)    u.parent.right = null;
        u.parent = null;
        balanceOnRemoval(u);
    }
    private void removeNodeWithTwoChildren(Unit u)
    {
        Unit n = getNext(u);
        u.value = n.value;
        Unit removedsRight = n.right;
        if (n.parent == u)  u.right = removedsRight;
        else    n.parent.left = removedsRight;
        if (removedsRight.isRed)    removedsRight.isRed = false;
        n.right = null;
        n.parent = null;
    }
    private void removeNodeWithOneChild(Unit u)
    {
        Unit n = (u.right == null) ? u.left : u.right;
        if (n.isRed)    n.isRed = false;
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
            n.parent = u.parent;
        }
        u.parent = null;
    }
    //TODO: implement case 5 and 6
    private void balanceOnRemoval(Unit u)
    {
        while (u != root)
        {
            Unit s = getSibling(u);
            if (s.isRed)
            {
                u.parent.isRed = true;
                s.isRed = false;
                if (u == u.parent.left)
                    rotateLeft(u.parent);
                if (u == u.parent.right)
                    rotateRight(u.parent);
            }
            if (!s.isRed)
            {
                if (!s.left.isRed && !s.right.isRed)
                {
                    if (!u.parent.isRed)
                    {
                        s.isRed = true;
                        u = u.parent;
                    }
                    else
                    {
                        u.parent.isRed = false;
                        s.isRed = true;
                        return;
                    }
                }
            }
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
            if (u == null)  return null;
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
    public void drawTree(Graphics g)
    {
        drawNode(root, g, 320, 1);
    }
    private void drawNode(Unit n, Graphics g, int x, int y)
    {
        if (n == null)  return;
        g.setColor(n.isRed ? Color.red : Color.GRAY);
        if (n.parent == null)  g.setColor(Color.DARK_GRAY);
        g.fillOval(x, y * 64, 32, 32);
        g.setColor(Color.BLACK);
        if (n.left != null)   g.drawLine(x+16, y * 64 + 16, x - 128 / y + 16, (y+1) * 64 + 16);
        if (n.right != null)   g.drawLine(x+16, y * 64 + 16, x + 128 / y + 16, (y+1) * 64 + 16);
        g.setFont(new Font("Serif", Font.BOLD, 32));
        g.drawString(n.value.toString(), x, y * 64);
        drawNode(n.left, g, x - 128 / y, y+1);
        drawNode(n.right, g, x + 128 / y, y+1);
    }
}
