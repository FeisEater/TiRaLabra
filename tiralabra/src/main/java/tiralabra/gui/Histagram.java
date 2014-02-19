
package tiralabra.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * Graphical representation of AngleElimination algorithm's stats.
 * @author Pavel
 */
public class Histagram extends JPanel implements Runnable {
/** Amount of columns in a histagram. */
    private final int precision = 100;
    private final int border = 32;
    
    private JFrame frame;
/**
 * Fetches Angle Elimination stats and distributes it by frequency.
 * @return Array of amount of occurences per column.
 */
    public int[] fetchStats()
    {
        Scanner sc = getFile();
        if (sc == null) return null;
        
        int total = 0;
        int[] count = new int[precision];
        while (sc.hasNextLine())
        {
            double ratio = getRatio(sc.nextLine());
            
            if (ratio == 1) count[precision - 1]++;
            else    count[(int)Math.floor(ratio * precision)]++;
            
            total++;
        }
        System.out.println("Total: " + total);
        return count;
    }
/**
 * Exception-safe way to retrtieve a file.
 * @return Scanner object with a correct file.
 */
    public Scanner getFile()
    {
        try {
            return new Scanner(new File("stats.txt"));
        }   catch (Throwable e)
        {
            System.out.println(e);
            return null;
        }
    }
/**
 * Calculates a ratio of the data in the line in the file.
 * @param line line of the stat file.
 * @return calculated ratio.
 */
    public double getRatio(String line)
    {
        String[] str = line.split(" ");
        double a = (double)Integer.parseInt(str[1]);
        double b = (double)Integer.parseInt(str[0]);
        return a / b;
    }
    @Override
    public void run()
    {
        frame = new JFrame("histagrammi");
        frame.setPreferredSize(new Dimension(640, 480));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
    }
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        int[] columns = fetchStats();
        int max = getMax(columns);

        boolean toggle = true;
        double width = (double)(frame.getWidth() - 2 * border) / (double)precision;
        int maxheight = frame.getHeight() - 32 - 2 * border;
        for (int i = 0; i < columns.length; i++)
        {
            g.setColor(toggle ? Color.BLUE : Color.green);
            int height = (int)(maxheight * ((double)columns[i] / (double)max));
            g.fill3DRect(border + (int)(i * width), 
                    frame.getHeight() - border - 32 - height, 
                    (int)width, height, true);
            toggle = !toggle;
        }
        
        drawXAxisText(g);
    }
/**
 * Finds the biggest integer in the array.
 * @param array Integer array
 * @return biggest integer in the array.
 */
    public int getMax(int[] array)
    {
        int max = 0;
        for (int i = 0; i < array.length; i++)
            if (array[i] > max)   max = array[i];
        return max;
    }
/**
 * Draws text below histagram
 * @param g Graphics object.
 */
    public void drawXAxisText(Graphics g)
    {
        g.setColor(Color.black);
        for (int i = 0; i <= 10; i++)
            g.drawString("" + i*10,
                    border - 8 + i * (frame.getWidth() - 2 * border) / 10,
                    frame.getHeight() - border - 16);
    }
}
