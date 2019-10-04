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
        if (layer == Layer.ONE) {
            this.F = new Matrix[2];
        } else {
            this.F = new Matrix[5];
        }
        this.F[0] = new Matrix(R, R.v(1), R.v(1)); // TODO (TRIANGULAR SUPERIOR I < J)
        this.F[1] = new Matrix(R, R.v(1), R.o(1));
        //
        Matrix T1=R.T(1);
        Matrix T2 = R.T(2);
        Matrix T3 = R.T(3);
        Matrix F1T = F(1).transpose();
        Matrix A = F(1).add(F1T);
        Matrix T1T = R.T(1).transpose();
        Matrix T2T = R.T(2).transpose();
        Matrix B = T1T.mult(F(2));
        Matrix T3T = R.T(3).transpose();
        Matrix F2mT3=F(2).mult(T3);
        Matrix F2TmT2=F(2).transpose().mult(T2);
        Matrix F5sF5T=F(5).add(F(5).transpose());
        // -- c a p a  u n o  -- //
        if (layer == Layer.ONE) {
            this.Q[0] = this.F(1);
            this.Q[1] = A.mult(T1).add(F(2));
            this.Q[2] = A.mult(T2).add(F2mT3);
            this.Q[3] = T1T.mult(F(1)).mult(T1).add(B).UT();
            this.Q[4] = T1T.mult(A).mult(T2).add(B.mult(T3)).add(F2TmT2);
            this.Q[5] = T2T.mult(F(1)).mult(T2).add(T2T.mult(F2mT3)).UT();
        } else { // -- c a p a  d o s -- //
            this.F[2] = new Matrix(R, R.o(2), R.o(2));
            this.F[3] = new Matrix(R, R.o(1), R.o(1)); // TODO UPPER TRIANGULAR
            this.F[4] = new Matrix(R, R.o(2), R.o(2));
            this.Q[0]=this.F(1);
            this.Q[1]=A.mult(T1).add(F(2));
            this.Q[2]=A.mult(T2).add(F2mT3).add(F(3));
            this.Q[3]=T1T.mult(F(1)).mult(T1).add(T1T.mult(F(2)).add(F(5))).UT();  
            this.Q[4]=T1T.mult(A.mult(T2)).add(B.mult(T3)).add(T1T.mult(F(3))).mult(F2TmT2).add(F5sF5T).mult(T3).add(F(6));
            this.Q[5]=T2T.mult(F(1)).mult(T2).add(T2T.mult(F2mT3)).add(T3T.mult(F(5).mult(T3))).add(T2T.mult(F(3))).add(T3T.mult(F(6))).UT();
        }
    }

    public final Matrix F(int k) {
        switch (k) {
            case 1:
                return this.F[0];
            case 2:
                return this.F[1];
            case 3:
                return this.F[2];
            case 5:
                return this.F[3];
            case 6:
                return this.F[4];
            default:
                throw new IllegalArgumentException("Index no válido.");
        }
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
            case 9:
                return this.Q[5];
            default:
                throw new IllegalArgumentException("Index no válido.");
        }
    }

}
