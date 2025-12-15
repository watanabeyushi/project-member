package jp.ac.chitose.ir.presentation.views.commission.university;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import jp.ac.chitose.ir.application.service.commission.GradeService;
import jp.ac.chitose.ir.application.service.commission.UniversityService;
import jp.ac.chitose.ir.presentation.component.MainLayout;
import jp.ac.chitose.ir.presentation.views.commission.university.components.BackButton;
import jp.ac.chitose.ir.presentation.views.commission.university.components.SelectButton;
import jp.ac.chitose.ir.presentation.views.commission.university.layouts.annual.studentsupport.Scholarship;
import jp.ac.chitose.ir.presentation.views.commission.university.layouts.annual.teachertraining.TeacherTraining;
import jp.ac.chitose.ir.presentation.views.commission.university.layouts.classwork.ActiveLearning;
import jp.ac.chitose.ir.presentation.views.commission.university.layouts.classwork.GraduationCredits;
import jp.ac.chitose.ir.presentation.views.commission.university.layouts.classwork.NumberOfForeignLanguageClass;
import jp.ac.chitose.ir.presentation.views.commission.university.layouts.exam.EnrollmentCapacity;
import jp.ac.chitose.ir.presentation.views.commission.university.layouts.people.*;
import jp.ac.chitose.ir.presentation.views.commission.university.layouts.people.numberOfStudents.NumberOfStudents;

import java.util.ArrayList;
@PageTitle("University")
@Route(value = "university", layout = MainLayout.class)
@RolesAllowed("commission")

public class UniversityView extends VerticalLayout {
    private GradeService gradeService;
    private VerticalLayout mainLayout;
    private RadioButtonGroup<String> category;
    private ArrayList<ArrayList<Button>> buttons;
    private  ArrayList<Button> classwork;
    private  ArrayList<Button> course;
    private ArrayList<Button> exam;
    private ArrayList<Button> people;
    private ArrayList<Button> annualReport;
    private ArrayList<VerticalLayout> layouts;
    private FormLayout buttonLayout;
    private UniversityService universityService;


    public UniversityView(GradeService gradeService,UniversityService universityService) {

        this.gradeService = gradeService;
        this.universityService = universityService;

        mainLayout = new VerticalLayout();
        add(mainLayout);

        mainLayout.add(new H1("大学情報"));
        mainLayout.add(new Paragraph("この画面では、大学に関する情報を見ることが出来ます。"));


        //カテゴリ別ラジオボタンを追加
        category = new RadioButtonGroup<>();
        category.setItems("授業","進路","入試","人数","年報");
        mainLayout.add(category);
        selectRadio();


        buttons = new ArrayList<>();

        //授業に関係する内容のボタンを入れるリスト
        classwork = new ArrayList<>();
        buttons.add(classwork);

        //進路に関係する内容のボタンを入れるリスト
        course = new ArrayList<>();
        buttons.add(course);

        //入試に関係する内容のボタンを入れるリスト
        exam = new ArrayList<>();
        buttons.add(exam);

        //人数に関係する内容のボタンを入れるリスト
        people = new ArrayList<>();
        buttons.add(people);

        //大学年報に関係する内容のボタンを入れるリスト
        annualReport = new ArrayList<>();
        buttons.add(annualReport);

        layouts = new ArrayList<>();

        buttonLayout = new FormLayout();
        buttonLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0",1),
                new FormLayout.ResponsiveStep("300px",2),
                new FormLayout.ResponsiveStep("600px",3),
                new FormLayout.ResponsiveStep("900px",4)
        );
        mainLayout.add(buttonLayout);

        //backButtonを追加
        BackButton backButtonNumberOfTeachers = new BackButton(layouts, mainLayout);
        BackButton backButtonNumberOfStudent = new BackButton(layouts, mainLayout);
        BackButton backButtonStudentRatio = new BackButton(layouts, mainLayout);
        BackButton backButtonTeacherStudentRatio = new BackButton(layouts, mainLayout);
        BackButton backButtonForeignTeacher = new BackButton(layouts, mainLayout);
        BackButton backButtonWorkingAdultStudent = new BackButton(layouts, mainLayout);
        BackButton backButtonLeaveOfAbsence = new BackButton(layouts, mainLayout);
        BackButton backButtonDropoutOrExpelled = new BackButton(layouts, mainLayout);
        BackButton backButtonEnrollmentCapacity = new BackButton(layouts, mainLayout);
        BackButton backButtonNumberOfForeignLanguageClass = new BackButton(layouts, mainLayout);
        BackButton backButtonActiveLearning = new BackButton(layouts, mainLayout);





        BackButton backButtonGraduationCredits = new BackButton(layouts, mainLayout);
        BackButton backButtonTeacherTraining = new BackButton(layouts, mainLayout);
        BackButton backButtonSchalarship = new BackButton(layouts, mainLayout);



