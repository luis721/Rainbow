package utils;

import org.bouncycastle.pqc.math.linearalgebra.GF2mField;

/**
 *
 * @author Usuario
 */
public class Field extends GF2mField {

    public Field(int q) {
        super(q);
    }

    public Field(int q, int pol) {
        super(q, pol);
    }

}
