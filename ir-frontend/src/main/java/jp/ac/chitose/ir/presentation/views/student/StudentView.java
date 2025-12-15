package jp.ac.chitose.ir.presentation.views.student;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import jp.ac.chitose.ir.application.service.commission.GradeService;
import jp.ac.chitose.ir.application.service.management.SecurityService;
import jp.ac.chitose.ir.application.service.student.StudentGrade;
import jp.ac.chitose.ir.application.service.student.StudentGradeService;
import jp.ac.chitose.ir.presentation.component.MainLayout;
import jp.ac.chitose.ir.presentation.views.student.filter.Filter;
import jp.ac.chitose.ir.presentation.views.student.filter.RadioButtonFilter;
import jp.ac.chitose.ir.presentation.views.student.filterablecomponent.FilterPosition;
import jp.ac.chitose.ir.presentation.views.student.filterablecomponent.FilterableComboBox;
import jp.ac.chitose.ir.presentation.views.student.gpaview.GPAView;
import jp.ac.chitose.ir.presentation.views.student.subjectview.SubjectView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;

@PermitAll
@PageTitle("GradeStudent")
@Route(value = "grade/student", layout = MainLayout.class)
public class StudentView extends VerticalLayout {
    private final StudentGradeService studentGradeService;
    private final SecurityService securityService;
    private final FilterableComboBox<String, StudentGrade> subjectComboBox;
    private final GPAView gpaView;
    private final SubjectView subjectView;
    private final static String[] FILTER_NAMES = {"学年", "学科"};
    private final static RadioButtonValues[] FILTER_VALUES = {RadioButtonValues.SCHOOL_YEARS, RadioButtonValues.DEPARTMENTS};
    private final static List<BiPredicate<StudentGrade, String>> FILTER_FUNCTIONS = List.of(
            (grade, str) -> matchesFilter(str, grade.schoolYear()),
            (grade, str) -> matchesFilter(str, grade.department()));

    public StudentView(StudentGradeService studentGradeService, GradeService gradeService, SecurityService securityService) {
        this.studentGradeService = studentGradeService;
        this.securityService = securityService;
        subjectComboBox = new FilterableComboBox<>("科目名", createComboBoxFilters(), FilterPosition.TOP);
        String studentNumber = securityService.getLoginUser().getLoginId();
        subjectView = new SubjectView(studentGradeService, studentGradeService.getStudentNumberSubjects(studentNumber).data().get(0).account_id());
        gpaView = new GPAView(gradeService, studentGradeService, studentNumber, subjectComboBox);
        // 画面タイトルと説明
        H1 title = new H1("履修した科目の詳細情報");
        Paragraph description = new Paragraph("履修した科目で設定した目標や振り返り、自身の成績などが確認できます。");

        initializeSubjectComboBox();
        add(title, description);
        addComponentToLayout();
    }

    private static boolean matchesFilter(String filterValue, String itemValue) {
        return filterValue.equals("全体") || filterValue.equals(itemValue);
    }

    private List<Filter<String, StudentGrade>> createComboBoxFilters() {
        List<Filter<String, StudentGrade>> filters = new ArrayList<>();
        for(int i = 0; i < FILTER_NAMES.length; i++) {
            filters.add(new RadioButtonFilter<>(FILTER_VALUES[i].getValues(), FILTER_FUNCTIONS.get(i), FILTER_NAMES[i]));
        }
        return filters;
    }

    private void initializeSubjectComboBox() {
        String studentNumber = securityService.getLoginUser().getLoginId();
        subjectComboBox.setComboBoxWidth("40%");
        subjectComboBox.setItemLabelGenerator(StudentGrade::lecture_name);
        subjectComboBox.setPlaceholder("GPAのグラフを表示しています。選んだ科目のグラフに切り替わります。");
        subjectComboBox.setItems(studentGradeService.getStudentNumberSubjects(studentNumber).data());
        subjectComboBox.setClearButtonVisible(true);
        subjectComboBox.addValueChangeListener(this::updateView);
    }

    private void updateView(AbstractField.ComponentValueChangeEvent<ComboBox<StudentGrade>, StudentGrade> event) {
        if (event.getValue() == null) {
            remove(subjectComboBox);
            remove(subjectView);
            add(gpaView);
        } else {
            subjectView.update(findByCourseId(event.getValue().course_id()));
            remove(gpaView);
            add(subjectComboBox, subjectView);
        }
    }

    private StudentGrade findByCourseId(String courseId) {
        Optional<StudentGrade> gradeOptional = subjectComboBox.getItems()
                .filter(item -> item.course_id().equals(courseId))
                .findFirst();
        return gradeOptional.orElse(subjectComboBox.getItems().findFirst().get());
    }

    private void addComponentToLayout() {
        add(gpaView);
    }
}