package rainbow;

import java.util.HashMap;

/**
 *
 * @author mlcarcamo
 */
public class Layer {

    private HashMap<Integer, RainbowPolynomial> P;

    public Layer(Rainbow R, int index) {
        if (index == 1) {
            // creación de los polinimios de la capa
            for (int i = R.v(1) + 1; i <= R.v(2); i++) {
                RainbowPolynomial RP = new RainbowPolynomial(R, RainbowPolynomial.Layer.ONE);
                P.put(i, RP);
            }
        } else { // Layer 2

        }
    }
    
    // RETURNS  Qk del i-ésimo polinomio
    public Matrix Q(int i, int k){
        return this.P.get(i).Q(k);
    }

}
