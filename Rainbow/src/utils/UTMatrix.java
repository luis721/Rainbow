package utils;

import org.bouncycastle.util.encoders.Hex;
import rainbow.RainbowKeyPairGenerator;

/**
 * This class describes upper triangular matrices.
 * <br>
 * Operations are optimized for these kind of matrices.
 * <br>
 * Operations with other type of matrices are defined as well.
 * <br>
 *
 * @author mlcarcamo
 */
public final class UTMatrix extends Matrix {

    private final int n;
    private final int V[];
    private final Field F;

    /**
     * Creates random squared matrix in upper-triangular form.
     *
     * @param R Rainbow instance used to generate the random items.
     * @param n Number of ros/columns of the matrix.
     */
    public UTMatrix(RainbowKeyPairGenerator R, int n) {
        this.n = n;
        this.V = new int[n * (n + 1) / 2];
        this.F = R.GF();
        for (int i = 0; i < n; i++) {
            int j = i;
            while (j < n) {
                this.setElement(i, j, R.randomFieldItem());
                j++;
            }
        }
    }

    /**
     * Crates an empty squared matrix in upper-triangular form.
     *
     * @param F Field to where the matrix belongs.
     * @param n
     */
    public UTMatrix(Field F, int n) {
        this.F = F;
        this.n = n;
        this.V = new int[n * (n + 1) / 2];
    }

    /**
     * Adds two upper triangular matrices. Take into account that the sum of two
     * upper triangular matrices results in another upper triangular matrix.
     *
     * @param B Matrix to which <i>this</i> is added to.
     * @return C = A + B.
     */
    public UTMatrix add(UTMatrix B) throws IllegalArgumentException {
        verifyAdd(B);
        UTMatrix C = new UTMatrix(this.F, this.n);
        int j;
        for (int i = 0; i < this.n; i++) {
            j = i;
            while (j < this.n) {
                C.setElement(i, j, F.add(this.getElement(i, j), B.getElement(i, j)));
                j++;
            }
        }
        return C;
    }

    /**
     * Adds the a Lower Triangular matrix to this matrix.
     * <br>
     * The resulting upper elements are those of the UTMatrix (this) and the
     * resulting lower elements are those of the LTMatrix. Finally, the diagonal
     * is the sum of both diagonals.
     *
     * @param B Lower Triangular Matrix to be added.
     * @return C = A + B.
     */
    public FullMatrix add(LTMatrix B) {
        verifyAdd(B);
        FullMatrix C = new FullMatrix(F, n, n);
        int j;
        for (int i = 0; i < n; i++) {
            j = 0;
            // Lower Part of C
            while (j < i) {
                C.setElement(i, j, B.getElement(i, j));
                j++;
            }
            // Diagonal. C[i][i] = A[i][i] + B[i][i]
            C.setElement(i, i, F.add(this.getElement(i, i), B.getElement(i, i)));
            // Upper Part of C
            while (j < n) {
                C.setElement(i, j, this.getElement(i, j));
                j++;
            }
        }
        return C;
    }

    /**
     * Multiplies two upper triangular matrices.
     * <br>
     * Take into account that the product of two upper triangular matrices
     * always results in another upper triangular matrix.
     *
     * @param B Matrix to be multiplied with.
     * @return C = A * B;
     */
    public UTMatrix mult(UTMatrix B) {
        verifyMult(B);
        UTMatrix C = new UTMatrix(F, this.n);
        int j;
        int k;
        int value;
        for (int i = 0; i < this.n; i++) {
            j = i;
            while (j < B.n) {
                value = 0;
                k = 0;
                while (k <= j) {
                    value = F.add(value,
                            F.mult(this.getElement(i, k), B.getElement(k, j)));
                    k++;
                }
                C.setElement(i, j, value);
            }
        }
        return C;
    }

    /**
     * Performs the multiplication of this matrix with the full matrix B.
     * <br>
     * This method is optimized in such way that innecesary multiplications
     * aren't performed. i.e.: None of the lower elements of the matrix are
     * accessed when performing the matrix multiplication.
     *
     * @param B
     * @return
     */
    public FullMatrix mult(FullMatrix B) {
        // Verify if dimensions are compatible
        verifyMult(B);
        // C = A * B, where A = this.
        FullMatrix C = new FullMatrix(F, n, B.cols());
        int j;
        int k;
        for (int i = 0; i < this.n; i++) {
            j = 0;
            while (j < B.cols()) {
                k = i;
                int value = 0;
                while (k < n) {
                    // C[i,j] += A(i,k) * B(k, j);
                    value = F.add(value, F.mult(this.getElement(i, k), B.getElement(k, j)));
                    k++;
                }
                C.setElement(i, j, value);
                j++;
            }
        }
        return C;
    }

    /**
     *
     * @return Field in which the matrix is defined.
     */
    public Field field() {
        return F;
    }

    @Override
    public void setElement(int i, int j, int value) throws IllegalArgumentException {
        if (j < i) {
            throw new IllegalArgumentException("You can't set this position in an Upper Triangular Matrix");
        }
        this.V[getPosition(i, j)] = value;
    }

    @Override
    public int getElement(int i, int j) {
        if (j < i) {
            return 0;
        }
        return this.V[getPosition(i, j)];
    }

    private int getPosition(int i, int j) {
        assert (0 <= i && i <= this.rows() && 0 <= j && j <= this.cols());
        return n * i - Math.floorDiv((i - 1) * i, 2) + (j - i);
    }

    public LTMatrix transpose() {
        return new LTMatrix(this);
    }

    @Override
    public int rows() {
        return this.n;
    }

    @Override
    public int cols() {
        return this.n;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        byte[] c = new byte[1];
        int j;
        for (int i = 0; i < n; i++) {
            j = i;
            while (j < n) {
                // assert F.isElementOfThisField(elements[i][j]);
                c[0] = (byte) getElement(i, j);
                b.append(Hex.toHexString(c));
                j++;
            }
        }
        return b.toString();
    }

}
