package jp.ac.chitose.ir.presentation.component.graph;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.*;
import com.github.appreciated.apexcharts.config.chart.StackType;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.plotoptions.builder.BarBuilder;
import com.github.appreciated.apexcharts.helper.Series;

public class Bar {
    public ApexCharts bar(Series... series) {
        return createBarChart(series);
    }

    private ApexCharts createBarChart(Series... series) {
        return ApexChartsBuilder.get().withChart(
                        ChartBuilder.get()
                                .withType(Type.BAR)// Typeにヒストグラムがない。公式サイトのissueによるBARでやるように指示がある
                                .withStacked(true)
                                .withStackType(StackType.FULL)
                                .build())
                .withPlotOptions(PlotOptionsBuilder.get()
                        .withBar(BarBuilder.get().withRangeBarGroupRows(true).withHorizontal(true).build()) // BARの間隔を０に近づける（見た目を調整してヒストグラムにみえるようにする）
                        .build())
                .withSeries(series)
                .build();
    }
}
