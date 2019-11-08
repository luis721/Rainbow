package rainbow;

import utils.FullMatrix;
import utils.Matrix;
import utils.UTMatrix;

/**
 *
 * @author mlcarcamo
 */
public class RainbowPolynomial {

    private final Matrix[] F;
    private final Matrix[] Q;
    private final int layer;

    public RainbowPolynomial(RainbowKeyPairGenerator R, int layer) {
        // Check if layer index is indeed valid
        if (layer <= 0 || layer > 2) {
            throw new IllegalArgumentException("Invalid layer index.");
        }
        // The amount of submatrices depends on wheter if the polynomial
        // belongs to the layer one or to the layer two.
        if (layer == 1) {
            this.F = new Matrix[2];
        } else {
            this.F = new Matrix[5];
        }
        this.layer = layer;
        // Matrices related to the affine map T
        AffineMapT T = R.T();
        FullMatrix T1 = T.T(1);
        FullMatrix T2 = T.T(2);
        FullMatrix T3 = T.T(3);
        // Set of six matrices for the polynomial
        this.Q = new Matrix[6];
        /* Two random submatrices for the polynomial 
         (These two random submatrices are common to both layers)*/
        // F1: Random upper triangular matrix of dimensions v1 x v1
        UTMatrix F1 = new UTMatrix(R, Parameters.v(1));
        this.F[0] = F1;
        // F2: Random matrix of dimensions v1  x o1.
        FullMatrix F2 = new FullMatrix(R, Parameters.v(1), Parameters.o(1)); // F2 of dims v1 x o1
        this.F[1] = F2;
        // -- auxiliar matrices used to reduce multiplications computation -- //
        FullMatrix A = F1.add(F1.transpose()); // A = F1 + F1T
        FullMatrix B = F2.mult(T3);            // B = F2 * T3
        FullMatrix C = A.mult(T1);             // C = A * T1
        FullMatrix D = C.add(F2);              // D = C + F2;
        // Some repeatdly used tranposed are precomputed
        FullMatrix T1T = T1.transpose(); // Transpose of T1
        FullMatrix T2T = T2.transpose(); // Transpose of T2
        // -- Layer one -- // 
        if (layer == 1) {
            // Q1 = F1.
            this.Q[0] = this.F(1);
            // Q2 = C + F2;
            this.Q[1] = D;
            // Q3 = A*T2 + B;
            this.Q[2] = A.mult(T2).add(B);
            // Q5 = UT(T1T * (F1 * T1 + F2))
            this.Q[3] = T1T.mult(F1.mult(T1).add(F2)).UT();
            // Q6 = Q2T * T2  + T1T * B;
            this.Q[4] = D.transpose().mult(T2).add(T1T.mult(B));
            // Q9 = UT(T2T  * (F1 * T2 + B))
            this.Q[5] = T2T.mult(F1.mult(T2).add(B)).UT();
        } else { // -- Layer two -- //
            // -- creación de los bloques de la matriz del polinomio -- //
            // F3: Random matrix of dimensions v1 x o2.
            this.F[2] = new FullMatrix(R, Parameters.v(1), Parameters.o(2));
            // F5: Random upper triangular matrix of dimensions o1 x o1.
            UTMatrix F5 = new UTMatrix(R, Parameters.o(1));
            this.F[3] = F5;
            // F6: Random  matrix of dimensions o1 x o2.
            FullMatrix F6 = new FullMatrix(R, Parameters.o(1), Parameters.o(2));
            F[4] = F6;
            // -- Creation of the needed auxiliar matrices
            FullMatrix T3T = T3.transpose(); // Transpose of T3
            FullMatrix E = B.add((FullMatrix) F(3));
            FullMatrix G = A.mult(T2).add(E);
            FullMatrix F5sF5T = F5.add(F5.transpose());
            // -- creación de las matrices Q -- //
            // Q1 = F1;
            this.Q[0] = this.F(1);
            // Q2 = D;
            this.Q[1] = D;
            // Q3 = A * T2 + E.
            this.Q[2] = G;
            // Q5 = UT(T1T *(F1 * T1 + F2)).
            this.Q[3] = T1T.mult(F1.mult(T1).add(F2)).UT();
            // Q6 = (D + F2T) * T2 + T1T * E + (F5 + F5T) * T3 + F6 
            this.Q[4] = T1T.mult(G).add(F2.transpose().mult(T2)).add(F5sF5T.mult(T3)).add(F6);
            // Q9 = UI( T2T * (F1 * T2 + E) + T3T * (F5 * T3 + F6) )
            this.Q[5] = T2T.mult(F1.mult(T2).add(E)).add(T3T.mult(F5.mult(T3).add(F6))).UT();
        }
    }

    /**
     *
     * @param k index of the submatrix to be retrieved.
     * @return submatrix Fk of the polynomial.
     */
    public final Matrix F(int k) {
        Matrix Re;
        switch (k) {
            case 1:
                Re = this.F[0];
                break;
            case 2:
                Re = this.F[1];
                break;
            case 3:
                Re = this.F[2];
                break;
            case 5:
                Re = this.F[3];
                break;
            case 6:
                Re = this.F[4];
                break;
            default:
                throw new IllegalArgumentException("Index no válido.");
        }
        return Re;
    }

    /**
     * Returns alpha(i,j).
     *
     * @param i
     * @param j
     * @return
     */
    public int getAlpha(int i, int j) {
        if (this.layer == 1) {
            return this.F(1).getElement(i, j);
        } else { // layer == 2
            if (i < Parameters.V1) {
                if (j < Parameters.V1) {
                    return this.F(1).getElement(i, j);
                } else {
                    return this.F(2).getElement(i, j - Parameters.V1);
                }
            } else {
                return this.F(5).getElement(i - Parameters.V1, j - Parameters.V1);
            }
        }
    }

    /**
     * Returns beta given i and j
     *
     * @param i
     * @param j
     * @return
     */
    public int getBeta(int i, int j) {
        //System.out.println("i: " + i + " j: " + j);
        if (this.layer == 1) {
            return this.F(2).getElement(i, j - Parameters.V1);
        } else { // layer == 2
            if (i < Parameters.V1) {
                return this.F(3).getElement(i, j - Parameters.V2);
            } else {
                return this.F(6).getElement(i - Parameters.V1, j - Parameters.V2);
            }
        }
    }

    /**
     *
     * @param i Index of the Q submatrix.
     * @return Submatrix Qi related to the Q matrix of the polynomial.
     */
    public Matrix Q(int i) {
        switch (i) {
            case 1:
                return this.Q[0];
            case 2:
                return this.Q[1];
            case 3:
                return this.Q[2];
            case 5:
                return this.Q[3];
            case 6:
                return this.Q[4];
            case 9:
                return this.Q[5];
            default:
                throw new IllegalArgumentException("Index no válido.");
        }
    }

    /**
     *
     * @return Representación en cadena del polinomio. Es la representación
     * hexadecimal de cada uno de sus elementos, es decir, la representación
     * hexadecimal de cada una de sus matrices.
     *
     * Las matrices se muestran una por una.
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (Matrix matrix : F) {
            if (matrix.getClass().toString().equals("UTMatrix")) {
                b.append(((UTMatrix) matrix).toString());
            } else {
                b.append(matrix.toString());
            }
        }
        return b.toString();
    }

}

