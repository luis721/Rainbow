package rainbow;

import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author mlcarcamo
 */
public class Rainbow {

    public static void main(String[] args) throws IOException {
        // Generación de las llaves
        RainbowKeyPairGenerator R = new RainbowKeyPairGenerator();
        R.getPk().writeToFile("MP.txt");
        R.getSk().writeToFile();
        // Generates a random hashed document of size m
        int[] h = new int[Parameters.M];
        Arrays.setAll(h, i -> 0);
        // Generates the signature for h
        int[] z = R.getSk().signature(h);
        // Shows the signature
        for (int i = 0; i < z.length; i++) {
            System.out.print(z[i] + " ");
        }
        System.out.println("\n¿Es valida?");
        System.out.println(R.getPk().isValid(z, h));
        System.out.println("");
    }
}
