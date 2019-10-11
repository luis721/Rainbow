package utils;

import org.bouncycastle.pqc.math.linearalgebra.GF2mField;

/**
 *
 * @author mlcarcamo
 */
public class Field extends GF2mField {

    public Field(int q) {
        super(q);
    }

    public Field(int q, int pol) {
        super(q, pol);
    }

    @Override
    public int add(int arg0, int arg1) {
        return super.add(arg0, arg1) & 0xf;
    }

}
