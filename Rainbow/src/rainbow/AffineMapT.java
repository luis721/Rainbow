package rainbow;

import utils.FullMatrix;

/**
 * Represents the invertible affine map T.
 * <br>
 * This map is defined as upper triangular matrix with an all-ones main
 * diagonal. The remaining upper part of the matrix to represent this map is
 * defined as three submatrices T1, T2 and T3.
 * <br>
 * T = [I T1 T2; 0 I T3; 0 0 I].
 *
 * @author mlcarcamo
 */
public class AffineMapT {

    private final FullMatrix T1;
    private final FullMatrix T2;
    private final FullMatrix T3;

    /**
     * Creates an invertible random affine map (T) of size n x n.
     *
     * @param R Rainbow instance used to generate the random elements in the
     * matrices T1, T2 and T3.
     */
    public AffineMapT(Rainbow R) {
        // T1 is a v1 x o1 random matrix.
        this.T1 = new FullMatrix(R, R.v(1), R.o(1));
        // T2 is a v1 x o2 random matrix.
        this.T2 = new FullMatrix(R, R.v(1), R.o(2));
        // T3 is a o1 x o2 random matrix.
        this.T3 = new FullMatrix(R, R.o(1), R.o(2));
    }

    private AffineMapT(FullMatrix T1, FullMatrix T2, FullMatrix T3) {
        this.T1 = T1;
        this.T2 = T2;
        this.T3 = T3;
    }

    /**
     * Computes the inverse of the Affine Map.
     * <br>
     * Given it's definition, this computation is pretty straightforward.
     *
     * @return The inverse of T.
     */
    public AffineMapT inverse() {
        FullMatrix T4 = T1.mult(T3).add(T2);
        return new AffineMapT(T1, T4, T3);
    }

    /**
     * Retrieves one of the three submatrices of the upper part of T.
     *
     * @param index Submatrix to be retrieved.
     * @return Submatrix T<sub>index</sub>.
     */
    public FullMatrix T(int index) {
        switch (index) {
            case 1:
                return T1;
            case 2:
                return T2;
            case 3:
                return T3;
            default:
                throw new IllegalArgumentException("Invalid index.");
        }
    }

    /**
     * String representation of the affine map T.
     * <br>
     * The representation is the concatenation of the string representation of
     * the submatrices T1, T2 and T3. i.e.: str(T1) || str(T2) || str(T3).
     * <br>
     * Recall that those are the only non-zero elements (besides the main
     * diagonal) of the affine map T, so it can be rebuilt with this
     * information.
     *
     * @return String containing the hexadecimal representation of the affine
     * map T.
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder(this.T1.toString());
        b.append(T2.toString());
        b.append(T3.toString());
        return b.toString();
    }

}
