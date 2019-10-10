package rainbow;

/**
 * This class represents the Rainbow Central map.
 * <br>
 * The representation is divided into two parts, each one corresponding to each
 * of the two layers needed in this implementation.
 *
 * @author mlcarcamo
 */
public class RainbowMap {

    private final Layer[] layers;

    /**
     * Creates a Central Map based in the Rainbow instance.
     * <br>
     * In this implementation, the central map consists of two layers.
     *
     * @param R Rainbow instance used to generate each of the random elements in
     * the layers.
     */
    public RainbowMap(Rainbow R) {
        this.layers = new Layer[]{
            new Layer(R, 1),
            new Layer(R, 2)
        };
    }

    /**
     *
     * @return Layer one of the central map.
     */
    public Layer LayerOne() {
        return layers[0];
    }

    /**
     *
     * @return Layer two of the central map.
     */
    public Layer LayerTwo() {
        return layers[1];
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("Capa 1:\n");
        b.append(this.layers[0].toString());
        b.append("\nCapa 2:\n");
        b.append(this.layers[1].toString());
        return b.toString();
    }

}
