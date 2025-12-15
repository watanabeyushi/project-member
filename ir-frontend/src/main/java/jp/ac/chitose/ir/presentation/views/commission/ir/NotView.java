package jp.ac.chitose.ir.presentation.views.commission.ir;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//実装していないページを
public class NotView {
    public static VerticalLayout view(){
        VerticalLayout main = new VerticalLayout();
        main.add(new H2("未実装"));
        return main;
    }
}
