package rainbow;

import java.security.SecureRandom;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author usuario
 */
public class PrivateKeyTest {

    public PrivateKeyTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of signature method, of class PrivateKey.
     */
    @Test
    public void testSignature() {
        System.out.println("signature");
        // Random PrivateKey generation
        RainbowKeyPairGenerator RKPG = new RainbowKeyPairGenerator();
        PrivateKey instance = RKPG.getSk();
        AffineMapS S = instance.getS();
        AffineMapT T = instance.getT();
        RainbowMap F = instance.getF();
        // Random hashed document
        int[] h = new int[Parameters.M];
        Arrays.setAll(h, i -> Parameters.F.getRandomNonZeroElement(new SecureRandom()));
        // Generate signature
        int[] z = instance.signature(h);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
