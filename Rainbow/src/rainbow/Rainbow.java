package rainbow;

import java.security.SecureRandom;
import org.bouncycastle.pqc.math.linearalgebra.GF2mField;

/**
 *
 * @author mlcarcamo
 */
public final class Rainbow {

    private final int q;
    private final int[] v;
    private final int[] o;
    private final SecureRandom sr;
    private final PublicKey pk;
    private final PrivateKey sk;
    private final AffineMapT T;
    private final AffineMapS S;
    private final GF2mField GF;

    public Rainbow(int q, int v1, int o1, int o2) {
        this.q = q;
        this.GF = new GF2mField(2 ^ q);
        // Valores de V y O
        this.v = new int[]{v1, o1 + v1, v1 + o1 + o2};
        this.o = new int[]{o1, o2};
        // Creación de la semilla y el generador de elementos aleatorios
        byte[] seed = seed(256); // Random seed of 256 bits
        this.sr = new SecureRandom(seed);
        // Creacion de los mapas afines invertibles
        this.S = new AffineMapS(this);
        this.T = new AffineMapT(this);
        // Creación del Mapa de Rainbow
        RainbowMap F = new RainbowMap(this);
        // Creación de las llaves pública y privada
        this.sk = new PrivateKey(F, T, S);
        this.pk = new PublicKey(F, this.S.S1());
    }

    public GF2mField GF() {
        return this.GF;
    }

    /**
     * Returns seed of the give bits size.
     *
     * @param size Number of bits
     * @return
     */
    private byte[] seed(int size) {
        SecureRandom srnd = new SecureRandom();
        byte[] rndBytes = srnd.generateSeed(size / 8);
        return rndBytes;
    }

    public Matrix T(int index) {
        return this.T.T(index);
    }

    /**
     * Returns a random element in the given field Fq.
     *
     * @return
     */
    public int randomFieldItem() {
        return GF.getRandomElement(this.sr);
    }

    public PublicKey getPk() {
        return this.pk;
    }

    public PrivateKey getSk() {
        return this.sk;
    }

    public int v(int k) {
        return v[k - 1];
    }

    public int o(int k) {
        return o[k - 1];
    }

    public int m() {
        return o(1) + o(2);
    }

    public int n() {
        return v[2];
    }

    public static void main(String[] args) {
        Rainbow R = new Rainbow(8, 68, 48, 48);
        RainbowMap RM = new RainbowMap(R);
    }
}
