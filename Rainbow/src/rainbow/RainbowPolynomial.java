package rainbow;

/**
 *
 * @author mlcarcamo
 */
public class RainbowPolynomial {

    private Rainbow R;
    private Matrix[] F;
    private Matrix[] Q;

    public enum Layer {
        ONE, TWO
    }

    public RainbowPolynomial(Rainbow R, Layer layer) {
        // -- c a p a  u n o  -- //
        if (layer == Layer.ONE) {
            this.F = new Matrix[]{
                new Matrix(R, R.v(1), R.v(1)), // TODO (TRIANGULAR SUPERIOR I < J)
                new Matrix(R, R.v(1), R.o(1))
            };
            Matrix T2 = R.T(2);
            Matrix T3 = R.T(3);
            Matrix F1T = F(1).transpose();
            Matrix T1T = R.T(1).transpose();
            Matrix T2T = R.T(2).transpose();
            Matrix A = F(1).add(F1T);
            Matrix B = T1T.mult(F(2));
            Matrix C = F(2).mult(T3);
            this.Q[0] = this.F(1);
            this.Q[1] = A.mult(R.T(1)).add(F(2));
            this.Q[2] = A.mult(T2).add(C);
            this.Q[3] = T1T.mult(F(1)).mult(R.T(1)).add(B).UT();
            this.Q[4] = T1T.mult(A).mult(T2).add(B.mult(T3)).add(F(2).transpose().mult(T2));
            this.Q[5] = T2T.mult(F(1)).mult(T2).add(T2T.mult(C)).UT();
        } else { // -- c a p a  d o s -- //

        }
    }

    public final Matrix F(int k) {
        return this.F[k + 1];
    }

    public Matrix Q(int i) {
        switch (i) {
            case 1:
                return this.Q[0];
            case 2:
                return this.Q[1];
            case 3:
                return this.Q[2];
            case 5:
                return this.Q[3];
            case 6:
                return this.Q[4];
            default:
                return this.Q[5];
        }
    }

}
