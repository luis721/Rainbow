package rainbow;

import java.security.SecureRandom;
import utils.ComputeInField;

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
     * @param T Affine Map T
     */
    public RainbowMap(RainbowKeyPairGenerator R, AffineMapT T) {
        this.layers = new Layer[]{
            new Layer(R, T, 1),
            new Layer(R, T, 2)
        };
    }

    /**
     * Given the value x, returns y such that F(y) = x.
     *
     * @param x
     * @return y such that F(y) = x.
     */
    public int[] inverse(int[] x) {
        int[] y = new int[Parameters.N];
        // Create random values for (y1 ... yv1)
        boolean valid = false;
        int[] yp;
        do {
            // initial random values. 
            for (int i = 0; i < Parameters.V1; i++) {
                y[i] = Parameters.F.getRandomNonZeroElement(new SecureRandom());
            }
            // Layer 1
            int[][] A1 = this.layers[0].coefficientMatrix(y);
            int[] b1 = this.layers[0].constantPart(x, y);
            // system to solve for the first layer
            ComputeInField C = new ComputeInField();
            yp = C.solveEquation(A1, b1);
            if (yp != null) {
                for (int i = Parameters.V1; i < Parameters.V2; i++) {
                    if (y[i] != 0) {
                        System.out.println("HIHIHIHI");
                    }
                    y[i] = yp[i - Parameters.V1];
                }
                // Layer 2
                int[][] A2 = this.layers[1].coefficientMatrix(y);
                int[] b2 = this.layers[1].constantPart(x, y);
                // system to solve for the second layer
                yp = C.solveEquation(A2, b2);
                if (yp != null) {
                    for (int i = Parameters.V2; i < Parameters.N; i++) {
                        y[i] = yp[i - Parameters.V2];
                    }
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



