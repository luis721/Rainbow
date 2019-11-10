package utils;

import java.security.SecureRandom;
import org.bouncycastle.pqc.math.linearalgebra.GF2mField;

/**
 * This class encapsulates the field operations.
 *
 * @author mlcarcamo
 */
public class Field extends GF2mField {

    private static final int MASK = 0x00ff;

    /**
     *
     * @param q Degree of the field.
     * @param pol Polynomial used for mod in the multiplications.F
     */
    public Field(int q, int pol) {
        super(q, pol);
    }

    /**
     * Sums two numbers over the field.
     * <br>
     * A mask is applied to the result.
     *
     * @param a
     * @param b
     * @return c = (a + b) & 255
     */
    @Override
    public int add(int a, int b) {
        int r = super.add(a, b) & MASK;
        assert (r <= 255);
        return r;
    }

    /**
     * Multiplies two items over the field.
     *
     * @param a
     * @param b
     * @return c = (a * b) & 255
     */
    @Override
    public int mult(int a, int b) {
        int r = super.mult(a, b) & MASK;
        assert (r <= 255);
        return r;
    }

    @Override
    public int inverse(int a) {
        int r = super.inverse(a);
        assert (r <= 255);
        return r;
    }

    @Override
    public int getRandomNonZeroElement(SecureRandom sr) {
        return super.getRandomNonZeroElement(sr) & MASK;
    }

}
