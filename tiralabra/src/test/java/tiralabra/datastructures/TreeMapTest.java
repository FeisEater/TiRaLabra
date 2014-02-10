
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
public class TreeMapTest {
    private TreeMap<String, String> map;
    private class StringComparator implements Comparator
    {
        @Override
        public int compare(Object o1, Object o2)
        {
            String s1 = (String)o1;
            String s2 = (String)o2;
            return s1.compareTo(s2);
        }
    }
    public TreeMapTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        map = new TreeMap<>(new StringComparator());
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void stuffCanBePutAndGotten()
    {
        assertTrue(map.get("key1").equals("value1") &&
                map.get("key2").equals("value2") &&
                map.get("key3").equals("value3"));
    }
    @Test
    public void ifReputOverwrite()
    {
        map.put("key2", "changedvalue");
        assertTrue(map.get("key1").equals("value1") &&
                map.get("key2").equals("changedvalue") &&
                map.get("key3").equals("value3"));
    }
    @Test
    public void returnsNullIfInvalidKey()
    {
        assertTrue(map.get("stupidkey") == null);
    }
}
