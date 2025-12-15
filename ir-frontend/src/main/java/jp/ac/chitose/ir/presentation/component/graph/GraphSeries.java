package jp.ac.chitose.ir.presentation.component.graph;

/**
 * ApexChartsのSeriesの代わりにこのクラスを使ってください。
 * 使い方はSeriesと全く同じ使い方ができます。
 * @param <T> 行のデータの型
 */
public class GraphSeries<T> {
    private String name;
    private T[] data;

    public GraphSeries(T... data) {
        this.data = data;
    }

    public GraphSeries(String name, T... data) {
        this.name = name;
        this.data = data;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setData(T... data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public T[] getData() {
        return data;
    }
}