package jp.ac.chitose.ir.presentation.views.student.gpaview;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jp.ac.chitose.ir.application.service.commission.GradeGpaGraph;
import jp.ac.chitose.ir.application.service.commission.GradeService;
import jp.ac.chitose.ir.application.service.student.GradeCount;
import jp.ac.chitose.ir.application.service.student.StudentGPA;
import jp.ac.chitose.ir.presentation.component.graph.Data;
import jp.ac.chitose.ir.presentation.component.graph.Graph;
import jp.ac.chitose.ir.presentation.component.graph.GraphAlign;
import jp.ac.chitose.ir.presentation.component.graph.GraphSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GPAGraph extends VerticalLayout {
    private final GradeService gradeService;
    private static final String HEIGHT = "60vh";
    private static final String[] GPA_CATEGORY_LABEL = {"0.0~0.5","0.5~1.0","1.0~1.5","1.5~2.0","2.0~2.5","2.5~3.0","3.0~3.5","3.5~4.0","4.0"};

    public GPAGraph(GradeService gradeService, String schoolYear) {
        this.gradeService = gradeService;
        this.setHeight(HEIGHT);
        GraphSeries<Data<String, Integer>> series = createSeries(schoolYear);
        Graph graph = createGraph(series);
        add(graph.getGraph());
    }

    private GraphSeries<Data<String, Integer>> createSeries(String schoolYear) {
        Optional<GradeGpaGraph> optionalGraphData = gradeService.getGradeGpaGraph(schoolYear).data().stream()
                .filter(graphData -> graphData.getName().equals("全体"))
                .findFirst();
        if(optionalGraphData.isPresent()) {
            GradeGpaGraph graphData = optionalGraphData.get();
            return new GraphSeries<>(new Data<>(GPA_CATEGORY_LABEL[0], graphData.a()),
                    new Data<>(GPA_CATEGORY_LABEL[1], graphData.b()),
                    new Data<>(GPA_CATEGORY_LABEL[2], graphData.c()),
                    new Data<>(GPA_CATEGORY_LABEL[3], graphData.d()),
                    new Data<>(GPA_CATEGORY_LABEL[4], graphData.e()),
                    new Data<>(GPA_CATEGORY_LABEL[5], graphData.f()),
                    new Data<>(GPA_CATEGORY_LABEL[6], graphData.g()),
                    new Data<>(GPA_CATEGORY_LABEL[7], graphData.h()),
                    new Data<>(GPA_CATEGORY_LABEL[8], graphData.i()));
        }
        return null;
    }

    private Graph createGraph(GraphSeries<Data<String, Integer>> series) {
        return Graph.Builder.get()
                .histogram()
                .series(series)
                .width("100%")
                .height("100%")
                .YAxisForceNiceScale(true)
                .title("GPA", GraphAlign.CENTER)
                .dataLabelsEnabled(false)
                .legendShow(false)
                .colors("#0000FF", "#0000FF", "#0000FF", "#0000FF", "#0000FF", "#0000FF", "#0000FF", "#0000FF", "#0000FF")
                .build();
    }
}
