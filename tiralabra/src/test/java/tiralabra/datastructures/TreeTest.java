
package tiralabra.datastructures;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pavel
 */
public class TreeTest {
    private Tree<Integer> tree;
    private class IntegerComparator implements Comparator
    {
        @Override
        public int compare(Object o1, Object o2)
        {
            return (int)o1 - (int)o2;
        }
    }
    public TreeTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        tree = new Tree<>(new IntegerComparator());
    }
    
    @After
    public void tearDown() {
    }

    public int getTreeHeight(int height, Object n) throws Throwable
    {
        if (n == null)  return height;
        Field left = Tree.class.getDeclaredClasses()[0].getDeclaredField("left");
        left.setAccessible(true);
        Field right = Tree.class.getDeclaredClasses()[0].getDeclaredField("right");
        right.setAccessible(true);
        return Math.max(getTreeHeight(height + 1, left.get(n)), getTreeHeight(height + 1, right.get(n)));
    }
    @Test
    public void treeHasReasonableHeight1() throws Throwable
    {
        for (int i = 1; i <= 1024; i++)
            tree.add(i);
        Field field = Tree.class.getDeclaredField("root");
        field.setAccessible(true);
        int height = getTreeHeight(0, field.get(tree));
        assertTrue(height <= 20);
    }
    @Test
    public void treeHasReasonableHeight2() throws Throwable
    {
        for (int i = 1024; i >= 0; i--)
            tree.add(i);
        Field field = Tree.class.getDeclaredField("root");
        field.setAccessible(true);
        int height = getTreeHeight(0, field.get(tree));
        assertTrue(height <= 20);
    }
    @Test
    public void treeHasReasonableHeight3() throws Throwable
    {
        for (int i = 1; i <= 1024; i++)
            tree.add((int)(Math.random() * 10000));
        Field field = Tree.class.getDeclaredField("root");
        field.setAccessible(true);
        int height = getTreeHeight(0, field.get(tree));
        assertTrue(height <= 20);
    }
    @Test
    public void treeHasReasonableHeightAfterDeletions() throws Throwable
    {
        for (int i = 1; i <= 2048; i++)
            tree.add(i);
        for (int i = 1; i <= 1024; i++)
            tree.remove(i);
        Field field = Tree.class.getDeclaredField("root");
        field.setAccessible(true);
        int height = getTreeHeight(0, field.get(tree));
        assertTrue(height <= 20);
    }
    @Test
    public void treeHasReasonableHeightAfterDeletions2() throws Throwable
    {
        for (int i = 1; i <= 2048; i++)
            tree.add((int)(Math.random() * 2048));
        for (int i = 1; i <= 1024; i++)
            tree.remove((int)(Math.random() * 2048));
        Field field = Tree.class.getDeclaredField("root");
        field.setAccessible(true);
        int height = getTreeHeight(0, field.get(tree));
        assertTrue(height <= 20);
    }
    @Test
    public void afterClearingTreeIsEmpty()
    {
        for (int i = 0; i < 2048; i++)
            tree.add((int)(Math.random() * 2048));
        tree.clear();
        assertTrue(tree.isEmpty());
    }
    @Test
    public void returnsLinkedList()
    {
        for (int i = 0; i < 2048; i++)
            tree.add(i);
        LinkedList<Integer> list = tree.toLinkedList();
        boolean test = true;
        for (int i = 0; i < 2048; i++)
        {
            test = false;
            list.reset();
            while (list.hasNext())
            {
                int j = list.getNext();
                if (j == i)
                {
                    test = true;
                    break;
                }
            }
            if (!test)   break;
        }
        assertTrue(test);
    }
    @Test
    public void returnsSmallestElement()
    {
        tree.add(0);
        for (int i = 0; i < 2048; i++)
            tree.add((int)(1 + Math.random() * 2048));
        assertTrue(tree.getMin() == 0);
    }
    @Test
    public void noSmallestElementIfTreeIsEmpty()
    {
        for (int i = 0; i < 2048; i++)
            tree.add((int)(Math.random() * 2048));
        tree.clear();
        assertTrue(tree.getMin() == null);
    }
    @Test
    public void sizeIsCorrect()
    {
        boolean test = true;
        for (int i = 0; i < 1000; i++)
        {
            tree.clear();
            Random rand = new Random();
            int r = rand.nextInt(1000);
            for (int j = 0; j < r; j++) tree.add(j);
            int b = (r <= 0) ? 0 : rand.nextInt(r);
            for (int j = 0; j < b; j++) tree.remove(j);
            if (tree.size() != r - b)
            {
                test = false;
                break;
            }
        }
        assertTrue(test);
    }
    @Test
    public void containsWorks()
    {
        boolean test = true;
        for (int i = 0; i < 1000; i++)
        {
            tree.clear();
            for (int j = 0; j < 1000; j++) tree.add(j);
            int r = (int)(Math.random() * 1000);
            if (!tree.contains(r))
            {
                test = false;
                break;
            }
        }
        assertTrue(test);
    }
    @Test
    public void containsIsFalseIfDoesntContain()
    {
        for (int j = 0; j < 1000; j++) tree.add(j);
        assertTrue(!tree.contains(-1));
    }
}
