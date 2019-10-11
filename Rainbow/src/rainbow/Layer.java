package rainbow;

import utils.FullMatrix;
import java.util.HashMap;
import java.util.Map;
import utils.Matrix;

/**
 * Represents the layers in the Rainbow scheme.
 *
 * @author mlcarcamo
 */
public class Layer {

    // Stores the polynomials in form <key, value>
    // where the key is the polynomial index v1 + 1 <= k <= n
    private final HashMap<Integer, RainbowPolynomial> P;
    private final FullMatrix MQ;

    /**
     * Creates an instance of a Rainbow layer.
     *
     * @param R Rainbow instance.
     * @param index Index of the layer. This may be 1 or 2.
     */
    public Layer(Rainbow R, int index) {
        // Check if layer index is indeed valid
        if (index <= 0 || index > 2) {
            throw new IllegalArgumentException("Invalid layer index.");
        }
        // The hash map will store the polynomials that belong to the layer.
        this.P = new HashMap<>();
        // Contains the starting index of the polynomials in the layer
        int delta = R.v(index) + 1;
        RainbowPolynomial RP; // Avoiding temporal object creation in each iter
        this.MQ = new FullMatrix(R.GF(), R.o(index), (R.n() * (R.n() + 1)) / 2);
        // Creation of the polynomials of the layer
        for (int i = R.v(index) + 1; i <= R.v(index + 1); i++) {
            RP = new RainbowPolynomial(R, index);
            P.put(i, RP);
        }
        // Creation of matrix MQ for the layer.
        // Each matrix Q of the polynomials is inserted in a matrix MQ.
        // The way this is done is described in the report.
        int j;
        for (int f = 0; f < this.P.size(); f++) {
            int c = 0;
            RP = this.P.get(f + delta);
            for (int i = 0; i < R.v(1); i++) {//Q1||Q2
                for (j = i; j < R.v(1); j++) {
                    assert MQ.getElement(f, c) == 0;
                    MQ.setElement(f, c, RP.Q(1).getElement(i, j));
                    c++;
                }
                for (j = 0; j < R.o(1); j++) {
                    assert MQ.getElement(f, c) == 0;
                    MQ.setElement(f, c, RP.Q(2).getElement(i, j));
                    c++;
                }
            }
            for (int i = 0; i < R.v(1); i++) {//Q3
                for (j = 0; j < R.o(2); j++) {
                    assert MQ.getElement(f, c) == 0;
                    MQ.setElement(f, c, RP.Q(3).getElement(i, j));
                    c++;
                }
            }
            for (int i = 0; i < R.o(1); i++) {//Q5||Q6
                for (j = i; j < R.o(1); j++) {
                    assert MQ.getElement(f, c) == 0;
                    MQ.setElement(f, c, RP.Q(5).getElement(i, j));
                    c++;
                }
                for (j = 0; j < R.o(2); j++) {
                    assert MQ.getElement(f, c) == 0;
                    MQ.setElement(f, c, RP.Q(6).getElement(i, j));
                    c++;
                }
            }
            for (int i = 0; i < R.o(2); i++) {//Q9
                for (j = i; j < R.o(2); j++) {
                    assert MQ.getElement(f, c) == 0;
                    MQ.setElement(f, c, RP.Q(9).getElement(i, j));
                    c++;
                }
            }
        }
    }

    /**
     *
     * @return The matrix MQ of the layer.
     */
    public FullMatrix MQ() {
        return this.MQ;
    }

    /**
     *
     * @return Representación hexadecimal de cada uno de los polinomios de la
     * capa Cada polinomio va en una linea.
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        RainbowPolynomial polinomio;
        for (Map.Entry<Integer, RainbowPolynomial> entry : P.entrySet()) {
            polinomio = entry.getValue();
            b.append(polinomio.toString());
            //b.append('\n');
        }
        return b.toString();
    }

}
