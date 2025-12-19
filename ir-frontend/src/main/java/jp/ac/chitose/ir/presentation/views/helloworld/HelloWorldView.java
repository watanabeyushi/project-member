package jp.ac.chitose.ir.presentation.views.helloworld;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import jp.ac.chitose.ir.application.service.helloworld.HelloService;
import jp.ac.chitose.ir.application.service.helloworld.HelloworldGrade;
import jp.ac.chitose.ir.presentation.component.MainLayout;
import jp.ac.chitose.ir.presentation.component.graph.GRAPH_TYPE;
import jp.ac.chitose.ir.presentation.component.graph.Graph;
import jp.ac.chitose.ir.presentation.component.graph.GraphSeries;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.details.Details;
import java.util.List; // List<HelloworldGrade>のために必要


@PageTitle("Hello World")
@Route(value = "hello", layout = MainLayout.class)
@PermitAll
public class HelloWorldView extends VerticalLayout {

    private final HelloService helloService;
    private VerticalLayout graphContainer; // グラフを格納するためのコンテナ

    public HelloWorldView(HelloService helloService) {
        this.helloService = helloService;

        // H1の見出し：開始位置を100pxに設定します。
        H1 title = new H1("CIST-IR");
        title.getStyle().set("position", "fixed");
        title.getStyle().set("top", "100px");
        title.getStyle().set("left", "20px");
        title.getStyle().set("z-index", "10");

        // H2の見出し：H1から50px下に配置します。
        H2 title2 = new H2("成績と他要因の関係");
        title2.getStyle().set("position", "fixed");
        title2.getStyle().set("top", "150px");
        title2.getStyle().set("left", "20px");
        title2.getStyle().set("z-index", "10");

        // 学年選択：ラジオボタンのラベルを含めた高さを考慮し、間隔を広げます。
        RadioButtonGroup<String> grade = new RadioButtonGroup<>();
        grade.setLabel("学年を選択");
        grade.setItems("全体", "1年", "2年", "3年", "4年");
        grade.getStyle().set("position", "fixed");
        grade.getStyle().set("top", "250px");
        grade.getStyle().set("left", "20px");

        // 学期選択：gradeから約90px下に配置します。
        RadioButtonGroup<String> semester = new RadioButtonGroup<>();
        semester.setLabel("学期を選択");
        semester.setItems("春学期", "秋学期");
        semester.getStyle().set("position", "fixed");
        semester.getStyle().set("top", "340px");
        semester.getStyle().set("left", "20px");

        // 学科選択：semesterから約90px下に配置します。
        RadioButtonGroup<String> department = new RadioButtonGroup<>();
        department.setLabel("学科を選択");
        department.setItems("理工学専攻", "応用科学生物学科", "電子光工学科", "情報システム工学科");
        department.getStyle().set("position", "fixed");
        department.getStyle().set("top", "430px");
        department.getStyle().set("left", "20px");

        // 検索ボタン：画面右下に固定します。
        Button sampleButton = new Button("検索！");
        sampleButton.getStyle().set("position", "fixed");
        sampleButton.getStyle().set("top", "530px");
        sampleButton.getStyle().set("left", "100px");
        sampleButton.getStyle().set("z-index", "10");

        // コンストラクタ内のクリックリスナー部分を以下のように書き換えます。
        sampleButton.addClickListener(event -> {
            getUI().ifPresent(ui -> ui.navigate("AnalysisScreen"));
            // 各ラジオボタンの現在選択されている値を取得します。
            String selectedGrade = grade.getValue();
            String selectedSemester = semester.getValue();
            String selectedDept = department.getValue();

            // 未選択状態（null）を考慮し、比較の際は null チェックを含めるか、
            // 確実に文字列と比較するために、Objects.equals や "値".equals() を使用します。

            // 1. 「1年」が選択されている、または「2年 かつ 春学期」が選択されている
            boolean isTargetCondition = "1年".equals(selectedGrade) ||
                    ("2年".equals(selectedGrade) && "春学期".equals(selectedSemester));

            // 2. その上で「理工学専攻」が選ばれていない（null または 他の学科）
            boolean isNotScienceAndEng = !"理工学専攻".equals(selectedDept);

            // 条件判定を実行します。
            if (isTargetCondition && isNotScienceAndEng) {
                // エラー通知を表示します。
                Notification notification = Notification.show(
                        "当てはまるデータがありません",
                        3000,
                        Notification.Position.MIDDLE // 画面中央に表示して注意を促します
                );
                // 通知にエラー用の赤いテーマを適用します。
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } else {
                // 条件を満たしている、あるいはチェック対象外の場合は画面遷移を実行します。
                getUI().ifPresent(ui -> ui.navigate("AnalysisScreen"));
            }
        });
        sampleButton.addClickListener(event -> {
            // 各項目の選択値を取得します。
            String selectedGrade = grade.getValue();
            String selectedSemester = semester.getValue();
            String selectedDept = department.getValue();

            // 1. 全ての項目が選択されているか確認します。
            // いずれかが null であれば、未選択の項目があると判断します。
            if (selectedGrade == null || selectedSemester == null || selectedDept == null) {
                Notification.show(
                        "条件を入力してください",
                        3000,
                        Notification.Position.MIDDLE
                ).addThemeVariants(NotificationVariant.LUMO_ERROR);

                // 処理を中断し、画面遷移させません。
                return;
            }

            // 2. 「1年」または「2年春学期」かつ「理工学専攻以外」の組み合わせをチェックします。
            boolean isTargetPeriod = "1年".equals(selectedGrade) ||
                    ("2年".equals(selectedGrade) && "春学期".equals(selectedSemester));

            boolean isNotScienceAndEng = !"理工学専攻".equals(selectedDept);

            if (isTargetPeriod && isNotScienceAndEng) {
                Notification.show(
                        "当てはまるデータがありません",
                        3000,
                        Notification.Position.MIDDLE
                ).addThemeVariants(NotificationVariant.LUMO_ERROR);

                // 処理を中断し、画面遷移させません。
                return;
            }

            // 3. すべてのバリデーションを通過した場合のみ遷移します。
            getUI().ifPresent(ui -> ui.navigate("AnalysisScreen"));
        });

        // 全コンポーネントをレイアウトに追加
        add(title, title2,grade, semester, department, sampleButton);
    }
}