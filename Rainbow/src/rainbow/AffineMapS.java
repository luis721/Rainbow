package rainbow;

/**
 *
 * @author fabia
 */
public class AffineMapS extends AffineMap {

    private final Matrix Sp;

    public AffineMapS(Matrix Sp) {
        this.Sp = Sp;
    }

    public AffineMapS(Rainbow R) {
        this.Sp = new Matrix(R, R.o(1), R.o(2), false);
    }
    
    public Matrix S1(){
        return this.Sp;
    }

    @Override
    public AffineMap inverse() {
        return this;
    }

    @Override
    public int getPosition(int i, int j) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String toString()
    {
        // TODO
        // MOSTRAR 0<=i<o1; i<=j<o1;
        return null;
    }

}