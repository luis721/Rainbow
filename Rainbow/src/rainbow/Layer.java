package rainbow;

import utils.FullMatrix;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents the layers in the Rainbow scheme.
 *
 * @author mlcarcamo
 */
public class Layer {

    // Stores the polynomials in form <key, value>
    // where the key is the polynomial index v1 + 1 <= k <= n
    private final int index;
    private final Map<Integer, RainbowPolynomial> P;
    private final FullMatrix MQ;

    /**
     * Creates an instance of a Rainbow layer.
     *
     * @param R Rainbow key pair generator instance.
     * @param T
     * @param index Index of the layer. This may be 1 or 2.
     */
    public Layer(RainbowKeyPairGenerator R, AffineMapT T, int index) {
        // Check if layer index is indeed valid
        if (index <= 0 || index > 2) {
            throw new IllegalArgumentException("Invalid layer index.");
        }
        this.index = index;
        // The hash map will store the polynomials that belong to the layer.
        this.P = new TreeMap<>();
        // Contains the starting index of the polynomials in the layer
        int delta = Parameters.v(index) + 1;
        this.MQ = new FullMatrix(Parameters.F, Parameters.o(index), Math.floorDiv(Parameters.N * (Parameters.N + 1), 2));
        // Creation of the polynomials of the layer
        for (int i = Parameters.v(index) + 1; i <= Parameters.v(index + 1); i++) {
            P.put(i, new RainbowPolynomial(R, T, i));
        }
        // Creation of matrix MQ for the layer.
        // Each matrix Q of the polynomials is inserted in a matrix MQ.
        // The way this is done is described in the report.
        //RainbowPolynomial RP; // Avoiding temporal object creation in each iter
        assert ((this.P.size() == Parameters.O1 && index == 1) || (this.P.size() == Parameters.O2 && index == 2));
        for (int f = 0; f < this.P.size(); f++) {
            int c = 0;
            RainbowPolynomial RP = this.P.get(f + delta);
            for (int i = 0; i < Parameters.V1; i++) {//Q1||Q2
                for (int j = i; j < Parameters.V1; j++) {
                    assert MQ.getElement(f, c) == 0;
                    MQ.setElement(f, c, RP.Q(1).getElement(i, j));
                    c++;
                }
                for (int j = 0; j < Parameters.O1; j++) {
                    assert MQ.getElement(f, c) == 0;
                    MQ.setElement(f, c, RP.Q(2).getElement(i, j));
                    c++;
                }
                for (int j = 0; j < Parameters.O2; j++) {
                    assert MQ.getElement(f, c) == 0;
                    MQ.setElement(f, c, RP.Q(3).getElement(i, j));
                    c++;
                }
            }
            for (int i = 0; i < Parameters.O1; i++) {//Q5||Q6
                for (int j = i; j < Parameters.O1; j++) {
                    assert MQ.getElement(f, c) == 0;
                    MQ.setElement(f, c, RP.Q(5).getElement(i, j));
                    c++;
                }
                for (int j = 0; j < Parameters.O2; j++) {
                    assert MQ.getElement(f, c) == 0;
                    MQ.setElement(f, c, RP.Q(6).getElement(i, j));
                    c++;
                }
            }
            for (int i = 0; i < Parameters.O2; i++) {//Q9
                for (int j = i; j < Parameters.O2; j++) {
                    assert MQ.getElement(f, c) == 0;
                    MQ.setElement(f, c, RP.Q(9).getElement(i, j));
                    c++;
                }
            }
        }
    }

    /**
     *
     * @param y is a vector of vl elements. Where l is the layer index;
     * @return Coefficient matrix A for the central map inversion
     */
    public int[][] coefficientMatrix(int[] y) {
        // A is a ol x o1 matrix
        // ol is the number of polynomials in the layer
        int ol = this.P.size();
        int beta;
        int[][] A = new int[ol][ol];
        int delta = Parameters.v(index);
        for (int k = 0; k < ol; k++) {
            for (int j = 0; j < ol; j++) {
                int s = 0;
                for (int i = 0; i < Parameters.v(index); i++) {
                    beta = this.P.get(Parameters.v(index) + 1 + k).getBeta(i, delta + j);
                    s = Parameters.F.add(s, Parameters.F.mult(beta, y[i]));
                }
                A[k][j] = s;
            }
        }
        return A;
    }

    /**
     * Calculates for vector (column matrix) of the linear system A*y = b;
     * Recall b = x - c.
     *
     * @param x
     * @param y Already known y values.
     * @return Constant part. i.e.: the b in A*y=b.
     */
    public int[] constantPart(int[] x, int[] y) {
        int[] b = new int[Parameters.O1];
        for (int k = 0; k < Parameters.o(index); k++) {
            int r = Parameters.v(index) + 1 + k;
            b[k] = Parameters.F.add(x[k], c(y, r));
        }
        return b;
    }

    private int c(int[] y, int k) {
        int ck = 0;
        int alfa;
        for (int j = 0; j < Parameters.v(index); j++) {
            for (int i = 0; i < j; i++) {
                alfa = this.P.get(k).getAlpha(i, j);
                ck = Parameters.F.add(ck, Parameters.F.mult(alfa, Parameters.F.mult(y[i], y[j])));
            }
        }
        return ck;
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
