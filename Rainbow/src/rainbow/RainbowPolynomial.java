package rainbow;

/**
 *
 * @author mlcarcamo
 */
public class RainbowPolynomial {

    private Rainbow R;
    private final short[][] M;

    public RainbowPolynomial(Rainbow R, int k) {
        this.R = R;
        int l = getLayer(k);
        this.M = new short[R.n()][R.n()];
        // Random alpha values
        for (int j = 0; j < R.v(l); j++) {
            for (int i = 0; i < j; i++) {
                M[i][j] = R.randomFieldItem();
            }
        }
        // Random beta values
        for (int i = R.v(l); i < R.v(l) + 1; i++) {
            for (int j = 0; j < R.v(l + 1); j++) {
                M[i][j] = R.randomFieldItem();
            }
        }
    }

    /**
     * Given an index k, returns the layer of the current polynomial.
     *
     * @param k
     * @return
     */
    private int getLayer(int k) {
        if (k < R.v(2)) {
            return 1;
        }
        return 2;
    }

}
