package rainbow;

import java.security.SecureRandom;
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
     * Creates a new Rainbow instance based inhe parameters established in the
     * static class Parameters.
     *
     */
    public RainbowKeyPairGenerator() {
        // Generation of the 256-bits random seed.
        byte[] seed = seed(256);
        // Creation of the PRNG with the obtained seed for the creation of 
        // the elements of the field.
        this.sr = new SecureRandom(seed);
        // Creation of the random invertible affine maps.
        AffineMapS S = new AffineMapS(this); // Random invertible affine map S.
        AffineMapT T = new AffineMapT(this); // Random invertible affine map T.
        // Creation of the central map.
        RainbowMap F = new RainbowMap(this, T);
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
     * Returns a random non-zero element in the given field Fq.
     *
     * @return
     */
    public int randomFieldItem() {
        int e = Parameters.F.getRandomNonZeroElement(this.sr);
        assert (e <= 255);
        return e;
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

}
