package rainbow;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 *
 * @author usuario
 */
public class Rainbow {

    public static void main(String[] args) throws IOException {
        // Generación de las llaves
        RainbowKeyPairGenerator R = new RainbowKeyPairGenerator();
        // Generates a random hashed document of size m
        int[] h = new int[Parameters.M];
        Arrays.setAll(h, i -> Parameters.F.getRandomNonZeroElement(new SecureRandom()));
        // Generates the signature for h
        int[] s = R.getSk().signature(h);
        // Shows the signature
        for (int i = 0; i < s.length; i++) {
            System.out.print(s[i] + " ");
        }
        System.out.println("¿Es valida?");
        System.out.println(R.getPk().isValid(s, h));
        System.out.println("");
        R.getPk().writeToFile("public.key");
        R.getSk().writeToFile("private.key");
    }
}
