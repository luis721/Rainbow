package rainbow;

import java.util.HashMap;

/**
 *
 * @author mlcarcamo
 */
public class Layer {

    private final HashMap<Integer, RainbowPolynomial> P;
    private final Matrix MQ;

    public Layer(Rainbow R, int index) {
        this.P = new HashMap<>();
        int delta;
        if (index == 1) {
            delta = R.v(1) + 1;
            this.MQ = new Matrix(R, R.o(1), (R.n() * (R.n() + 1)) / 2);
            // creación de los polinimios de la capa
            for (int i = R.v(1) + 1; i <= R.v(2); i++) {
                RainbowPolynomial RP = new RainbowPolynomial(R, RainbowPolynomial.Layer.ONE);
                P.put(i, RP);
            }
        } else { // Layer 2
            delta = R.v(2) + 1;
            this.MQ = new Matrix(R, R.n() - R.v(2), (R.n() * (R.n() + 1)) / 2);
            for (int i = R.v(2) + 1; i <= R.n(); i++) {
                RainbowPolynomial RP = new RainbowPolynomial(R, RainbowPolynomial.Layer.TWO);
                P.put(i, RP);
            }
        }

        // Creation of MQ for THE layer.
        for (int f = 0; f < this.P.size(); f++) {
            int c = 0;
            RainbowPolynomial RP = this.P.get(f + delta);
            for (int i = 0; i < R.v(1); i++) {//Q1||Q2
                for (int j = i; j < R.v(1); j++) {
                    MQ.setElement(f, c, RP.Q(1).getElement(i, j));
                    c++;
                }
                for (int j = 0; j < R.o(1); j++) {
                    MQ.setElement(f, c, RP.Q(2).getElement(i, j));
                    c++;
                }
            }
            for (int i = 0; i < R.v(1); i++) {//Q3
                for (int j = 0; j < R.o(2); j++) {
                    MQ.setElement(f, c, RP.Q(3).getElement(i, j));
                    c++;
                }
            }
            for (int i = 0; i < R.o(1); i++) {//Q5||Q6
                for (int j = i; j < R.o(1); j++) {
                    MQ.setElement(f, c, RP.Q(5).getElement(i, j));
                    c++;
                }
                for (int j = 0; j < R.o(2); j++) {
                    MQ.setElement(f, c, RP.Q(6).getElement(i, j));
                    c++;
                }
            }
            for (int i = 0; i < R.o(2); i++) {//Q9
                for (int j = i; j < R.o(2); j++) {
                    MQ.setElement(f, c, RP.Q(9).getElement(i, j));
                    c++;
                }
            }
        }
    }

    // RETURNS  Qk del i-ésimo polinomio
    public final Matrix Q(int i, int k) {
        return this.P.get(i).Q(k);
    }

    public Matrix MQ() {
        return this.MQ;
    }
    
    @Override
    public String toString(){
        // TODO
        // MOSTRAR CADA POLINOMIO (Y POR TANTO, IMPLEMENTAR EL MÉTODO TO STRING
        // EN LA CLASE RAINBOWPOLYNOMIAL)
        return null;
    }

}
