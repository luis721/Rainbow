package rainbow;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import utils.FullMatrix;

/**
 *
 * @author mlcarcamo
 */
public class PublicKey {

    private final FullMatrix MP1;
    private final FullMatrix MP2;

    public PublicKey(RainbowMap RM, FullMatrix Sp) {
        FullMatrix MQ1 = RM.LayerOne().MQ();
        FullMatrix MQ2 = RM.LayerTwo().MQ();
        this.MP1 = MQ1.add(Sp.mult(MQ2));
        this.MP2 = MQ2;
    }

    public FullMatrix MP1() {
        return this.MP1;
    }

    public FullMatrix MP2() {
        return this.MP2;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder(MP1.toString());
        b.append(MP2.toString());
        return b.toString();
    }

    public void writeToFile(String file) throws IOException {
        File f = new File(file);
        try (FileWriter w = new FileWriter(f)) {
            w.write(this.toString());
        }
    }
}
