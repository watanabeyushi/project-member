package jp.ac.chitose.ir.presentation.views.usermanagement;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import jp.ac.chitose.ir.application.exception.UserManagementException;
import jp.ac.chitose.ir.application.service.management.UserManagementService;
import jp.ac.chitose.ir.application.service.management.UsersData;
import jp.ac.chitose.ir.application.service.management.UsersService;
import jp.ac.chitose.ir.presentation.component.MainLayout;
import jp.ac.chitose.ir.presentation.component.notification.ErrorNotification;
import jp.ac.chitose.ir.presentation.component.notification.SuccessNotification;

import java.util.Set;

@PageTitle("UserActivate")
@Route(value = "/user_management/activate", layout = MainLayout.class)
@RolesAllowed({"administrator"})
public class UserActivateView extends VerticalLayout {
    private final UserManagementService userManagementService;
    private final UsersService usersService;
    private Button activateAccount;
    private Button cancelButton;
    private final UsersDataGrid usersDataGrid;

    // コンストラクタ
    public UserActivateView(UserManagementService userManagementService, UsersService usersService) {
        // 文字列がセッションに渡されていたら成功メッセージを出力→セッションの文字列をnullに戻す
        if(UI.getCurrent().getSession().getAttribute(String.class) != null) {
            new SuccessNotification(UI.getCurrent().getSession().getAttribute(String.class));
            UI.getCurrent().getSession().setAttribute(String.class, null);
        }
        this.userManagementService = userManagementService;
        this.usersService = usersService;
        initializeButton();
        usersDataGrid = new UsersDataGrid(this.userManagementService, UsersDataGrid.SelectionMode.MULTI);
        addComponents();
    }


    // ボタンの初期設定
    private void initializeButton() {
        activateAccount = new Button("有効化", new Icon(VaadinIcon.PLAY), buttonClickEvent -> {
            // 選択されているユーザーの情報を取得
            Set<UsersData> selectedUsers = usersDataGrid.getGrid().getSelectedItems();

            // ユーザ有効化処理
            // 正常終了→成功メッセージ 異常終了→エラーメッセージ
            try {
                usersService.activateUsers(selectedUsers);
                // 成功メッセージをセッションに渡して画面をリロード
                UI.getCurrent().getSession().setAttribute(String.class, selectedUsers.size() + " 件のユーザの有効化に成功");
                UI.getCurrent().getPage().reload();
            } catch (UserManagementException e){
                if(e.getMessage().isEmpty()) new ErrorNotification("エラーが発生しました");
                else new ErrorNotification(e.getMessage());
            } catch (RuntimeException e){
                e.printStackTrace();
                new ErrorNotification("エラーが発生しました");
            }
        });
        activateAccount.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        cancelButton = new Button("戻る", buttonClickEvent -> {
            UI.getCurrent().navigate("/user_management");
        });
    }

    // 各種コンポーネントの追加
    private void addComponents() {
        add(new H1("ユーザーの有効化"), new Paragraph("ユーザーのアクセス権限を有効化することができます。有効化したいユーザーを選んで有効化ボタンを押してください。"));
        add(cancelButton);
        add(usersDataGrid);
        add(activateAccount);
    }
}
