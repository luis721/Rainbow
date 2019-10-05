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
            new Layer(R, 2)
        };
    }

    public Layer LayerOne() {
        return layers[0];
    }

    public Layer LayerTwo() {
        return layers[1];
    }
    
    public String toString()
    {
        return this.layers[1].toString() + this.layers[2].toString();
    }

}
