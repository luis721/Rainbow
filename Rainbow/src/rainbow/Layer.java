package rainbow;

import java.util.HashMap;

/**
 *
 * @author mlcarcamo
 */
public class Layer {

    private final HashMap<Integer, RainbowPolynomial> f;
    private final int start;
    private final int end;

    /**
     * A Rainbow layer, which is composed of Rainbow Polynomials.
     * @param R
     * @param k 
     */
    public Layer(Rainbow R, int k) {
        if (k == 1) {
            this.start = R.v(1) + 1;
            this.end = R.v(2);
        } else {
            this.start = R.v(2) + 1;
            this.end = R.n();
        }
        this.f = new HashMap<>();
        for (int i = start; i < end; i++) {
            this.f.put(i, new RainbowPolynomial(R, i));
        }
    }

}
