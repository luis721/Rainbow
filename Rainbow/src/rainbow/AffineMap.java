package rainbow;

public abstract class AffineMap {

    public abstract AffineMap inverse();

    public abstract int getPosition(int i, int j);
}