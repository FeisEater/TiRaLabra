
package tiralabra.datastructures;

import java.util.Comparator;
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
public class HeapTest {
    private Heap<Integer> heap;
    private class IntegerComparator implements Comparator
    {
        @Override
        public int compare(Object o1, Object o2)
        {
            return (int)o1 - (int)o2;
        }
    }

    public HeapTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        heap = new Heap<>(15, new IntegerComparator());
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void poppingReturnsSmallestElements()
    {
        for (int i = 0; i < 1000; i++)
            heap.insert((int)(Math.random() * 10000));
        int old = -1;
        boolean test = true;
        while (!heap.isEmpty())
        {
            int i = heap.pop();
            if (i < old)   test = false;
            old = i;
        }
        assertTrue(test);
    }
    @Test
    public void heapIsEmptyWhenCleared()
    {
        for (int i = 0; i < 1000; i++)
            heap.insert((int)(Math.random() * 10000));
        heap.clear(15);
        assertTrue(heap.isEmpty());
    }
    @Test
    public void heapsSizeIsCorrect()
    {
        for (int i = 0; i < 1337; i++)
            heap.insert((int)(Math.random() * 10000));
        assertTrue(heap.size() == 1337);
    }
    @Test
    public void poppingHeapDecreasesByOne()
    {
        for (int i = 0; i < 1337; i++)
            heap.insert((int)(Math.random() * 10000));
        heap.pop();
        assertTrue(heap.size() == 1336);
    }
    @Test
    public void increasesArrayOnOverflow()
    {
        for (int i = 0; i < 14; i++)
            heap.insert((int)(Math.random() * 10000));
        boolean b1 = heap.getArray().length == 15;
        heap.insert((int)(Math.random() * 10000));
        boolean b2 = heap.getArray().length == 30;
        assertTrue(b1 && b2);
    }
    @Test
    public void insertsByHeapRules()
    {
        for (int i = 1; i < 30; i+=2)
            heap.insert(i);
        heap.insert(6);
        int[] ar = new int[heap.size()];
        for (int i = 0; i < heap.size(); i++)
            ar[i] = (i*2) + 1;
        ar[3] = 6;
        ar[7] = 7;
        ar[15] = 15;
        boolean test = true;
        for (int i = 0; i < heap.size(); i++)
            if (ar[i] != (int)heap.getArray()[i])    test = false;
        assertTrue(test);
    }
    @Test
    public void popsByHeapRules()
    {
        for (int i = 1; i < 16; i++)
            heap.insert(i);
        heap.pop();
        int[] ar = new int[heap.size()];
        for (int i = 0; i < heap.size(); i++)
            ar[i] = i + 1;
        ar[0] = 2;
        ar[1] = 4;
        ar[3] = 8;
        ar[7] = 15;
        boolean test = true;
        for (int i = 0; i < heap.size(); i++)
            if (ar[i] != (int)heap.getArray()[i])    test = false;
        assertTrue(test);
    }
/*    @Test
    public void decreasesKey()
    {
        for (int i = 1; i < 30; i+=2)
            heap.insert(i);
        heap.changeValue(19, 2);
        int[] ar = new int[heap.size()];
        for (int i = 0; i < heap.size(); i++)
            ar[i] = (i*2) + 1;
        ar[1] = 2;
        ar[4] = 3;
        ar[9] = 9;
        boolean test = true;
        for (int i = 0; i < heap.size(); i++)
            if (ar[i] != (int)heap.getArray()[i])    test = false;
        assertTrue(test);
    }
    @Test
    public void increasesKey()
    {
        for (int i = 1; i < 30; i+=2)
            heap.insert(i);
        heap.changeValue(5, 26);
        int[] ar = new int[heap.size()];
        for (int i = 0; i < heap.size(); i++)
            ar[i] = (i*2) + 1;
        ar[2] = 11;
        ar[5] = 23;
        ar[11] = 26;
        boolean test = true;
        for (int i = 0; i < heap.size(); i++)
            if (ar[i] != (int)heap.getArray()[i])    test = false;
        assertTrue(test);
    }*/
    @Test
    public void peekingDoesntChangeHeap()
    {
        heap.insert(1);
        for (int i = 0; i < 1336; i++)
            heap.insert((int)(2 + Math.random() * 10000));
        int j = -1;
        for (int i = 0; i < 1337; i++)
            j = heap.peek();
        assertTrue(j == 1 && heap.size() == 1337);
    }
}
