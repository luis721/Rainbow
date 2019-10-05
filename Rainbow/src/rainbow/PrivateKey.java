package rainbow;

/**
 *
 * @author mlcarcamo
 */
public class PrivateKey {

    private final AffineMapT T;

    private final AffineMapS S;

    private final RainbowMap F;

    public PrivateKey(RainbowMap F, AffineMapT T, AffineMapS S) {
        this.F = F;
        this.T = T;
        this.S = S;
    }

    public AffineMapS getS() {
        return S;
    }

    public AffineMapT getT() {
        return T;
    }

    public RainbowMap getF() {
        return F;
    }

    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

}
