package rainbow;

/**
 *
 * @author mlcarcamo
 */
public class Rainbow {

    private final int q;
    private final int[] v;
    private final int[] o;
    private final char[] seed;
    private final PublicKey pk;
    private final PrivateKey sk;

    public Rainbow(int q, int v1, int o1, int o2) {
        this.q = q;
        // CREAR CAMPO Fq
        this.v = new int[]{v1, o1 + v1, v1 + o1 + o2};
        this.o = new int[]{o1, o2};
        this.seed = seed(256);
        AffineMap S = createS();
        AffineMap T = createT();
        RainbowMap F = new RainbowMap(this); //  TOOD
        this.sk = new PrivateKey(F, T, S);
        this.pk = new PublicKey();
    }

    /**
     * Returns seed of the give bits size.
     *
     * @param size
     * @return
     */
    private char[] seed(int size) {
        // TODO
        return null;
    }

    /**
     * Creates an invertible affine map of size n x n.
     *
     * @param seed
     * @return
     */
    private AffineMap createT() {
        // TODO
        // Usar seed en la generación
        short[][] M = new short[n()][n()]; // MATRIZ DE n x n
        short[][] T1 = randomInvertible(v(1), o(1)); // T1
        short[][] T2 = randomInvertible(v(1), o(2)); // T2
        short[][] T3 = randomInvertible(o(1), o(2)); // T3
        // Meter T1, T2 y T3 en M
        // TODO
        return new AffineMap(M, q);
    }

    /**
     * Returns a random element in the given field Fq.
     *
     * @return
     */
    public short randomFieldItem() {
        // TODO
        // Usar seed para generar.
        return 0;
    }

    /**
     * Creates a random squared matrix of size r x c.
     *
     * @param r Number of rows
     * @param c Number of columns
     * @return
     */
    private short[][] random(int r, int c) {
        short[][] M = new short[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                M[i][j] = this.randomFieldItem();
            }
        }
        return M;
    }

    /**
     * Creates a random invertible matrix of size r x c in Fq.
     *
     * @param r
     * @param c
     * @return
     */
    private short[][] randomInvertible(int r, int c) {
        short[][] M;
        do {
            M = random(r, c);
        } while (!isInvertible(M));
        return M;
    }

    /**
     * Checks whether if the given matriz M is INVERTIBLE under Fq.
     *
     * @param M
     * @return
     */
    private boolean isInvertible(short[][] M) {
        // TODO
        return false;
    }

    /**
     * Creates an invertible affine map of size m x m.
     *
     * @param seed
     * @return
     */
    private AffineMap createS() {
        short[][] M = new short[m()][m()]; // MATRIZ DE m x m
        short[][] S = randomInvertible(o(1), o(2)); // S'
        // METER S EN M.
        // TODO
        return new AffineMap(M, q);
    }

    public PublicKey getPk() {
        return pk;
    }

    public PrivateKey getSk() {
        return sk;
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
