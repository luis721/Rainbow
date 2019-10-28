package utils;

/**
 * Matriz descrita por matrices bloque.
 *
 * @author jdpad
 */
public class BlockMatrix extends Matrix {

    private final Matrix[] M;
    private final int r;
    private final int c;
    private final Field F;

    private class Coordinates {

        int i;
        int j;
        int col = 0;
        int k = 0;

        public Coordinates(int i, int j, int col, int k) {
            this.i = i;
            this.j = j;
            this.col = col;
            this.k = k;
        }
    }

    /**
     *
     * @param F field
     * @param rows Number of blocks per row
     * @param cols Number of blocks per column
     * @param M List of matrices
     */
    public BlockMatrix(Field F, int rows, int cols, Matrix... M) {
        this.r = rows;
        this.c = cols;
        this.M = M;
        this.F = F;
    }

    @Override
    public int getElement(int i, int j) {
        Coordinates coor = getBlock(i, j);
        return this.M[coor.k * this.c + coor.col].getElement(coor.i, coor.j);
    }

    @Override
    public void setElement(int i, int j, int value) {
        Coordinates coor = getBlock(i, j);
        this.M[coor.k * this.c + coor.col].setElement(coor.i, coor.j, value);
    }

    /**
     * Block that contains the position (i, j)
     *
     * @param i
     * @param j
     * @return
     */
    private Coordinates getBlock(int i, int j) {
        int row = 0, col = 0, k = 0;
        while (this.M[col].cols() <= j) {
            j -= this.M[col].cols();
            col++;
        }
        while (this.M[row].rows() <= i) {
            i -= this.M[row].rows();
            row = row + this.c;
            k++;
        }
        return new Coordinates(i, j, col, k);
    }

    /**
     * Performs an RREF over the matrix.
     * <br>
     * The current matrix is read as M = A || b.
     *
     * @param y
     * @param start Starting position to fill the y array
     * @return Solution to a system of the form Ay=b.
     */
    public int[] Gauss(int[] y, int start) {
        int cols = this.cols();
        int rows = this.rows();
        FullMatrix Mat = new FullMatrix(this.F, rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Mat.setElement(i, j, this.getElement(i, j));
            }
        }
        for (int k = 0; k < cols; k++) {
            for (int i = k + 1; i < rows; i++) {
                int factor = this.F.div(Mat.getElement(i, k), Mat.getElement(k, k));
                for (int j = k; j < cols; j++) {
                    Mat.setElement(i, j, this.F.add(Mat.getElement(i, j), this.F.mult(Mat.getElement(k, j), factor)));
                }
            }
        }
        // once the RREF is done, we retrieve the last column
        for (int i = 0; i < rows; i++) {
            y[start + i] = Mat.getElement(i, cols - 1);
        }
        return y;
    }

    @Override
    public int rows() { // TODO cache this value as class attribute
        int ro = 0;
        for (int i = 0; i < this.r; i++) {
            ro += this.M[i * this.r].rows();
        }
        return ro;
    }

    @Override
    public int cols() { // TODO cache this value as class attribute
        int co = 0;
        for (int i = 0; i < this.c; i++) {
            co += this.M[i].cols();
        }
        return co;
    }

}
