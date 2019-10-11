package utils;

import org.bouncycastle.util.encoders.Hex;
import rainbow.Rainbow;

/**
 *
 * @author fabia
 */
public class FullMatrix extends Matrix {

    // Array containning the matrix elements
    private final int[][] elements;
    // Number of rows and cols of the matrix
    private final int rows, cols;
    // Field to which the matrix belongs
    private final Field F;

    /**
     * Creates a random matrix with elements generated with the seed associated
     * with the Rainbow instance, i.e.: elements in F such that are generated
     * with the SecureRandom generated in the Rainbow instance.
     *
     * @param R Rainbow instance
     * @param rows Number of rows
     * @param cols Number of columns
     */
    public FullMatrix(Rainbow R, int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.elements = new int[rows][cols];
        this.F = R.GF();
        int j;
        for (int i = 0; i < rows; i++) {
            j = 0;
            while (j < cols) {
                // Assigns to the current position an random generated
                // element in the field in which the rainbow instance lies.
                this.elements[i][j] = R.randomFieldItem();
                j++;
            }
        }
    }

    /**
     * Creates an empty matrix of the given type, rows and cols, lying in the
     * given field F.
     *
     * @param F Field in which the matrix lies.
     * @param rows Number of rows.
     * @param cols Number of columns.
     */
    public FullMatrix(Field F, int rows, int cols) {
        this.F = F;
        this.rows = rows;
        this.cols = cols;
        this.elements = new int[rows][cols];
    }

    @Override
    public void setElement(int x, int y, int value) {
        elements[x][y] = value;
    }

    /**
     * Gets the element in the position x,y of the matrix.
     *
     * @param i Row of the element to be <i>getted</i>.
     * @param j Column of the element to be <i>getted</i>.
     * @return
     */
    @Override
    public int getElement(int i, int j) {
        return elements[i][j];
    }

    public FullMatrix add(FullMatrix B) {
        verifyAdd(B);
        if (this.rows != B.rows) {
            throw new IllegalArgumentException("Rows size must be the same.");
        }
        if (this.cols != B.cols) {
            throw new IllegalArgumentException("Cols size must be the same.");
        }
        FullMatrix result = new FullMatrix(F, this.rows, this.cols);
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                result.setElement(row, col,
                        F.add(this.getElement(row, col), B.getElement(row, col)));
            }
        }
        return result;
    }

    /**
     * Performs matrix multiplication between the current (this) matrix and the
     * given matrix B. i.e: C = A * B;
     *
     * @param B Matrix to be multiplied with the current (this) matrix.
     * @return
     */
    public FullMatrix mult(FullMatrix B) {
        verifyMult(B);
        FullMatrix result = new FullMatrix(F, this.rows, B.cols());
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

    public FullMatrix transpose() {
        FullMatrix B = new FullMatrix(F, cols, rows);
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                B.setElement(i, j, this.getElement(j, i));
            }
        }
        return B;
    }

    /**
     *
     * @return Number of rows of the matrix.
     */
    @Override
    public int rows() {
        return rows;
    }

    /**
     *
     * @return Number of columns of the matrix.
     */
    @Override
    public int cols() {
        return cols;
    }

    /**
     * Transforms the matrix to the upper triangular form.
     *
     * @return Matrix in Upper Triangular form.
     */
    public UTMatrix UT() {
        if (rows != cols) {
            throw new UnsupportedOperationException("You can't UT a non-squared matrix");
        }
        UTMatrix result = new UTMatrix(F, rows);
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

    private UTMatrix findPivot(UTMatrix matrix, int column, int row) {
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

    private UTMatrix swapLines(Matrix matrix, int row1, int row2) {
        UTMatrix result = new UTMatrix(F, matrix.rows());
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

    /**
     * All non-zero elements (based in the matrix type) of the matrix are
     * transform to its repsective hexadecimal representation and concatenated
     * in a string.
     *
     * The is filled from top to bottom and left to right. i.e: The first
     * elements in the string will be those of the first row, the next ones are
     * the elements of the 2nd row, and so forth.
     *
     * @return string hex-based representation of the elements in the matrix.
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        byte[] c = new byte[1];
        int j;
        for (int i = 0; i < rows; i++) {
            j = 0;
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
