package rainbow;

import utils.Matrix;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import utils.Field;

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
    private final Field GF;

    public Rainbow(int v1, int o1, int o2) {
        // Creaciòn del campo
        this.q = 8;
        // Polinomio = 1010 0111 => 0xA7
        int pol = Integer.decode("0x11B");
        // OJO A ESTA PARTE TODO TODO TODO
        this.GF = new Field(8, pol); //GF(2^8) = GF(256)[x^8+x^4+x^3+x^1]
        //this.GF = new GF2mField(8); //GF(2^8) = GF(256)
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

    public Field GF() {
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
     * Returns a random non-zeror element in the given field Fq.
     *
     * @return
     */
    public int randomFieldItem() {
        return GF.getRandomNonZeroElement(this.sr);
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

    public static void main(String[] args) throws IOException {
        Rainbow R = new Rainbow(68, 36, 36); // GF(256)
        R.getPk().writeToFile("public.key");
    }
}
