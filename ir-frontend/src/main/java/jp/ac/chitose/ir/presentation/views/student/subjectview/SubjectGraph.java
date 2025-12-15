package jp.ac.chitose.ir.presentation.views.student.subjectview;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jp.ac.chitose.ir.application.service.student.StudentGrade;
import jp.ac.chitose.ir.application.service.student.GradeCount;
import jp.ac.chitose.ir.presentation.component.graph.*;

import java.util.Arrays;

public class SubjectGraph extends VerticalLayout {
    private Graph mainGraph;
    private final Graph preYearGraph;
    private final HorizontalLayout mainGraphLayout;
    private final HorizontalLayout subGraphsLayout;
    private static final String[] GRADE_LABELS = {"不可", "可", "良", "優", "秀"};
    private static final String BLUE = "#4795F5";
    private static final String RED = "#FF5192";
    private static final String LAYOUT_HEIGHT = "40vh";
    private static final String NO_DATA_LAYOUT_HEIGHT = "0px";
    private static final String GRAPH_HEIGHT = "100%";

    public SubjectGraph() {
        mainGraphLayout = createLayout();
        subGraphsLayout = createLayout();
        mainGraph = createInitialGraph();
        preYearGraph = createInitialPreYearGraph();
        addInitialLayout();
    }

    private HorizontalLayout createLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setHeight(SubjectGraph.LAYOUT_HEIGHT);
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

    private Graph createInitialPreYearGraph() {
        return Graph.Builder.get()
                .graphType(GRAPH_TYPE.BAR)
                .YAxisForceNiceScale(true)
                .distributed(true)
                .dataLabelsEnabled(false)
                .series(new GraphSeries<>(0, 0, 0, 0, 0))
                .colors()
                .height(GRAPH_HEIGHT)
                .title("昨年度", GraphAlign.CENTER)
                .legendShow(false)
                .build();
    }

    private void addInitialLayout() {
        mainGraphLayout.add(mainGraph.getGraph());
        subGraphsLayout.add(preYearGraph.getGraph());
        add(mainGraphLayout, subGraphsLayout);
    }

    public void updateGraphs(GradeCount histData, GradeCount preYearHistData, StudentGrade studentGrade) {
        // メイングラフの更新
        final String[] colors = createColors(studentGrade.grading());
        final String[] labels = createLabels(studentGrade.grading());
        final GraphSeries<Data<String, Integer>> mainGraphSeries = createSeries(histData, studentGrade.lecture_name());
        updateMainGraph(studentGrade.grading(), targetGradeLabel(histData), colors, labels, mainGraphSeries);

        // サブグラフの更新
        updateSubGraphs(preYearHistData, studentGrade);
    }

    private String[] createColors(String grade) {
        return Arrays.stream(GRADE_LABELS)
                .map(label -> label.equals(grade) ? RED : BLUE)
                .toArray(String[]::new);
    }

    private String[] createLabels(String grade) {
        return Arrays.stream(GRADE_LABELS)
                .map(label -> label.equals(grade) ? grade + "(あなたの成績位置)" : label)
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

    private void updateMainGraph(String grade, String target, String[] colors, String[] labels, GraphSeries<Data<String, Integer>> series) {
        final String annotationString = target.equals(grade) ? target + "(あなたの成績位置)" : target;
        mainGraphLayout.remove(mainGraph.getGraph());
        mainGraph = mainGraph.getBuilder()
                .labels(labels)
                .colors(colors)
                .series(series)
                .resetAnnotations()
                .XAxisAnnotation(annotationString, "20px", "horizontal", "middle", "平均値")
                .build();
        mainGraphLayout.add(mainGraph.getGraph());
    }

    private void updateSubGraphs(GradeCount preYearHistData, StudentGrade studentGrade) {
        if (studentGrade.pre_year_course_id() == null) {
            hidePreYearGraph();
        } else {
            GraphSeries<Data<String, Integer>> preYearSeries = createSeries(preYearHistData, studentGrade.lecture_name());
            updatePreYearGraph(preYearSeries);
        }
    }

    private void hidePreYearGraph() {
        subGraphsLayout.remove(preYearGraph.getGraph());
        subGraphsLayout.setHeight(NO_DATA_LAYOUT_HEIGHT);
    }

    private void updatePreYearGraph(GraphSeries<Data<String, Integer>> preYearSeries) {
        subGraphsLayout.setHeight(LAYOUT_HEIGHT);
        if (subGraphsLayout.getChildren().findAny().isEmpty()) subGraphsLayout.add(preYearGraph.getGraph());
        preYearGraph.updateSeries(preYearSeries);
    }
}