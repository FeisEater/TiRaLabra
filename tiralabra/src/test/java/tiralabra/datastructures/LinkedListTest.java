/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void addsStuff()
    {
        LinkedList list = new LinkedList();
        list.add("Hello");
        list.add("world");
        list.add("whats");
        list.add("up!");
        System.out.println(list);
        assertTrue(list.toString().equals("[Hello, world, whats, up!]"));
    }
}
