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
        // Generates a random hashed document of size m
        int[] h = new int[Parameters.M];
        Arrays.setAll(h, i -> R.randomFieldItem());
        // Generates the signature for h
        int[] z = R.getSk().signature(h);
        // Shows the signature
        for (int i = 0; i < z.length; i++) {
            System.out.print(z[i] + " ");
        }
        // Test verdadero
        System.out.println("\n¿Es valida? (Resultado esperado: verdadero)");
        System.out.println(R.getPk().isValid(z, h));
        // Test falso
        System.out.println("¿Es valida? (Resultado esperado: falso)");
        z[0] = 1; // change any value
        System.out.println(R.getPk().isValid(z, h));
    }
}
