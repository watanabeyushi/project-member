package jp.ac.chitose.ir.presentation.views.common.grade;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.ValueProvider;
import jp.ac.chitose.ir.application.service.student.StudentGrade;
import jp.ac.chitose.ir.application.service.student.StudentGradeService;
import jp.ac.chitose.ir.presentation.views.student.RadioButtonValues;
import jp.ac.chitose.ir.presentation.views.student.filter.Filter;
import jp.ac.chitose.ir.presentation.views.student.filter.RadioButtonFilter;
import jp.ac.chitose.ir.presentation.views.student.filterablecomponent.FilterPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public class SelectCommonView extends VerticalLayout {
    private final StudentGradeService studentGradeService;
    private final CommonGrid grid;
    private final static List<String> FILTER_NAMES = List.of("学年", "学科", "必選別");
    private final static List<RadioButtonValues> FILTER_VALUES = List.of(
            RadioButtonValues.SCHOOL_YEARS,
            RadioButtonValues.DEPARTMENTS,
            RadioButtonValues.SUBJECT_TYPES);
    private final static List<BiPredicate<StudentGrade, String>> FILTER_FUNCTIONS = List.of(
            (grade, str) -> matchesFilter(str, grade.schoolYear()),
            (grade, str) -> matchesFilter(str, grade.department()),
            (grade, str) -> matchesFilter(str, grade.compulsory_subjects()));
    private final static List<String> GRADE_HEADER_NAMES = List.of("対象学年", "対象学科", "必選別", "単位数");
    private final static List<ValueProvider<StudentGrade, String>> GRADE_VALUE_PROVIDERS = List.of(
            StudentGrade::schoolYear,
            StudentGrade::department,
            StudentGrade::compulsory_subjects,
            grade -> String.valueOf(grade.number_credits_course()));

    public SelectCommonView(final StudentGradeService studentGradeService, CommonView commonView) {
        this.studentGradeService = studentGradeService;
        grid = createCommonGrid(commonView);
        H1 title = new H1("授業に関する情報公開");
        Paragraph explanation = new Paragraph("成績評価分布や授業評価アンケートの結果を科目ごとに確認できます。");
        add(title, explanation, grid);
    }

    private CommonGrid createCommonGrid(CommonView commonView) {
        CommonGrid grid = new CommonGrid(createCommonGridFilters(), FilterPosition.TOP);
        grid.addComponentColumn(grade -> {
            Button button = new Button(grade.lecture_name());
            button.addClickListener(buttonClickEvent -> updateView(grade, commonView));
            return button;
        }).setHeader("科目名").setSortable(true);
        for(int i = 0; i < GRADE_HEADER_NAMES.size(); i++) {
            grid.addColumn(GRADE_VALUE_PROVIDERS.get(i), GRADE_HEADER_NAMES.get(i)).setSortable(true);
        }
        grid.setItems(studentGradeService.getSubjectStudents().data());
        grid.setAllRowsVisible(true);
        return grid;
    }

    public void updateView(StudentGrade grade, CommonView commonView) {
        commonView.updateView(grade);
    }

    private List<Filter<String, StudentGrade>> createCommonGridFilters() {
        List<Filter<String, StudentGrade>> filters = new ArrayList<>();
        for(int i = 0; i < Math.min(FILTER_VALUES.size(), FILTER_FUNCTIONS.size()); i++) {
            filters.add(new RadioButtonFilter<>(FILTER_VALUES.get(i).getValues(), FILTER_FUNCTIONS.get(i), FILTER_NAMES.get(i)));
        }
        return filters;
    }

    private static boolean matchesFilter(String filterValue, String itemValue) {
        return filterValue.equals("全体") || filterValue.equals(itemValue);
    }
}
