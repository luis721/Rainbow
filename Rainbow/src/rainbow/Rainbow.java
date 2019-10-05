package rainbow;

import java.security.SecureRandom;
import org.bouncycastle.pqc.math.linearalgebra.GF2mField;

/**
 *
 * @author mlcarcamo
 */
public class Rainbow {

    private final int q;
    private final int[] v;
    private final int[] o;
    private final byte[] seed;
    private final PublicKey pk;
    //private final PrivateKey sk;
    private Matrix[] T;
    private Matrix Sp;
    private Matrix[] QL1;
    private final GF2mField GF;

    public Rainbow(int q, int v1, int o1, int o2) {
        this.q = q;
        this.GF = new GF2mField(2 ^ q);
        this.v = new int[]{v1, o1 + v1, v1 + o1 + o2};
        this.o = new int[]{o1, o2};
        this.seed = seed();
        createS();
        createT();
        RainbowMap F = new RainbowMap(this); //  TOOD
        //this.sk = new PrivateKey(F, T, S);
        this.pk = new PublicKey(F,this.Sp);
        this.QL1 = new Matrix[6];
    }

    /**
     * Returns seed of the give bits size.
     *
     * @param size
     * @return
     */
    private byte[] seed() {
        SecureRandom sr = new SecureRandom();
        byte[] rndBytes = sr.generateSeed(32);
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
    public short randomFieldItem() {
        // Usar seed para generar.
        // TODO TODO TODO TODO TODO
        return 0;
    }

    /**
     * Creates an invertible affine map of size m x m.
     *
     * @param seed
     * @return
     */
    private void createS() {
        this.Sp = new Matrix(this, o(1), o(2)); // S'
    }

    public Matrix T(int index){
        return this.T[index++];
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

}
