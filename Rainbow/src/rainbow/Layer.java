package rainbow;

import utils.FullMatrix;
import java.util.HashMap;
import java.util.Map;
import utils.Field;
import utils.Matrix;

/**
 * Represents the layers in the Rainbow scheme.
 *
 * @author mlcarcamo
 */
public class Layer {

    // Stores the polynomials in form <key, value>
    // where the key is the polynomial index v1 + 1 <= k <= n
    private final int index;
    private final HashMap<Integer, RainbowPolynomial> P;
    private final FullMatrix MQ;
    private final Field F;

    /**
     * Creates an instance of a Rainbow layer.
     *
     * @param R Rainbow instance.
     * @param index Index of the layer. This may be 1 or 2.
     */
    public Layer(Rainbow R, int index) {
        this.F = R.GF();
        // Check if layer index is indeed valid
        if (index <= 0 || index > 2) {
            throw new IllegalArgumentException("Invalid layer index.");
        }
        this.index = index;
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
     * @param y is a vector of vl elements. Where l is the layer index;
     * @return Coefficient matrix A for the central map inversion
     */
    public Matrix coefficientMatrix(int[] y) {
        // A is a ol x o1 matrix
        // ol is the number of polynomials in the layer
        int ol = this.P.size();
        Matrix A = new FullMatrix(F, ol, ol + 1);
        for (int k = 0; k < ol; k++) {
            for (int j = 0; j < ol; j++) {
                int s = 0;
                for (int i = 0; i < Parameters.v(index); i++) {
                    s = F.add(s, F.mult(0, y[i])); // TODO GET BETA
                }
                A.setElement(k, j, s);
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
    public Matrix constantPart(int[] x, int[] y) {
        Matrix b = new FullMatrix(F, Parameters.o(index), 1);
        for (int k = 0; k < Parameters.o(index); k++) {
            int r = Parameters.v(index) + k;
            b.setElement(k, 0, F.add(x[k], c(y, r)));
        }
        return b;
    }

    private int c(int[] y, int k) {
        int ck = 0;
        for (int j = 0; j < Parameters.v(index); j++) {
            for (int i = 0; i < j; i++) {
                ck = F.add(ck, F.mult(i, F.mult(y[i], y[j]))); // en i va alpha k(i,j)
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
