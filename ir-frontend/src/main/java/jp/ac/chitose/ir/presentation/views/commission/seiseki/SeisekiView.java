package jp.ac.chitose.ir.presentation.views.commission.seiseki;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import jp.ac.chitose.ir.application.service.commission.GradeService;

public class SeisekiView implements View {

    private VerticalLayout yearFirstLayout;
    private VerticalLayout subjectFirstLayout;
    private GradeService gradeService;


    public SeisekiView(GradeService gradeService){
        this.gradeService = gradeService;
    }
    //成績統計に関するレイアウトを返すメソッド
    public VerticalLayout view(){

        VerticalLayout main = new VerticalLayout();
        H1 a = new H1("GPAと基本統計量に関するデータ");
        main.add(a);

        main.add(new Paragraph("この画面ではGPAに関する情報を見ることが出来ます。"));
        main.add(new H2("2022年度"));
        //学年・学科を選択するラジオボタン
        RadioButtonGroup<String> yearOrSubject = new RadioButtonGroup<>();
        yearOrSubject.setItems("学年","学科");
        yearOrSubject.setValue("学年");
        main.add(yearOrSubject);

        yearFirstLayout = getSeisekiYearFirstLayout();
        main.add(yearFirstLayout);

        //学年・学科の選択により、片方のレイアウトだけが表示されるようにラジオボタンを制御
        yearOrSubject.addValueChangeListener(e -> {
            if(e.getValue().equals("学年")){
                yearFirstLayout = getSeisekiYearFirstLayout();
                main.add(yearFirstLayout);
                main.remove(subjectFirstLayout);
            }
            else if(e.getValue().equals("学科")){
                subjectFirstLayout = getSeisekiSubjectFirstLayout();
                main.remove(yearFirstLayout);
                main.add(subjectFirstLayout);
            }
        });
        return main;
    }
    //学科で比較するレイアウトを返すメソッド
    private VerticalLayout getSeisekiYearFirstLayout(){
        VerticalLayout seisekiYearFirstLayout = new VerticalLayout();
        Seiseki seiseki = new Seiseki(0);
        seisekiYearFirstLayout.add(seiseki.view());
        SeisekiGraphView seisekiGraphView=new SeisekiGraphView(seiseki,gradeService);
        seisekiYearFirstLayout.add(seisekiGraphView.view());
        return seisekiYearFirstLayout;

    }
    //学年で比較する例アウトを返すメソッド
    private VerticalLayout getSeisekiSubjectFirstLayout(){
        VerticalLayout seisekiSubjectFirstLayout = new VerticalLayout();
        Seiseki seiseki = new Seiseki(1);
        seisekiSubjectFirstLayout.add(seiseki.view());
        SeisekiGraphView seisekiGraphView =  new SeisekiGraphView(seiseki,gradeService);
        seisekiSubjectFirstLayout.add(seisekiGraphView.view());
        return seisekiSubjectFirstLayout;
    }


}
