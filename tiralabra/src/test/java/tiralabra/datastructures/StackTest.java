package tiralabra.datastructures;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pavel
 */
public class StackTest {
    private Stack<Integer> stack;
    
    @Before
    public void setUp() {
        stack = new Stack<>();
        stack.add(2);
        stack.add(3);
        stack.add(11);
        stack.add(1);
    }
    
    @Test
    public void addsAndPopsLikeAGoodStack()
    {
        assertTrue(stack.pop() == 1 &&
                stack.pop() == 11 &&
                stack.pop() == 3 &&
                stack.pop() == 2);
    }
    @Test
    public void ifPoppedTooMuchReturnNull()
    {
        for (int i = 0; i < 10; i++)
            stack.pop();
        assertTrue(stack.pop() == null);
    }
    @Test
    public void ifPoppedTooMuchIsEmpty()
    {
        for (int i = 0; i < 10; i++)
            stack.pop();
        assertTrue(stack.isEmpty());
    }
    @Test
    public void knowsIfNotEmpty()
    {
        assertTrue(!stack.isEmpty());
    }
    @Test
    public void ifPoppedTooLittleIsntEmpty()
    {
        for (int i = 0; i < 2; i++)
            stack.pop();
        assertTrue(!stack.isEmpty());
    }
}
