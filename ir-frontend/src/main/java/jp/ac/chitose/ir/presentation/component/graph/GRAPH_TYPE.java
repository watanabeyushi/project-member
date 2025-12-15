package jp.ac.chitose.ir.presentation.component.graph;

import com.github.appreciated.apexcharts.config.chart.Type;

/**
 * Graph.BuilderのgraphTypeメソッドの引数で使用するグラフのタイプを表すenum
 */
public enum GRAPH_TYPE {
    SCATTER(Type.SCATTER),
    BAR(Type.BAR),
    PIE(Type.PIE),
    DONUT(Type.DONUT),
    BOXPLOT(Type.BOXPLOT),
    RANGEBAR(Type.RANGEBAR),
    AREA(Type.AREA),
    BUBBLE(Type.BUBBLE),
    CANDLESTICK(Type.CANDLESTICK),
    HEATMAP(Type.HEATMAP),
    RADAR(Type.RADAR),
    RADIALBAR(Type.RADIALBAR),
    LINE(Type.LINE);

    Type type;
    GRAPH_TYPE(Type type) {this.type = type;}
}