package jp.ac.chitose.ir.presentation.views.settings;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import jp.ac.chitose.ir.application.exception.UserManagementException;
import jp.ac.chitose.ir.application.service.management.UsersService;
import jp.ac.chitose.ir.presentation.component.MainLayout;
import jp.ac.chitose.ir.presentation.component.notification.ErrorNotification;
import jp.ac.chitose.ir.presentation.component.notification.SuccessNotification;

@PageTitle("PasswordUpdate")
@Route(value = "/settings/password", layout = MainLayout.class)
@PermitAll
public class PasswordUpdateView extends VerticalLayout {
    private final UsersService usersService;
    private PasswordField prePasswordTextField;
    private PasswordField newPasswordTextField;
    private PasswordField confirmPasswordTextField;
    private Button updatePassword;
    private Button cancelButton;

    // コンストラクタ
    public PasswordUpdateView(UsersService usersService) {
        this.usersService = usersService;
        initializeTextField();
        initializeButton();
        addComponents();
    }

    private void initializeTextField(){
        prePasswordTextField = new PasswordField("現在のパスワード");
        newPasswordTextField = new PasswordField("新しいパスワード");
        confirmPasswordTextField = new PasswordField("新しいパスワード(確認用)");
    }

    private void initializeButton(){
        updatePassword = new Button("適用", new Icon(VaadinIcon.PLAY), buttonClickEvent -> {
            String prePassword = prePasswordTextField.getValue();
            String newPassword = newPasswordTextField.getValue();
            String confirmPassword = confirmPasswordTextField.getValue();
            try {
                usersService.updateLoginUserPassword(prePassword, newPassword, confirmPassword);
                new SuccessNotification("パスワードの変更が完了しました");
            } catch (UserManagementException e){
                if(e.getMessage().isEmpty()) new ErrorNotification("エラーが発生しました");
                else new ErrorNotification(e.getMessage());
            } catch (RuntimeException e){
                e.printStackTrace();
                new ErrorNotification("エラーが発生しました");
            }
        });
        updatePassword.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        cancelButton = new Button("戻る", buttonClickEvent -> {
            UI.getCurrent().navigate("/top");
        });
    }

    private void addComponents(){
        add(cancelButton);
        add(new H1("パスワード変更"), new Paragraph("パスワードを変更することができます。現在のパスワードと変更後のパスワードを入力してください。※パスワードは12文字以上で設定してください。"));
        FormLayout formLayout1 = new FormLayout(prePasswordTextField);
        FormLayout formLayout2 = new FormLayout(newPasswordTextField, confirmPasswordTextField);
        formLayout1.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));
        formLayout2.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));
        add(formLayout1, formLayout2);
        add(updatePassword);
    }

}
