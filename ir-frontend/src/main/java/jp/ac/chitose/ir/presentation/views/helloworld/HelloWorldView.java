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


@PageTitle("Hello World")
@Route(value = "hello", layout = MainLayout.class)
@PermitAll
public class HelloWorldView extends VerticalLayout {

    private HelloService helloService;

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


        add(title,title2,nameField1,nameField2,radioGroup,commentArea,sampleButton,title3);



        sampleButton.addClickListener(event -> {
            /*

            ここに追加
             */
            String enteredName = nameField1.getValue();      // 名前を取得
            String enteredStudentID = nameField2.getValue(); // 学籍番号を取得
            String selectedGrade = radioGroup.getValue();    // 学年を取得

            String notificationMessage = String.format(
                    "登録完了！ 名前: %s, 学籍番号: %s, 学年: %s が登録されました。",
                    // 値が未入力・未選択の場合は「未入力/未選択」と表示する
                    enteredName.isEmpty() ? "未入力" : enteredName,
                    enteredStudentID.isEmpty() ? "未入力" : enteredStudentID,
                    selectedGrade == null ? "未選択" : selectedGrade
            );

            Notification notification = Notification.show(
                    notificationMessage,
                    3000, // 表示時間 (3秒)
                    Notification.Position.BOTTOM_END // 画面右下に表示
            );
            // 成功を示すLumoテーマのバリアント (通常は緑色) を適用
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });

        sampleButton.addClickListener(event -> {
            Graph graph = creatGradeGraph();
            add(graph.getGraph());
        });
    }
    private Graph creatGradeGraph() {
        HelloworldGrade data = helloService.getGradeGraph().data().get(0);
        GraphSeries<Integer> datas = new GraphSeries<>(
                data.不可(), data.可(),
                data.良(), data.優(), data.秀());
        String[] label = {"不可", "可", "良", "優", "秀"};
        return Graph.Builder.get()
                .graphType(GRAPH_TYPE.BAR)
                .width("100%")
                .height("100%")
                .legendShow(false)
                .series(datas)
                .labels(label)
                .build();
    }

}
