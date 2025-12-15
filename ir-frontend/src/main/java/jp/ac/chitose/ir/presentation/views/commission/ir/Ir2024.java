package jp.ac.chitose.ir.presentation.views.commission.ir;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;

import java.util.ArrayList;
//2024年度のIRアンケート結果を記述するクラス
public class Ir2024 {

    public VerticalLayout view(){
        VerticalLayout main = new VerticalLayout();
        RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
        radioGroup.setLabel("学年");
        radioGroup.setItems("全体", "1年生", "2年生","3年生","4年生");
        main.add(radioGroup);

        VerticalLayout all = new VerticalLayout();//全体
        VerticalLayout first = new VerticalLayout();//1年生
        VerticalLayout second = new VerticalLayout();//2年生
        VerticalLayout third = new VerticalLayout();//3年生
        VerticalLayout fourth = new VerticalLayout();//4年生
        all.setVisible(false);
        first.setVisible(false);
        second.setVisible(false);
        third.setVisible(false);
        fourth.setVisible(false);
        main.add(all);
        main.add(first);
        main.add(second);
        main.add(third);
        main.add(fourth);

        radioGroup.addValueChangeListener(e -> all.setVisible(e.getValue().equals("全体")));
        radioGroup.addValueChangeListener(e -> first.setVisible(e.getValue().equals("1年生")));
        radioGroup.addValueChangeListener(e -> second.setVisible(e.getValue().equals("2年生")));
        radioGroup.addValueChangeListener(e -> third.setVisible(e.getValue().equals("3年生")));
        radioGroup.addValueChangeListener(e -> fourth.setVisible(e.getValue().equals("4年生")));

        //ここから全体
        all.add(NotView.view());

        //ここから1年生
        first.add((NotView.view()));

        //ここから2年生


        ArrayList<Question> questions2 = new ArrayList<>();
        //各質問のインスタンスを作成
        Question course2 = new Question("進路希望","現時点における希望進路を教えてください。");
        Question job2 = new Question("希望職種","現時点における将来の希望職種を教えてください。");
        Question learningSatisfaction2 = new Question("学びについての満足度","本学での入学から今までの学びについて総合的に満足していますか。");
        Question commonSatisfaction2 = new Question("共通教育での学び（カリキュラム）についての満足度","共通教育での学び（カリキュラム）について満足していますか。");
        Question majorSatisfaction2 = new Question("所属学科での学び（カリキュラム）についての満足度","所属学科での学び（カリキュラム）について満足していますか。");
        Question commonGoodSubject2 = new Question("ためになった科目（共通教育）","共通教育科目の中で、自分のためになった科目を書いてください。");
        Question commonBadSubject2 = new Question("ためにならなかった科目（共通教育）","共通教育科目の中で、自分のためにならなかった科目を書いてください。");
        Question majorGoodSubject2 = new Question("ためになった科目（学科科目）","学科科目の中で、自分のためになった科目を書いてください");
        Question majorBadSubject2 = new Question("ためにならなかった科目（学科科目）","学科科目の中で、自分のためにならなかった科目を書いてください。");
        Question timeOfStudy2 = new Question("授業外学習時間","授業の予習・復習などの授業外学習（全ての授業）に1週間あたり平均何時間取り組みましたか。整数を半角で入力してください。");
        Question seasonOfMajor2 = new Question("学科配属の時期","適切だと思う学科配属の時期について聞かせてください。");
        Question schoolSatisfaction2 = new Question("学生生活（サークル・学生活動）の満足度","授業以外の学生生活（サークル・学生活動）について満足していますか。");
        Question friend2 = new Question("友人関係","大学時代に親しい友人を得ることができましたか。");
        Question facilitySatisfaction2 = new Question("設備・環境の満足度","本学の設備・環境には満足していますか。");
        Question counter2 = new Question("窓口対応","本学の事務職員の窓口対応は適切ですか。");
        Question powerOfWant2 = new Question("力を入れたいこと","今後、本学での学びで力を入れたいことを選んでください（複数回答可）。");
        Question powerOfSociety2 = new Question("社会で活かしたい力","将来、社会で活かしたい力はどれでしょうか（複数回答可）。");
        //作成したインスタンスをリストに格納
        questions2.add(course2);
        questions2.add(job2);
        questions2.add(learningSatisfaction2);
        questions2.add(commonSatisfaction2);
        questions2.add(majorSatisfaction2);
        questions2.add(commonGoodSubject2);
        questions2.add(commonBadSubject2);
        questions2.add(majorGoodSubject2);
        questions2.add(majorBadSubject2);
        questions2.add(timeOfStudy2);
        questions2.add(seasonOfMajor2);
        questions2.add(schoolSatisfaction2);
        questions2.add(friend2);
        questions2.add(facilitySatisfaction2);
        questions2.add(counter2);
        questions2.add(powerOfWant2);
        questions2.add(powerOfSociety2);
        //インスタンスを用いて各質問のレイアウトの作成
        VerticalLayout course2Layout = course2.view();
        VerticalLayout job2Layout = job2.view();
        VerticalLayout learningSatisfaction2Layout = learningSatisfaction2.view();
        VerticalLayout commonSatisfaction2Layout = commonSatisfaction2.view();
        VerticalLayout majorSatisfaction2Layout = majorSatisfaction2.view();
        VerticalLayout commonGoodSubject2Layout = commonSatisfaction2.view();
        VerticalLayout commonBadSubject2Layout = commonBadSubject2.view();
        VerticalLayout majorGoodSubject2Layout = majorGoodSubject2.view();
        VerticalLayout majorBadSubjectLayout = majorBadSubject2.view();
        VerticalLayout timeOfStudy2Layout = timeOfStudy2.view();
        VerticalLayout seasonOfMajor2Layout = seasonOfMajor2.view();
        VerticalLayout schoolSatisfaction2Layout = schoolSatisfaction2.view();
        VerticalLayout friend2Layout = friend2.view();
        VerticalLayout facilitySatisfaction2Layout = facilitySatisfaction2.view();
        VerticalLayout counter2Layout = counter2.view();
        VerticalLayout powerOfWant2Layout = powerOfWant2.view();
        VerticalLayout powerOfSociety2Layout = powerOfSociety2.view();
        //各レイアウトをリストに格納
        ArrayList<VerticalLayout> questions2List = new ArrayList<>();
        questions2List.add(course2Layout);
        questions2List.add(job2Layout);
        questions2List.add(learningSatisfaction2Layout);
        questions2List.add(commonSatisfaction2Layout);
        questions2List.add(majorSatisfaction2Layout);
        questions2List.add(commonGoodSubject2Layout);
        questions2List.add(commonBadSubject2Layout);
        questions2List.add(majorGoodSubject2Layout);
        questions2List.add(majorBadSubjectLayout);
        questions2List.add(timeOfStudy2Layout);
        questions2List.add(seasonOfMajor2Layout);
        questions2List.add(schoolSatisfaction2Layout);
        questions2List.add(friend2Layout);
        questions2List.add(facilitySatisfaction2Layout);
        questions2List.add(counter2Layout);
        questions2List.add(powerOfWant2Layout);
        questions2List.add(powerOfSociety2Layout);
        //セレクトボックスの追加
        second.add(new H3("目次"));
        Select<String> selectSecond = new Select<>();
        selectSecond.setLabel("質問項目一覧");
        ArrayList<String> nameList = new ArrayList<>();
        for(Question question :questions2){
            nameList.add(question.getQuestionName());
        }
        selectSecond.setItems(nameList);
        second.add(selectSecond);
        //レイアウトの追加
        for(VerticalLayout layout : questions2List){
            second.add(layout);
        }

            selectSecond.addValueChangeListener(e -> {
                for(int i = 0;i < questions2List.size();i++) {
                    questions2List.get(i).setVisible(e.getValue().equals(questions2.get(i).getQuestionName()));
                }
            });



//        VerticalLayout course2 = new VerticalLayout();//進路希望
//        VerticalLayout job2 = new VerticalLayout();//希望職種
//        VerticalLayout learningSatisfaction2 = new VerticalLayout();//学びについての満足度
//        VerticalLayout commonSatisfaction2 = new VerticalLayout();//共通教育での学び（カリキュラム）についての満足度
//        VerticalLayout majorSatisfaction2 = new VerticalLayout();//所属学科での学び（カリキュラム）についての満足度
//        VerticalLayout commonGoodSubject2 = new VerticalLayout();//ためになった科目（共通教育）
//        VerticalLayout commonBadSubject2 = new VerticalLayout();//ためにならなかった科目（共通教育）
//        VerticalLayout majorGoodSubject2 = new VerticalLayout();//ためになった科目（学科科目）
//        VerticalLayout majorBadSubject2 = new VerticalLayout();//ためにならなかった科目（学科科目）
//        VerticalLayout timeOfStudy2 = new VerticalLayout();//授業外学習時間
//        VerticalLayout seasonOfMajor2 = new VerticalLayout();//学科配属の時期
//        VerticalLayout schoolSatisfaction2 = new VerticalLayout();//学生生活（サークル・学生活動）の満足度
//        VerticalLayout friend2 = new VerticalLayout();//友人関係
//        VerticalLayout facilitySatisfaction2 = new VerticalLayout();//設備・環境の満足度
//        VerticalLayout counter2 = new VerticalLayout();//窓口対応
//        VerticalLayout powerOfWant2 = new VerticalLayout();//力を入れたいこと
//        VerticalLayout powerOfSociety2 = new VerticalLayout();//社会で活かしたい力

        //ここから3年生
        third.add(NotView.view());
        //ここから4年生
        fourth.add(NotView.view());


        return main;
    }
}
