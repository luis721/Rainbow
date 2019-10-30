package rainbow;

import utils.Field;

/**
 *
 * @author Usuario
 */
public class Parameters {

    private final static int pol = Integer.decode("0x11B");
    public final static Field F = new Field(8, pol);
    public final static int V1 = 68;
    public final static int O1 = 36;
    public final static int O2 = 36;
    public final static int V2 = V1 + O1;
    public final static int N = V2 + O2;
    public final static int M = O1 + O2;

    public static int v(int l) {
        switch (l) {
            case 1:
                return V1;
            case 2:
                return V2;
            default: // v3
                return N;
        }
    }

    public static int o(int l) {
        if (l == 1) {
            return O1;
        } else {
            return O2; // l == 2
        }
    }

    /**
     * Don't initialize.
     */
    private Parameters() {
    }
}

