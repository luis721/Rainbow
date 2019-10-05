package rainbow;

/**
 *
 * @author mlcarcamo
 */
public class AffineMapT extends AffineMap {

    private final Matrix T1;
    private final Matrix T2;
    private final Matrix T3;

    public AffineMapT(Matrix T1, Matrix T2, Matrix T3) {
        this.T1 = T1;
        this.T2 = T2;
        this.T3 = T3;
    }

    public AffineMapT(Rainbow R) {
        this.T1 = new Matrix(R, R.v(1), R.o(1), false); // T1
        this.T2 = new Matrix(R, R.v(1), R.o(2), false); // T2
        this.T3 = new Matrix(R, R.o(1), R.o(2), false); // T3
    }

    @Override
    public AffineMap inverse() {
        Matrix T4 = T1.mult(T3).add(T2);
        return new AffineMapT(T1, T4, T3);
    }

    @Override
    public int getPosition(int i, int j) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Matrix T(int index) {
        switch (index) {
            case 1:
                return T1;
            case 2:
                return T2;
            case 3:
                return T3;
            default:
                throw new IllegalArgumentException("Invalid index.");
        }
    }
    
    public String toString(){
        // TODO
        return null;
    }

}
