package rainbow;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import utils.Field;
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

    private int getElement(int i, int j) {
        if (i < 0 || j < 0) {
            throw new IllegalArgumentException("Index must not be non-negative.");
        }
        if (i >= Parameters.M) {
            throw new IllegalArgumentException("Row index must not me equal to or exceed " + Parameters.M);
        }
        if (i < Parameters.O1) {
            return this.MP1.getElement(i, j);
        } else {
            return this.MP2.getElement(i - Parameters.O1, j);
        }
    }

    /**
     * Checks wheter if a given signature z is valid for a hashed document h
     *
     * @param z Signature
     * @param h Hash for the document
     * @return True or false, depending on whether if P(z) == h.
     */
    public boolean isValid(int[] z, int[] h) {
        int[] v = this.eval(z);
        int i = 0;
        while (i < v.length) {
            if (v[i] != h[i]) {
                return false;
            }
            i++;
        }
        return true;
    }

    private int[] eval(int[] z) {
        Field F = Parameters.F;
        int[] r = new int[Parameters.M];
        Arrays.setAll(r, i -> 0);
        for (int k = 0; k < Parameters.M; k++) {
            int col = 0;
            for (int i = 0; i < Parameters.N; i++) {
                for (int j = i; j < Parameters.N; j++) {
                    r[k] = F.add(r[k], F.mult(this.getElement(k, col), F.mult(z[i], z[j])));
                    col = col + 1;
                }
            }
        }
        return r;
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
