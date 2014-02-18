
package tiralabra.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Custom writer class for writing Angle Elimination stats.
 * @author Pavel
 */
public class StatWriter extends FileWriter {

    public StatWriter(File file, boolean b) throws IOException {
        super(file, b);
    }
/**
 * Writes results of Angle Elimination algorithm.
 * @param allAngles All angles present in the field.
 * @param reducedAngles Reduced amount of angles after flattening.
 * @throws IOException Causes exceptions.
 */
    public void writeAngleEliminations(int allAngles, int reducedAngles) throws IOException
    {
        append("" + allAngles + " " + reducedAngles);
        append(System.getProperty("line.separator"));
    }
}
