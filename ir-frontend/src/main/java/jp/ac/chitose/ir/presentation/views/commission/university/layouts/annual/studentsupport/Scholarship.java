package jp.ac.chitose.ir.presentation.views.commission.university.layouts.annual.studentsupport;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jp.ac.chitose.ir.application.service.TableData;
import jp.ac.chitose.ir.application.service.commission.ResearchSupports;
import jp.ac.chitose.ir.application.service.commission.ScholarShipJasso;
import jp.ac.chitose.ir.application.service.commission.ScholarShipOthers;
import jp.ac.chitose.ir.application.service.commission.UniversityService;
import jp.ac.chitose.ir.presentation.views.commission.university.components.BackButton;

public class Scholarship extends VerticalLayout {

    private UniversityService universityService;
    public Scholarship(UniversityService universityService, BackButton backButton) {
        this.universityService = universityService;
        add(new H1("奨学金"));
        add(new Paragraph("大学年報の奨学金に関する情報を見ることが出来ます。"));
        add(backButton);
        add(new H2("2022年度"));
        add(new H3("日本学生支援機構"));
        add(createJassoGrid());
        add(new H3("大学院研究援助金"));
        add(createResearchSupportGrid());
        add(new H3("その他奨学金"));
        add(creatOtherSupportgrid());

    }

    //日本学生支援機構の奨学金に関するGrid
    private Grid<ScholarShipJasso> createJassoGrid(){

        TableData<ScholarShipJasso> scholarShipJassoTableData=this.universityService.getScholarShipJasso(2022);



        Grid<ScholarShipJasso> grid=new Grid<>();
        grid.setItems(scholarShipJassoTableData.data());
        grid.addColumn(ScholarShipJasso::academic_category).setHeader("学種");
        grid.addColumn(ScholarShipJasso::loan_type).setHeader("貸与種別");
        grid.addColumn(ScholarShipJasso::reservation).setHeader("予約");
        grid.addColumn(ScholarShipJasso::enrollment).setHeader("在学");
        grid.addColumn(ScholarShipJasso::emergency_support).setHeader("緊急・応急");
        grid.addColumn(ScholarShipJasso::total).setHeader("採用計");

        grid.setHeight("240px");

        return grid;
    };

    //大学院研究援助金に関するGrid
    private Grid<ResearchSupports> createResearchSupportGrid(){
      TableData<ResearchSupports> researchSupportsTableData=this.universityService.getResearchSupports(2022);


      Grid<ResearchSupports> grid = new Grid<>();
      grid.setItems(researchSupportsTableData.data());
      grid.addColumn(ResearchSupports::category).setHeader("種別").setWidth("80px");
      grid.addColumn(ResearchSupports::total).setHeader("採用件数").setWidth("80px");
      grid.setHeight("130px");

      return grid;
    };

    //上記以外の奨学金に関するGrid
    private Grid<ScholarShipOthers> creatOtherSupportgrid(){
        TableData<ScholarShipOthers> scholarShipOthersTableData=this.universityService.getScholarShipOthers(2022);


        Grid<ScholarShipOthers> grid = new Grid<>();

        grid.setItems(scholarShipOthersTableData.data());
        grid.addColumn(ScholarShipOthers::scholarship_name).setHeader("奨学金名");
        grid.addColumn(ScholarShipOthers::loan_type).setHeader("貸与・給付");
        grid.addColumn(ScholarShipOthers::academic_category).setHeader("学種");
        grid.addColumn(ScholarShipOthers::total).setHeader("採用人数");

        return grid;
    };
}
