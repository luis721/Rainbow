package rainbow;

import utils.Field;
import utils.FullMatrix;

/**
 * Represents the invertible affine map S.
 *
 * @author fabia
 */
public class AffineMapS {

    private final FullMatrix Sp;
    private final Field F;

    /**
     * Creates a random invertible affine map (S) of size m x m.
     * <br>
     * This is defined as an upper triangular matrix where the diagonal elements
     * are all ones. And the upper left-most block of size o1 x o2 is the matrix
     * <b>Sp</b>, which is generated randomly.
     * <br>
     * S = [I Sp; 0 I].
     * <br>
     *
     * @param R Rainbow instance used to generate the random elements of the
     * matrix.
     */
    public AffineMapS(Rainbow R) {
        this.Sp = new FullMatrix(R, R.o(1), R.o(2));
        this.F = R.GF();
    }

    /**
     * Retrieves the upper left-most o1 x o2 submatrix of the affine map.
     *
     * @return The matrix that forms the upper left-most block of the matrix
     * representing the affine map.
     */
    public FullMatrix S1() {
        return this.Sp;
    }

    /**
     * The inverse of the affine map S, given the way it's defined, this is the
     * same affine map. i.e.: inverse(S) = S.
     *
     * @return The inverse of this affine map.
     */
    public AffineMapS inverse() {
        return this;
    }

    /**
     * Calculates S(x).
     *
     * @param h
     * @return S(x).
     */
    public int[] eval(int[] h) {
        int[] x = new int[Parameters.M];
        // given the form of S, this is multiplying the diagonal
        // by h.
        for (int i = 0; i < Parameters.M; i++) {
            x[i] = h[i];
        }
        int j;
        for (int i = 0; i < Parameters.O1; i++) {
            j = Parameters.O1;
            while (j < Parameters.O2) {
                x[i] = F.add(x[i], F.mult(Sp.getElement(i, j), h[j]));
                j++;
            }
        }
        return x;
    }

    /**
     * This method returns an string representing the affine map.
     * <br>
     * Given it's definition, this string will be exactly the same definition as
     * for Sp, given that only with the information of Sp it's possible to
     * rebuild the affine map. Recall that all other elements -besides the main
     * diagonal- are all zeros.
     *
     * @return String representing the affine map.
     *
     */
    @Override
    public String toString() {
        return this.Sp.toString();
    }

}
