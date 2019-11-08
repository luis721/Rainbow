package rainbow;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import utils.Field;

/**
 * Rainbow key pair generator.
 *
 * @author mlcarcamo
 */
public final class RainbowKeyPairGenerator {

    private final SecureRandom sr;
    private final PublicKey pk;
    private final PrivateKey sk;

    /**
     * Creates a new Rainbow instance.
     *
     * @param v1
     * @param o1 Number of polynomials in the first layer.
     * @param o2 Number of polynomials in the second layer.
     */
    public RainbowKeyPairGenerator(int v1, int o1, int o2) {
        // Generation of the 256-bits random seed.
        byte[] seed = seed(256);
        // Creation of the PRNG with the obtained seed for the creation of 
        // the elements of the field.
        this.sr = new SecureRandom(seed);
        // Creation of the random invertible affine maps.
        AffineMapS S = new AffineMapS(this); // Random invertible affine map S.
        AffineMapT T = new AffineMapT(this); // Random invertible affine map T.
        // Creation of the central map.
        RainbowMap F = new RainbowMap(this);
        // Creation of the Private key
        this.sk = new PrivateKey(F, T, S);
        // Creation of the public key.
        this.pk = new PublicKey(F, S.S1());
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

    public Field GF() {
        return Parameters.F;
    }

    /**
     *
     * @return Invertible affine map T.
     */
    public AffineMapT T() {
        return sk.getT();
    }

    /**
     * Returns a random non-zero element in the given field Fq.
     *
     * @return
     */
    public int randomFieldItem() {
        return Parameters.F.getRandomNonZeroElement(this.sr);
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

    public static void main(String[] args) throws IOException {
        RainbowKeyPairGenerator R = new RainbowKeyPairGenerator(Parameters.V1, Parameters.O1, Parameters.O2); // GF(256)
        // Generates a random hashed document of size m
        int[] h = new int[Parameters.M];
        Arrays.setAll(h, i -> Parameters.F.getRandomNonZeroElement(new SecureRandom()));
        // Generates the signature for h
        int[] s = R.sk.signature(h);
        // Shows the signature
        for (int i = 0; i < s.length; i++) {
            System.out.print(s[i] + " ");
        }
        System.out.println("¿Es valida?");
        System.out.println(R.pk.isValid(s, h));
        System.out.println("");
        R.getPk().writeToFile("public.key");
        R.getSk().writeToFile("private.key");
    }

}
