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
    // semesterのフィールド定義を削除しました
    private final RadioButtonGroup<String> department = new RadioButtonGroup<>();

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
        setupLayout();
        loadMasterListData();
    }

    private void setupLayout() {
        H1 title = new H1("CIST-IR");
        title.getStyle().set("position", "fixed").set("top", "100px").set("left", "20px").set("z-index", "10");

        H2 title2 = new H2("成績と他要因の関係");
        title2.getStyle().set("position", "fixed").set("top", "150px").set("left", "20px").set("z-index", "10");

        grade.setLabel("学年を選択");
        grade.getStyle().set("position", "fixed").set("top", "250px").set("left", "20px");

        // semesterのラベル設定と配置処理を削除しました<-

        department.setLabel("学科を選択");
        // 配置場所を上に詰めました（340pxへ変更）
        department.getStyle().set("position", "fixed").set("top", "340px").set("left", "20px");

        Button searchButton = new Button("検索！");
        // ボタンの配置場所も調整しました
        searchButton.getStyle().set("position", "fixed").set("top", "440px").set("left", "100px").set("z-index", "10");

        searchButton.addClickListener(event -> handleSearch());

        // addメソッドの引数からsemesterを除外しました
        add(title, title2, grade, department, searchButton);
    }

    private void loadMasterListData() {
        try {
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
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                MasterListResponse result = mapper.readValue(response.body(), MasterListResponse.class);

                if (result != null && result.data != null) {
                    grade.setItems(result.data.targetGrades);
                    // semester.setItemsの呼び出しを削除しました
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
        // selectedSemesterの取得を削除しました
        String selectedDept = department.getValue();

        // バリデーションからselectedSemesterを除外しました
        if (selectedGrade == null || selectedDept == null) {
            showNotification("条件を入力してください", NotificationVariant.LUMO_ERROR);
            return;
        }

        // 学期に依存していたバリデーション論理を削除し、簡略化しました
        if ("1".equals(selectedGrade) && !"理工学部".equals(selectedDept)) {
            showNotification("当てはまるデータがありません", NotificationVariant.LUMO_ERROR);
            return;
        }

        // クエリパラメータからsemesterを除外しました
        QueryParameters params = QueryParameters.simple(Map.of(
                "grade", selectedGrade,
                "dept", selectedDept
        ));
        getUI().ifPresent(ui -> ui.navigate("AnalysisScreen", params));
    }

    private void showNotification(String message, NotificationVariant variant) {
        Notification n = Notification.show(message, 3000, Notification.Position.MIDDLE);
        n.addThemeVariants(variant);
    }
}