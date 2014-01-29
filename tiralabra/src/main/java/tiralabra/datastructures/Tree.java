
package tiralabra.datastructures;

/**
 *
 * @author Pavel
 */
public class Tree<E> {
    private class Unit
    {
        private Unit left;
        private Unit right;
        private Unit parent;
        private E value;
        public Unit(E e)   {value = e;}
        public E getValue()   {return value;}
        public Unit getLeft()   {return left;}
        public void setLeft(Unit n)   {left = n;}
        public Unit getRight()   {return right;}
        public void setRight(Unit n)   {right = n;}
        public Unit getParent()   {return parent;}
        public void setParent(Unit n)   {parent = n;}
    }

}
