package rainbow;

/**
 *
 * @author mlcarcamo
 */
public class PrivateKey {

    private final AffineMap T;

    private final AffineMap S;

    private final RainbowMap F;

    public PrivateKey(RainbowMap F, AffineMap T, AffineMap S) {
        this.F = F;
        this.T = T;
        this.S = S;
    }

    public AffineMap getS() {
        return S;
    }

    public AffineMap getT() {
        return T;
    }

    public RainbowMap getF() {
        return F;
    }

}
