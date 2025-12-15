package jp.ac.chitose.ir.presentation.views.commission.seiseki;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jp.ac.chitose.ir.application.service.commission.*;

import java.util.ArrayList;

public class SeisekiGraphView {
    SeisekiGraph seisekiGraph;
    Seiseki seiseki;
    private GradeService gradeService;


    public SeisekiGraphView(Seiseki seiseki, GradeService gradeService){
        this.gradeService=gradeService;
        this.seisekiGraph=new SeisekiGraph(this.gradeService);
        this.seiseki=seiseki;

    }
    //モード0:学科で比較、モード1:学年で比較
    public VerticalLayout view(){
        int mode = seiseki.getMode();
        VerticalLayout layout = new VerticalLayout();
        if(mode == 0){
            layout = compareBySubject();
        }
        else if(mode == 1){
            layout = compareByYear();
        }
        return layout;

    }
    //学科で比較するメソッド
    private VerticalLayout compareBySubject(){
        VerticalLayout main=new VerticalLayout();//グラフ全体のレイアウトを返す
        VerticalLayout all = new VerticalLayout();//学科全体を表示
        VerticalLayout first = new VerticalLayout();//1年生
        VerticalLayout second = new VerticalLayout();//2年生
        VerticalLayout third = new VerticalLayout();//３年生
        VerticalLayout fourth = new VerticalLayout();//４年生
        //全て非表示にする
        all.setVisible(false);
        first.setVisible(false);
        second.setVisible(false);
        third.setVisible(false);
        fourth.setVisible(false);
        //ラジオボタンの項目ごとに見せるレイアウトを分ける(選択されたものだけsetVisible(true))
        seiseki.getR1().addValueChangeListener(e->all.setVisible(e.getValue().equals("全体")));
        seiseki.getR1().addValueChangeListener(e->first.setVisible(e.getValue().equals("1年")));
        seiseki.getR1().addValueChangeListener(e->second.setVisible(e.getValue().equals("2年")));
        seiseki.getR1().addValueChangeListener(e->third.setVisible(e.getValue().equals("3年")));
        seiseki.getR1().addValueChangeListener(e->fourth.setVisible(e.getValue().equals("4年")));

        main.add(all,first,second,third,fourth);

        //ここからall
        HorizontalLayout layoutUpAll=new HorizontalLayout();
        FormLayout layoutUnderAll = getUnderLayout();
        //レイアウト呼び出し
        ArrayList<VerticalLayout> chartListAll=seisekiGraph.makeAllYear();
        ArrayList<VerticalLayout> chartListAllB=seisekiGraph.makeBigAllYear();
        //呼び出したレイアウトを追加
        for(VerticalLayout chart: chartListAll){
            layoutUnderAll.add(chart);
        }
        for(VerticalLayout chart: chartListAllB){
            layoutUpAll.add(chart);
            chart.setVisible(false);
        }

        //allに加える
        all.add(layoutUpAll);
        all.add(layoutUnderAll);

        //ラジオボタンの制御
        //ラジオボタンで選択されたものを上に大きく表示(ヒストグラム、円グラフ)、選択されていないものを下に小さく表示(ヒストグラム)
        seiseki.getR2().addValueChangeListener(e -> {
                    chartListAll.get(0).setVisible(!(e.getValue().equals("全体")));
                    chartListAllB.get(0).setVisible(e.getValue().equals("全体"));
                }
        );
        seiseki.getR2().addValueChangeListener(e -> {
            chartListAll.get(1).setVisible(!(e.getValue().equals("応用化学生物学科")));
            chartListAllB.get(1).setVisible(e.getValue().equals("応用化学生物学科"));
        });
        seiseki.getR2().addValueChangeListener(e -> {
            chartListAll.get(2).setVisible(!(e.getValue().equals("電子光工学科")));
            chartListAllB.get(2).setVisible(e.getValue().equals("電子光工学科"));

        });
        seiseki.getR2().addValueChangeListener(e -> {
            chartListAll.get(3).setVisible(!(e.getValue().equals("情報システム工学科")));
            chartListAllB.get(3).setVisible(e.getValue().equals("情報システム工学科"));
        });
        seiseki.getR2().addValueChangeListener(e -> {
            chartListAll.get(4).setVisible(!(e.getValue().equals("共通教育")));
            chartListAllB.get(4).setVisible(e.getValue().equals("共通教育"));

        });

        //ここまでall

        //ここからfirst
        //allと同じようにプログラムを設計
        HorizontalLayout layoutUpFirst=new HorizontalLayout();
        FormLayout layoutUnderFirst=getUnderLayout();
        ArrayList<VerticalLayout> chartListFirst=seisekiGraph.makeFirst();
        ArrayList<VerticalLayout> chartListFirstB =seisekiGraph.makeBigFirst();

        for(VerticalLayout chart: chartListFirst){
            layoutUnderFirst.add(chart);

        }
        for(VerticalLayout chart: chartListFirstB){

            layoutUpFirst.add(chart);
            chart.setVisible(false);
        }


        first.add(layoutUpFirst);
        first.add(layoutUnderFirst);


        seiseki.getR3().addValueChangeListener(e -> {
                    chartListFirst.get(0).setVisible(!(e.getValue().equals("学年全体")));
                    chartListFirstB.get(0).setVisible(e.getValue().equals("学年全体"));
                }
        );
        seiseki.getR3().addValueChangeListener(e -> {
            chartListFirst.get(1).setVisible(!(e.getValue().equals("Aクラス")));
            chartListFirstB.get(1).setVisible(e.getValue().equals("Aクラス"));
        });
        seiseki.getR3().addValueChangeListener(e -> {
            chartListFirst.get(2).setVisible(!(e.getValue().equals("Bクラス")));
            chartListFirstB.get(2).setVisible(e.getValue().equals("Bクラス"));

        });
        seiseki.getR3().addValueChangeListener(e -> {
            chartListFirst.get(3).setVisible(!(e.getValue().equals("Cクラス")));
            chartListFirstB.get(3).setVisible(e.getValue().equals("Cクラス"));
        });
        seiseki.getR3().addValueChangeListener(e -> {
            chartListFirst.get(4).setVisible(!(e.getValue().equals("Dクラス")));
            chartListFirstB.get(4).setVisible(e.getValue().equals("Dクラス"));

        });


        //ここからsecond
        HorizontalLayout layoutUpSecond =new HorizontalLayout();
        FormLayout layoutUnderSecond = getUnderLayout();
        ArrayList<VerticalLayout> chartListSecond =seisekiGraph.makeSecond();
        ArrayList<VerticalLayout> chartListSecondB=seisekiGraph.makeBigSecond();

        for(VerticalLayout chart: chartListSecond){
            layoutUnderSecond.add(chart);
        }
        for(VerticalLayout chart: chartListSecondB){
            layoutUpSecond.add(chart);
            chart.setVisible(false);
        }


        second.add(layoutUpSecond);
        second.add(layoutUnderSecond);


        seiseki.getR4().addValueChangeListener(e -> {
                    chartListSecond.get(0).setVisible(!(e.getValue().equals("学年全体")));
                    chartListSecondB.get(0).setVisible(e.getValue().equals("学年全体"));
                }
        );
        seiseki.getR4().addValueChangeListener(e -> {
            chartListSecond.get(1).setVisible(!(e.getValue().equals("応用化学生物学科")));
            chartListSecondB.get(1).setVisible(e.getValue().equals("応用化学生物学科"));
        });
        seiseki.getR4().addValueChangeListener(e -> {
            chartListSecond.get(2).setVisible(!(e.getValue().equals("電子光工学科")));
            chartListSecondB.get(2).setVisible(e.getValue().equals("電子光工学科"));

        });
        seiseki.getR4().addValueChangeListener(e -> {
            chartListSecond.get(3).setVisible(!(e.getValue().equals("情報システム工学科")));
            chartListSecondB.get(3).setVisible(e.getValue().equals("情報システム工学科"));
        });


        //ここまでsecond

        //ここからthird
        //allと同じようにプログラムを設計
        HorizontalLayout layoutUpThird =new HorizontalLayout();
        FormLayout layoutUnderThird =getUnderLayout();
        ArrayList<VerticalLayout> chartListThird =seisekiGraph.makeThird();
        ArrayList<VerticalLayout> chartListThirdB =seisekiGraph.makeBigThird();

        for(VerticalLayout chart: chartListThird){
            layoutUnderThird.add(chart);
        }
        for(VerticalLayout chart: chartListThirdB){
            layoutUpThird.add(chart);
            chart.setVisible(false);
        }


        third.add(layoutUpThird);
        third.add(layoutUnderThird);


        seiseki.getR5().addValueChangeListener(e -> {
                    chartListThird.get(0).setVisible(!(e.getValue().equals("学年全体")));
                    chartListThirdB.get(0).setVisible(e.getValue().equals("学年全体"));
                }
        );
        seiseki.getR5().addValueChangeListener(e -> {
            chartListThird.get(1).setVisible(!(e.getValue().equals("応用化学生物学科")));
            chartListThirdB.get(1).setVisible(e.getValue().equals("応用化学生物学科"));
        });
        seiseki.getR5().addValueChangeListener(e -> {
            chartListThird.get(2).setVisible(!(e.getValue().equals("電子光工学科")));
            chartListThirdB.get(2).setVisible(e.getValue().equals("電子光工学科"));

        });
        seiseki.getR5().addValueChangeListener(e -> {
            chartListThird.get(3).setVisible(!(e.getValue().equals("情報システム工学科")));
            chartListThirdB.get(3).setVisible(e.getValue().equals("情報システム工学科"));
        });


        //ここまでthird

        //ここからfourth
        //allと同じようにプログラムを設計
        FormLayout layoutUpFourth =getUnderLayout();
        HorizontalLayout layoutUnderFourth =new HorizontalLayout();
        ArrayList<VerticalLayout> chartListFourth =seisekiGraph.makeFourth();
        ArrayList<VerticalLayout> chartListFourthB =seisekiGraph.makeBigFourth();

        for(VerticalLayout chart: chartListFourth){
            layoutUpFourth.add(chart);
        }
        for(VerticalLayout chart: chartListFourthB){
            layoutUnderFourth.add(chart);
            chart.setVisible(false);
        }


        fourth.add(layoutUnderFourth);
        fourth.add(layoutUpFourth);


        seiseki.getR6().addValueChangeListener(e -> {
                    chartListFourth.get(0).setVisible(!(e.getValue().equals("学年全体")));
                    chartListFourthB.get(0).setVisible(e.getValue().equals("学年全体"));
                }
        );
        seiseki.getR6().addValueChangeListener(e -> {
            chartListFourth.get(1).setVisible(!(e.getValue().equals("応用化学生物学科")));
            chartListFourthB.get(1).setVisible(e.getValue().equals("応用化学生物学科"));
        });
        seiseki.getR6().addValueChangeListener(e -> {
            chartListFourth.get(2).setVisible(!(e.getValue().equals("電子光工学科")));
            chartListFourthB.get(2).setVisible(e.getValue().equals("電子光工学科"));

        });
        seiseki.getR6().addValueChangeListener(e -> {
            chartListFourth.get(3).setVisible(!(e.getValue().equals("情報システム工学科")));
            chartListFourthB.get(3).setVisible(e.getValue().equals("情報システム工学科"));
        });


        //ここまでfourth

        //ここから基本統計量
        //各学年のグリッドを呼び出し
        SeisekiTable table = new SeisekiTable(gradeService);
        Grid<GradeGpaStat> gridAll = table.getTableYearFirstAll();
        Grid<GradeGpaStat> gridFirst = table.getTableFirst();
        Grid<GradeGpaStat> gridSecond = table.getTableSecond();
        Grid<GradeGpaStat> gridThird = table.getTableThird();
        Grid<GradeGpaStat> gridFourth = table.getTableFourth();

        H3 str = new H3("基本統計量");
        H3 str1 = new H3("基本統計量");
        H3 str2 = new H3("基本統計量");
        H3 str3 = new H3("基本統計量");
        H3 str4 = new H3("基本統計量");
        //各学年に対応するレイアウトにグリッドを追加
        all.add(str,gridAll);
        first.add(str1,gridFirst);
        second.add(str2,gridSecond);
        third.add(str3,gridThird);
        fourth.add(str4,gridFourth);
        //ここまで基本統計量
        return main;
    }

