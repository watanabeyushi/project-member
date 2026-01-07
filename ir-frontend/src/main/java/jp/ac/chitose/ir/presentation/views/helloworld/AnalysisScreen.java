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

    // UIエリアの定義
    private final Div fixedHeaderArea = new Div();
    private final Div scrollableBodyArea = new Div();

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

        // 全体の余白設定を無効化（座標指定レイアウトのため）
        setPadding(false);
        setSpacing(false);
        setSizeFull();

        setupLayout();
    }

    /**
     * 画面の基本レイアウト（固定枠とスクロール枠）を構築します
     */
    private void setupLayout() {
        // 1. 左上の共通ヘッダータイトル
        H1 headerTitle = new H1("CIST-IR");
        headerTitle.getStyle()
                .set("position", "fixed")
                .set("top", "120px") // サイドバー等との位置関係に合わせて調整
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

        // 3. 詳細情報を表示する固定エリア (背景白で透過防止)
        fixedHeaderArea.getStyle()
                .set("position", "fixed")
                .set("top", "120px")
                .set("left", "240px")
                .set("right", "20px")
                .set("height", "140px") // 高さ固定
                .set("background-color", "white") // 下のコンテンツが透けないように
                .set("z-index", "20") // 最前面に表示
                .set("border-bottom", "1px solid #ddd") // 境界線
                .set("padding-left", "10px"); // 内部の余白

        // 4. グラフを表示するスクロールエリア
        // 固定エリアの下から開始し、画面下部まで広げる
        scrollableBodyArea.getStyle()
                .set("position", "fixed")
                .set("top", "270px") // 固定ヘッダー(120+140=260)の直下 + 余白
                .set("left", "240px")
                .set("right", "20px")
                .set("bottom", "80px") // 下部のボタン用スペースを空ける
                .set("overflow-y", "auto") // 縦スクロールを有効化
                .set("z-index", "5")
                .set("padding-right", "10px"); // スクロールバーとの被り防止

        // 5. 戻るボタン
        Button backButton = new Button("戻る", e -> getUI().ifPresent(ui -> ui.navigate("hello")));
        backButton.getStyle()
                .set("position", "fixed")
                .set("bottom", "20px")
                .set("left", "20px")
                .set("z-index", "30");

        // コンポーネントをルートに追加
        add(headerTitle, pageTitle, fixedHeaderArea, scrollableBodyArea, backButton);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String lectureName) {
        if (lectureName != null && !lectureName.isEmpty()) {
            fetchLectureDetail(lectureName);
        } else {
            Notification.show("科目名が指定されていません");
            fixedHeaderArea.removeAll();
            scrollableBodyArea.removeAll();
        }
    }

    private void fetchLectureDetail(String lectureName) {
        try {
            // 日本語URLエンコード
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
        Random random = new Random();

        // ---------------------------------------------------------
        // 1. 固定ヘッダー部分への情報追加
        // ---------------------------------------------------------
        // 座標は fixedHeaderArea 内の相対位置 (0,0 スタート)
        fixedHeaderArea.add(createSubjectTitle(data.lectureName != null ? data.lectureName : "未設定科目", "10px", "0px"));

        // 2行目の詳細情報
        fixedHeaderArea.add(createDetailRow("開講年度", (data.year != null ? data.year : "2024") + "年度", "60px", "0px"));
        fixedHeaderArea.add(createDetailRow("担当教員", data.lectureTeacher != null ? data.lectureTeacher : "未設定", "60px", "200px"));
        fixedHeaderArea.add(createDetailRow("対象学科", data.department != null ? data.department : "全学科", "60px", "450px"));
        fixedHeaderArea.add(createDetailRow("必修区分", data.classification != null ? data.classification : "選択", "60px", "800px"));
        fixedHeaderArea.add(createDetailRow("単位数", (data.credits != null ? data.credits : "2") + "単位", "60px", "1000px"));

        // ---------------------------------------------------------
        // 2. スクロールエリアへのヒートマップ追加
        // ---------------------------------------------------------

        // 【ダミーデータ生成ロジック】 (APIからデータがない場合のフォールバック)
        if (data.crosstab == null || data.crosstab.isEmpty()) {
            data.crosstab = new HashMap<>();
            String[] ranges = {"14-16", "11-13", "8-10", "4-7", "0-3"};
            for (String range : ranges) {
                Map<String, Integer> row = new HashMap<>();
                for (int grade = 0; grade <= 4; grade++) {
                    int count = 0;
                    // 適当な分布を作成
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

        // --- ヒートマップ1: 出席数 vs 評価 ---
        if (data.crosstab != null && !data.crosstab.isEmpty()) {
            // タイトルとY軸ラベルを指定して作成
            Div heatmap1 = createHeatmap(data.crosstab, "出席数 vs 評価 クロス集計ヒートマップ", "出席数(回)");

            // レイアウト調整
            heatmap1.getStyle().set("margin-top", "20px"); // 上の余白
            heatmap1.getStyle().set("margin-bottom", "60px"); // 下の余白
            heatmap1.getStyle().set("width", "95%");

            scrollableBodyArea.add(heatmap1);
        }

        // --- ヒートマップ2: 勉強時間 vs 評価 (ダミーデータ) ---
        Map<String, Map<String, Integer>> studyTimeData = new HashMap<>();
        String[] studyRanges = {"90-120", "60-90", "30-60", "0-30"};
        for (String range : studyRanges) {
            Map<String, Integer> row = new HashMap<>();
            for (int grade = 0; grade <= 4; grade++) {
                int count = random.nextInt(10); // 完全ランダム
                if (count > 0) row.put(String.valueOf(grade), count);
            }
            if (!row.isEmpty()) studyTimeData.put(range, row);
        }

        Div heatmap2 = createHeatmap(studyTimeData, "勉強時間(分) vs 評価 クロス集計ヒートマップ", "勉強時間(分)");
        heatmap2.getStyle().set("margin-bottom", "100px"); // 最下部の余白
        heatmap2.getStyle().set("width", "95%");

        scrollableBodyArea.add(heatmap2);

        Notification.show("※データが表示されました", 3000, Notification.Position.BOTTOM_END);
    }

    /**
     * ヒートマップ用グリッドを作成するメソッド（修正版：ヘッダーに合計数表示）
     */
    private Div createHeatmap(Map<String, Map<String, Integer>> crosstabData, String chartTitle, String yAxisLabelText) {
        // 全体を包むコンテナ
        Div container = new Div();
        container.getStyle().set("display", "flex");
        container.getStyle().set("flex-direction", "column");
        container.getStyle().set("align-items", "center");

        // 1. グラフタイトル
        H3 title = new H3(chartTitle);
        title.getStyle().set("margin-bottom", "10px");
        title.getStyle().set("align-self", "flex-start");
        container.add(title);

        // 2. グラフエリア（左の軸ラベル + グリッド）
        Div graphArea = new Div();
        graphArea.getStyle().set("display", "flex");
        graphArea.getStyle().set("flex-direction", "row");
        graphArea.getStyle().set("align-items", "center");
        graphArea.getStyle().set("width", "100%");

        // --- 左側の軸ラベル ---
        Span yLabel = new Span(yAxisLabelText);
        yLabel.getStyle().set("writing-mode", "vertical-rl");
        yLabel.getStyle().set("text-orientation", "mixed");
        yLabel.getStyle().set("font-weight", "bold");
        yLabel.getStyle().set("margin-right", "10px");
        yLabel.getStyle().set("height", "300px");
        yLabel.getStyle().set("text-align", "center");
        graphArea.add(yLabel);

        // --- データ処理 ---
        // 行データの並び替え
        List<String> rows = new ArrayList<>(crosstabData.keySet());
        rows.sort((a, b) -> {
            double valA = extractFirstNumber(a);
            double valB = extractFirstNumber(b);
            return Double.compare(valB, valA); // 降順
        });

        // 列データの抽出と並び替え
        Set<String> columnSet = new HashSet<>();
        for (Map<String, Integer> rowMap : crosstabData.values()) {
            columnSet.addAll(rowMap.keySet());
        }
        List<String> columns = new ArrayList<>(columnSet);
        Collections.sort(columns);

        // 【追加】列ごとの合計（縦計）を計算する
        Map<String, Integer> columnTotals = new HashMap<>();
        for (String colKey : columns) {
            int sum = 0;
            for (Map<String, Integer> rowMap : crosstabData.values()) {
                sum += rowMap.getOrDefault(colKey, 0);
            }
            columnTotals.put(colKey, sum);
        }

        // 最大値を取得（色の濃さ用）
        int maxValue = 0;
        for (Map<String, Integer> map : crosstabData.values()) {
            for (int val : map.values()) {
                if (val > maxValue) maxValue = val;
            }
        }
        final int maxVal = (maxValue == 0) ? 1 : maxValue;

        // --- グリッド作成 ---
        Grid<String> heatmapGrid = new Grid<>();
        heatmapGrid.setItems(rows);

        // 行ヘッダー（14-16など）
        heatmapGrid.addColumn(key -> key)
                .setHeader("")
                .setWidth("100px")
                .setFlexGrow(0)
                .setFrozen(true);

        // データ列の追加
        for (String colKey : columns) {
            String label = convertGradeToLabel(colKey);     // "秀" など
            int total = columnTotals.getOrDefault(colKey, 0); // 合計値

            // 【修正】ヘッダーに合計数を付与する (例: "秀 (24)")
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
                    .setHeader(headerText) // ここに合計付きのテキストを設定
                    .setAutoWidth(true);
        }

        heatmapGrid.setHeight("350px");
        heatmapGrid.getStyle().set("flex-grow", "1");

        graphArea.add(heatmapGrid);
        container.add(graphArea);

        // 3. 下側の軸ラベル
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

    // 0-4の評価値を日本語に変換するメソッド
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