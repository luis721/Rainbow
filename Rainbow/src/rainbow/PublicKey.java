package rainbow;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import utils.FullMatrix;

/**
 * Class representing the public key for Rainbow.
 *
 * @author mlcarcamo
 */
public class PublicKey {

    private final FullMatrix MP1;
    private final FullMatrix MP2;

    /**
     * Creates a public key based in the rainbow instance.
     * <br>
     * This public key is represented as two matrices MP1 and MP2.
     *
     * @param RM Rainbow Central Map
     * @param Sp Upper-left submatrix of the invertible affine map S.
     */
    public PublicKey(RainbowMap RM, FullMatrix Sp) {
        FullMatrix MQ1 = RM.LayerOne().MQ();
        FullMatrix MQ2 = RM.LayerTwo().MQ();
        // MP1 = MQ1 + S * MQ2.
        this.MP1 = MQ1.add(Sp.mult(MQ2));
        // MP2 = MQ2.
        this.MP2 = MQ2;
    }

    /**
     *
     * @return First matrix of the public key.
     */
    public FullMatrix MP1() {
        return this.MP1;
    }

    /**
     *
     * @return Second matrix of the public key.
     */
    public FullMatrix MP2() {
        return this.MP2;
    }

    /**
     * This string representation is the representation of the two matrices MP1
     * and MP2 separated by an EOL.
     *
     * @return String representation of the public key.
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder(MP1.toString());
        b.append('\n');
        b.append(MP2.toString());
        return b.toString();
    }

    /**
     * Writes the string representation of the public key in a given file.
     *
     * @param file Route of the file where the public key will be stores.
     * @throws IOException
     */
    public void writeToFile(String file) throws IOException {
        File f = new File(file);
        try ( FileWriter w = new FileWriter(f)) {
            w.write(this.toString());
        }
    }
}
