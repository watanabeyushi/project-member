package jp.ac.chitose.ir.presentation.views.commission.university.layouts.people.numberOfStudents;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jp.ac.chitose.ir.application.service.TableData;
import jp.ac.chitose.ir.application.service.commission.GradeGpaStat;
import jp.ac.chitose.ir.application.service.commission.GradeService;
import jp.ac.chitose.ir.presentation.views.commission.university.components.BackButton;

import java.util.ArrayList;

//学生数
public class NumberOfStudents extends VerticalLayout {
    private GradeService gradeService;
    public NumberOfStudents(GradeService gradeService,BackButton backButton) {

        this.gradeService = gradeService;

        add(new H1("学生数"));
        add(new Paragraph("学年ごとの学生数を見ることが出来ます。"));
        add(backButton);
        add(new H2("2022年度"));
        add(new H3("1年生"));
        add(createFirstGrid());
        add(new H3("2~4年生"));
        add(createGrid());
        add(new H3("合計"));
        add(createAllGrid());
    }
    private Grid<Number> createGrid(){
        ArrayList<Number> numbers = new ArrayList<>();
        TableData<GradeGpaStat> secondTable = gradeService.getGradeGpaStat("B2");
        TableData<GradeGpaStat> thirdTable = gradeService.getGradeGpaStat("B3");
        TableData<GradeGpaStat> fourthTable = gradeService.getGradeGpaStat("B4");

        Number second = new Number("2年",
                secondTable.data().get(0).人数(),
                secondTable.data().get(1).人数(),
                secondTable.data().get(2).人数(),
                secondTable.data().get(3).人数());
        numbers.add(second);

        Number third = new Number("3年",thirdTable.data().get(0).人数(),thirdTable.data().get(1).人数(),thirdTable.data().get(2).人数(),thirdTable.data().get(3).人数());
        numbers.add(third);

        Number fourth = new Number("4年",fourthTable.data().get(0).人数(),fourthTable.data().get(1).人数(),fourthTable.data().get(2).人数(),fourthTable.data().get(3).人数());
        numbers.add(fourth);

        Grid<Number> grid = new Grid<>();
        grid.setItems(numbers);
        grid.addColumn(Number::grade).setHeader("学年");
        grid.addColumn(Number::science).setHeader("応用化学生物学科");
        grid.addColumn(Number::elect).setHeader("電子光工学科");
        grid.addColumn(Number::info).setHeader("情報システム工学科");
        grid.setHeight("180px");

        return grid;
    }

    private Grid<FirstNumber> createFirstGrid(){
        ArrayList<FirstNumber> firstNumbers = new ArrayList<>();
        TableData<GradeGpaStat> firstTable = gradeService.getGradeGpaStat("B1");

        FirstNumber first = new FirstNumber("1年",
                firstTable.data().get(0).人数(),
                firstTable.data().get(1).人数(),
                firstTable.data().get(2).人数(),
                firstTable.data().get(3).人数(),
                firstTable.data().get(4).人数());

        firstNumbers.add(first);

        Grid<FirstNumber> grid = new Grid<>();
        grid.setItems(firstNumbers);
        grid.addColumn(FirstNumber::grade).setHeader("学年");
        grid.addColumn(FirstNumber::A).setHeader("Aクラス");
        grid.addColumn(FirstNumber::B).setHeader("Bクラス");
        grid.addColumn(FirstNumber::C).setHeader("Cクラス");
        grid.addColumn(FirstNumber::D).setHeader("Dクラス");

        grid.setHeight("100px");
        return grid;
    }

    private Grid<AllNumber> createAllGrid(){
        ArrayList<AllNumber> allNumbers = new ArrayList<>();

        TableData<GradeGpaStat> allTable = gradeService.getGradeGpaStat("B");
        AllNumber all = new AllNumber(allTable.data().get(0).人数(),
                allTable.data().get(1).人数(),
                allTable.data().get(2).人数(),
                allTable.data().get(3).人数(),
                allTable.data().get(4).人数());

        allNumbers.add(all);

        Grid<AllNumber> grid = new Grid<>();
        grid.setItems(allNumbers);
        grid.addColumn(AllNumber::all).setHeader("全体");
        grid.addColumn(AllNumber::first).setHeader("1年");
        grid.addColumn(AllNumber::science).setHeader("応用化学生物学科");
        grid.addColumn(AllNumber::elect).setHeader("電子光工学科");
        grid.addColumn(AllNumber::info).setHeader("情報システム工学科");
        grid.setHeight("100px");
        return grid;
    }
}

