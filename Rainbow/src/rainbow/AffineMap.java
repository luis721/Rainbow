package rainbow;

/**
 *
 * @author mlcarcamo
 */
public class AffineMap {

    /**
     * Dimension of the map. This indicates it's of dimensions d x d.
     */
    private final int d;

    /**
     * Used for determining the field in which the map lies (Fq).
     */
    private final int q;

    /**
     * Matrix used to represent the AffineMap.
     */
    private final short[][] M;

    public AffineMap(int d, int q) {
        this.d = d;
        this.q = q;
        this.M = new short[d][d];
    }

    public int getSize() {
        return d;
    }

    public int getFieldDim() {
        return q;
    }

}
