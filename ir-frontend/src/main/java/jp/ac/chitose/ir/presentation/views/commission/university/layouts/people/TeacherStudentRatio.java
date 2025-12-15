package jp.ac.chitose.ir.presentation.views.commission.university.layouts.people;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jp.ac.chitose.ir.presentation.views.commission.university.components.BackButton;

//教員：学生比率
public class TeacherStudentRatio extends VerticalLayout {
    public TeacherStudentRatio(BackButton backButton) {
        add(new H1("教員と学生の比率"));
        add(new Paragraph("説明"));
        add(backButton);
    }
}
