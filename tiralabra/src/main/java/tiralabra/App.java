package tiralabra;

import java.io.File;
import java.util.Scanner;
import tiralabra.gui.GraphicInterface;
import javax.swing.SwingUtilities;

/**
 * Starting point of the program.
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        VertexContainer vc = new VertexContainer();
        GraphicInterface g = new GraphicInterface(vc);
        SwingUtilities.invokeLater(g);
        fetchStats();
    }
    /**
     * Fetches Angle Elimination stats and distributes it by frequency.
     */
    public static void fetchStats()
    {
        Scanner sc;
        try {
            sc = new Scanner(new File("stats.txt"));
        }   catch (Throwable e)
        {
            System.out.println(e);
            return;
        }
        int total = 0;
        int[] count = new int[10];
        while (sc.hasNextLine())
        {
            String line = sc.nextLine();
            String[] str = line.split(" ");
            double ratio =
                (double)(Integer.parseInt(str[1])) / (double)(Integer.parseInt(str[0]));
            if (ratio == 1) count[9]++;
            else    count[(int)Math.floor(ratio * 10)]++;
            total++;
        }
        for (int i = 0; i < 10; i++)
            System.out.println(i + ": " + count[i]);
        System.out.println("Total: " + total);
    }
}
