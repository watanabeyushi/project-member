package jp.ac.chitose.ir.presentation.views.helloworld;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import jp.ac.chitose.ir.application.service.helloworld.HelloService;
import jp.ac.chitose.ir.presentation.component.MainLayout;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Hello World")
@Route(value = "hello", layout = MainLayout.class)
@PermitAll
public class HelloWorldView extends VerticalLayout {

    private final HelloService helloService;

    // 検索条件コンポーネント
    private final RadioButtonGroup<String> grade = new RadioButtonGroup<>();
    private final RadioButtonGroup<String> department = new RadioButtonGroup<>();
    private final RadioButtonGroup<String> classification = new RadioButtonGroup<>();

    // メインの表示グリッド
    private final Grid<SubjectData> grid = new Grid<>(SubjectData.class, false);

    // マスタデータ受け取り用クラス
    static class MasterListResponse {
        public MasterListData data;
    }
    static class MasterListData {
        @JsonProperty("target_grades") public List<String> targetGrades;
        @JsonProperty("target_departments") public List<String> targetDepartments;
        @JsonProperty("compulsory_subjects") public List<String> compulsorySubjects;
    }

    // 検索結果データクラス
    public static class SubjectData {
        @JsonProperty("subject_name") private String subjectName;
        @JsonProperty("year") private Integer year;
        @JsonProperty("target_grade") private String targetGrade;
        @JsonProperty("department") private String department;
        @JsonProperty("classification") private String classification;
        @JsonProperty("credits") private Integer credits;

        public String getSubjectName() { return subjectName; }
        public Integer getYear() { return year; }
        public String getTargetGrade() { return targetGrade; }
        public String getDepartment() { return department; }
        public String getClassification() { return classification; }
        public Integer getCredits() { return credits; }
    }

    public HelloWorldView(HelloService helloService) {
        this.helloService = helloService;

        // 画面幅いっぱい、高さは内容なり
        setWidthFull();

        setupLayout();
        loadMasterListData();
    }

    private void setupLayout() {
        H1 title = new H1("CIST-IR");
        H2 title2 = new H2("成績と他要因の関係");

        // ラベル設定
        grade.setLabel("学年");
        department.setLabel("学科");
        classification.setLabel("必修区分");

        // 変更リスナー（即時反映）
        grade.addValueChangeListener(e -> { if (e.isFromClient()) updateSubjectList(); });
        department.addValueChangeListener(e -> { if (e.isFromClient()) updateSubjectList(); });
        classification.addValueChangeListener(e -> { if (e.isFromClient()) updateSubjectList(); });

        // --- リセットボタンの作成 ---
        Button resetButton = new Button("条件リセット");
        // 見た目を少し控えめにするなどのスタイル調整（お好みで）
        resetButton.addThemeVariants(ButtonVariant.LUMO_SMALL);

        // クリック時の動作
        resetButton.addClickListener(e -> {
            // すべてのラジオボタンを「全体」に戻す
            grade.setValue("全て");
            department.setValue("全て");
            classification.setValue("全て");

            // 値をセットしただけでは isFromClient() が false になりリスナーが動かないため、
            // 明示的に検索メソッドを呼び出して画面を更新する
            updateSubjectList();
        });

        // --- レイアウト ---

        // 1段目: 学年、必修区分、そしてリセットボタンを横並びにする
        HorizontalLayout row1 = new HorizontalLayout(grade, classification, resetButton);
        row1.setDefaultVerticalComponentAlignment(Alignment.END); // 下揃えにして高さを合わせる

        // 2段目: 学科
        HorizontalLayout row2 = new HorizontalLayout(department);
        row2.setDefaultVerticalComponentAlignment(Alignment.END);

        VerticalLayout filters = new VerticalLayout(row1, row2);
        filters.setPadding(false);
        filters.setSpacing(false);

        // グリッド設定
        configureGrid();

        add(title, title2, filters, grid);
    }

