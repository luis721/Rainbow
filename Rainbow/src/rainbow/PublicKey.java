package rainbow;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import utils.Matrix;

/**
 *
 * @author mlcarcamo
 */
public class PublicKey {

    private final Matrix MP1;
    private final Matrix MP2;

    public PublicKey(RainbowMap RM, Matrix Sp) {
        Matrix MQ1 = RM.LayerOne().MQ();
        Matrix MQ2 = RM.LayerTwo().MQ();
        this.MP1 = MQ1.add(Sp.mult(MQ2));
        this.MP2 = MQ2;
    }

    public Matrix MP1() {
        return this.MP1;
    }

    public Matrix MP2() {
        return this.MP2;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder(MP1.toString());
        b.append(MP2.toString());
        return b.toString();
    }
    
    public void writeToFile(String file) throws IOException{
        File f = new File(file);
        FileWriter w = new FileWriter(f);
        w.write(this.toString());
        w.close();
    }
}
