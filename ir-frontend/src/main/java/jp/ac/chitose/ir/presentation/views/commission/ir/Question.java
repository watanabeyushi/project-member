package jp.ac.chitose.ir.presentation.views.commission.ir;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

public class Question {
    private String questionName;
    private String question;
    //質問文とその回答を表示するクラス
    public Question(String questionName,String question){
        this.questionName = questionName;
        this.question = question;
    }

    public VerticalLayout view(){
        VerticalLayout main = new VerticalLayout();

        main.add(new H2(question));
        main.add(new Paragraph(("説明文")));

        RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
        radioGroup.setLabel("学科");
        radioGroup.setItems("全体", "応用科学生物学科","電子光工学科","情報システム工学科");
        main.add(radioGroup);
        main.add(new H2("表やグラフを用いてアンケート結果を表示する予定"));

        main.setVisible(false);

        return main;
    }

    public String getQuestionName(){
        return questionName;
    }
}