    private void configureGrid() {
        // 科目名（ボタン表示）
        grid.addComponentColumn(subject -> {
            Button button = new Button(subject.getSubjectName());
            button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            button.getStyle().set("background-color", "#e8f0fe");
            button.getStyle().set("color", "#1a73e8");
            button.getStyle().set("font-weight", "bold");
            button.getStyle().set("padding", "0 10px");
            button.getStyle().set("height", "auto");

            button.addClickListener(e -> handleSubjectClick(subject));
            return button;
        }).setHeader("科目名").setSortable(true).setAutoWidth(true);

        grid.addColumn(SubjectData::getYear).setHeader("開講年度").setSortable(true);
        grid.addColumn(SubjectData::getTargetGrade).setHeader("対象学年").setSortable(true);
        grid.addColumn(SubjectData::getDepartment).setHeader("対象学科").setSortable(true).setAutoWidth(true);
        grid.addColumn(SubjectData::getClassification).setHeader("必選別").setSortable(true);
        grid.addColumn(SubjectData::getCredits).setHeader("単位数").setSortable(true);

        // 全行表示設定
        grid.setAllRowsVisible(true);
        grid.setWidthFull();
    }

    private void loadMasterListData() {
        try {
            HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3)).build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:8000/grade/master_list"))
                    .GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                MasterListResponse result = mapper.readValue(response.body(), MasterListResponse.class);

                if (result != null && result.data != null) {
                    setItemsWithAll(grade, result.data.targetGrades);
                    setItemsWithAll(department, result.data.targetDepartments);
                    setItemsWithAll(classification, result.data.compulsorySubjects);

                    updateSubjectList();
                }
            } else {
                showNotification("APIエラー: " + response.statusCode(), NotificationVariant.LUMO_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showNotification("マスタデータ取得失敗", NotificationVariant.LUMO_ERROR);
        }
    }

    private void setItemsWithAll(RadioButtonGroup<String> group, List<String> items) {
        List<String> list = new ArrayList<>();
        list.add("全て");
        if (items != null) list.addAll(items);
        group.setItems(list);
        group.setValue("全て");
    }

    private void updateSubjectList() {
        String selectedGrade = grade.getValue();
        String selectedDept = department.getValue();
        String selectedClass = classification.getValue();

        if (selectedGrade == null || selectedDept == null || selectedClass == null) {
            return;
        }

        try {
            String paramGrade = selectedGrade.equals("全て") ? "all" : URLEncoder.encode(selectedGrade, StandardCharsets.UTF_8);
            String paramDept = selectedDept.equals("全て") ? "all" : URLEncoder.encode(selectedDept, StandardCharsets.UTF_8);
            String paramClass = selectedClass.equals("全て") ? "all" : URLEncoder.encode(selectedClass, StandardCharsets.UTF_8);

            String apiUrl = String.format("http://127.0.0.1:8000/grade/search?grade=%s&dept=%s&classification=%s",
                    paramGrade, paramDept, paramClass);

            fetchAndDisplayData(apiUrl);

        } catch (Exception e) {
            e.printStackTrace();
            showNotification("リクエスト作成エラー", NotificationVariant.LUMO_ERROR);
        }
    }

    private void fetchAndDisplayData(String apiUrl) {
        try {
            HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                List<SubjectData> subjects = mapper.readValue(response.body(), new TypeReference<List<SubjectData>>(){});

                grid.setItems(subjects);

            } else {
                showNotification("検索エラー: " + response.statusCode(), NotificationVariant.LUMO_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showNotification("通信エラー", NotificationVariant.LUMO_ERROR);
        }
    }

    private void handleSubjectClick(SubjectData subject) {
        // 詳細画面へ遷移
        getUI().ifPresent(ui -> ui.navigate(AnalysisScreen.class, subject.getSubjectName()));
    }

    private void showNotification(String message, NotificationVariant variant) {
        Notification n = Notification.show(message, 3000, Notification.Position.MIDDLE);
        n.addThemeVariants(variant);
    }
}