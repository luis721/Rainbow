package rainbow;

import org.bouncycastle.pqc.math.linearalgebra.GF2mField;

/**
 *
 * @author fabia
 */
public class Matrix {

    private long[][] elements;
    private int rows, columns;

    public Matrix(Matrix matrix) {
        rows = matrix.getRows();
        columns = matrix.getColumns();

        elements = new long[rows][columns];
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < columns; y++) {
                elements[x][y] = matrix.getElement(x, y);
            }
        }
    }

    public Matrix(long[][] matrix) {
        rows = matrix.length;
        columns = matrix[0].length;

        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < columns; y++) {
                elements[x][y] = matrix[x][y];
            }
        }
    }

    public Matrix(Rainbow R, int rows, int cols) {
        // TODO RETURN RANDOM MATRIX BASED IN GF
        this.elements = new long[rows][cols];
    }

    public void setElement(int x, int y, long value) {
        elements[x][y] = value;
    }

    public long getElement(int x, int y) {
        return elements[x][y];
    }
    
    public Matrix add(Matrix B){
        return this; // TODO ADDS THIS TO B
    }

    public Matrix transpose() {
        long[][] transposedMatrix = new long[columns][rows];
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < columns; y++) {
                transposedMatrix[y][x] = elements[x][y];
            }
        }
        return new Matrix(transposedMatrix);
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Matrix UT() {
        return this;
    }
    
    public Matrix mult(Matrix B){
        return this; // TODO RETURN this * B;
    }

    @Override
    public boolean equals(Object matrix) {
        if (!(matrix instanceof Matrix)) {
            return false;
        }

        if (((Matrix) matrix).getRows() != rows || ((Matrix) matrix).getColumns() != columns) {
            return false;
        }

        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < columns; y++) {
                if (((Matrix) matrix).getElement(x, y) != elements[x][y]) {
                    return false;
                }
            }
        }
        return true;
    }
}
