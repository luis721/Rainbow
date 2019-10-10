package utils;

/**
 * This class describes lower triangular matrices.
 * <br>
 * Operations are optimized for these kind of matrices.
 * <br>
 * Operations with other type of matrices are defined as well.
 *
 * @author mlcarcamo
 */
public final class LTMatrix extends Matrix {

    private final int n;
    private final int V[];
    private final Field F;

    private LTMatrix(Field F, int n) {
        this.F = F;
        this.n = n;
        this.V = new int[n * (n + 1) / 2];
    }

    /**
     * Creates a lower triangular matrix that's equal to the transpose of the
     * upper triangular matrix received as parameter. i.e.: this = transpose(B).
     *
     * @param B Upper triangular matrix.
     */
    public LTMatrix(UTMatrix B) {
        this.n = B.rows();
        this.F = B.field();
        this.V = new int[n * (n + 1) / 2];
        int j;
        for (int i = 0; i < n; i++) {
            j = 0;
            while (j <= i) {
                this.setElement(i, j, B.getElement(j, i));
                j++;
            }
        }
    }

    public LTMatrix add(LTMatrix B) {
        verifyAdd(B);
        LTMatrix C = new LTMatrix(this.F, this.n);
        int j;
        for (int i = 0; i < this.n; i++) {
            j = 0;
            while (j <= i) {
                C.setElement(i, j, F.add(this.getElement(i, j), B.getElement(i, j)));
                j++;
            }
        }
        return C;
    }

    /**
     *
     * @return
     */
    public UTMatrix transpose() {
        return new UTMatrix(this);
    }

    @Override
    public int getElement(int i, int j) {
        if (j > i) {
            return 0;
        }
        return this.V[getPosition(i, j)];
    }

    @Override
    public void setElement(int i, int j, int value) throws IllegalArgumentException {
        if (j > i) {
            throw new IllegalArgumentException("You can't set this position in a Lower Triangular Matrix");
        }
        this.V[getPosition(i, j)] = value;
    }

    private int getPosition(int i, int j) {
        return i * (i + 1) / 2 + j;
    }

    @Override
    public int rows() {
        return this.n;
    }

    @Override
    public int cols() {
        return this.n;
    }

    public Field field() {
        return F;
    }

}
