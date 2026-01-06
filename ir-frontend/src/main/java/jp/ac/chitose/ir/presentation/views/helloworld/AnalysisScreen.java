package jp.ac.chitose.ir.presentation.views.helloworld;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;
import jp.ac.chitose.ir.application.service.helloworld.HelloService;
import jp.ac.chitose.ir.presentation.component.MainLayout;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

@PageTitle("Analysis Screen")
@Route(value = "analysis", layout = MainLayout.class)
@PermitAll
public class AnalysisScreen extends VerticalLayout implements HasUrlParameter<String> {

    private final HelloService helloService;

    // 自由配置するために VerticalLayout ではなく Div を使用します
    private final Div contentArea = new Div();

    static class DetailResponse {
        public LectureDetailData data;
    }

    static class LectureDetailData {
        @JsonProperty("lecture_name") public String lectureName;
        @JsonProperty("lecture_teacher") public String lectureTeacher;
        @JsonProperty("number_credits_course") public String credits;
        @JsonProperty("year") public String year;
        @JsonProperty("target_department") public String department;
        @JsonProperty("classification") public String classification;

        // クロス集計データ
        @JsonProperty("crosstab") public Map<String, Map<String, Integer>> crosstab;
    }

    public AnalysisScreen(HelloService helloService) {
        this.helloService = helloService;

        // レイアウト設定リセット
        setPadding(false);
        setSpacing(false);
        setSizeFull();

        setupLayout();
    }

    private void setupLayout() {
        // 1. ヘッダータイトル
        H1 headerTitle = new H1("CIST-IR");
        headerTitle.getStyle()
                .set("position", "fixed")
                .set("top", "120px")
                .set("left", "20px")
                .set("margin", "0")
                .set("z-index", "10");

        // 2. ページタイトル
        H2 pageTitle = new H2("科目詳細分析");
        pageTitle.getStyle()
                .set("position", "fixed")
                .set("top", "180px")
                .set("left", "20px")
                .set("margin", "0")
                .set("width", "200px")
                .set("z-index", "10");

        // 3. 詳細情報表示エリア (キャンバス)
        contentArea.getStyle()
                .set("position", "fixed")
                .set("top", "80px")      // エリアの開始位置 (Y)
                .set("left", "240px")    // エリアの開始位置 (X)
                .set("right", "20px")    // 画面右端まで
                .set("bottom", "80px")   // 下端
                .set("overflow-y", "auto"); // はみ出たらスクロール

        // 4. 戻るボタン
        Button backButton = new Button("戻る", e -> getUI().ifPresent(ui -> ui.navigate("hello")));
        backButton.getStyle()
                .set("position", "fixed")
                .set("bottom", "20px")
                .set("left", "20px")
                .set("z-index", "10");

        add(headerTitle, pageTitle, contentArea, backButton);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String lectureName) {
        if (lectureName != null && !lectureName.isEmpty()) {
            fetchLectureDetail(lectureName);
        } else {
            Notification.show("科目名が指定されていません");
            contentArea.removeAll();
        }
    }

