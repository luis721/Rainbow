package rainbow;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author mlcarcamo
 */
public class PrivateKey {

    private final AffineMapT T;

    private final AffineMapS S;

    private final RainbowMap F;

    public PrivateKey(RainbowMap F, AffineMapT T, AffineMapS S) {
        this.F = F;
        this.T = T;
        this.S = S;
    }

    public AffineMapS getS() {
        return S;
    }

    public AffineMapT getT() {
        return T;
    }

    public RainbowMap getF() {
        return F;
    }

    /**
     * Guarda la epresentación de la llave privada en un archivo. La primera
     * linea contiene a S'. La segunda linea contiene a str(T1) || str(T2) ||
     * str(T3).
     *
     * @param file Ruta del archivo donde se guardará.
     * @throws IOException
     */
    public void writeToFile(String file) throws IOException {
        File f = new File(file);
        FileWriter w = new FileWriter(f);
        w.write(this.S.toString());
        w.write('\n');
        w.write(this.T.toString());
        w.write('\n');
        w.write(this.F.toString());
        w.close();
    }

}
