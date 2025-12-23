package jp.ac.chitose.ir.presentation.views.helloworld;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;
import jp.ac.chitose.ir.application.service.helloworld.HelloService;
import jp.ac.chitose.ir.presentation.component.MainLayout;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@PageTitle("Analysis Screen")
@Route(value = "AnalysisScreen", layout = MainLayout.class)
@PermitAll
public class AnalysisScreen extends VerticalLayout implements HasUrlParameter<String> {

    private final HelloService helloService;
    private final Grid<LectureData> grid = new Grid<>(LectureData.class, false);
    private final VerticalLayout scrollableContent = new VerticalLayout();

    static class SearchResponse {
        public List<LectureData> results;
    }

    static class LectureData {
        @JsonProperty("lecture_name")
        public String lectureName;
        @JsonProperty("lecture_teacher")
        public String lectureTeacher;
        @JsonProperty("number_credits_course")
        public String credits;
    }

    public AnalysisScreen(HelloService helloService) {
        this.helloService = helloService;

        setupStaticLayout();
        setupGrid();

        Button backButton = new Button("戻る", e -> getUI().ifPresent(ui -> ui.navigate("hello")));
        backButton.getStyle().set("position", "fixed").set("bottom", "20px").set("left", "20px").set("z-index", "10");

        add(scrollableContent, backButton);
    }

    private void setupStaticLayout() {
        H1 title = new H1("CIST-IR");
        title.getStyle().set("position", "fixed").set("top", "100px").set("left", "20px").set("z-index", "10");

        H2 title2 = new H2("成績と他要因の関係");
        title2.getStyle().set("position", "fixed").set("top", "150px").set("left", "20px").set("z-index", "10");

        scrollableContent.getStyle().set("position", "absolute").set("top", "220px").set("bottom", "80px").set("overflow-y", "auto");
        scrollableContent.setWidthFull();

        add(title, title2);
    }

    private void setupGrid() {
        // カラム定義の修正
        grid.addColumn(d -> d.lectureName).setHeader("授業名").setFlexGrow(1);
        grid.addColumn(d -> d.lectureTeacher).setHeader("教員").setFlexGrow(1);
        grid.addColumn(d -> d.credits).setHeader("単位数").setWidth("100px");

        // 高さと幅を明示的に指定して描画を確実にします
        grid.setHeight("500px");
        grid.setWidthFull();

        scrollableContent.add(grid);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        Location location = event.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        Map<String, List<String>> parametersMap = queryParameters.getParameters();

        String grade = parametersMap.getOrDefault("grade", Collections.singletonList("")).get(0);
        String semester = parametersMap.getOrDefault("semester", Collections.singletonList("")).get(0);
        String dept = parametersMap.getOrDefault("dept", Collections.singletonList("")).get(0);

        if (!grade.isEmpty()) {
            fetchLecturesFromApi(grade, semester, dept);
        }
    }

    private void fetchLecturesFromApi(String grade, String semester, String dept) {
        try {
            String encodedGrade = java.net.URLEncoder.encode(grade, java.nio.charset.StandardCharsets.UTF_8);
            String encodedSemester = java.net.URLEncoder.encode(semester, java.nio.charset.StandardCharsets.UTF_8);
            String encodedDept = java.net.URLEncoder.encode(dept, java.nio.charset.StandardCharsets.UTF_8);

            String url = String.format("http://127.0.0.1:8000/grade/search?target_grade=%s&available_semester=%s&target_department=%s",
                    encodedGrade, encodedSemester, encodedDept);

            System.out.println("Request URL: " + url);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                SearchResponse result = mapper.readValue(response.body(), SearchResponse.class);

                if (result != null && result.results != null) {
                    System.out.println("取得件数: " + result.results.size());
                    grid.setItems(result.results);
                }
            } else {
                System.out.println("APIエラー ステータスコード: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}