    private void fetchLectureDetail(String lectureName) {
        try {
            String encodedName = URLEncoder.encode(lectureName, StandardCharsets.UTF_8);
            String url = "http://127.0.0.1:8000/grade/helloworld?lecture_name=" + encodedName;

            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                DetailResponse result = mapper.readValue(response.body(), DetailResponse.class);

                if (result != null && result.data != null) {
                    displayData(result.data);
                }
            } else {
                Notification.show("データ取得エラー: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Notification.show("通信エラーが発生しました");
        }
    }

    private void displayData(LectureDetailData data) {
        contentArea.removeAll();

        // 1. 基本情報の表示
        contentArea.add(createSubjectTitle(data.lectureName != null ? data.lectureName : "テスト科目", "50px", "0px"));
        contentArea.add(createDetailRow("開講年度", (data.year != null ? data.year : "2024") + "年度", "100px", "0px"));
        contentArea.add(createDetailRow("担当教員", data.lectureTeacher != null ? data.lectureTeacher : "テスト教員", "100px", "200px"));
        contentArea.add(createDetailRow("対象学科", data.department != null ? data.department : "情報工学科", "100px", "465px"));
        contentArea.add(createDetailRow("必修区分", data.classification != null ? data.classification : "必修", "100px", "800px"));
        contentArea.add(createDetailRow("単位数", (data.credits != null ? data.credits : "2") + "単位", "100px", "970px"));

        // ============================================================
        // ★★★ テスト用：ダミーデータ生成 (Attendance範囲指定, Grading 0-4) ★★★
        // ============================================================
        if (data.crosstab == null || data.crosstab.isEmpty()) {
            data.crosstab = new HashMap<>();
            Random random = new Random();

            // 0~16を5つの範囲に分割
            String[] ranges = {"14-16", "11-13", "8-10", "4-7", "0-3"};

            for (String range : ranges) {
                Map<String, Integer> row = new HashMap<>();

                // 評価 0 ～ 4 の分布を生成
                for (int grade = 0; grade <= 4; grade++) {
                    int count = 0;

                    // 範囲ごとの傾向を設定
                    switch (range) {
                        case "14-16": // 出席率 高 -> 高評価が多い
                            if (grade >= 3) count = random.nextInt(15) + 5;
                            else if (grade == 2) count = random.nextInt(5);
                            else count = 0;
                            break;

                        case "11-13": // 出席率 中の上
                            if (grade >= 2 && grade <= 4) count = random.nextInt(10) + 2;
                            else count = random.nextInt(2);
                            break;

                        case "8-10":  // 出席率 中
                            if (grade >= 1 && grade <= 3) count = random.nextInt(8) + 1;
                            else count = random.nextInt(3);
                            break;

                        case "4-7":   // 出席率 低
                            if (grade <= 2) count = random.nextInt(10) + 1;
                            else count = 0;
                            break;

                        case "0-3":   // 出席率 極低
                            if (grade == 0) count = random.nextInt(10) + 5;
                            else if (grade == 1) count = random.nextInt(2);
                            else count = 0;
                            break;
                    }

                    if (count > 0) {
                        row.put(String.valueOf(grade), count);
                    }
                }

                if (!row.isEmpty()) {
                    data.crosstab.put(range, row);
                }
            }

            Notification.show("※テスト用データ(出席回数5分割)を表示しています", 3000, Notification.Position.MIDDLE);
        }
        // ============================================================

        // 2. クロス集計ヒートマップの表示
        if (data.crosstab != null && !data.crosstab.isEmpty()) {
            Div heatmapContainer = createHeatmap(data.crosstab);

            heatmapContainer.getStyle().set("position", "absolute");
            heatmapContainer.getStyle().set("top", "180px");
            heatmapContainer.getStyle().set("left", "0px");
            heatmapContainer.getStyle().set("width", "95%");

            contentArea.add(heatmapContainer);
        } else {
            Div noDataMessage = new Div();
            noDataMessage.setText("※ 表示できるクロス集計データがありません");
            noDataMessage.getStyle().set("color", "#d32f2f");
            noDataMessage.getStyle().set("position", "absolute");
            noDataMessage.getStyle().set("top", "180px");
            noDataMessage.getStyle().set("left", "0px");
            contentArea.add(noDataMessage);
        }
    }

    private Div createHeatmap(Map<String, Map<String, Integer>> crosstabData) {
        Div container = new Div();

        H3 title = new H3("出席数 と 評価 クロス集計ヒートマップ");
        title.getStyle().set("margin-bottom", "10px");
        container.add(title);

        // --- データ前処理 ---
        List<String> rows = new ArrayList<>(crosstabData.keySet());

        rows.sort((a, b) -> {
            double valA = extractFirstNumber(a);
            double valB = extractFirstNumber(b);
            return Double.compare(valB, valA);
        });

        Set<String> columnSet = new HashSet<>();
        for (Map<String, Integer> rowMap : crosstabData.values()) {
            columnSet.addAll(rowMap.keySet());
        }
        List<String> columns = new ArrayList<>(columnSet);
        Collections.sort(columns);

        int maxValue = 0;
        for (Map<String, Integer> map : crosstabData.values()) {
            for (int val : map.values()) {
                if (val > maxValue) maxValue = val;
            }
        }
        final int maxVal = (maxValue == 0) ? 1 : maxValue;

        Grid<String> heatmapGrid = new Grid<>();
        heatmapGrid.setItems(rows);

        heatmapGrid.addColumn(attendanceKey -> attendanceKey)
                .setHeader("出席数")
                .setWidth("100px")
                .setFlexGrow(0)
                .setFrozen(true);

        for (String colKey : columns) {
            heatmapGrid.addComponentColumn(attendanceKey -> {
                        Map<String, Integer> rowMap = crosstabData.get(attendanceKey);
                        Integer count = rowMap != null ? rowMap.getOrDefault(colKey, 0) : 0;

                        Span cellContent = new Span(String.valueOf(count));
                        cellContent.getStyle().set("width", "100%").set("height", "100%").set("display", "block").set("text-align", "center");

                        if (count > 0) {
                            double ratio = (double) count / maxVal;
                            String rgbaColor = String.format("rgba(26, 115, 232, %.2f)", 0.1 + (0.9 * ratio));
                            cellContent.getStyle().set("background-color", rgbaColor);
                            if (ratio > 0.5) {
                                cellContent.getStyle().set("color", "white");
                            } else {
                                cellContent.getStyle().set("color", "black");
                            }
                            cellContent.getStyle().set("font-weight", "bold");
                        } else {
                            cellContent.getStyle().set("color", "#ccc");
                        }

                        return cellContent;
                    })
                    .setHeader(colKey)
                    .setAutoWidth(true);
        }

        heatmapGrid.setHeight("400px");
        container.add(heatmapGrid);
        return container;
    }

    private H3 createSubjectTitle(String text, String top, String left) {
        H3 title = new H3(text);
        title.getStyle()
                .set("color", "black")
                .set("margin", "0")
                .set("position", "absolute")
                .set("top", top)
                .set("left", left);
        return title;
    }

    // 修正箇所: createDetailRowメソッド
    private Div createDetailRow(String labelText, String valueText, String top, String left) {
        Div row = new Div();
        row.getStyle().set("position", "absolute");
        row.getStyle().set("top", top);
        row.getStyle().set("left", left);

        row.getStyle().set("display", "flex").set("align-items", "baseline").set("font-size", "1.1em");

        // 変更点1: ": " の空白を削除して ":" に変更
        // 変更点2: .set("width", "100px") を削除（これにより余計な隙間がなくなります）
        Span label = new Span(labelText + ":");
        label.getStyle().set("font-weight", "bold").set("color", "#555");

        Span value = new Span(valueText);
        value.getStyle().set("font-weight", "normal");

        // 念のため少しだけマージンを入れてくっつきすぎないようにする（不要なら削除可）
        // value.getStyle().set("margin-left", "5px");

        row.add(label, value);
        return row;
    }

    private double extractFirstNumber(String text) {
        try {
            if (text == null || text.isEmpty()) return 0;
            String numPart = text.split("-")[0].trim();
            return Double.parseDouble(numPart);
        } catch (Exception e) {
            return 0;
        }
    }
}