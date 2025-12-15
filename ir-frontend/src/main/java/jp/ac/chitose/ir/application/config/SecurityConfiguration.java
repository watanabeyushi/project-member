package jp.ac.chitose.ir.application.config;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import jp.ac.chitose.ir.application.service.management.AuthenticationService;
import jp.ac.chitose.ir.application.service.management.RoleService;
import jp.ac.chitose.ir.application.service.management.UserDetailsAuthenticationProvider;
import jp.ac.chitose.ir.presentation.views.login.LoginView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private RoleService roleService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // ログイン画面の指定
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }
    @Bean
    public AuthenticationProvider getAuthenticationProvider() {
        // 認証状態とユーザーの情報を取得
        return new UserDetailsAuthenticationProvider(authenticationService, roleService);
    }

}
