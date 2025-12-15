package jp.ac.chitose.ir.presentation.component.graph;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.YAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.AnimationsBuilder;
import com.github.appreciated.apexcharts.helper.Series;

public class Scatter {
    public ApexCharts scatter(Series... series) {
        return makeScatterChart(series);
    }

    private ApexCharts makeScatterChart(Series... series) {
        return ApexChartsBuilder.get()
                .withChart(ChartBuilder.get()
                        .withType(Type.SCATTER)
                        .withAnimations(AnimationsBuilder.get()
                                .withEnabled(true)
                                .build())
                        .build())
                .withSeries(series)
                .withYaxis(YAxisBuilder.get()
                        .withForceNiceScale(true)
                        .build())
                .build();
    }
}