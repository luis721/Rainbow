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

    public AffineMap(short[][] M, int q) {
        this.q = q;
        this.d = M.length;
        this.M = M;
    }

    public int getSize() {
        return d;
    }

    public int getFieldDim() {
        return q;
    }

}
