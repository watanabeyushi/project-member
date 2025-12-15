package jp.ac.chitose.ir.presentation.component.graph;

/**
 * ApexChartsのCoordinateの代わりにこのクラスを使ってください。
 * 使い方はCoordinateと全く同じ使い方ができます。
 * @param <S> カラム名
 * @param <T> 列のデータの型
 */
public class Data<S, T> {
    private S x;
    private T[] y;

    public Data(S x, T... y) {
        this.x = x;
        this.y = y;
    }

    public void setX(S x) {
        this.x = x;
    }

    public void setY(T[] y) {
        this.y = y;
    }

    public T[] getY() {
        return y;
    }

    public S getX() {
        return x;
    }
}