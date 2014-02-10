
package tiralabra.datastructures;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Comparator;

/**
 * Binary red-black search tree. On insertion and removal the tree
 * uses self-balancing techniques to keep the height of the tree
 * logarithmic in respect to the amount of elements.
 * @author Pavel
 * @param <E> Class of the elements in the tree.
 */
public class Tree<E> {
/**
 * Element of the tree.
 */
    protected class Node
    {
        public Node left;
        public Node right;
        public Node parent;
        public E key;
        public boolean isRed;
        public Node(E e)
        {
            key = e;
            isRed = true;
        }
    }
    private Comparator comparator;
    protected Node root;
    private int size;
    private Node min;
/**
 * Constructor.
 * @param comp Comparator class used for placing the element at the correct spot.
 */
    public Tree(Comparator comp)
    {
        comparator = comp;
        size = 0;
    }
/**
 * 
 * @return true if tree has no elements in it.
 */
    public boolean isEmpty()
    {
        return root == null;
    }
/**
 * Removes all elements in the tree.
 */
    public void clear()
    {
        root = null;
        size = 0;
        min = null;
    }
/**
 * 
 * @return smallest element in a tree.
 */
    public E getMin()
    {
        if (isEmpty())  return null;
        return min.key;
    }
/**
 * Adds an element to the tree.
 * @param e Added element.
 */
    public void add(E e)
    {
        add(new Node(e));
    }
/**
 * Adds an element to the tree.
 * @param tobeAdded Added node.
 */
    protected void add(Node tobeAdded)
    {
        if (!contains(tobeAdded.key))   size++;
        if (root == null)
        {
            root = tobeAdded;
            min = tobeAdded;
            tobeAdded.isRed = false;
            return;
        }
        findPlacement(tobeAdded);
        balanceOnInsertion(tobeAdded);
        root.parent = null;
        if (comparator.compare(tobeAdded.key, min.key) < 0)
            min = tobeAdded;
    }
/**
 * Finds and places a newly added element at a correct spot.
 * @param tobeAdded Element that will be added.
 */
    private void findPlacement(Node tobeAdded)
    {
        Node u = root;
        while (true)
        {
            if (comparator.compare(tobeAdded.key, u.key) < 0)
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
/**
 * Balances the tree after insertion.
 * @param u Node that was added and may violate the balance.
 */
    private void balanceOnInsertion(Node u)
    {
        while (true)
        {
            if (u == root)
            {
                u.isRed = false;
                return;
            }
            if (!u.parent.isRed)    return;
            Node a = getAunt(u);
            if (a != null && a.isRed)
            {
                u.parent.isRed = false;
                a.isRed = false;
                getGrandParent(u).isRed = true;
                u = getGrandParent(u);
            }
            else
            {
                Node g = getGrandParent(u);
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
/**
 * Rotates the branch left.
 * @param u Node which will be rotated left.
 */
    private void rotateLeft(Node u)
    {
        int hookType;
        if (root == u)   hookType = 0;
        else if (u == u.parent.left)    hookType = -1;
        else    hookType = 1;
        Node nodesRight = u.right;
        Node rightsLeft = nodesRight.left;
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
/**
 * Rotates the branch right.
 * @param u Node which will be rotated right.
 */
    private void rotateRight(Node u)
    {
        int hookType;
        if (root == u)   hookType = 0;
        else if (u == u.parent.left)    hookType = -1;
        else    hookType = 1;
        Node nodesLeft = u.left;
        Node leftsRight = nodesLeft.right;
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
/**
 * Finds the grandparent of the given node.
 * @param u Given node.
 * @return Grandparent of the node.
 */
    private Node getGrandParent(Node u)
    {
        if (u.parent == null)   return null;
        return u.parent.parent;
    }
/**
 * Finds the given node's grandparent's child that isn't node's parent.
 * @param u Given node.
 * @return Node's aunt or uncle.
 */
    private Node getAunt(Node u)
    {
        Node g = getGrandParent(u);
        if (g == null)  return null;
        if (u.parent == g.left) return g.right;
        return g.left;
    }
/**
 * Finds the sibling of the given node.
 * @param u Given node.
 * @return Sibling of the node.
 */
    private Node getSibling(Node u)
    {
        if (u.parent == null)   return null;
        if (u.parent.left == u) return u.parent.right;
        return u.parent.left;
    }
/**
 * Removes given element from the tree.
 * @param e Given element.
 */
    public void remove(E e)
    {
        Node u = find(e);
        if (u == null)
            return;
        
        if (size <= 1)  min = null;
        else if (e == min.key)
            min = getNext(min);
        
        size--;
        if (u.left == null && u.right == null)
            removeNodeWithoutChildren(u);
        else if (u.left != null && u.right != null)
            removeNodeWithTwoChildren(u);
        else    removeNodeWithOneChild(u);
        if (root != null)   root.parent = null;
    }
/**
 * Removes a node that has no children.
 * @param u Removed node.
 */
    private void removeNodeWithoutChildren(Node u)
    {
        if (root == u)
        {
            root = null;
            return;
        }
        if (!u.isRed)   balanceOnRemoval(u);
        if (u.parent.left == u) u.parent.left = null;
        else if (u.parent.right == u)    u.parent.right = null;
        u.parent = null;
    }
/**
 * Removes a node with two children.
 * @param u Removed node.
 */
    private void removeNodeWithTwoChildren(Node u)
    {
        Node n = getNext(u);
        u.key = n.key;
        Node removedsRight = n.right;
        if (n.parent == u)
        {
            u.right = removedsRight;
            if (removedsRight != null)   removedsRight.parent = u;
        }
        else
        {
            n.parent.left = removedsRight;
            if (removedsRight != null)   removedsRight.parent = n.parent;
        }
        if (isRed(removedsRight))
            removedsRight.isRed = false;
        else    balanceOnRemoval(n);
        n.right = null;
        n.parent = null;
    }
/**
 * Removes a node with one child.
 * @param u Removed node.
 */
    private void removeNodeWithOneChild(Node u)
    {
        Node n = (u.right == null) ? u.left : u.right;
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
/**
 * Checks if node is red. This check will assume that the node
 * is black if it's null.
 * @param u Given node.
 * @return true if node is red. false if black.
 */
    private boolean isRed(Node u)
    {
        if (u == null)  return false;
        return u.isRed;
    }
/**
 * Balances the tree after removing a node.
 * @param u Node that was removed. Pointers to the parent, left and right
 * child are still intact.
 */
    private void balanceOnRemoval(Node u)
    {
        while (u != root)
        {
            Node s = getSibling(u);
            if (isRed(s))
            {
                u.parent.isRed = true;
                s.isRed = false;
                if (u == u.parent.left)
                    rotateLeft(u.parent);
                if (u == u.parent.right)
                    rotateRight(u.parent);
            }
            s = getSibling(u);
            if (s == null)  return;
            if (!isRed(s))
            {
                if (!isRed(s.left) && !isRed(s.right))
                {
                    if (!isRed(u.parent))
                    {
                        s.isRed = true;
                        u = u.parent;
                        continue;
                    }
                    else
                    {
                        u.parent.isRed = false;
                        s.isRed = true;
                        return;
                    }
                }
                else if (u == u.parent.left && !isRed(s.right) && isRed(s.left))
                {
                    s.isRed = true;
                    s.left.isRed = false;
                    rotateRight(s);
                }
                else if (u == u.parent.right && !isRed(s.left) && isRed(s.right))
                {
                    s.isRed = true;
                    s.right.isRed = false;
                    rotateLeft(s);
                }
            }
            s = getSibling(u);
            if (s == null)  return;
            s.isRed = u.parent.isRed;
            u.parent.isRed = false;
            if (u == u.parent.left)
            {
                if (s.right != null)   s.right.isRed = false;
                rotateLeft(u.parent);
            }
            else
            {
                if (s.left != null)   s.left.isRed = false;
                rotateRight(u.parent);
            }
        }
    }
/**
 * Finds the successor of the given node. Assuming that the tree's elements
 * are smaller to the left, this method will find the smallest node
 * that is bigger than the given node.
 * @param u Given node.
 * @return Node's successor.
 */
    private Node getNext(Node u)
    {
        if (u.right == null)
        {
            Node n = u;
            do {
                u = n;
                n = n.parent;
            } while (u == n.right);
            return n;
        }
        Node n = u.right;
        while (n.left != null)
            n = n.left;
        return n;
    }
/**
 * Finds the node of a given element. Used to access node's children and parent.
 * @param e Element that is to be found.
 * @return Node where element is stored.
 */
    protected Node find(E e)
    {
        if (isEmpty())
            return null;
        Node u = root;
        while (u.key != e)
        {
            if (comparator.compare(e, u.key) < 0)
                u = u.left;
            else    u = u.right;
            if (u == null)  return null;
        }
        return u;
    }
/**
 * True if tree contains the specified element.
 * @param e specified element
 * @return true if tree contains the specified element.
 */
    public boolean contains(E e)
    {
        return find(e) != null;
    }
/**
 * Forms a list of elements in the tree.
 * @return list of elements in the tree.
 */
    public LinkedList<E> toLinkedList()
    {
        LinkedList<E> result = new LinkedList<>();
        if (isEmpty())  return result;
        Queue<Node> q = new Queue<>();
        q.enqueue(root);
        while (!q.isEmpty())
        {
            Node u = q.dequeue();
            result.add(u.key);
            if (u.left != null)   q.enqueue(u.left);
            if (u.right != null)   q.enqueue(u.right);
        }
        return result;
    }
/**
 * 
 * @return amount of elements in the tree.
 */
    public int size()
    {
        return size;
    }
    @Override
    public boolean equals(Object o)
    {
        if (o.getClass() != Tree.class) return false;
        Tree t = (Tree)o;
        LinkedList list = toLinkedList();
        while (list.hasNext())
            if (!t.contains(list.getNext()))    return false;
        return size == t.size();
    }
    @Override
    public String toString()
    {
        if (isEmpty())  return "empty";
        String result = "";
        Queue<Node> q = new Queue<>();
        q.enqueue(root);
        Node firstOnLevel = null;
        while (!q.isEmpty())
        {
            Node u = q.dequeue();
            if (u == firstOnLevel)
            {
                firstOnLevel = null;
                result += "\n";
            }
            if (u != root)  result += u.parent.key + "->";
            if (u.isRed)    result += "r";
            result += u.key + " ";
            if (u.left != null)   q.enqueue(u.left);
            if (u.right != null)   q.enqueue(u.right);
            if (firstOnLevel == null)   firstOnLevel = (u.left == null) ? u.right : u.left;
        }
        return result + "\n---";
    }
/**
 * Draws a visual representation of the tree. Used for debugging.
 * @param g Graphics object.
 */
    public void drawTree(Graphics g)
    {
        drawNode(root, g, 320, 1);
    }
/**
 * Recursive call to draw relevant information of a single node
 * and its children.
 * @param n Node that is drawn.
 * @param g Graphics object.
 * @param x X position of the drawn circle.
 * @param y Layer of the node.
 */
    private void drawNode(Node n, Graphics g, int x, int y)
    {
        if (n == null)  return;
        g.setColor(n.isRed ? Color.red : Color.GRAY);
        if (n.parent == null)  g.setColor(Color.DARK_GRAY);
        g.fillOval(x, y * 64, 32, 32);
        g.setColor(Color.BLACK);
        if (n.left != null)   g.drawLine(x+16, y * 64 + 16, x - 128 / y + 16, (y+1) * 64 + 16);
        if (n.right != null)   g.drawLine(x+16, y * 64 + 16, x + 128 / y + 16, (y+1) * 64 + 16);
        g.setFont(new Font("Serif", Font.BOLD, 32));
        g.drawString(n.key.toString(), x, y * 64);
        drawNode(n.left, g, x - 128 / y, y+1);
        drawNode(n.right, g, x + 128 / y, y+1);
    }
}
