package jp.ac.chitose.ir.presentation.component.graph;

import com.github.appreciated.apexcharts.config.subtitle.Align;

public enum GraphAlign {
    CENTER(Align.CENTER),
    LEFT(Align.LEFT),
    RIGHT(Align.RIGHT);

    Align align;
    GraphAlign(Align type) {this.align = type;}
}
