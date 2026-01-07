package jp.ac.chitose.ir.presentation.views.helloworld;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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

    // UIエリアの定義
    private final Div fixedHeaderArea = new Div();
    private final Div scrollableBodyArea = new Div();
    private final Div sideNavArea = new Div();

    // データマッピング用クラス
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
        @JsonProperty("crosstab") public Map<String, Map<String, Integer>> crosstab;
    }

    public AnalysisScreen(HelloService helloService) {
        this.helloService = helloService;

        setPadding(false);
        setSpacing(false);
        setSizeFull();

        setupLayout();
    }

    /**
     * 画面の基本レイアウト（固定枠、スクロール枠、サイドナビ）を構築します
     */
    private void setupLayout() {
        // 1. 左上の共通ヘッダータイトル
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

        // 3. 詳細情報を表示する固定エリア
        fixedHeaderArea.getStyle()
                .set("position", "fixed")
                .set("top", "120px")
                .set("left", "240px")
                .set("right", "240px")
                .set("height", "140px")
                .set("background-color", "white")
                .set("z-index", "20")
                .set("border-bottom", "1px solid #ddd")
                .set("padding-left", "10px");

        // 4. グラフを表示するスクロールエリア
        scrollableBodyArea.getStyle()
                .set("position", "fixed")
                .set("top", "270px")
                .set("left", "240px")
                .set("right", "240px")
                .set("bottom", "80px")
                .set("overflow-y", "auto")
                .set("z-index", "5")
                .set("padding-right", "10px");

        // 5. サイドナビゲーションエリア (目次)
        sideNavArea.getStyle()
                .set("position", "fixed")
                .set("top", "120px")
                .set("right", "20px")
                .set("width", "200px")
                .set("bottom", "80px")
                .set("padding", "10px")
                .set("border-left", "1px solid #eee")
                .set("z-index", "15")
                .set("overflow-y", "auto");

        H4 navTitle = new H4("目次");
        navTitle.getStyle().set("margin-top", "0").set("color", "#555");
        sideNavArea.add(navTitle);

        // 6. 戻るボタン (青文字リンク風)
        Button backButton = new Button("← 前のページへ戻る", e -> getUI().ifPresent(ui -> ui.navigate("hello")));
        backButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        backButton.getStyle()
                .set("position", "fixed")
                .set("top", "85px")
                .set("left", "20px")
                .set("z-index", "30")
                .set("color", "#1a73e8")
                .set("padding", "0")
                .set("font-weight", "normal")
                .set("cursor", "pointer");

        backButton.getElement().addEventListener("mouseover", e -> backButton.getStyle().set("text-decoration", "underline"));
        backButton.getElement().addEventListener("mouseout", e -> backButton.getStyle().set("text-decoration", "none"));

        add(headerTitle, pageTitle, fixedHeaderArea, scrollableBodyArea, sideNavArea, backButton);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String lectureName) {
        if (lectureName != null && !lectureName.isEmpty()) {
            fetchLectureDetail(lectureName);
        } else {
            Notification.show("科目名が指定されていません");
            fixedHeaderArea.removeAll();
            scrollableBodyArea.removeAll();
            sideNavArea.removeAll();
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
        fixedHeaderArea.removeAll();
        scrollableBodyArea.removeAll();

        // ナビゲーション初期化
        sideNavArea.removeAll();
        H4 navTitle = new H4("目次");
        navTitle.getStyle().set("margin-top", "0").set("color", "#555");
        sideNavArea.add(navTitle);

        Random random = new Random();

        // 1. 固定ヘッダー部分
        fixedHeaderArea.add(createSubjectTitle(data.lectureName != null ? data.lectureName : "未設定科目", "10px", "0px"));
        fixedHeaderArea.add(createDetailRow("開講年度", (data.year != null ? data.year : "2024") + "年度", "60px", "0px"));
        fixedHeaderArea.add(createDetailRow("担当教員", data.lectureTeacher != null ? data.lectureTeacher : "未設定", "60px", "200px"));
        fixedHeaderArea.add(createDetailRow("対象学科", data.department != null ? data.department : "全学科", "60px", "450px"));
        fixedHeaderArea.add(createDetailRow("必修区分", data.classification != null ? data.classification : "選択", "60px", "800px"));
        fixedHeaderArea.add(createDetailRow("単位数", (data.credits != null ? data.credits : "2") + "単位", "60px", "950px"));

        // -------------------------------------------------------------------------
        // 2. スクロールエリア (ヒートマップ群)
        // -------------------------------------------------------------------------

        // --- ヒートマップ1: 出席数 (既存データorダミー) ---
        if (data.crosstab == null || data.crosstab.isEmpty()) {
            data.crosstab = new HashMap<>();
            String[] ranges = {"14-16", "11-13", "8-10", "4-7", "0-3"};
            for (String range : ranges) {
                Map<String, Integer> row = new HashMap<>();
                for (int grade = 0; grade <= 4; grade++) {
                    int count = 0;
                    // 簡単な分布ロジック
                    switch (range) {
                        case "14-16": if (grade >= 3) count = random.nextInt(15) + 5; break;
                        case "11-13": if (grade >= 2 && grade <= 4) count = random.nextInt(10) + 2; break;
                        case "8-10": if (grade >= 1 && grade <= 3) count = random.nextInt(8) + 1; break;
                        case "4-7": if (grade <= 2) count = random.nextInt(10) + 1; break;
                        case "0-3": if (grade == 0) count = random.nextInt(10) + 5; break;
                    }
                    if (count > 0) row.put(String.valueOf(grade), count);
                }
                if (!row.isEmpty()) data.crosstab.put(range, row);
            }
        }

        if (!data.crosstab.isEmpty()) {
            Div heatmap1 = createHeatmap(data.crosstab, "出席数 vs 評価 クロス集計ヒートマップ", "出席数(回)");
            heatmap1.getStyle().set("margin-top", "20px");
            heatmap1.getStyle().set("margin-bottom", "60px");
            heatmap1.getStyle().set("width", "95%");

            String id1 = "section-attendance";
            heatmap1.setId(id1);

            scrollableBodyArea.add(heatmap1);
            addNavLink("出席数分布", id1);
        }

        // --- ヒートマップ2: 勉強時間 (ダミーデータ) ---
        Map<String, Map<String, Integer>> studyTimeData = new HashMap<>();
        String[] studyRanges = {"90-120", "60-90", "30-60", "0-30"};
        for (String range : studyRanges) {
            Map<String, Integer> row = new HashMap<>();
            for (int grade = 0; grade <= 4; grade++) {
                int count = random.nextInt(8); // ランダム
                if (count > 0) row.put(String.valueOf(grade), count);
            }
            if (!row.isEmpty()) studyTimeData.put(range, row);
        }

        Div heatmap2 = createHeatmap(studyTimeData, "勉強時間(分) vs 評価 クロス集計ヒートマップ", "勉強時間(分)");
        heatmap2.getStyle().set("margin-bottom", "60px");
        heatmap2.getStyle().set("width", "95%");

        String id2 = "section-study";
        heatmap2.setId(id2);

        scrollableBodyArea.add(heatmap2);
        addNavLink("学習時間分布", id2);

        // --- ヒートマップ3: 理解度 (ダミーデータ) ---
        // 90-100%, 80-89%, ...
        Map<String, Map<String, Integer>> understandingData = new HashMap<>();
        String[] understandingRanges = {"90-100%", "80-89%", "70-79%", "60-69%", "0-59%"};
        for (String range : understandingRanges) {
            Map<String, Integer> row = new HashMap<>();
            for (int grade = 0; grade <= 4; grade++) {
                int count = 0;
                // 理解度が高いほど成績が良い傾向を作る
                switch (range) {
                    case "90-100%": if (grade == 4) count = random.nextInt(12) + 5; else if (grade == 3) count = random.nextInt(5); break;
                    case "80-89%":  if (grade == 3 || grade == 4) count = random.nextInt(8) + 2; break;
                    case "70-79%":  if (grade == 2 || grade == 3) count = random.nextInt(8) + 2; break;
                    case "60-69%":  if (grade == 1 || grade == 2) count = random.nextInt(8) + 2; break;
                    case "0-59%":   if (grade == 0 || grade == 1) count = random.nextInt(10) + 3; break;
                }
                if (count > 0) row.put(String.valueOf(grade), count);
            }
            if (!row.isEmpty()) understandingData.put(range, row);
        }

        Div heatmap3 = createHeatmap(understandingData, "自己評価(理解度) vs 評価 クロス集計ヒートマップ", "理解度(%)");
        heatmap3.getStyle().set("margin-bottom", "60px");
        heatmap3.getStyle().set("width", "95%");

        String id3 = "section-understanding";
        heatmap3.setId(id3);

        scrollableBodyArea.add(heatmap3);
        addNavLink("理解度分布", id3);

        // --- ヒートマップ4: GPA (ダミーデータ) ---
        // 3.5-4.0, 3.0-3.4, ...
        Map<String, Map<String, Integer>> gpaData = new HashMap<>();
        String[] gpaRanges = {"3.5-4.0", "3.0-3.4", "2.5-2.9", "2.0-2.4", "0.0-1.9"};
        for (String range : gpaRanges) {
            Map<String, Integer> row = new HashMap<>();
            for (int grade = 0; grade <= 4; grade++) {
                int count = 0;
                // GPAが高いほど成績が良い傾向を作る
                switch (range) {
                    case "3.5-4.0": if (grade >= 3) count = random.nextInt(10) + 5; break;
                    case "3.0-3.4": if (grade >= 2 && grade <= 4) count = random.nextInt(8) + 2; break;
                    case "2.5-2.9": if (grade >= 1 && grade <= 3) count = random.nextInt(8) + 2; break;
                    case "2.0-2.4": if (grade <= 2) count = random.nextInt(8) + 2; break;
                    case "0.0-1.9": if (grade <= 1) count = random.nextInt(5) + 3; break;
                }
                if (count > 0) row.put(String.valueOf(grade), count);
            }
            if (!row.isEmpty()) gpaData.put(range, row);
        }

        Div heatmap4 = createHeatmap(gpaData, "累積GPA vs 評価 クロス集計ヒートマップ", "累積GPA");
        heatmap4.getStyle().set("margin-bottom", "100px"); // 最後なので余白多め
        heatmap4.getStyle().set("width", "95%");

        String id4 = "section-gpa";
        heatmap4.setId(id4);

        scrollableBodyArea.add(heatmap4);
        addNavLink("GPA分布", id4);

        Notification.show("※データが表示されました", 3000, Notification.Position.BOTTOM_END);
    }

    private void addNavLink(String text, String targetId) {
        Anchor link = new Anchor();
        link.setText(text);
        link.getStyle().set("display", "block");
        link.getStyle().set("padding", "8px 0");
        link.getStyle().set("color", "#1a73e8");
        link.getStyle().set("text-decoration", "none");
        link.getStyle().set("cursor", "pointer");
        link.getStyle().set("font-size", "0.95em");

        link.getElement().addEventListener("mouseover", e -> link.getStyle().set("text-decoration", "underline"));
        link.getElement().addEventListener("mouseout", e -> link.getStyle().set("text-decoration", "none"));

        link.getElement().addEventListener("click", e -> {
            if (targetId != null) {
                UI.getCurrent().getPage().executeJs(
                        "const el = document.getElementById($0);" +
                                "if(el) el.scrollIntoView({behavior: 'smooth', block: 'start'});",
                        targetId
                );
            }
        });

        sideNavArea.add(link);
    }

    private Div createHeatmap(Map<String, Map<String, Integer>> crosstabData, String chartTitle, String yAxisLabelText) {
        Div container = new Div();
        container.getStyle().set("display", "flex");
        container.getStyle().set("flex-direction", "column");
        container.getStyle().set("align-items", "center");

        H3 title = new H3(chartTitle);
        title.getStyle().set("margin-bottom", "10px");
        title.getStyle().set("align-self", "flex-start");
        container.add(title);

        Div graphArea = new Div();
        graphArea.getStyle().set("display", "flex");
        graphArea.getStyle().set("flex-direction", "row");
        graphArea.getStyle().set("align-items", "center");
        graphArea.getStyle().set("width", "100%");

        Span yLabel = new Span(yAxisLabelText);
        yLabel.getStyle().set("writing-mode", "vertical-rl");
        yLabel.getStyle().set("text-orientation", "mixed");
        yLabel.getStyle().set("font-weight", "bold");
        yLabel.getStyle().set("margin-right", "10px");
        yLabel.getStyle().set("height", "300px");
        yLabel.getStyle().set("text-align", "center");
        graphArea.add(yLabel);

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

        Map<String, Integer> columnTotals = new HashMap<>();
        for (String colKey : columns) {
            int sum = 0;
            for (Map<String, Integer> rowMap : crosstabData.values()) {
                sum += rowMap.getOrDefault(colKey, 0);
            }
            columnTotals.put(colKey, sum);
        }

        int maxValue = 0;
        for (Map<String, Integer> map : crosstabData.values()) {
            for (int val : map.values()) {
                if (val > maxValue) maxValue = val;
            }
        }
        final int maxVal = (maxValue == 0) ? 1 : maxValue;

        Grid<String> heatmapGrid = new Grid<>();
        heatmapGrid.setItems(rows);

        heatmapGrid.addColumn(key -> key)
                .setHeader("")
                .setWidth("100px")
                .setFlexGrow(0)
                .setFrozen(true);

        for (String colKey : columns) {
            String label = convertGradeToLabel(colKey);
            int total = columnTotals.getOrDefault(colKey, 0);
            String headerText = String.format("%s (%d)", label, total);

            heatmapGrid.addComponentColumn(rowKey -> {
                        Map<String, Integer> rowMap = crosstabData.get(rowKey);
                        Integer count = rowMap != null ? rowMap.getOrDefault(colKey, 0) : 0;

                        Span cellContent = new Span(String.valueOf(count));
                        cellContent.getStyle()
                                .set("width", "100%")
                                .set("height", "100%")
                                .set("display", "flex")
                                .set("align-items", "center")
                                .set("justify-content", "center");

                        if (count > 0) {
                            double ratio = (double) count / maxVal;
                            String rgbaColor = String.format("rgba(26, 115, 232, %.2f)", 0.1 + (0.9 * ratio));
                            cellContent.getStyle().set("background-color", rgbaColor);
                            cellContent.getStyle().set("color", ratio > 0.5 ? "white" : "black");
                            cellContent.getStyle().set("font-weight", "bold");
                        } else {
                            cellContent.getStyle().set("color", "#ccc");
                        }
                        return cellContent;
                    })
                    .setHeader(headerText)
                    .setAutoWidth(true);
        }

        heatmapGrid.setHeight("350px");
        heatmapGrid.getStyle().set("flex-grow", "1");

        graphArea.add(heatmapGrid);
        container.add(graphArea);

        Span xLabel = new Span("成績");
        xLabel.getStyle().set("font-weight", "bold");
        xLabel.getStyle().set("margin-top", "5px");
        container.add(xLabel);

        return container;
    }

    private H3 createSubjectTitle(String text, String top, String left) {
        H3 title = new H3(text);
        title.getStyle().set("color", "black").set("margin", "0").set("position", "absolute").set("top", top).set("left", left);
        return title;
    }

    private Div createDetailRow(String labelText, String valueText, String top, String left) {
        Div row = new Div();
        row.getStyle().set("position", "absolute").set("top", top).set("left", left);
        row.getStyle().set("display", "flex").set("align-items", "baseline").set("font-size", "1.1em");
        Span label = new Span(labelText + ":");
        label.getStyle().set("font-weight", "bold").set("color", "#555").set("margin-right", "8px");
        Span value = new Span(valueText);
        value.getStyle().set("font-weight", "normal");
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

    private String convertGradeToLabel(String gradeKey) {
        switch (gradeKey) {
            case "0": return "不可";
            case "1": return "可";
            case "2": return "良";
            case "3": return "優";
            case "4": return "秀";
            default: return gradeKey;
        }
    }
}