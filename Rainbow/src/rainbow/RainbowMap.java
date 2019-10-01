package rainbow;

/**
 *
 * @author mlcarcamo
 */
public class RainbowMap {

    private final Layer[] layers;

    public RainbowMap(Rainbow R) {
        this.layers = new Layer[]{
            new Layer(R, 1),
            new Layer(R, 1)
        };
    }

}
