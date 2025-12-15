package jp.ac.chitose.ir.presentation.views.commission.university.layouts.people;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jp.ac.chitose.ir.presentation.views.commission.university.components.BackButton;
//教員数
public class NumberOfTeachers extends VerticalLayout {

    private BackButton button;
    private String name;
    public NumberOfTeachers(BackButton backButton) {
        add(new H1("教員数"));
        add(new Paragraph("説明"));
        add(backButton);

    }

}
