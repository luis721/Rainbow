package rainbow;

/**
 *
 * @author mlcarcamo
 */
public class Rainbow {

    private final int q;
    private final int v1;
    private final int o1;
    private final int o2;
    private final char[] seed;
    private final PublicKey pk;
    private final PrivateKey sk;

    public Rainbow(int q, int v1, int o1, int o2) {
        this.q = q;
        // CREAR CAMPO Fq
        this.v1 = v1;
        this.o1 = o1;
        this.o2 = o2;
        this.seed = seed(256);
        AffineMap S = createS(seed);
        AffineMap T = createT(seed);
        RainbowMap F = new RainbowMap(); //  TOOD
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

    private AffineMap createT(char[] seed) {
        // TODO
        return null;
    }

    private AffineMap createS(char[] seed) {
        // TODO
        return null;
    }

    public PublicKey getPk() {
        return pk;
    }

    public PrivateKey getSk() {
        return sk;
    }

    public int m() {
        return o1 + o2;
    }

    public int n() {
        return o1 + o2 + v1;
    }

}
