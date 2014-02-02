
package tiralabra.datastructures;

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
public class LinkedListTest {
    private LinkedList<String> list;
    public LinkedListTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        list = new LinkedList<>();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void addsStuff()
    {
        list.add("Hello");
        list.add("world");
        list.add("whaz");
        list.add("good?");
        assertTrue(list.getNext().equals("Hello") &&
                list.getNext().equals("world") &&
                list.getNext().equals("whaz") &&
                list.getNext().equals("good?") &&
                list.getNext() == null);
    }
    @Test
    public void sizeIsCorrect()
    {
        for (int i = 0; i < 1337; i++)
            list.add("bleep");
        assertTrue(list.size() == 1337);
    }
    @Test
    public void afterClearListIsEmpty()
    {
        for (int i = 0; i < 1337; i++)
            list.add("bleep");
        list.clear();
        assertTrue(list.isEmpty());
    }
    @Test
    public void afterClearNoNextNode()
    {
        for (int i = 0; i < 1337; i++)
            list.add("bleep");
        list.clear();
        assertTrue(list.getNext() == null);
    }
    @Test
    public void addsNewStuffAfterClearing()
    {
        for (int i = 0; i < 1337; i++)
            list.add("bleep");
        list.clear();
        for (int i = 0; i < 13; i++)
            list.add("bloop");
        assertTrue(list.size() == 13);
    }
    @Test
    public void getNextIsNotNullUntilListIsOver()
    {
        for (int i = 0; i < 1337; i++)
            list.add("bleep");
        boolean test = true;
        for (int i = 0; i < 1337; i++)
            if (list.getNext() == null) test = false;
        assertTrue(test);
    }
    @Test
    public void hasNextUntilListIsOver()
    {
        for (int i = 0; i < 1337; i++)
            list.add("bleep");
        for (int i = 0; i < 1337; i++)
        {
            if (!list.hasNext()) assertTrue(false);
            list.getNext();
        }
        assertTrue(!list.hasNext());
    }
    @Test
    public void listCanBeRepeatedIfReset()
    {
        list.add("apples");
        list.add("oranges");
        list.add("pickles");
        list.add("tommytoes");
        assertTrue(list.getNext().equals("apples") &&
                list.getNext().equals("oranges") &&
                list.getNext().equals("pickles") &&
                list.getNext().equals("tommytoes") &&
                list.getNext() == null);
        list.reset();
        assertTrue(list.getNext().equals("apples") &&
                list.getNext().equals("oranges") &&
                list.getNext().equals("pickles") &&
                list.getNext().equals("tommytoes") &&
                list.getNext() == null);
    }
    @Test
    public void listIsResetted()
    {
        list.add("first");
        for (int i = 0; i < 1337; i++)
            list.add("bleep");
        for (int i = 0; i < 478; i++)
            list.getNext();
        list.reset();
        assertTrue(list.getNext().equals("first"));
    }

}