        //各レイアウトのボタン、レイアウトを追加
        //人数に関するボタン
        //教員数
        VerticalLayout numberOfTeachers = new NumberOfTeachers(backButtonNumberOfTeachers);
        setLayout(numberOfTeachers,"教員数",people);
        add(numberOfTeachers);

        //学生数
        VerticalLayout numberOfStudents = new NumberOfStudents(gradeService,backButtonNumberOfStudent);
        setLayout(numberOfStudents,"学生数",people);
        add(numberOfStudents);

        //学部生と大学院生の比率
        VerticalLayout studentRatio = new StudentRatio(backButtonStudentRatio);
        setLayout(studentRatio,"学部生と大学院生の比率",people);
        add(studentRatio);

        //教員と学生の比率
        VerticalLayout teacherStudentRatio = new TeacherStudentRatio(backButtonTeacherStudentRatio);
        setLayout(teacherStudentRatio,"教員と学生の比率",people);
        add(teacherStudentRatio);

        //外国人教員数
        VerticalLayout foreignTeacher = new ForeignTeacher(backButtonForeignTeacher);
        setLayout(foreignTeacher,"外国人教員数",people);
        add(foreignTeacher);

        //社会人学生数
        VerticalLayout workingAdultStudent =new WorkingAdultStudent(backButtonWorkingAdultStudent);
        setLayout(workingAdultStudent,"社会人学生数",people);
        add(workingAdultStudent);

        //休学者数
        VerticalLayout leaveOfAbsence = new LeaveOfAbsence(backButtonLeaveOfAbsence);
        setLayout(leaveOfAbsence,"休学者数",people);
        add(leaveOfAbsence);

        //退学、除籍者数
        VerticalLayout dropoutOrExpelled = new DropoutOrExpelled(backButtonDropoutOrExpelled);
        setLayout(dropoutOrExpelled,"退学、除籍者数",people);
        add(dropoutOrExpelled);


        //入試に関するボタン
        //入学定員
        VerticalLayout enrollmentCapacity = new EnrollmentCapacity(backButtonEnrollmentCapacity);
        setLayout(enrollmentCapacity,"入学定員",exam);
        add(enrollmentCapacity);


        //授業に関するボタン
        //外国語科目数
        VerticalLayout numberOfForeignLanguageClass = new NumberOfForeignLanguageClass(backButtonNumberOfForeignLanguageClass);
        setLayout(numberOfForeignLanguageClass,"外国語科目数",classwork);
        add(numberOfForeignLanguageClass);

        //アクティブラーニング実施率
        VerticalLayout activeLearning = new ActiveLearning(backButtonActiveLearning);
        setLayout(activeLearning,"アクティブラーニング実施率",classwork);
        add(activeLearning);

        //卒業単位数
        VerticalLayout graduationCredits = new GraduationCredits(universityService,backButtonGraduationCredits);
        setLayout(graduationCredits,"卒業単位数",classwork);
        add(graduationCredits);

        //大学年報
        //教職課程
        VerticalLayout teacherTraining = new TeacherTraining(universityService,backButtonTeacherTraining);
        setLayout(teacherTraining,"教職課程",annualReport);
        add(teacherTraining);

        //奨学金
        VerticalLayout schalarship= new Scholarship(universityService,backButtonSchalarship);
        setLayout(schalarship,"奨学金",annualReport);
        add(schalarship);


    }


    //button作成のためのメソッド
    private void setLayout(VerticalLayout layout, String name, ArrayList<Button> buttons) {
        layout.setVisible(false);
        SelectButton sR = new SelectButton(name,layout,mainLayout);
        sR.setVisible(false);
        buttonLayout.add(sR);
        buttons.add(sR);
        layouts.add(layout);
    }


    //カテゴリー別ラジオボタンを押した際の対応するボタンを表示するメソッド
    private void selectRadio(){
        category.addValueChangeListener(e -> {
            if(e.getValue().equals("授業")){
                deleteAll();
                for(Button button : classwork) {
                    button.setVisible(true);
                }
            }
            else if(e.getValue().equals("進路")){
                deleteAll();
                for(Button button : course) {
                    button.setVisible(true);
                }
            }
            else if(e.getValue().equals("入試")){
                deleteAll();
                for(Button button : exam) {
                    button.setVisible(true);
                }
            } else if(e.getValue().equals("人数")) {
                deleteAll();
                for(Button button : people) {
                    button.setVisible(true);
                }
            }
            else if(e.getValue().equals("年報")){
                deleteAll();
                for(Button button : annualReport) {
                    button.setVisible(true);
                }
            }
        });
    }


    private void deleteAll(){
        for(ArrayList<Button> buttonAndLayout : buttons) {
                for(Button button : buttonAndLayout) {
                    button.setVisible(false);
                }
            }
    }

}
