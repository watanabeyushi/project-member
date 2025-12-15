package jp.ac.chitose.ir.presentation.views.usermanagement;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import jp.ac.chitose.ir.application.exception.UserManagementException;
import jp.ac.chitose.ir.application.service.management.RoleService;
import jp.ac.chitose.ir.application.service.management.UsersService;
import jp.ac.chitose.ir.presentation.component.MainLayout;
import jp.ac.chitose.ir.presentation.component.notification.ErrorNotification;
import jp.ac.chitose.ir.presentation.component.notification.SuccessNotification;

import java.util.Set;

@PageTitle("UserAdd")
@Route(value = "/user_management/add", layout = MainLayout.class)
@RolesAllowed({"administrator"})
public class UserAddView extends VerticalLayout {
    private final UsersService usersService;
    private Button createAccount;
    private Button cancelButton;
    private UserManagementTextFields userManagementTextFields;
    private final RoleService roleService;

    public UserAddView(UsersService usersService, RoleService roleService) {
        this.usersService = usersService;
        this.roleService = roleService;
        userManagementTextFields = new UserManagementTextFields(this.roleService);
        initializeButton();
        addComponents();
    }

    // ボタンの初期設定
    private void initializeButton() {
        createAccount = new Button("追加", new Icon(VaadinIcon.PLUS), buttonClickEvent -> {
            // テキストフィールド・チェックボックス上の値をサービスに渡す
            String loginID = userManagementTextFields.getLoginID();
            String userName = userManagementTextFields.getUserName();
            String password = userManagementTextFields.getUserPassword();
            Set<Integer> selectedRoleIds = userManagementTextFields.getRoleIds();

            try {
                usersService.addUser(loginID, userName, password, selectedRoleIds);
                new SuccessNotification(userName + "の追加に成功");
            } catch (UserManagementException e){
                if(e.getMessage().isEmpty()) new ErrorNotification("エラーが発生しました");
                else new ErrorNotification(e.getMessage());
            } catch (RuntimeException e){
                e.printStackTrace();
                new ErrorNotification("エラーが発生しました");
            }
        });
        cancelButton = new Button("戻る", buttonClickEvent -> {
            UI.getCurrent().navigate("/user_management");
        });
    }

    // 各種コンポーネントの追加
    private void addComponents() {
        add(new H1("ユーザーの追加"), new Paragraph("ユーザーを追加することができます。追加したいユーザーのユーザID,ユーザーネーム,12文字以上のパスワードを入力し、ロールを選択してください。"));
        add(cancelButton);
        add(userManagementTextFields);
        add(createAccount);
    }
}
