package jp.ac.chitose.ir.presentation.views.top;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.PermitAll;
import jp.ac.chitose.ir.application.service.management.SecurityService;
import jp.ac.chitose.ir.presentation.component.MainLayout;

// Top画面
@PageTitle("IRTop")
@Route(value = "/top", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
public class TopView extends VerticalLayout {

    private final SecurityService securityService;

    public TopView(SecurityService securityService) {
        this.securityService = securityService;


        // 共通メニュー
        H1 menutitle1 = new H1("共通メニュー");
        Paragraph menutitle1Discription = new Paragraph("全ユーザー共通のメニューです。");
        add(menutitle1, menutitle1Discription);

        // 成績情報ページ（StudentView）の紹介
        H3 grade = new H3(new Anchor("common/grade", "授業に関する情報公開"));
        Paragraph gradeDescription = new Paragraph("成績評価分布や授業評価アンケートの結果を科目ごとに確認できます。");
        add(grade, createIndent(gradeDescription));

        // 成績評価分布状況表(未実装)
        H3 gradeGird = new H3(new Anchor("common/grid", "成績評価分布状況表"));
        Paragraph gradeGridDescription = new Paragraph("成績評価分布をまとめた表を確認できます。");
        add(gradeGird, createIndent(gradeGridDescription));

        // 固有メニュー
        H1 menutitle2 = new H1("固有メニュー");
        add(menutitle2);

        if (securityService.getLoginUser().isAdmin()) {
            addAdminView();
        }
        if (securityService.getLoginUser().isCommission()) {
            addCommissionView();
        }
        if (securityService.getLoginUser().isTeacher()) {
            addTeacherView();
        }
        if (securityService.getLoginUser().isStudent()) {
            addStudentView();
        }


    }

    // 管理者向け要素の追加
    public void addAdminView() {
        H2 admin = new H2("管理者向けメニュー");
        H3 users = new H3(new Anchor("/user_management", "ユーザー管理"));
        add(admin, users);
        Paragraph usersDescription = new Paragraph("ユーザー管理を行う画面です。");
        add(createIndent(usersDescription));
    }

    // IR委員会向け要素の追加
    public void addCommissionView() {
        H2 commission = new H2("IR委員会向けメニュー");
        H3 stat = new H3(new Anchor("/commission", "GPAと基本統計量に関するデータ"));
        H3 universityInfo = new H3(new Anchor("university", "大学情報"));
        add(commission);
        Paragraph statDescription = new Paragraph("GPAに関する統計情報が確認できます。");
        Paragraph universityInfoDescription = new Paragraph("大学の基本的な情報が確認出来ます");
        add(stat, createIndent(statDescription));
        add(universityInfo, createIndent(universityInfoDescription));
    }

    // 教員向け要素の追加
    public void addTeacherView() {
        H2 teacher = new H2("教員向けメニュー");
        H3 gradeAssess = new H3(new Anchor("/questionnaire", "担当した科目の詳細情報"));
        Paragraph gradeAssessDescription = new Paragraph("担当した科目の詳細情報を確認できます。");
        add(teacher, gradeAssess, createIndent(gradeAssessDescription));
    }

    // 学生向け要素の追加
    public void addStudentView() {
        H2 student = new H2("学生向けメニュー");
        H3 studentAssess = new H3(new Anchor("/grade/student", "履修した科目の詳細情報"));
        add(student);
        Paragraph studentAssessDescription = new Paragraph("履修した科目の詳細情報を確認できます");
        add(studentAssess, createIndent(studentAssessDescription));
    }

    // paragraphにインデントを付ける
    private Paragraph createIndent(Paragraph paragraph) {
        paragraph.getStyle().set("margin-left", "2em");
        return paragraph;
    }

}