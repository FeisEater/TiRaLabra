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
import tiralabra.util.Tools;

/**
 *
 * @author Pavel
 */
public class PointTest {
    
    public PointTest() {
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
    public void angleIsCalculatedCorrectly()
    {
        Point center = new Point(0,0);
        for (double d = -Math.PI; d <= 3 * Math.PI; d += 0.1)
        {
            center.setLeft(new Point(32 * Math.cos(d), 32 * Math.sin(d)));
            center.setRight(new Point(32 * Math.cos(d + Math.PI / 2), 32 * Math.sin(d + Math.PI / 2)));
            double angle = Tools.round(center.getAngle());
            if (angle != Tools.round(Math.PI / 2))  assertTrue(false);
        }
        assertTrue(true);
    }
}
