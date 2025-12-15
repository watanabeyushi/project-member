package jp.ac.chitose.ir.presentation.views.student.gpaview;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.ValueProvider;
import jp.ac.chitose.ir.application.service.commission.GradeService;
import jp.ac.chitose.ir.application.service.student.StudentGrade;
import jp.ac.chitose.ir.application.service.student.StudentGradeService;
import jp.ac.chitose.ir.presentation.views.student.RadioButtonValues;
import jp.ac.chitose.ir.presentation.views.student.filter.Filter;
import jp.ac.chitose.ir.presentation.views.student.filter.RadioButtonFilter;
import jp.ac.chitose.ir.presentation.views.student.filterablecomponent.FilterPosition;
import jp.ac.chitose.ir.presentation.views.student.filterablecomponent.FilterableComboBox;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public class GPAView extends VerticalLayout {
    private final GradeService gradeService;
    private final StudentGradeService studentGradeService;
    //private final GPAGraph graph;
    private final GradeGrid grid;
    private final static List<String> FILTER_NAMES = List.of("学年", "学科", "必選別", "成績評価");
    private final static List<RadioButtonValues> FILTER_VALUES = List.of(
            RadioButtonValues.SCHOOL_YEARS,
            RadioButtonValues.DEPARTMENTS,
            RadioButtonValues.SUBJECT_TYPES,
            RadioButtonValues.GRADES);
    private final static List<BiPredicate<StudentGrade, String>> FILTER_FUNCTIONS = List.of(
            (grade, str) -> matchesFilter(str, grade.schoolYear()),
            (grade, str) -> matchesFilter(str, grade.department()),
            (grade, str) -> matchesFilter(str, grade.compulsory_subjects()),
            (grade, str) -> matchesFilter(str, grade.grading()));
    private final static List<String> GRADE_HEADER_NAMES = List.of("対象学年", "対象学科", "必選別", "単位数", "成績評価");
    private final static List<ValueProvider<StudentGrade, String>> GRADE_VALUE_PROVIDERS = List.of(
            StudentGrade::schoolYear,
            StudentGrade::department,
            StudentGrade::compulsory_subjects,
            grade -> String.valueOf(grade.number_credits_course()),
            StudentGrade::grading);

    public GPAView(GradeService gradeService, StudentGradeService studentGradeService, String studentNumber, FilterableComboBox<String, StudentGrade> subjectComboBox) {
        this.gradeService = gradeService;
        this.studentGradeService = studentGradeService;
        /*String schoolYear = studentService.getStudentSchoolYear(studentNumber).data().get(0).学年();
        graph = new GPAGraph(gradeService, schoolYear);*/
        grid = createGradeGrid(studentNumber, subjectComboBox);
        addComponentToLayout();
    }

    private GradeGrid createGradeGrid(String studentNumber, FilterableComboBox<String, StudentGrade> subjectComboBox) {
        GradeGrid grid = new GradeGrid(createGradeGridFilters(), FilterPosition.TOP);
        grid.addComponentColumn(grade -> {
            Button button = new Button(grade.lecture_name());
            button.addClickListener(buttonClickEvent -> subjectComboBox.setValue(grade));
            return button;
        }).setHeader("科目名").setSortable(true);
        for(int i = 0; i < GRADE_HEADER_NAMES.size(); i++) {
            grid.addColumn(GRADE_VALUE_PROVIDERS.get(i), GRADE_HEADER_NAMES.get(i)).setSortable(true);
        }
        grid.setAllRowsVisible(true);
        grid.setItems(studentGradeService.getStudentNumberSubjects(studentNumber).data());
        return grid;
    }

    private List<Filter<String, StudentGrade>> createGradeGridFilters() {
        List<Filter<String, StudentGrade>> filters = new ArrayList<>();
        for(int i = 0; i < Math.min(FILTER_VALUES.size(), FILTER_FUNCTIONS.size()); i++) {
            filters.add(new RadioButtonFilter<>(FILTER_VALUES.get(i).getValues(), FILTER_FUNCTIONS.get(i), FILTER_NAMES.get(i)));
        }
        return filters;
    }

    private void addComponentToLayout() {
        //add(graph);
        add(grid);
    }

    private static boolean matchesFilter(String filterValue, String itemValue) {
        return filterValue.equals("全体") || filterValue.equals(itemValue);
    }
}
