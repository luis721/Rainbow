package utils;

/**
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

    public FullMatrix UT() {
        int co = this.cols();
        int ro = this.rows();
        FullMatrix Mat = new FullMatrix(this.F, ro, co);
        for (int i = 0; i < ro; i++) {
            for (int j = 0; j < co; j++) {
                Mat.setElement(i, j, this.getElement(i, j));
            }
        }
        for (int k = 0; k < co; k++) {
            for (int i = k + 1; i < ro; i++) {
                int factor = this.F.div(Mat.getElement(i, k), Mat.getElement(k, k));
                for (int j = k; j < co; j++) {
                    Mat.setElement(i, j, this.F.add(Mat.getElement(i, j), this.F.mult(Mat.getElement(k, j), factor)));
                }
            }
        }
        return Mat;
    }

    @Override
    public int rows() {
        int ro = 0;
        for (int i = 0; i < this.r; i++) {
            ro += this.M[i * this.r].rows();
        }
        return ro;
    }

    @Override
    public int cols() {
        int co = 0;
        for (int i = 0; i < this.c; i++) {
            co += this.M[i].cols();
        }
        return co;
    }

}
