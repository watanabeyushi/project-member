package jp.ac.chitose.ir.presentation.views.common.grid;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import jp.ac.chitose.ir.application.service.student.GradeCount;
import jp.ac.chitose.ir.application.service.student.StudentGrade;
import jp.ac.chitose.ir.application.service.student.StudentGradeService;
import jp.ac.chitose.ir.presentation.component.MainLayout;
import jp.ac.chitose.ir.presentation.views.common.grade.CommonGrid;
import jp.ac.chitose.ir.presentation.views.student.RadioButtonValues;
import jp.ac.chitose.ir.presentation.views.student.filter.Filter;
import jp.ac.chitose.ir.presentation.views.student.filter.RadioButtonFilter;
import jp.ac.chitose.ir.presentation.views.student.filterablecomponent.FilterPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

@PermitAll
@PageTitle("commonGrid")
@Route(value = "common/grid", layout = MainLayout.class)
public class CommonGridView extends VerticalLayout {
    private final StudentGradeService studentGradeService;
    private final Grid<GradeCount> gradeGrid;
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
    private final static List<String> GRADE_HEADER_NAMES = List.of("科目名", "開講年", "対象学年", "対象学科", "必選別", "単位数");
    private final static List<ValueProvider<StudentGrade, String>> GRADE_VALUE_PROVIDERS = List.of(
            StudentGrade::lecture_name,
            grade -> String.valueOf(grade.available_year()),
            StudentGrade::schoolYear,
            StudentGrade::department,
            StudentGrade::compulsory_subjects,
            grade -> String.valueOf(grade.number_credits_course()));
    private final static List<String> SUBJECT_HEADER_NAMES = List.of("科目名", "担当教員名", "受講人数", "不可", "可", "良", "優", "秀", "平均", "分散");
    private final static List<ValueProvider<GradeCount, String>> SUBJECT_VALUE_PROVIDERS = List.of(
            GradeCount::科目名,
            GradeCount::教員名,
            count -> String.valueOf(count.合計の人数()),
            count -> String.valueOf(count.不可()),
            count -> String.valueOf(count.可()),
            count -> String.valueOf(count.良()),
            count -> String.valueOf(count.優()),
            count -> String.valueOf(count.秀()),
            count -> String.valueOf((float) Math.round(count.平均() * 100) / 100f),
            count -> String.valueOf((float) Math.round(count.分散() * 100) / 100f));

    public CommonGridView(final StudentGradeService studentGradeService) {
        this.studentGradeService = studentGradeService;
        gradeGrid = createGradeGrid();
        grid = createCommonGrid();
        H1 title = new H1("成績評価分布状況表");
        Paragraph explanation = new Paragraph("成績評価分布をまとめた表を確認できます。");
        Button button = new Button("決定");
        Button backButton = new Button("戻る");
        button.addClickListener(buttonClickEvent -> {
            List<GradeCount> gradeCounts = new ArrayList<>();
            for(var item : grid.getSelectedItems()) {
                boolean ok = true;
                for(var filter : grid.getTypeFilters()) {
                    if(!ok) break;
                    ok = filter.applyFilter(item);
                }
                if(!ok) continue;
                gradeCounts.add(studentGradeService.getGradeGraph(item.course_id()).data().get(0));
            }
            gradeGrid.setItems(gradeCounts);
            removeAll();
            add(backButton, gradeGrid);
        });
        backButton.addClickListener(backButtonClickEvent -> {
            removeAll();
            add(title, explanation, grid, button);
        });
        add(title, explanation, grid, button);
    }

    private CommonGrid createCommonGrid() {
        CommonGrid grid = new CommonGrid(createCommonGridFilters(), FilterPosition.TOP);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        for(int i = 0; i < GRADE_HEADER_NAMES.size(); i++) {
            Grid.Column<StudentGrade> column = grid.addColumn(GRADE_VALUE_PROVIDERS.get(i), GRADE_HEADER_NAMES.get(i)).setSortable(true);
            if(GRADE_HEADER_NAMES.get(i).equals("科目名")) column.setWidth("20vw");
        }
        grid.setItems(studentGradeService.getAllSubjectStudents().data());
        grid.setAllRowsVisible(true);
        return grid;
    }

    private Grid<GradeCount> createGradeGrid() {
        Grid<GradeCount> grid = new Grid<>();
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        for(int i = 0; i < SUBJECT_HEADER_NAMES.size(); i++) {
            Grid.Column<GradeCount> column = grid.addColumn(SUBJECT_VALUE_PROVIDERS.get(i)).setHeader(SUBJECT_HEADER_NAMES.get(i)).setSortable(true);
            if(SUBJECT_HEADER_NAMES.get(i).equals("科目名") || SUBJECT_HEADER_NAMES.get(i).equals("担当教員名")) column.setWidth("20vw");
        }
        grid.setAllRowsVisible(true);
        return grid;
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
