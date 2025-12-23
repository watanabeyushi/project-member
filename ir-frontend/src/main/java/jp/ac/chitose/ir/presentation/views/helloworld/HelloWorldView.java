package jp.ac.chitose.ir.presentation.views.helloworld;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import jp.ac.chitose.ir.application.service.helloworld.HelloService;
import jp.ac.chitose.ir.presentation.component.MainLayout;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@PageTitle("Hello World")
@Route(value = "hello", layout = MainLayout.class)
@PermitAll
public class HelloWorldView extends VerticalLayout {

    private final HelloService helloService;
    private final RadioButtonGroup<String> grade = new RadioButtonGroup<>();
    private final RadioButtonGroup<String> semester = new RadioButtonGroup<>();
    private final RadioButtonGroup<String> department = new RadioButtonGroup<>();

    // APIレスポンス用DTOクラス
    static class MasterListResponse {
        public MasterListData data;
    }

    static class MasterListData {
        @JsonProperty("target_grades")
        public List<String> targetGrades;
        @JsonProperty("available_semesters")
        public List<String> availableSemesters;
        @JsonProperty("target_departments")
        public List<String> targetDepartments;
    }

    public HelloWorldView(HelloService helloService) {
        this.helloService = helloService;

        // 1. UIの配置を先に行います（エラー時でも画面を表示させるため）
        setupLayout();

        // 2. 外部データをロードします
        loadMasterListData();
    }

    private void setupLayout() {
        H1 title = new H1("CIST-IR");
        title.getStyle().set("position", "fixed").set("top", "100px").set("left", "20px").set("z-index", "10");

        H2 title2 = new H2("成績と他要因の関係");
        title2.getStyle().set("position", "fixed").set("top", "150px").set("left", "20px").set("z-index", "10");

        grade.setLabel("学年を選択");
        grade.getStyle().set("position", "fixed").set("top", "250px").set("left", "20px");

        semester.setLabel("学期を選択");
        semester.getStyle().set("position", "fixed").set("top", "340px").set("left", "20px");

        department.setLabel("学科を選択");
        department.getStyle().set("position", "fixed").set("top", "430px").set("left", "20px");

        Button searchButton = new Button("検索！");
        searchButton.getStyle().set("position", "fixed").set("top", "530px").set("left", "100px").set("z-index", "10");

        // ボタンクリック時のイベントリスナー
        searchButton.addClickListener(event -> handleSearch());

        add(title, title2, grade, semester, department, searchButton);
    }

    private void loadMasterListData() {
        try {
            // タイムアウトを設定したHttpClientを作成します
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(3))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:8000/grade/master_list"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                // クラスに定義していないJSONキーがあってもエラーにしない設定
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                MasterListResponse result = mapper.readValue(response.body(), MasterListResponse.class);

                if (result != null && result.data != null) {
                    grade.setItems(result.data.targetGrades);
                    semester.setItems(result.data.availableSemesters);
                    department.setItems(result.data.targetDepartments);
                }
            } else {
                showNotification("APIサーバーエラー: " + response.statusCode(), NotificationVariant.LUMO_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showNotification("API接続エラーが発生しました。サーバーの状態を確認してください。", NotificationVariant.LUMO_ERROR);
        }
    }

    private void handleSearch() {
        String selectedGrade = grade.getValue();
        String selectedSemester = semester.getValue();
        String selectedDept = department.getValue();

        // バリデーション：未選択チェック
        if (selectedGrade == null || selectedSemester == null || selectedDept == null) {
            showNotification("条件を入力してください", NotificationVariant.LUMO_ERROR);
            return;
        }

        // バリデーション：データ不在条件（例として理工学部以外かつ特定の学年）
        boolean isTargetPeriod = "1".equals(selectedGrade) || ("2".equals(selectedGrade) && "3".equals(selectedSemester));
        boolean isNotScienceAndEng = !"理工学部".equals(selectedDept);

        if (isTargetPeriod && isNotScienceAndEng) {
            showNotification("当てはまるデータがありません", NotificationVariant.LUMO_ERROR);
            return;
        }

        // AnalysisScreenへ遷移し、クエリパラメータで選択値を渡します
        QueryParameters params = QueryParameters.simple(Map.of(
                "grade", selectedGrade,
                "semester", selectedSemester,
                "dept", selectedDept
        ));
        getUI().ifPresent(ui -> ui.navigate("AnalysisScreen", params));
    }

    private void showNotification(String message, NotificationVariant variant) {
        Notification n = Notification.show(message, 3000, Notification.Position.MIDDLE);
        n.addThemeVariants(variant);
    }
}