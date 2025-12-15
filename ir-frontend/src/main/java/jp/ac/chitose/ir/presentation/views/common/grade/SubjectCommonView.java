package jp.ac.chitose.ir.presentation.views.common.grade;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import jp.ac.chitose.ir.application.service.class_select.*;
import jp.ac.chitose.ir.application.service.student.GradeCount;
import jp.ac.chitose.ir.application.service.student.StudentGrade;
import jp.ac.chitose.ir.application.service.student.StudentGradeService;
import jp.ac.chitose.ir.presentation.component.scroll.ScrollManager;
import jp.ac.chitose.ir.presentation.views.questionnaire.QuestionnaireGrid;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SubjectCommonView extends VerticalLayout {
    private final StudentGradeService studentGradeService;
    private final CommonView commonView;
    private final H1 lectureName;
    private final Button backButton;
    private final RadioButtonGroup<String> years;
    private final CommonGraph commonGraph;
    private ClassSelect classSelect;
    private ScrollManager scrollManager;
    private QuestionnaireGraph questionnaireGraph;
    private QuestionMatters questionMatters;
    private jp.ac.chitose.ir.application.service.class_select.QuestionnaireGrid questionGrid;
    private QuestionDescribe questionDescribe;
    private QuestionnaireGrid questionnaireGrid;
    private final VerticalLayout review;

    public SubjectCommonView(StudentGradeService studentGradeService, ClassSelect classSelect, CommonView commonView) {
        this.studentGradeService = studentGradeService;
        this.commonView = commonView;
        this.review = new VerticalLayout();
        this.lectureName = new H1();
        this.backButton = createBackButton(commonView);
        years = new RadioButtonGroup<>();
        commonGraph = new CommonGraph();
        add(lectureName, backButton, years, commonGraph, review);
        this.classSelect = classSelect;
        this.scrollManager = new ScrollManager();
        this.questionnaireGraph=new QuestionnaireGraph();
        this.questionMatters = new QuestionMatters(scrollManager);
        this.questionGrid = new jp.ac.chitose.ir.application.service.class_select.QuestionnaireGrid();
        this.questionDescribe = new QuestionDescribe(classSelect);
    }

    public void updateView() {
        commonView.updateView(null);
    }

    public void updateData(StudentGrade grade) {
        review.removeAll();
        lectureName.setText(grade.lecture_name());
        Map<String, Map<String, String>> dict = commonView.getCourseIdDict().courseIdDict();
        List<String> keys = new java.util.ArrayList<>(dict.get(grade.lecture_name()).keySet().stream().sorted().toList());
        Collections.reverse(keys);
        years.setItems(keys);
        years.setValue(keys.get(0));
        years.addValueChangeListener(value -> updateGraph(value.getValue()));
        String courseId = dict.get(grade.lecture_name()).get(years.getValue());
        GradeCount histData = studentGradeService.getGradeGraph(dict.get(grade.lecture_name()).get(years.getValue())).data().get(0);
        commonGraph.updateGraphs(histData, grade.lecture_name());
        var classTests = classSelect.getClassQPOJFICHKVJB(courseId).data();
        var Classtitle = classSelect.getReviewTitle(courseId).data();
        var ranking =classTests.get(0);
        var reviewData = classSelect.getReviewQPOJFICHKVJBDescription(courseId).data();
        if(classTests.size() > 0) {
            int flag = classTests.get(0).Flag();
            for (int i = 0; i < 11; i++) {
                if (i == 3 && flag == 1) {
                    review.add(questionMatters.generateQuestionMatters(3,Classtitle));
                    review.add(questionGrid.generateGrid(i,reviewData)); // 自由記述
                    continue;
                }

                review.add(questionMatters.generateQuestionMatters(i,Classtitle)); // Example
                review.add(questionnaireGraph.generateQuestionnaireGraph(i + 4,classTests).getGraph());
                review.add(questionDescribe.getStatics(i + 4, courseId,ranking));
            }

            // 追加のループで質問と自由記述を追加
            for (int i = 13; i <= 15; i++) {
                review.add(questionMatters.generateQuestionMatters(i,Classtitle));
                review.add(questionGrid.generateGrid(i,reviewData)); // 自由記述
            }
        }
    }

    private void updateGraph(String year) {
        Map<String, Map<String, String>> dict = commonView.getCourseIdDict().courseIdDict();
        String courseId = dict.get(lectureName.getText()).get(year);
        GradeCount histData = studentGradeService.getGradeGraph(courseId).data().get(0);
        commonGraph.updateGraphs(histData, lectureName.getText());
        review.removeAll();
        var classTests = classSelect.getClassQPOJFICHKVJB(courseId).data();
        var Classtitle = classSelect.getReviewTitle(courseId).data();
        var ranking = classTests.get(0);
        List<ReviewQPOJFICHKVJBDescription> reviewData = classSelect.getReviewQPOJFICHKVJBDescription(courseId).data();
        if(classTests.size() > 0) {
            int flag = classTests.get(0).Flag();
            for (int i = 0; i < 11; i++) {
                if (i == 3 && flag == 1) {
                    review.add(questionMatters.generateQuestionMatters(3,Classtitle));
                    review.add(questionGrid.generateGrid(i,reviewData)); // 自由記述
                    continue;
                }

                review.add(questionMatters.generateQuestionMatters(i,Classtitle)); // Example
                review.add(questionnaireGraph.generateQuestionnaireGraph(i + 4,classTests).getGraph());
                review.add(questionDescribe.getStatics(i + 4, courseId,ranking));
            }

            // 追加のループで質問と自由記述を追加
            for (int i = 13; i <= 15; i++) {
                review.add(questionMatters.generateQuestionMatters(i,Classtitle));
                review.add(questionGrid.generateGrid(i,reviewData)); // 自由記述
            }
        }
    }

    private Button createBackButton(CommonView commonView) {
        Button button = new Button("戻る");
        button.addClickListener(click -> commonView.updateView(null));
        return button;
    }
}