    //学年で比較するメソッド
    private VerticalLayout compareByYear(){
        VerticalLayout main = new VerticalLayout();//グラフ全体のレイアウトを返す
        VerticalLayout all = new VerticalLayout();//学年全体を表示
        VerticalLayout science = new VerticalLayout();//応用生物化学
        VerticalLayout electronic = new VerticalLayout();//電子光
        VerticalLayout information = new VerticalLayout();//情報システム
        //レイアウトを非表示にする
        all.setVisible(false);
        science.setVisible(false);
        electronic.setVisible(false);
        information.setVisible(false);

        //mainのレイアウトに各学科のレイアウトを加える
        main.add(all,science,electronic,information);

        //ラジオボタンの項目ごとに見せるレイアウトを分ける(選択されたものだけsetVisible(true))
        seiseki.getR1().addValueChangeListener(e->all.setVisible(e.getValue().equals("全体")));
        seiseki.getR1().addValueChangeListener(e->science.setVisible(e.getValue().equals("応用化学生物学科")));
        seiseki.getR1().addValueChangeListener(e->electronic.setVisible(e.getValue().equals("電子光工学科")));
        seiseki.getR1().addValueChangeListener(e->information.setVisible(e.getValue().equals("情報システム工学科")));

        //ここからall
        HorizontalLayout layoutUpAll =new HorizontalLayout();
        FormLayout layoutUnderAll = getUnderLayout();
        //レイアウト呼び出し
        ArrayList<VerticalLayout> chartListAll=seisekiGraph.makeAllSubject();
        ArrayList<VerticalLayout> chartListAllB =seisekiGraph.makeBigAllSubject();
        //レイアウトを追加
        for(VerticalLayout chart: chartListAll){
            layoutUnderAll.add(chart);
        }
        for(VerticalLayout chart: chartListAllB){
            layoutUpAll.add(chart);
            chart.setVisible(false);
        }

        all.add(layoutUpAll);
        all.add(layoutUnderAll);
        //ラジオボタンの制御
        //ラジオボタンで選択されたものを上に大きく表示(ヒストグラム、円グラフ)、選択されていないものを下に小さく表示(ヒストグラム)
        seiseki.getR2().addValueChangeListener(e -> {
                    chartListAll.get(0).setVisible(!(e.getValue().equals("全体")));
                    chartListAllB.get(0).setVisible(e.getValue().equals("全体"));
                }
        );
        seiseki.getR2().addValueChangeListener(e -> {
            chartListAll.get(1).setVisible(!(e.getValue().equals("1年")));
            chartListAllB.get(1).setVisible(e.getValue().equals("1年"));
        });
        seiseki.getR2().addValueChangeListener(e -> {
            chartListAll.get(2).setVisible(!(e.getValue().equals("2年")));
            chartListAllB.get(2).setVisible(e.getValue().equals("2年"));

        });
        seiseki.getR2().addValueChangeListener(e -> {
            chartListAll.get(3).setVisible(!(e.getValue().equals("3年")));
            chartListAllB.get(3).setVisible(e.getValue().equals("3年"));
        });
        seiseki.getR2().addValueChangeListener(e -> {
            chartListAll.get(4).setVisible(!(e.getValue().equals("4年")));
            chartListAllB.get(4).setVisible(e.getValue().equals("4年"));

        });

        //ここまでall

        //ここからscience
        //allと同じようにプログラムを設計
        HorizontalLayout layoutUpScience =new HorizontalLayout();
        FormLayout layoutUnderScience =getUnderLayout();
        ArrayList<VerticalLayout> chartListScience =seisekiGraph.makeScience();
        ArrayList<VerticalLayout> chartListScienceB =seisekiGraph.makeBigScience();

        for(VerticalLayout chart: chartListScience){
            layoutUnderScience.add(chart);
        }
        for(VerticalLayout chart: chartListScienceB){
            layoutUpScience.add(chart);
            chart.setVisible(false);
        }


        science.add(layoutUpScience);
        science.add(layoutUnderScience);


        seiseki.getR3().addValueChangeListener(e -> {
                    chartListScience.get(0).setVisible(!(e.getValue().equals("全体")));
                    chartListScienceB.get(0).setVisible(e.getValue().equals("全体"));
                }
        );
        seiseki.getR3().addValueChangeListener(e -> {
            chartListScience.get(1).setVisible(!(e.getValue().equals("2年")));
            chartListScienceB.get(1).setVisible(e.getValue().equals("2年"));
        });
        seiseki.getR3().addValueChangeListener(e -> {
            chartListScience.get(2).setVisible(!(e.getValue().equals("3年")));
            chartListScienceB.get(2).setVisible(e.getValue().equals("3年"));

        });
        seiseki.getR3().addValueChangeListener(e -> {
            chartListScience.get(3).setVisible(!(e.getValue().equals("4年")));
            chartListScienceB.get(3).setVisible(e.getValue().equals("4年"));
        });


        //ここまでscience

        //ここからelectronic
        HorizontalLayout layoutUpElectronic =new HorizontalLayout();
        FormLayout layoutUnderElectronic = getUnderLayout();
        ArrayList<VerticalLayout> chartListElectronic =seisekiGraph.makeElectronic();
        ArrayList<VerticalLayout> chartListElectronicB =seisekiGraph.makeBigElectronic();

        for(VerticalLayout chart: chartListElectronic){
            layoutUnderElectronic.add(chart);
        }
        for(VerticalLayout chart: chartListElectronicB){
            layoutUpElectronic.add(chart);
            chart.setVisible(false);
        }


        electronic.add(layoutUpElectronic);
        electronic.add(layoutUnderElectronic);


        seiseki.getR4().addValueChangeListener(e -> {
                    chartListElectronic.get(0).setVisible(!(e.getValue().equals("全体")));
                    chartListElectronicB.get(0).setVisible(e.getValue().equals("全体"));
                }
        );
        seiseki.getR4().addValueChangeListener(e -> {
            chartListElectronic.get(1).setVisible(!(e.getValue().equals("2年")));
            chartListElectronicB.get(1).setVisible(e.getValue().equals("2年"));
        });
        seiseki.getR4().addValueChangeListener(e -> {
            chartListElectronic.get(2).setVisible(!(e.getValue().equals("3年")));
            chartListElectronicB.get(2).setVisible(e.getValue().equals("3年"));

        });
        seiseki.getR4().addValueChangeListener(e -> {
            chartListElectronic.get(3).setVisible(!(e.getValue().equals("4年")));
            chartListElectronicB.get(3).setVisible(e.getValue().equals("4年"));
        });


        //ここまでelectronic

        //ここからinformation
        //allと同じようにプログラムを設計
        HorizontalLayout layoutUpInformation =new HorizontalLayout();
        FormLayout layoutUnderInformation =getUnderLayout();
        ArrayList<VerticalLayout> chartListInformation =seisekiGraph.makeInformation();
        ArrayList<VerticalLayout> chartListInformationB =seisekiGraph.makeBigInformation();

        for(VerticalLayout chart: chartListInformation){
            layoutUnderInformation.add(chart);
        }
        for(VerticalLayout chart: chartListInformationB){
            layoutUpInformation.add(chart);
            chart.setVisible(false);
        }


        information.add(layoutUpInformation);
        information.add(layoutUnderInformation);


        seiseki.getR5().addValueChangeListener(e -> {
                    chartListInformation.get(0).setVisible(!(e.getValue().equals("全体")));
                    chartListInformationB.get(0).setVisible(e.getValue().equals("全体"));
                }
        );
        seiseki.getR5().addValueChangeListener(e -> {
            chartListInformation.get(1).setVisible(!(e.getValue().equals("2年")));
            chartListInformationB.get(1).setVisible(e.getValue().equals("2年"));
        });
        seiseki.getR5().addValueChangeListener(e -> {
            chartListInformation.get(2).setVisible(!(e.getValue().equals("3年")));
            chartListInformationB.get(2).setVisible(e.getValue().equals("3年"));

        });
        seiseki.getR5().addValueChangeListener(e -> {
            chartListInformation.get(3).setVisible(!(e.getValue().equals("4年")));
            chartListInformationB.get(3).setVisible(e.getValue().equals("4年"));
        });


        //ここまでinformation

        //ここから基本統計量
        //SeisekiTableをインスタンス化
        SeisekiTable table = new SeisekiTable(gradeService);
        //各グリッドを呼び出し
        Grid<GetTableData> gridAll = table.getTableSubjectFirst();
        Grid<GetTableData> gridScience = table.getTableSubjectFirstScience();
        Grid<GetTableData> gridElectronic = table.getTableSubjectFirstElectronic();
        Grid<GetTableData> gridInformation = table.getTableSubjectFirstInformation();

        H3 str = new H3("基本統計量");
        H3 str1 = new H3("基本統計量");
        H3 str2 = new H3("基本統計量");
        H3 str3 = new H3("基本統計量");

        //各学科のレイアウトに対応するグリッドを追加
        all.add(str,gridAll);
        science.add(str1,gridScience);
        electronic.add(str2,gridElectronic);
        information.add(str3,gridInformation);

        return main;
    }
    //画面の大きさに合わせてコンポーネントの数を制御する例アウトを返すメソッド
    private FormLayout getUnderLayout(){
        FormLayout layout=new FormLayout();
        layout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0",1),
                new FormLayout.ResponsiveStep("600px",2),
                new FormLayout.ResponsiveStep("900px",3),
                new FormLayout.ResponsiveStep("1200px",4),
                new FormLayout.ResponsiveStep("1500px",5)
        );
        return layout;
    }

}
