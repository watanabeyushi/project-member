package jp.ac.chitose.ir.presentation.views.commission.university.layouts.annual.teachertraining;

import com.github.appreciated.apexcharts.ApexCharts;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import jp.ac.chitose.ir.application.service.commission.TeacherTraining;
import jp.ac.chitose.ir.application.service.commission.UniversityService;
import jp.ac.chitose.ir.presentation.component.graph.Data;
import jp.ac.chitose.ir.presentation.component.graph.GRAPH_TYPE;
import jp.ac.chitose.ir.presentation.component.graph.Graph;
import jp.ac.chitose.ir.presentation.component.graph.GraphSeries;

import java.util.ArrayList;

public class Heisei22 extends VerticalLayout {
    private RadioButtonGroup<String> year;
    private ArrayList<VerticalLayout> layouts;
    private ArrayList<ArrayList<Integer>> data = new ArrayList<>();
    private UniversityService universityService;
    TeacherTraining teacherTraining;
    public Heisei22(UniversityService universityService) {
        this.universityService = universityService;
        this.teacherTraining = universityService.getTeacherTraining(2010).data().get(0);
        getData();
        add(new H2("平成22年度"));
        year = new RadioButtonGroup<>();
        year.setItems("1年","2年","3年","4年");
        add(year);

        layouts = new ArrayList<>();
        VerticalLayout first = new VerticalLayout();
        first.add(new H3("合計：" + data.get(0).get(3).toString() + "人"));
        VerticalLayout second = getGraph("2年",data.get(1).get(0),data.get(1).get(1),data.get(1).get(2),data.get(1).get(3));
        VerticalLayout third = getGraph("3年",data.get(2).get(0),data.get(2).get(1),data.get(2).get(2),data.get(2).get(3));
        VerticalLayout fourth = getGraph("4年",data.get(3).get(0),data.get(3).get(1),data.get(3).get(2),data.get(3).get(3));
        layouts.add(first);
        layouts.add(second);
        layouts.add(third);
        layouts.add(fourth);
        deleteAll();
        add(first, second, third, fourth);
        add(new H2("前年度と比較した教職課程の履修維持率"));
        add(getGrid());

        year.addValueChangeListener(e -> {
            if(e.getValue().equals("1年")){
                deleteAll();
                first.setVisible(true);
            }
            else if(e.getValue().equals("2年")){
                deleteAll();
                second.setVisible(true);
            }
            else if(e.getValue().equals("3年")){
                deleteAll();
                third.setVisible(true);
            }
            else if(e.getValue().equals("4年")){
                deleteAll();
                fourth.setVisible(true);
            }
        });

    }
    private void getData(){
        ArrayList<Integer> firstYear = new ArrayList<>();
        firstYear.add(null);
        firstYear.add(null);
        firstYear.add(null);
        firstYear.add(teacherTraining.first_year());
        data.add(firstYear);

        ArrayList<Integer> secondYear = new ArrayList<>();
        secondYear.add(teacherTraining.second_year_ouyoukagaku());
        secondYear.add(teacherTraining.second_year_densihikari());
        secondYear.add(teacherTraining.second_year_jouhou());
        secondYear.add(teacherTraining.second_year_total());
        data.add(secondYear);

        ArrayList<Integer> thirdYear = new ArrayList<>();
        thirdYear.add(teacherTraining.third_year_ouyoukagaku());
        thirdYear.add(teacherTraining.third_year_densihikari());
        thirdYear.add(teacherTraining.third_year_jouhou());
        thirdYear.add(teacherTraining.third_year_total());
        data.add(thirdYear);

        ArrayList<Integer> fourthYear = new ArrayList<>();
        fourthYear.add(teacherTraining.fourth_year_ouyoukagaku());
        fourthYear.add(teacherTraining.fourth_year_densihikari());
        fourthYear.add(teacherTraining.fourth_year_jouhou());
        fourthYear.add(teacherTraining.fourth_year_total());
        data.add(fourthYear);
    }
    private VerticalLayout getGraph(String name,int science,int elect,int info,int all) {
        VerticalLayout secondGraph = new VerticalLayout();
        HorizontalLayout graph = new HorizontalLayout();

        graph.add(histgram(name,science,elect,info),pie(science,elect,info));
        secondGraph.add(new H3("合計：" + all + "人"));
        secondGraph.add(graph);
        return secondGraph;
    }
    private Grid<Raito> getGrid(){
        Grid<Raito> grid = new Grid<>();
        ArrayList<Raito> raitoData = new ArrayList<>();
        for (int i = 1; i < data.size(); i++) {
            String year = i+1 + "年";
            ArrayList<Integer> pyear = data.get(i-1);
            ArrayList<Integer> years = data.get(i);
            ArrayList<String> raitos = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                if(pyear.get(j) == null){
                    raitos.add(null);
                }
                else{
                    int raito = (int)((double)years.get(j) / (double)pyear.get(j)*100);
                    raitos.add(raito + "%");
                }
            }
            raitoData.add(new Raito(year,raitos.get(0),raitos.get(1),raitos.get(2),raitos.get(3)));
        }
        grid.setItems(raitoData);
        grid.addColumn(Raito::year).setHeader("学年");
        grid.addColumn(Raito::science).setHeader("応用化学生物学科");
        grid.addColumn(Raito::elect).setHeader("電子光工学科");
        grid.addColumn(Raito::info).setHeader("情報システム工学科");
        grid.addColumn(Raito::all).setHeader("全体");
        grid.setHeight("200px");
        return grid;

    }


    private ApexCharts histgram(String name,int a,int b,int c){
        GraphSeries<Data<String, Integer>> series = new GraphSeries<>(name,
                new Data<>("応用化学生物学科",a),
                new Data<>("電子光工学科",b),
                new Data<>("情報システム工学科",c));

        return Graph.Builder.get().histogram()
                .height("300px").width("700px").series(series).XAxisLabel("学科").YAxisLabel("人数(人)").animationsEnabled(false).build().getGraph();
    }
    private ApexCharts pie(int a,int b,int c){
        String[] name = {"応用化学生物学科","電子光工学科","情報システム工学科"};

        Double[] datalist = new Double[3];
        datalist[0] = (double)a;
        datalist[1] = (double)b;
        datalist[2] = (double)c;

        return Graph.Builder.get()
                .graphType(GRAPH_TYPE.PIE)
                .doubles(datalist)
                .labels(name)
                .height("450px")
                .width("450px")
                .colors("#1676F3","#4795F5","#71B0F7")
                .animationsEnabled(false)
                .build()
                .getGraph();

    }
    private void deleteAll(){
        for(VerticalLayout layout : layouts){
            layout.setVisible(false);
        }
    }
}
