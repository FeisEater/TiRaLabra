/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tiralabra.util;

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
public class ToolsTest {
    
    public ToolsTest() {
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
    public void roundsCorrectly1()
    {
        assertTrue(Tools.round(1.12345678) == 1.12);
    }
    @Test
    public void roundsCorrectly2()
    {
        assertTrue(Tools.round(1.129999345) == 1.13);
    }
    @Test
    public void roundsCorrectly3()
    {
        assertTrue(Tools.round(1.12) == 1.12);
    }
    @Test
    public void roundsCorrectly4()
    {
        assertTrue(Tools.round(1.1) == 1.1);
    }
    @Test
    public void roundsCorrectly5()
    {
        assertTrue(Tools.round(1) == 1);
    }
    @Test
    public void roundsCorrectly6()
    {
        assertTrue(Tools.round(2.9999999945) == 3);
    }
}
