package rainbow;

import java.security.SecureRandom;
import java.util.Arrays;
import org.bouncycastle.pqc.math.linearalgebra.GF2mField;

/**
 *
 * @author mlcarcamo
 */
public class Rainbow {

    private final int q;
    private final int[] v;
    private final int[] o;
    private final SecureRandom sr;
    private final PublicKey pk;
    //private final PrivateKey sk;
    private final Matrix[] T;
    private final Matrix Sp;
    private final Matrix[] QL1;
    private final GF2mField GF;

    public Rainbow(int q, int v1, int o1, int o2) {
        this.q = q;
        this.GF = new GF2mField(2 ^ q);
        this.v = new int[]{v1, o1 + v1, v1 + o1 + o2};
        this.o = new int[]{o1, o2};
        byte[] seed = seed(256); // Random seed of 256 bits
        this.sr = new SecureRandom(seed);
        this.T = new Matrix[3]; // T1, T2 and T3
        this.Sp = new Matrix(this, o(1), o(2)); // S'
        createT();
        RainbowMap F = new RainbowMap(this); //  TOOD
        //this.sk = new PrivateKey(F, T, S);
        this.pk = new PublicKey();
        this.QL1 = new Matrix[6];
    }

    public GF2mField GF() {
        return this.GF;
    }

    /**
     * Returns seed of the give bits size.
     *
     * @param size Number of bits
     * @return
     */
    private byte[] seed(int size) {
        SecureRandom srnd = new SecureRandom();
        byte[] rndBytes = srnd.generateSeed(size / 8);
        return rndBytes;
    }

    /**
     * Creates an invertible affine map of size n x n.
     *
     * @param seed
     * @return
     */
    private void createT() {
        this.T[0] = new Matrix(this, v(1), o(1)); // T1
        this.T[1] = new Matrix(this, v(1), o(2)); // T2
        this.T[2] = new Matrix(this, o(1), o(2)); // T3
    }

    /**
     * Returns a random element in the given field Fq.
     *
     * @return
     */
    public int randomFieldItem() {
        return GF.getRandomElement(this.sr);
    }

    public Matrix T(int index) {
        return this.T[--index];
    }

    public PublicKey getPk() {
        return pk;
    }

    public PrivateKey getSk() {
        // TODO
        return null;
    }

    public int v(int k) {
        return v[k - 1];
    }

    public int o(int k) {
        return o[k - 1];
    }

    public int m() {
        return o(1) + o(2);
    }

    public int n() {
        return v[2];
    }

    public static void main(String[] args) {
        Rainbow R = new Rainbow(8, 68, 48, 48);
        System.out.println(Arrays.toString(R.seed(256)));
        System.out.println(R.randomFieldItem());
        System.out.println(R.randomFieldItem());
        System.out.println(R.randomFieldItem());
        System.out.println(R.randomFieldItem());
    }
}
