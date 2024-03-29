package rainbow;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class represents the private key used in the Rainbow scheme.
 *
 * @author mlcarcamo
 */
public class PrivateKey {

    /*
    T is the invertible affine map T.
     */
    private final AffineMapT T;

    /*
    S is the invertible affine map S.
     */
    private final AffineMapS S;

    /*
    F is the Rainbow central map F.
     */
    private final RainbowMap F;

    /**
     * Creates an instance of a Rainbow Private Key.
     *
     * @param F Rainbow central map.
     * @param T Affine invertible map T.
     * @param S Affine invertible map S.
     */
    public PrivateKey(RainbowMap F, AffineMapT T, AffineMapS S) {
        this.F = F;
        this.T = T;
        this.S = S;
    }

    /**
     *
     * @return Invertible affine map S.
     */
    public AffineMapS getS() {
        return S;
    }

    /**
     *
     * @return Invertible affine map T.
     */
    public AffineMapT getT() {
        return T;
    }

    /**
     *
     * @return Rainbow central map F.
     */
    public RainbowMap getF() {
        return F;
    }

    /**
     * Signs an already hashed document d. Thus h = H(d). h must be an 1D-array
     * of m items.
     *
     * @param h Hashed document of size m.
     * @return Signature for the hashed document.
     */
    public int[] signature(int[] h) {
        // asserts the document size is valid
        if (h.length != Parameters.M) {
            throw new IllegalArgumentException("The size of the document must be " + Parameters.M);
        }
        // x = S*h;
        int[] x = this.S.eval(h);
        // y such that F(y)= x.
        int[] y = this.F.inverse(x);
        // z = T\y.
        return this.T.inverse().eval(y);
    }

    /**
     * Stores the representation of the private key into a file.
     *
     * The first line * is the string representation for the invertible affine
     * map S, which is the string representing the submatrix Sp.
     * <br>
     * The second one is the string representation for the invertible affine map
     * T, which is the concatenation of the string representation for the three
     * submatrices that form this map. i.e.: str(T1) || str(T2) || str(T3).
     * <br>
     * And finally, the remaining part of the file will be the string
     * representation of the central map F. This is, the string representation
     * for each of the m rainbow polynomials.
     *
     * @throws IOException
     */
    /**
     * Stores the representation of the private key into a file.
     *
     * The first line * is the string representation for the invertible affine
     * map S, which is the string representing the submatrix Sp.
     * <br>
     * The second one is the string representation for the invertible affine map
     * T, which is the concatenation of the string representation for the three
     * submatrices that form this map. i.e.: str(T1) || str(T2) || str(T3).
     * <br>
     * And finally, the remaining part of the file will be the string
     * representation of the central map F. This is, the string representation
     * for each of the m rainbow polynomials.
     *
     * @param file Route for the file where the key will be stored.
     * @throws IOException
     */
    public void writeToFile(String file) throws IOException {
        File f = new File(file);
        FileWriter w = new FileWriter(f);
        w.write(this.S.toString());
        w.write('\n');
        w.write(this.T.toString());
        w.write('\n');
        w.write(this.F.toString());
        w.close();
    }

}
