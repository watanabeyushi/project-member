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

    private HelloService helloService;
    private VerticalLayout graphContainer; // グラフを格納するためのコンテナ

    public HelloWorldView(HelloService helloService) {
        this.helloService = helloService;

        H1 title = new H1("Vaadin練習");
        H2 title2 = new H2("資料内容");
        H3 title3 = new H3("タイトル");
        TextField nameField1 = new TextField("名前を入力してください");
        nameField1.setPlaceholder("ここに氏名を入力");
        TextField nameField2 = new TextField("学籍番号を入力してください");
        nameField2.setPlaceholder("ここに学籍番号を入力");

        Paragraph p = new Paragraph("ボタン表示と学年選択(RadioButtonGroup)");

        TextArea commentArea = new TextArea("質問はありますか");
        commentArea.setPlaceholder("ここに質問を入力");
        commentArea.setWidthFull();
        Button sampleButton = new Button("登録！");
        RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
        radioGroup.setLabel("学年を選択");
        radioGroup.setItems("全体", "1年", "2年", "3年", "4年");

        Paragraph detailsContent = new Paragraph("このセクションには、入力フィールドに関する補足情報やヘルプを記述できます。");
        Paragraph moreContent = new Paragraph("これはボタンを押すまで非表示になっています。");

        // グラフコンテナを初期化し、幅を最大に設定
        this.graphContainer = new VerticalLayout();
        this.graphContainer.setWidthFull();
        this.graphContainer.setMargin(false);
        this.graphContainer.setPadding(false);


        // graphContainerをレイアウトの末尾に追加
        add(title,title2,nameField1,nameField2,radioGroup,commentArea,sampleButton,title3, graphContainer);


        // --- 通知表示リスナー ---
        sampleButton.addClickListener(event -> {
            String enteredName = nameField1.getValue();
            String enteredStudentID = nameField2. getValue();
            String selectedGrade = radioGroup.getValue();

            String notificationMessage = String.format(
                    "登録完了！ 名前: %s, 学籍番号: %s, 学年: %s が登録されました。",
                    enteredName.isEmpty() ? "未入力" : enteredName,
                    enteredStudentID.isEmpty() ? "未入力" : enteredStudentID,
                    selectedGrade == null ? "未選択" : selectedGrade
            );

            Notification notification = Notification.show(
                    notificationMessage,
                    3000,
                    Notification.Position.BOTTOM_END
            );
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });
        // ------------------------

        // --- グラフ表示リスナー (データチェックとエラー処理を強化) ---
        sampleButton.addClickListener(event -> {
            // 1. コンテナをクリア
            graphContainer.removeAll();

            // 2. グラフコンテナに高さを設定し、描画領域を確保する (重要)
            graphContainer.setHeight("400px");

            // 3. 処理中のメッセージを表示
            H3 loadingMessage = new H3("グラフを読み込み中...");
            graphContainer.add(loadingMessage);

            try {
                // 4. データ取得とグラフ生成
                Graph graph = creatGradeGraph();

                // 5. 成功したら、読み込み中メッセージを削除し、グラフを追加
                graphContainer.remove(loadingMessage);

                graph.getGraph().setHeight("100%"); // グラフコンポーネントの高さ設定

                graphContainer.add(graph.getGraph());

            } catch (Exception e) {
                // 6. エラー処理
                graphContainer.removeAll();
                H3 errorMessage = new H3("データの読み込み中にエラーが発生しました。");

                // データが空の場合の明確なメッセージをエラー詳細として表示
                String detail = e.getMessage() != null && !e.getMessage().isEmpty()
                        ? e.getMessage()
                        : "不明なエラーが発生しました。";

                Paragraph errorDetails = new Paragraph("エラー詳細: " + detail);
                graphContainer.add(errorMessage, errorDetails);

                e.printStackTrace();

                Notification.show("データ読み込みエラー: ログと画面を確認してください。", 5000,
                                Notification.Position.BOTTOM_END)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        // ------------------------------------
    }

    private Graph creatGradeGraph() {
        // 1. データリスト全体を取得
        List<HelloworldGrade> dataList = helloService.getGradeGraph().data();

        // 2. データが存在しない場合をチェックし、明確な例外を投げる
        if (dataList == null || dataList.isEmpty()) {
            throw new RuntimeException("APIからグラフ描画に必要なデータが取得できませんでした。");
        }

        // 3. 最初のデータ要素を取得
        HelloworldGrade data = dataList.get(0);

        // 4. グラフ生成
        GraphSeries<Integer> datas = new GraphSeries<>(
                data.不可(), data.可(),
                data.良(), data.優(), data.秀());

        String[] label = {"不可", "可", "良", "優", "秀"};

        return Graph.Builder.get()
                .graphType(GRAPH_TYPE.PIE) // 円グラフ
                .width("100%")
                .legendShow(true)
                .series(datas)
                .labels(label)
                .build();
    }
}