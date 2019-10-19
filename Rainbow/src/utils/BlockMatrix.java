package utils;

/**
 *
 * @author jdpad
 */
public class BlockMatrix extends Matrix {

    private final Matrix[] M;
    private final int r;
    private final int c;

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

    public BlockMatrix(int rows, int cols, Matrix... M) {
        this.r = rows;
        this.c = cols;
        this.M = M;
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
        while (this.M[col].cols() < j) {
            j -= this.M[col].cols();
            col++;
        }
        while (this.M[row].rows() < i) {
            i -= this.M[row].rows();
            row = row + this.c;
            k++;
        }
        return new Coordinates(i, j, col, k);
    }

    @Override
    public int rows() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int cols() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
