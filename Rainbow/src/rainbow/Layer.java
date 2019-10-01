package rainbow;

/**
 *
 * @author mlcarcamo
 */
public class Layer {

    private final RainbowPolynomial[] f;

    public Layer(Rainbow R) {
        this.f = new RainbowPolynomial[R.m()];
    }

}
