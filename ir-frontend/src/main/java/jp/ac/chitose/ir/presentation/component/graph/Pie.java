package jp.ac.chitose.ir.presentation.component.graph;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.YAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.AnimationsBuilder;
import com.github.appreciated.apexcharts.helper.Series;

public class Pie {
    public ApexCharts pie(Series... series) {
        return makePieChart(series);
    }

    private ApexCharts makePieChart(Series... series) {
        return ApexChartsBuilder.get()
                .withChart(ChartBuilder.get()
                        .withType(Type.PIE)
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
