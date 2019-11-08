package rainbow;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    public boolean isValid(int[] z, int[] h) {
        int[] v = this.eval(z);
        int i = 0;
        while (i < v.length) {
            if (v[i] == h[i]) {
                System.out.printf("v[%d] == h[%d]\n", i, i);
                i++;
            } else {
                i++;
            }
        }
        return true;
    }

    private int[] eval(int[] z) {
        Field F = Parameters.F;
        int[] r = new int[Parameters.M];
        int s;
        for (int k = 0; k < Parameters.M; k++) {
            r[k] = 0;
            int col = 0;
            // Q1 || Q2
            for (int i = 0; i < Parameters.V1; i++) {
                s = 0;
                for (int j = i; j < Parameters.V2; j++) {
                    s = F.add(s, F.mult(this.getElement(k, col), z[j]));
                    col++;
                }
                r[k] = F.add(r[k], F.mult(z[i], s));
            }
            //  Q3
            for (int i = 0; i < Parameters.V1; i++) {
                s = 0;
                for (int j = Parameters.V2; j < Parameters.N; j++) {
                    s = F.add(s, F.mult(this.getElement(k, col), z[j]));
                    col++;
                }
                r[k] = F.add(r[k], F.mult(z[i], s));
            }
            // Q5 || Q6
            for (int i = Parameters.V1; i < Parameters.V2; i++) {
                s = 0;
                for (int j = i; j < Parameters.N; j++) {
                    s = F.add(s, F.mult(this.getElement(k, col), z[j]));
                    col++;
                }
                r[k] = F.add(r[k], F.mult(z[i], s));
            }
            // Q9
            for (int i = Parameters.V2; i < Parameters.N; i++) {
                s = 0;
                for (int j = i; j < Parameters.N; j++) {
                    s = F.add(s, F.mult(this.getElement(k, col), z[j]));
                    col++;
                }
                r[k] = F.add(r[k], F.mult(z[i], s));
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
        try (FileWriter w = new FileWriter(f)) {
            w.write(this.toString());
        }
    }
}
