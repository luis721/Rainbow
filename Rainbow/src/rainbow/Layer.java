package rainbow;

import java.util.HashMap;

/**
 *
 * @author mlcarcamo
 */
public class Layer {

    private final HashMap<Integer, RainbowPolynomial> P;

    public Layer(Rainbow R, int index) {
        this.P = new HashMap<>();
        if (index == 1) {
            // creación de los polinimios de la capa
            for (int i = R.v(1) + 1; i <= R.v(2); i++) {
                RainbowPolynomial RP = new RainbowPolynomial(R, RainbowPolynomial.Layer.ONE);
                P.put(i, RP);
            }
        } else { // Layer 2
            for (int i = R.v(2) + 1; i <= R.n(); i++) {
                RainbowPolynomial RP = new RainbowPolynomial(R, RainbowPolynomial.Layer.TWO);
                P.put(i, RP);
            }
        }
    }

    // RETURNS  Qk del i-ésimo polinomio
    public Matrix Q(int i, int k) {
        return this.P.get(i).Q(k);
    }

}
