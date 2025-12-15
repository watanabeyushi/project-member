package jp.ac.chitose.ir.presentation.views.common.grade;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jp.ac.chitose.ir.application.service.student.GradeCount;
import jp.ac.chitose.ir.presentation.component.graph.*;

import java.util.Arrays;

public class CommonGraph extends VerticalLayout {
    private Graph mainGraph;
    private final HorizontalLayout mainGraphLayout;
    private static final String[] GRADE_LABELS = {"不可", "可", "良", "優", "秀"};
    private static final String BLUE = "#4795F5";
    private static final String RED = "#FF5192";
    private static final String LAYOUT_HEIGHT = "40vh";
    private static final String NO_DATA_LAYOUT_HEIGHT = "0px";
    private static final String GRAPH_HEIGHT = "100%";

    public CommonGraph() {
        mainGraphLayout = createLayout();
        mainGraph = createInitialGraph();
        addInitialLayout();
    }

    private HorizontalLayout createLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setHeight(LAYOUT_HEIGHT);
        return layout;
    }

    private Graph createInitialGraph() {
        return Graph.Builder.get()
                .graphType(GRAPH_TYPE.BAR)
                .width("100%")
                .height(GRAPH_HEIGHT)
                .legendShow(false)
                .dataLabelsEnabled(false)
                .YAxisForceNiceScale(true)
                .series(new GraphSeries<>())
                .distributed(true)
                .build();
    }


    private void addInitialLayout() {
        mainGraphLayout.add(mainGraph.getGraph());
        add(mainGraphLayout);
    }

    public void updateGraphs(GradeCount histData, String lectureName) {
        // メイングラフの更新
        final String[] colors = createColors(targetGradeLabel(histData));
        final GraphSeries<Data<String, Integer>> mainGraphSeries = createSeries(histData, lectureName);
        updateMainGraph(targetGradeLabel(histData), colors, mainGraphSeries);
    }

    private String[] createColors(String grade) {
        return Arrays.stream(GRADE_LABELS)
                .map(label -> label.equals(grade) ? RED : BLUE)
                .toArray(String[]::new);
    }

    private GraphSeries<Data<String, Integer>> createSeries(GradeCount histData, String subject) {
        Data<String, Integer>[] selectYearData = createDataArray(histData);
        return new GraphSeries<>(subject, selectYearData);
    }

    private Data<String, Integer>[] createDataArray(GradeCount data) {
        Data<String, Integer>[] dataArray = new Data[5];
        dataArray[0] = new Data<>(GRADE_LABELS[0], data.不可());
        dataArray[1] = new Data<>(GRADE_LABELS[1], data.可());
        dataArray[2] = new Data<>(GRADE_LABELS[2], data.良());
        dataArray[3] = new Data<>(GRADE_LABELS[3], data.優());
        dataArray[4] = new Data<>(GRADE_LABELS[4], data.秀());
        return dataArray;
    }

    private String targetGradeLabel(GradeCount histData) {
        return GRADE_LABELS[Math.round(histData.平均())];
    }

    private void updateMainGraph(String target, String[] colors, GraphSeries<Data<String, Integer>> series) {
        mainGraphLayout.remove(mainGraph.getGraph());
        mainGraph = mainGraph.getBuilder()
                .labels(GRADE_LABELS)
                .colors(colors)
                .series(series)
                .resetAnnotations()
                .XAxisAnnotation(target, "20px", "horizontal", "middle", "平均値")
                .build();
        mainGraphLayout.add(mainGraph.getGraph());
    }
}
