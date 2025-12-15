package jp.ac.chitose.ir.presentation.component.graph;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.*;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.plotoptions.builder.BarBuilder;
import com.github.appreciated.apexcharts.helper.Series;

public class Histogram {
    public ApexCharts histogram(Series... series) {
        return createHistogramChart(series);
    }

    private ApexCharts createHistogramChart(Series... series) {
        return ApexChartsBuilder.get().withChart(
                        ChartBuilder.get()
                                .withType(Type.BAR) // Typeにヒストグラムがない。公式サイトのissueによるBARでやるように指示がある
                                .build())
                .withDataLabels(DataLabelsBuilder.get()
                        .withEnabled(false)
                        .build())
                .withPlotOptions(PlotOptionsBuilder.get()
                        .withBar(BarBuilder.get().withColumnWidth("100%").build()) // BARの間隔を０に近づける（見た目を調整してヒストグラムにみえるようにする）
                        .build())
                .withStroke(StrokeBuilder.get().withWidth(0.1).withColors("#000").build()) // 柱（棒）の外枠を黒色に設定してヒストグラムに見た目を近づける
                .withYaxis(YAxisBuilder.get().withMax(80).build()) // Y軸の最大値；ここでは80に設定
                .withSeries(series)
                .build();
    }
}