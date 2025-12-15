package jp.ac.chitose.ir.presentation.views.class_select;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import jp.ac.chitose.ir.presentation.component.SubLayout;

@PageTitle("questionnaire_Top")
@Route(value = "class_select/Top", layout = SubLayout.class)
@PermitAll
public class QuestionnaireTop extends VerticalLayout {
    public QuestionnaireTop() {
        VerticalLayout layout = new VerticalLayout();
        add(new H1("test"));
        Anchor grade = new Anchor("/class_select/Top/QPOJFICHKVJB", "アンケート");
        add(grade);
    }
}
