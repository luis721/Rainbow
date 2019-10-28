package rainbow;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import utils.Field;

/**
 * Rainbow instance.
 *
 * @author mlcarcamo
 */
public final class Rainbow {

    private final Field GF;
    private final int[] v;
    private final int[] o;
    private final SecureRandom sr;
    private final AffineMapT T;
    private final AffineMapS S;
    private final PublicKey pk;
    private final PrivateKey sk;

    /**
     * Creates a new Rainbow instance.
     *
     * @param v1
     * @param o1 Number of polynomials in the first layer.
     * @param o2 Number of polynomials in the second layer.
     */
    public Rainbow(int v1, int o1, int o2) {
        // Polynomial = x^8 + x^4 + x^3 + x^1 + 1  => 1 0001 1011 => 0x11B
        int pol = Integer.decode("0x11B");
        // Creates the field GF(256) with ops defined mod the given polynomial.
        // This polynomial is the same one used for AES.
        // GF(2^8) = GF(256)[x^8+x^4+x^3+x^1]
        int q = 8;
        this.GF = new Field(q, pol);
        // Values of v1, v2 and v3 (n).
        this.v = new int[]{v1, v1 + o1, v1 + o1 + o2};
        // Values of o1 and o2
        this.o = new int[]{o1, o2};
        // Generation of the 256-bits random seed.
        byte[] seed = seed(256);
        // Creation of the PRNG with the obtained seed for the creation of 
        // the elements of the field.
        this.sr = new SecureRandom(seed);
        // Creation of the random invertible affine maps.
        this.S = new AffineMapS(this); // Random invertible affine map S.
        this.T = new AffineMapT(this); // Random invertible affine map T.
        // Creation of the central map.
        RainbowMap F = new RainbowMap(this);
        // Creation of the Private key
        this.sk = new PrivateKey(F, T, S);
        // Creation of the public key.
        this.pk = new PublicKey(F, this.S.S1());
    }

    /**
     *
     * @return The field in which the Rainbow instance is defined.
     */
    public Field GF() {
        return this.GF;
    }

    /**
     * Returns seed of the given bits size.
     *
     * @param size Number of bits
     * @return
     */
    private byte[] seed(int size) {
        SecureRandom srnd = new SecureRandom();
        byte[] rndBytes = srnd.generateSeed(size / 8);
        return rndBytes;
    }

    /**
     *
     * @return Invertible affine map T.
     */
    public AffineMapT T() {
        return this.T;
    }

    /**
     * Returns a random non-zero element in the given field Fq.
     *
     * @return
     */
    public int randomFieldItem() {
        return GF.getRandomNonZeroElement(this.sr);
    }

    /**
     *
     * @return Public key of the Rainbow instance.
     */
    public PublicKey getPk() {
        return this.pk;
    }

    /**
     *
     * @return Private key of the Rainbow instance.
     */
    public PrivateKey getSk() {
        return this.sk;
    }

    /**
     *
     * @param k Index of the v-value.
     * @return Value of v<sub>k</sub>.
     */
    public int v(int k) {
        return v[k - 1];
    }

    /**
     *
     * @param k Index of the v-value.
     * @return Value of o<sub>k</sub>.
     */
    public int o(int k) {
        return o[k - 1];
    }

    /**
     *
     * @return m, i.e.: Number of polynomials.
     */
    public int m() {
        return o(1) + o(2);
    }

    /**
     *
     * @return n, i.e.: Number of variables.
     */
    public int n() {
        return v[2];
    }

    /**
     * Signs an already hashed document d.so h = H(d). h must be an array of m
     * items.
     *
     * @param h Hashed document of size m.
     * @return Signature for the hashed document.
     */
    public int[] signature(int[] h) {
        // asserts the document size is valid
        if (h.length != m()) {
            throw new IllegalArgumentException("The size of the document must be " + Parameters.M);
        }
        int[] x = this.S.eval(h);
        int[] y = this.sk.getF().inverse(x);
        return this.T.inverse().eval(y);
    }

    public static void main(String[] args) throws IOException {
        Rainbow R = new Rainbow(Parameters.V1, Parameters.O1, Parameters.O2); // GF(256)
        // Generates a random hashed document of size m
        int[] h = new int[Parameters.M];
        Arrays.setAll(h, i -> R.GF.getRandomNonZeroElement(new SecureRandom()));
        // Generates the signature for h
        int[] s = R.signature(h);
        // Shows the signature
        for (int i = 0; i < s.length; i++) {
            System.out.print(s[i] + " ");
        }
        R.getPk().writeToFile("public.key");
        R.getSk().writeToFile("private.key");
    }

}
