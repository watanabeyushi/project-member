package jp.ac.chitose.ir.presentation.views.commission.university.layouts.people;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jp.ac.chitose.ir.presentation.views.commission.university.components.BackButton;

//休学者
public class LeaveOfAbsence extends VerticalLayout {
 public LeaveOfAbsence(BackButton backButton){
               add(new H1("休学者数"));
               add(new Paragraph("説明"));
               add(backButton);
 }

}
