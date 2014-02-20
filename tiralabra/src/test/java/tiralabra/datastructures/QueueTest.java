
package tiralabra.datastructures;

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
public class QueueTest {
    private Queue<String> queue;
    public QueueTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        queue = new Queue<>();
        queue.enqueue("apples");
        queue.enqueue("oranges");
        queue.enqueue("pickles");
        queue.enqueue("tommytoes");
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void enqueuesAndDequeues()
    {
        assertTrue(queue.dequeue().equals("apples") &&
                queue.dequeue().equals("oranges") &&
                queue.dequeue().equals("pickles") &&
                queue.dequeue().equals("tommytoes") &&
                queue.dequeue() == null);
    }
    @Test
    public void enqueuesAtTheEnd()
    {
        for (int i = 0; i < 3; i++) queue.dequeue();
        queue.enqueue("nice shoes");
        assertTrue(queue.dequeue().equals("tommytoes") &&
                queue.dequeue().equals("nice shoes") &&
                queue.dequeue() == null);
    }
    @Test
    public void emptyAfterClear()
    {
        queue.clear();
        assertTrue(queue.isEmpty());
    }
    @Test
    public void enqueuesAfterClear()
    {
        queue.clear();
        queue.enqueue("nice shoes");
        assertTrue(queue.dequeue().equals("nice shoes") &&
                queue.dequeue() == null);
    }
    @Test
    public void sizeIsCorrect()
    {
        boolean test = true;
        for (int i = 0; i < 10000; i++)
        {
            queue.clear();
            Random rand = new Random();
            int r = rand.nextInt(1000);
            for (int j = 0; j < r; j++) queue.enqueue("allo");
            int b = (r <= 0) ? 0 : rand.nextInt(r);
            for (int j = 0; j < b; j++) queue.dequeue();
            if (queue.size() != r - b)  test = false;
        }
        assertTrue(test);
    }

}
