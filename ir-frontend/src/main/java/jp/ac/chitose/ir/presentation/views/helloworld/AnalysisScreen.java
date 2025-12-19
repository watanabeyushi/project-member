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
@Route(value = "AnalysisScreen", layout = MainLayout.class)
@PermitAll
public class AnalysisScreen extends VerticalLayout {

    private HelloService helloService;
    private VerticalLayout graphContainer; // グラフを格納するためのコンテナ

    public AnalysisScreen(HelloService helloService) {
        this.helloService = helloService;

        // 1. 固定表示する見出し群を設定します。
        H1 title = new H1("CIST-IR");
        title.getStyle().set("position", "fixed");
        title.getStyle().set("top", "100px"); // 数値を調整しました
        title.getStyle().set("left", "20px");
        title.getStyle().set("z-index", "10");

        H2 title2 = new H2("成績と他要因の関係");
        title2.getStyle().set("position", "fixed");
        title2.getStyle().set("top", "150px");
        title2.getStyle().set("left", "20px");
        title2.getStyle().set("z-index", "10");

        H2 title3 = new H2("授業名：●●●　教員：●●○●　単位数：〇単位");
        title3.getStyle().set("position", "fixed");
        title3.getStyle().set("top", "150px");
        title3.getStyle().set("left", "400px");
        title3.getStyle().set("z-index", "10");

        // 2. スクロール可能なコンテンツ領域を作成します。
        // ここでは VerticalLayout をコンテナとして使用します。
        VerticalLayout scrollableContent = new VerticalLayout();

        // スクロール領域の位置とサイズを固定します。
        // 固定見出し（150px程度）の下から開始するように設定します。
        scrollableContent.getStyle().set("position", "absolute");
        scrollableContent.getStyle().set("top", "200px"); // 見出しと重ならない開始位置
        scrollableContent.getStyle().set("left", "0");
        scrollableContent.getStyle().set("bottom", "80px"); // 戻るボタンの領域を確保
        scrollableContent.getStyle().set("overflow-y", "auto"); // 縦方向にスクロール可能にする
        scrollableContent.setWidthFull();

        // スクロールしたい内容（title4やグラフなど）はこのコンテナに追加します。
        H3 title4 = new H3("ここにグラフを挿入");
        // テスト用に長いテキストを追加してスクロールを確認できるようにします。
        for (int i = 0; i < 20; i++) {
            scrollableContent.add(new Paragraph("データ行 " + i));
        }
        scrollableContent.add(title4);

        // 3. 戻るボタンの設定（変更なし）
        Button sampleButton = new Button("戻る");
        sampleButton.getStyle().set("position", "fixed");
        sampleButton.getStyle().set("bottom", "20px");
        sampleButton.getStyle().set("left", "20px");
        sampleButton.getStyle().set("z-index", "10");

        sampleButton.addClickListener(event -> {
            getUI().ifPresent(ui -> ui.navigate("hello"));
        });

        // 4. すべてをメインレイアウトに追加します。
        add(title, title2, title3, scrollableContent, sampleButton);
    }


}
