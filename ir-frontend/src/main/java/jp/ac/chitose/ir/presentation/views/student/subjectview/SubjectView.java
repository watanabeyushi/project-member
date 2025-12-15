package jp.ac.chitose.ir.presentation.views.student.subjectview;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.ValueProvider;
import jp.ac.chitose.ir.application.service.student.StudentGrade;
import jp.ac.chitose.ir.application.service.student.StudentGradeService;
import jp.ac.chitose.ir.application.service.student.GradeCount;
import jp.ac.chitose.ir.presentation.views.student.filterablecomponent.FilterPosition;

import java.util.Collections;
import java.util.List;

public class SubjectView extends VerticalLayout {
    private final StudentGradeService studentGradeService;
    private final SubjectGraph graph;
    private final SubjectGrid grid;
    private final SubjectTarget target;
    private final static List<String> SUBJECT_HEADER_NAMES = List.of("受講人数", "不可", "可", "良", "優", "秀", "平均", "分散");
    private final static List<ValueProvider<GradeCount, Number>> SUBJECT_VALUE_PROVIDERS = List.of(
            GradeCount::合計の人数,
            GradeCount::不可,
            GradeCount::可,
            GradeCount::良,
            GradeCount::優,
            GradeCount::秀,
            GradeCount::平均,
            GradeCount::分散);


    public SubjectView(StudentGradeService studentGradeService, String accountId) {
        this.studentGradeService = studentGradeService;
        this.graph = new SubjectGraph();
        this.grid = createSubjectGrid();
        this.target = new SubjectTarget(studentGradeService, accountId);
        addComponentToLayout();
    }

    private SubjectGrid createSubjectGrid() {
        SubjectGrid grid = new SubjectGrid(Collections.emptyList(), FilterPosition.TOP);
        for(int i = 0; i < SUBJECT_HEADER_NAMES.size(); i++) {
            grid.addColumn(SUBJECT_VALUE_PROVIDERS.get(i), SUBJECT_HEADER_NAMES.get(i));
        }
        grid.setAllRowsVisible(true);
        return grid;
    }

    private void addComponentToLayout() {
        add(target);
        add(graph);
        add(grid);
    }

    public void update(StudentGrade grade) {
        GradeCount data = studentGradeService.getGradeGraph(grade.course_id()).data().get(0);
        GradeCount preYearData = null;
        if(grade.pre_year_course_id() != null) preYearData = studentGradeService.getGradeGraph(grade.pre_year_course_id()).data().get(0);
        updateSubjectGraph(data, preYearData, grade);
        updateSubjectGrid(data);
        updateSubjectTarget(grade.course_id());
    }

    private void updateSubjectGraph(GradeCount data, GradeCount preYearData, StudentGrade studentGrade) {
        graph.updateGraphs(data, preYearData, studentGrade);
    }

    private void updateSubjectGrid(GradeCount data) {
        grid.setItems(List.of(data));
    }

    private void updateSubjectTarget(String courseId) {
        target.update(courseId);
    }
}
