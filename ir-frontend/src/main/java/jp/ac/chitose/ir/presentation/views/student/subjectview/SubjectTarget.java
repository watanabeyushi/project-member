package jp.ac.chitose.ir.presentation.views.student.subjectview;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jp.ac.chitose.ir.application.service.student.StudentGradeService;
import jp.ac.chitose.ir.application.service.student.Target;

import java.util.ArrayList;
import java.util.List;

public class SubjectTarget extends VerticalLayout {
    private final Grid<TargetRow> grid;
    private final H3 targetQuestion;
    private final H3 reviewQuestion;
    private final Paragraph targetAnswer;
    private final Paragraph reviewAnswer;
    private final String accountId;
    private final StudentGradeService studentGradeService;

    public SubjectTarget(final StudentGradeService studentGradeService, String studentNumber) {
        this.studentGradeService = studentGradeService;
        this.accountId = studentNumber;
        targetQuestion = new H3();
        reviewQuestion = new H3();
        targetAnswer = createParagraph();
        reviewAnswer = createParagraph();
        grid = createGrid();
        addComponentToLayout();
    }

    private Grid<TargetRow> createGrid() {
        Grid<TargetRow> grid = new Grid<>();
        grid.addColumn(TargetRow::attribute).setAutoWidth(true).setFlexGrow(0);;
        grid.addColumn(TargetRow::text);
        grid.setAllRowsVisible(true);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        return grid;
    }

    private void addComponentToLayout() {
        add(targetQuestion, targetAnswer);
        add(reviewQuestion, reviewAnswer);
        add(grid);
    }

    private Paragraph createParagraph() {
        Paragraph paragraph = new Paragraph();
        paragraph.getStyle().set("margin-left", "2em");
        return paragraph;
    }

    public void update(String courseId) {
        List<Target> subjectTargets = studentGradeService.getSubjectTarget(accountId, courseId).data();
        if(!subjectTargets.isEmpty()) {
            Target subjectTarget = subjectTargets.get(0);
            List<TargetRow> items = new ArrayList<>();
            if(subjectTarget.target_question_1().equals("なし")) items.add(new QuestionRow(grid.getListDataView().getItem(0).text()));
            else items.add(new QuestionRow(subjectTarget.target_question_1()));
            items.add(new AnswerRow(subjectTarget.target_answer_1()));
            if(subjectTarget.review_question_1().equals("なし")) items.add(new AnswerRow(grid.getListDataView().getItem(1).text()));
            else items.add(new QuestionRow(subjectTarget.review_question_1()));
            items.add(new AnswerRow(subjectTarget.review_answer_1()));
            grid.setItems(items);
        }
    }
}
