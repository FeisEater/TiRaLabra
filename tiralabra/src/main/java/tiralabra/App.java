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
        fetchStats(10);
    }
    /**
     * Fetches Angle Elimination stats and distributes it by frequency.
     * @param histagram Precision of the histogramm.
     */
    public static void fetchStats(int histagram)
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
        int[] count = new int[histagram];
        while (sc.hasNextLine())
        {
            String line = sc.nextLine();
            String[] str = line.split(" ");
            double ratio =
                (double)(Integer.parseInt(str[1])) / (double)(Integer.parseInt(str[0]));
            if (ratio == 1) count[histagram - 1]++;
            else    count[(int)Math.floor(ratio * histagram)]++;
            total++;
        }
        for (int i = 0; i < histagram; i++)
            System.out.println(((double)i / (double)histagram) + "-" + ((double)(i+1) / (double)histagram) + ": " + count[i]);
        System.out.println("Total: " + total);
    }
}
