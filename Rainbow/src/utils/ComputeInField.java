package utils;

import rainbow.Parameters;

/**
 * Esta clase fue tomada de la implementación de Rainbow disponible en la
 * librería de Bouncy Castle.
 *
 */
public class ComputeInField {

    private int[][] A; // used by solveEquation and inverse
    int[] x;

    /**
     * Constructor with no parameters
     */
    public ComputeInField() {
    }

    /**
     * This function finds a solution of the equation Bx = b. Exception is
     * thrown if the linear equation system has no solution
     *
     * @param B this matrix is the left part of the equation (B in the equation
     * above)
     * @param b the right part of the equation (b in the equation above)
     * @return x the solution of the equation if it is solvable null otherwise
     * @throws RuntimeException if LES is not solvable
     */
    public int[] solveEquation(int[][] B, int[] b) {
        if (B.length != b.length) {
            return null;   // not solvable in this form
        }

        try {

            /**
             * initialize *
             */
            // this matrix stores B and b from the equation B*x = b
            // b is stored as the last column.
            // B contains one column more than rows.
            // In this column we store a free coefficient that should be later subtracted from b
            A = new int[B.length][B.length + 1];
            // stores the solution of the LES
            x = new int[B.length];

            /**
             * copy B into the global matrix A *
             */
            for (int i = 0; i < B.length; i++) { // rows
                for (int j = 0; j < B[0].length; j++) { // cols
                    A[i][j] = B[i][j];
                }
            }

            /**
             * copy the vector b into the global A *
             */
            //the free coefficient, stored in the last column of A( A[i][b.length]
            // is to be subtracted from b
            for (int i = 0; i < b.length; i++) {
                A[i][b.length] = Parameters.F.add(b[i], A[i][b.length]);
            }

            /**
             * call the methods for gauss elimination and backward substitution
             * *
             */
            computeZerosUnder(false);     // obtain zeros under the diagonal
            substitute();

            return x;

        } catch (RuntimeException rte) {
            return null; // the LES is not solvable!
        }
    }

    /**
     * Elimination under the diagonal. This function changes a matrix so that it
     * contains only zeros under the diagonal(Ai,i) using only Gauss-Elimination
     * operations.
     * <p>
     * It is used in solveEquaton as well as in the function for finding an
     * inverse of a matrix: {@link}inverse. Both of them use the
     * Gauss-Elimination Method.
     * </p><p>
     * The result is stored in the global matrix A
     * </p>
     *
     * @param usedForInverse This parameter shows if the function is used by the
     * solveEquation-function or by the inverse-function and according to this
     * creates matrices of different sizes.
     * @throws RuntimeException in case a multiplicative inverse of 0 is needed
     */
    private void computeZerosUnder(boolean usedForInverse)
            throws RuntimeException {

        //the number of columns in the global A where the tmp results are stored
        int length;
        int tmp = 0;

        //the function is used in inverse() - A should have 2 times more columns than rows
        if (usedForInverse) {
            length = 2 * A.length;
        } //the function is used in solveEquation - A has 1 column more than rows
        else {
            length = A.length + 1;
        }

        //elimination operations to modify A so that that it contains only 0s under the diagonal
        for (int k = 0; k < A.length - 1; k++) { // the fixed row
            for (int i = k + 1; i < A.length; i++) { // rows
                int factor1 = A[i][k];
                int factor2 = Parameters.F.inverse(A[k][k]);

                //The element which multiplicative inverse is needed, is 0
                //in this case is the input matrix not invertible
                if (factor2 == 0) {
                    throw new IllegalStateException("Matrix not invertible! We have to choose another one!");
                }

                for (int j = k; j < length; j++) {// columns
                    // tmp=A[k,j] / A[k,k]
                    tmp = Parameters.F.mult(A[k][j], factor2);
                    // tmp = A[i,k] * A[k,j] / A[k,k]
                    tmp = Parameters.F.mult(factor1, tmp);
                    // A[i,j]=A[i,j]-A[i,k]/A[k,k]*A[k,j];
                    A[i][j] = Parameters.F.add(A[i][j], tmp);
                }
            }
        }
    }

    /**
     * This function uses backward substitution to find x of the linear equation
     * system (LES) B*x = b, where A a triangle-matrix is (contains only zeros
     * under the diagonal) and b is a vector
     * <p>
     * If the multiplicative inverse of 0 is needed, an exception is thrown. In
     * this case is the LES not solvable
     * </p>
     *
     * @throws RuntimeException in case a multiplicative inverse of 0 is needed
     */
    private void substitute()
            throws IllegalStateException {

        // for the temporary results of the operations in field
        int tmp, temp;

        temp = Parameters.F.inverse(A[A.length - 1][A.length - 1]);
        if (temp == 0) {
            throw new IllegalStateException("The equation system is not solvable");
        }

        /**
         * backward substitution *
         */
        x[A.length - 1] = Parameters.F.mult(A[A.length - 1][A.length], temp);
        for (int i = A.length - 2; i >= 0; i--) {
            tmp = A[i][A.length];
            for (int j = A.length - 1; j > i; j--) {
                temp = Parameters.F.mult(A[i][j], x[j]);
                tmp = Parameters.F.add(tmp, temp);
            }

            temp = Parameters.F.inverse(A[i][i]);
            if (temp == 0) {
                throw new IllegalStateException("Not solvable equation system");
            }
            x[i] = Parameters.F.mult(tmp, temp);
        }
    }

}
