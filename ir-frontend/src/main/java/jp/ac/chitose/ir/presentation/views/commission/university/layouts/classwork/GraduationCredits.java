package jp.ac.chitose.ir.presentation.views.commission.university.layouts.classwork;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jp.ac.chitose.ir.application.service.TableData;
import jp.ac.chitose.ir.application.service.commission.CommonUnits;
import jp.ac.chitose.ir.application.service.commission.MajorUnits;
import jp.ac.chitose.ir.application.service.commission.UniversityService;
import jp.ac.chitose.ir.presentation.views.commission.university.components.BackButton;


public class GraduationCredits extends VerticalLayout {

    private UniversityService universityService;

    //卒業単位数
    public GraduationCredits(UniversityService universityService, BackButton backButton) {
        this.universityService = universityService;

        add(new H1("卒業単位数"));
        add(new Paragraph("卒業に必要最低限必要な単位数に関する情報を見ることが出来ます"));
        add(backButton);
        add(new H2("2024年度"));
        add(new H2("共通教育科目"));
        add(creatcommonGrid());
        add(new H2("専門教育科目"));
        add(createMajorgrid());

    }

    //共通教育科目の卒業単位数に関するGrid
    private Grid<CommonUnits> creatcommonGrid(){

        TableData<CommonUnits> commonUnitsTableData=universityService.getCommonUnits(2024);

        Grid<CommonUnits> grid = new Grid<>();
        grid.setItems(commonUnitsTableData.data().get(0));
        grid.addColumn(CommonUnits::required).setHeader("必修");
        grid.addColumn(CommonUnits::required_elective_specialty1).setHeader("選択必修(専門基礎1)");
        grid.addColumn(CommonUnits::required_elective_specialty2).setHeader("選択必修(専門基礎2)");
        grid.addColumn(CommonUnits::required_elective_education).setHeader("選択必修(一般教養)");
        grid.addColumn(CommonUnits::elective).setHeader("選択");
        grid.addColumn(CommonUnits::foreign_language_required1).setHeader("外国語①");
        grid.addColumn(CommonUnits::foreign_language_required2).setHeader("外国語②");
        grid.addColumn(CommonUnits::total).setHeader("共通教育科目合計");

        grid.setHeight("100px");

        return grid;
    }

    //学科配属後の専門科目の卒業単位数に関するGrid
    private Grid<MajorUnits> createMajorgrid(){

        TableData<MajorUnits> majorUnitsTableData=universityService.getMajorUnits(2024);

        Grid<MajorUnits> grid=new Grid<>();
        grid.setItems(majorUnitsTableData.data());
        grid.addColumn(MajorUnits::department).setHeader("学科");
        grid.addColumn(MajorUnits::required).setHeader("必修");
        grid.addColumn(MajorUnits::required_elective).setHeader("選択必修");
        grid.addColumn(MajorUnits::elective).setHeader("選択");
        grid.addColumn(MajorUnits::specialty_total).setHeader("専門合計");
        grid.addColumn(MajorUnits::others).setHeader("その他");
        grid.addColumn(MajorUnits::total).setHeader("卒業単位数合計");

        grid.setHeight("180px");

        return grid;
    };


}
