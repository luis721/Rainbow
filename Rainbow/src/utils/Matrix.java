package utils;

import org.bouncycastle.util.encoders.Hex;
import rainbow.Rainbow;

/**
 *
 * @author fabia
 */
public class Matrix {

    private final int[][] elements;
    private final int rows, cols;
    private final Field F;
    private boolean isUT;

    public Matrix(Rainbow R, int rows, int cols, boolean isUT) {
        this.isUT = isUT;
        this.rows = rows;
        this.cols = cols;
        this.elements = new int[rows][cols];
        this.F = R.GF();
        int j;
        for (int i = 0; i < rows; i++) {
            j = 0;
            if (isUT) {
                j = i;
            }
            while (j < cols) {
                this.elements[i][j] = R.randomFieldItem();
                j++;
            }
        }
    }

    public Matrix(Rainbow R, Matrix matrix) {
        this.isUT = false;
        rows = matrix.rows();
        cols = matrix.cols();
        this.F = R.GF();
        elements = new int[rows][cols];
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                elements[x][y] = matrix.getElement(x, y);
            }
        }
    }

    public Matrix(Field GF, Matrix matrix) {
        rows = matrix.rows();
        cols = matrix.cols();
        this.F = GF;
        elements = new int[rows][cols];
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                elements[x][y] = matrix.getElement(x, y);
            }
        }
    }

    public Matrix(Field GF, int[][] matrix) {
        rows = matrix.length;
        cols = matrix[0].length;
        this.F = GF;
        this.elements = matrix;
    }

    public Matrix(Rainbow R, int rows, int cols) {
        this.F = R.GF();
        this.rows = rows;
        this.cols = cols;
        this.elements = new int[rows][cols];
    }

    public Matrix(Field GF, int rows, int cols) {
        this.F = GF;
        this.rows = rows;
        this.cols = cols;
        this.elements = new int[rows][cols];
    }

    public void setElement(int x, int y, int value) {
        elements[x][y] = value;
    }

    public int getElement(int x, int y) {
        return elements[x][y];
    }

    public Matrix add(Matrix B) {
        if (this.rows != B.rows) {
            throw new IllegalArgumentException("Rows size must be the same.");
        }
        if (this.cols != B.cols) {
            throw new IllegalArgumentException("Cols size must be the same.");
        }
        Matrix result = new Matrix(F, this.rows, this.cols);
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                result.setElement(row, col,
                        F.add(this.getElement(row, col), B.getElement(row, col)));
            }
        }
        return result;
    }

    public Matrix transpose() {
        int[][] transposedMatrix = new int[cols][rows];
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                transposedMatrix[y][x] = elements[x][y];
            }
        }
        return new Matrix(this.F, transposedMatrix);
    }

    public int rows() {
        return rows;
    }

    public int cols() {
        return cols;
    }

    public Matrix UT() {
        Matrix result = new Matrix(F, this);
        for (int diagPos = 1; diagPos < Math.min(this.rows, this.cols) + 1; diagPos++) {
            int value;
            //find column with pivot, swap lines if necessary
            int colPos = diagPos - 1;
            while (colPos < this.cols && result.getElement(diagPos - 1, colPos) == 0) {
                result = findPivot(result, colPos, diagPos - 1);
                if (result.getElement(diagPos - 1, colPos) == 0) {
                    colPos++;
                }
            }
            if (colPos == this.cols) {
                colPos = diagPos - 1;
            }
            for (int rowsUnderDiagPos = diagPos; rowsUnderDiagPos < this.rows; rowsUnderDiagPos++) {
                try {
                    //set value, it will be used to set column at diagPos to zero
                    value = F.mult(result.getElement(rowsUnderDiagPos, colPos), F.inverse(result.getElement(diagPos - 1, colPos)));
                    //subtract from line, pivot will be set to zero and other values will be edited
                    for (int colsUnderDiagPos = diagPos - 1; colsUnderDiagPos < this.cols; colsUnderDiagPos++) {
                        result.setElement(rowsUnderDiagPos, colsUnderDiagPos,
                                F.add(F.mult(result.getElement(diagPos - 1, colsUnderDiagPos), value),
                                        result.getElement(rowsUnderDiagPos, colsUnderDiagPos)));
                    }
                } catch (IllegalArgumentException ex) {
                    //catched division by zero, OK, thrown by columns full of zeroes
                }
            }
        }
        //Result matrix is in row echelon form
        return result;
    }

    public Matrix mult(Matrix B) {
        Matrix result = new Matrix(F, this.rows, B.cols);
        if (this.rows == 0 || B.rows() == 0) {
            throw new IllegalArgumentException("Matrix argument is empty, "
                    + "operation cannot be performed.");
        }

        if (this.cols == 0 || B.cols() == 0) {
            throw new IllegalArgumentException("Argument matrix has empty row, "
                    + "operation cannot be performed.");
        }

        if (this.cols != B.rows()) {
            throw new IllegalArgumentException("Argument matrices cannot be multiplied, "
                    + "their dimensions are wrong.");
        }
        for (int x = 0; x < this.rows; x++) {
            for (int y = 0; y < B.cols(); y++) {
                int value = 0;
                for (int z = 0; z < this.cols; z++) {
                    value = F.add(value,
                            F.mult(this.getElement(x, z), B.getElement(z, y)));
                }
                result.setElement(x, y, value);
            }
        }
        return result;
    }

    private Matrix findPivot(Matrix matrix, int column, int row) {
        for (int x = row; x < matrix.rows(); x++) {
            for (int y = 0; y < column + 1; y++) {
                if (matrix.getElement(x, y) != 0 && y == column) {
                    return swapLines(matrix, row, x);
                }
                if (matrix.getElement(x, y) != 0) {
                    break;
                }
            }
        }
        return matrix;
    }

    private Matrix swapLines(Matrix matrix, int row1, int row2) {
        Matrix result = new Matrix(F, matrix.rows(), matrix.cols());
        for (int x = 0; x < matrix.rows(); x++) {
            if (x == row1) {
                for (int y = 0; y < matrix.cols(); y++) {
                    result.setElement(row1, y, matrix.getElement(row2, y));
                }
            }
            if (x == row2) {
                for (int y = 0; y < matrix.cols(); y++) {
                    result.setElement(row2, y, matrix.getElement(row1, y));
                }
            }
            if (x != row1 && x != row2) {
                for (int y = 0; y < matrix.cols(); y++) {
                    result.setElement(x, y, matrix.getElement(x, y));
                }
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object matrix) {
        if (!(matrix instanceof Matrix)) {
            return false;
        }

        if (((Matrix) matrix).rows() != rows || ((Matrix) matrix).cols() != cols) {
            return false;
        }

        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                if (((Matrix) matrix).getElement(x, y) != elements[x][y]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        byte[] c = new byte[1];
        int j;
        for (int i = 0; i < rows; i++) {
            j = 0;
            if (this.isUT) {
                j = i;
            }
            while (j < cols) {
                // assert F.isElementOfThisField(elements[i][j]);
                c[0] = (byte) elements[i][j];
                b.append(Hex.toHexString(c));
                j++;
            }
        }
        return b.toString();
    }
}
