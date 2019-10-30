package rainbow;

import java.security.SecureRandom;
import utils.BlockMatrix;
import utils.Field;
import utils.Matrix;

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
     * Given the value x, returns y such that F(y) = x.
     *
     * @param x
     * @return y such that F(y) = x.
     */
    public int[] inverse(int[] x) {
        int[] y;
        // Create random values for (y1 ... yv1)
        boolean valid = false;
        do {
            y = new int[Parameters.N];
            // initial random values. 
            for (int i = 0; i < Parameters.V1; i++) {
                y[i] = Parameters.F.getRandomNonZeroElement(new SecureRandom());
            }
            // Layer 1
            Matrix A1 = this.layers[0].coefficientMatrix(y);
            Matrix b1 = this.layers[0].constantPart(x, y);
            // system to solve for the first layer
            y = new BlockMatrix(Parameters.F, 1, 2, A1, b1).Gauss(y, Parameters.V1);
            if (y != null) {
                // Layer 2
                Matrix A2 = this.layers[1].coefficientMatrix(y);
                Matrix b2 = this.layers[1].constantPart(x, y);
                // system to solve for the second layer
                y = new BlockMatrix(Parameters.F, 1, 2, A2, b2).Gauss(y, Parameters.V2);
                if (y != null) {
                    valid = true;
                }
            }
        } while (!valid);
        return y;
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
        StringBuilder b = new StringBuilder(this.layers[0].toString());
        b.append(this.layers[1].toString());
        return b.toString();
    }

}

