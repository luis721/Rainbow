package utils;

/**
 * Abstract class for matrices.
 * <br>
 * Contains some abstractly defined essential methods and two methods for
 * checking dimensions when performing operations with matrices.
 *
 * @author mlcarcamo
 */
public abstract class Matrix {

    /**
     * Retrieves the element in the given position.
     *
     * @param i Row of the element.
     * @param j Column of the element.
     * @return Element placed in the i-th row and j-th column of the matrix.
     */
    public abstract int getElement(int i, int j);

    /**
     * Sets the given position i,j of the matrix the given value.
     *
     * @param i Row to be set.
     * @param j Column to be set.
     * @param value New value of the (i-th, j-th) position of the matrix.
     * values.
     */
    public abstract void setElement(int i, int j, int value) throws IllegalArgumentException;

    /**
     *
     * @return Number of rows of the matrix.F
     */
    public abstract int rows();

    /**
     *
     * @return Number of columns of the matrix.
     */
    public abstract int cols();

    /**
     * Addes the given matrix to the current matrix.
     *
     * @param B Matrix to be added.
     * @return C = A + B, where A = this.
     */
    //public abstract Matrix add(Matrix B) throws IllegalArgumentException;
    /**
     * Checks for compatibility when multiplying two matrices.
     *
     * @param B Matrix to which <i>this</i> is compared against.
     */
    protected final void verifyMult(Matrix B) {
        if (this.rows() == 0 || B.rows() == 0) {
            throw new IllegalArgumentException("Matrix argument is empty, "
                    + "operation cannot be performed.");
        }

        if (this.cols() == 0 || B.cols() == 0) {
            throw new IllegalArgumentException("Argument matrix has empty row, "
                    + "operation cannot be performed.");
        }

        if (this.cols() != B.rows()) {
            throw new IllegalArgumentException("Argument matrices cannot be multiplied, "
                    + "their dimensions are wrong.");
        }
    }

    /**
     * Checks for compatibility when adding two matrices.
     *
     * @param B Matrix to which <i>this</i> is compared against.
     */
    protected final void verifyAdd(Matrix B) {
        if (this.rows() == 0 || B.rows() == 0) {
            throw new IllegalArgumentException("Matrix argument is empty, "
                    + "operation cannot be performed.");
        }

        if (this.cols() == 0 || B.cols() == 0) {
            throw new IllegalArgumentException("Argument matrix has empty row, "
                    + "operation cannot be performed.");
        }

        if (this.rows() != B.rows()) {
            throw new IllegalArgumentException("Argument matrices cannot be multiplied, "
                    + "their dimensions are wrong.");
        }

        if (this.cols() != B.cols()) {
            throw new IllegalArgumentException("Argument matrices cannot be multiplied, "
                    + "their dimensions are wrong.");
        }
    }

}
