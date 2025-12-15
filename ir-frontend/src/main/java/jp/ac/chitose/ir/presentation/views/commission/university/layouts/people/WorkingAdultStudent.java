package jp.ac.chitose.ir.presentation.views.commission.university.layouts.people;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jp.ac.chitose.ir.presentation.views.commission.university.components.BackButton;

//社会人学生
public class WorkingAdultStudent extends VerticalLayout {
    public WorkingAdultStudent(BackButton backButton){
                add(new H1("社会人学生数"));
                add(new Paragraph("説明"));
                add(backButton);
    }
}
