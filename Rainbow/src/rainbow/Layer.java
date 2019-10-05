package rainbow;

import java.util.HashMap;

/**
 *
 * @author mlcarcamo
 */
public class Layer {

    private HashMap<Integer, RainbowPolynomial> P;
    private Matrix MQ;
    public Layer(Rainbow R, int index) {
        if (index == 1) {
            // creación de los polinimios de la capa
            for (int i = R.v(1) + 1; i <= R.v(2); i++) {
                RainbowPolynomial RP = new RainbowPolynomial(R, RainbowPolynomial.Layer.ONE);
                P.put(i, RP);
            }
        } else { // Layer 2
            for(int i = R.v(2)+1; i<=R.n();i++){
                RainbowPolynomial RP = new RainbowPolynomial(R, RainbowPolynomial.Layer.TWO);
                P.put(i, RP);
            }
        }
        for (int f = 0; f < R.m(); f++) {
            int c=0;
            for (int i = 0; i < R.v(1); i++) {//Q1||Q2
                for (int j = i; j < R.v(1); j++) {
                    MQ.setElement(f, ++c, Q(f,1).getElement(i, j));
                }
                for (int j = 0; j < R.o(1); j++) {
                    MQ.setElement(f, ++c, Q(f,2).getElement(i, j));
                }
            }
            for (int i = 0; i < R.v(1); i++) {//Q3
                for (int j = 0; j < R.o(2); j++) {
                    MQ.setElement(f, ++c, Q(f,3).getElement(i, j));
                }
            }
            for (int i = 0; i < R.o(1); i++) {//Q5||Q6
                for (int j = i; j < R.o(1); j++) {
                    MQ.setElement(f, ++c, Q(f,1).getElement(i, j));
                }
                for (int j = 0; j < R.o(2); j++) {
                    MQ.setElement(f, ++c, Q(f,2).getElement(i, j));
                }
            }
            for (int i = 0; i < R.o(2); i++) {//Q9
                for (int j = i; j < R.o(2); j++) {
                    MQ.setElement(f, ++c, Q(f,9).getElement(i, j));
                }
            }
        }
    }
    
    // RETURNS  Qk del i-ésimo polinomio
    public Matrix Q(int i, int k){
        return this.P.get(i).Q(k);
    }
    public Matrix MQ(){
        return this.MQ;
    }

}
