package rainbow;

/**
 *
 * @author mlcarcamo
 */
public class PublicKey {
    
    private Matrix MP1,MP2;
    
    public PublicKey(RainbowMap RM,Matrix Sp){
    Matrix MQ1=RM.LayerOne().MQ();
    Matrix MQ2=RM.LayerTwo().MQ();
    this.MP1=MQ1.add(Sp.mult(MQ2));
    this.MP2=MQ2;
    }
    public Matrix MP1(){
    return this.MP1;
    }
    public Matrix MP2(){
    return this.MP2;
    }
}
