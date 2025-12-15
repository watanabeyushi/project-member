package jp.ac.chitose.ir.presentation.views.commission.university.layouts.annual.teachertraining;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jp.ac.chitose.ir.application.service.commission.UniversityService;
import jp.ac.chitose.ir.presentation.views.commission.university.components.BackButton;

public class TeacherTraining extends VerticalLayout {
    private UniversityService universityService;

    //教職課程
    public TeacherTraining(UniversityService universityService, BackButton backButton) {
        this.universityService = universityService;
        add(new H1("教職課程"));
        add(new Paragraph("教職課程に関する情報を見ることが出来ます。"));
        add(backButton);
        add(new H2("・教職課程履修者数"));
        add(new H3("入学年"));
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(new Button("平成22年"));
        add(buttonLayout);
        add(new Heisei22(this.universityService));
    }

}
