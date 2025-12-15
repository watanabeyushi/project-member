package jp.ac.chitose.ir.presentation.views.commission.seiseki;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
//学年比較、学科比較のラジオボタンを用意するクラス
public class Seiseki {
    private RadioButtonGroup<String> r1;
    private RadioButtonGroup<String> r2;
    private RadioButtonGroup<String> r3;
    private RadioButtonGroup<String> r4;
    private RadioButtonGroup<String> r5;
    private RadioButtonGroup<String> r6;

    private int mode;
    private String str1;
    private String str2;
    public Seiseki(int mode){
        this.mode = mode;

        HorizontalLayout radioAndText = new HorizontalLayout();
        //mode=0 : yearFirstのレイアウト、mode=1 : subjectFirstのレイアウト
        //モード別にラジオボタンを割り振る
        if(mode == 0) {
            str1 = "学年";
            str2 = "学科";
            this.r1 = new RadioButtonGroup<>();
            r1.setLabel("学年選択");
            r1.setItems("全体", "1年", "2年", "3年", "4年");
            radioAndText.add(r1);



            this.r2 = new RadioButtonGroup<>();
            r2.setLabel("学科を選択");
            r2.setItems("全体", "応用化学生物学科", "電子光工学科", "情報システム工学科", "共通教育");
            r2.setVisible(false);
            r1.addValueChangeListener(e -> r2.setVisible(e.getValue().equals("全体")));


            this.r3 = new RadioButtonGroup<>();
            r3.setLabel("クラスを選択");
            r3.setItems("学年全体", "Aクラス", "Bクラス", "Cクラス", "Dクラス");
            r3.setVisible(false);
            r1.addValueChangeListener(e -> r3.setVisible(e.getValue().equals("1年")));


            this.r4 = new RadioButtonGroup<>();
            r4.setLabel("学科を選択");
            r4.setItems("学年全体", "応用化学生物学科", "電子光工学科", "情報システム工学科");
            r4.setVisible(false);
            r1.addValueChangeListener(e -> r4.setVisible(e.getValue().equals("2年")));


            this.r5 = new RadioButtonGroup<>();
            r5.setLabel("学科を選択");
            r5.setItems("学年全体", "応用化学生物学科", "電子光工学科", "情報システム工学科");
            r5.setVisible(false);
            r1.addValueChangeListener(e -> r5.setVisible(e.getValue().equals("3年")));


            this.r6 = new RadioButtonGroup<>();
            r6.setLabel("学科を選択");
            r6.setItems("学年全体", "応用化学生物学科", "電子光工学科", "情報システム工学科");
            r6.setVisible(false);
            r1.addValueChangeListener(e -> r6.setVisible(e.getValue().equals("4年")));


        } else if (mode == 1) {
            str1 = "学科";
            str2 = "学年";
            this.r1 = new RadioButtonGroup<>();
            r1.setLabel("学科選択");
            r1.setItems("全体", "応用化学生物学科","電子光工学科", "情報システム工学科");
            radioAndText.add(r1);

            this.r2 = new RadioButtonGroup<>();
            r2.setLabel("学年選択");
            r2.setItems("全体","1年", "2年", "3年", "4年");
            r2.setVisible(false);
            r1.addValueChangeListener(e -> r2.setVisible(e.getValue().equals("全体")));

            this.r3 = new RadioButtonGroup<>();
            r3.setLabel("学年選択");
            r3.setItems("全体", "2年",  "3年", "4年");
            r3.setVisible(false);
            r1.addValueChangeListener(e -> r3.setVisible(e.getValue().equals("応用化学生物学科")));

            this.r4 = new RadioButtonGroup<>();
            r4.setLabel("学年選択");
            r4.setItems("全体", "2年",  "3年", "4年");
            r4.setVisible(false);
            r1.addValueChangeListener(e -> r4.setVisible(e.getValue().equals("電子光工学科")));

            this.r5 = new RadioButtonGroup<>();
            r5.setLabel("学年選択");
            r5.setItems("全体", "2年",  "3年", "4年");
            r5.setVisible(false);
            r1.addValueChangeListener(e -> r5.setVisible(e.getValue().equals("情報システム工学科")));
        }

    }
    //ラジオボタンを表示するレイアウトを返すクラス
    public VerticalLayout view(){
        VerticalLayout main = new VerticalLayout();
        main.add(new H3(str1));
        main.add(r1);
        main.add(new H3(str2));
        main.add(r2);
        main.add(r3);
        main.add(r4);
        main.add(r5);
        //学科で比較する場合、ラジオボタンは1つ不要
        if(mode==0) {
            main.add(r6);
        }
        return main;
    }
    //各ラジオボタンを返すクラス
    public RadioButtonGroup<String> getR1(){return r1;}
    public RadioButtonGroup<String> getR2(){return r2;}
    public RadioButtonGroup<String> getR3(){return r3;}
    public RadioButtonGroup<String> getR4(){return r4;}
    public RadioButtonGroup<String> getR5(){return r5;}
    public RadioButtonGroup<String> getR6(){return r6;}
    public int getMode(){
        return mode;
    }

}
