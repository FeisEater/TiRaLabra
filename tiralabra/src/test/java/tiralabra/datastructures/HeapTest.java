
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
}
