package jp.ac.chitose.ir.presentation.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jp.ac.chitose.ir.application.service.management.SecurityService;
import jp.ac.chitose.ir.presentation.component.scroll.ScrollManager;
import jp.ac.chitose.ir.presentation.views.class_select.QuestionnaireTop;
import jp.ac.chitose.ir.presentation.views.commission.ir.IrQuestionView;
import jp.ac.chitose.ir.presentation.views.commission.seiseki.CommissionView;
import jp.ac.chitose.ir.presentation.views.commission.university.UniversityView;
import jp.ac.chitose.ir.presentation.views.questionnaire.QuestionnaireTopView;
import jp.ac.chitose.ir.presentation.views.student.StudentView;
import jp.ac.chitose.ir.presentation.views.top.TopView;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIcon;

public class SubLayout extends AppLayout {
    private final SecurityService securityService;
    private ScrollManager scrollManager;

    public SubLayout(@Autowired SecurityService securityService) {
        this.securityService = securityService;

        createHeader();
        createDrawer();
    }

    private Component createHeaderContent() {
        Header header = new Header();
        header.addClassNames(LumoUtility.BoxSizing.BORDER, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.Width.FULL);

        Div layout = new Div();
        layout.addClassNames(LumoUtility.Display.FLEX, LumoUtility.AlignItems.CENTER, LumoUtility.Padding.Horizontal.LARGE);

        H1 appName = new H1("IR");
        appName.addClassNames(LumoUtility.Margin.Vertical.MEDIUM, LumoUtility.Margin.End.AUTO, LumoUtility.FontSize.LARGE);

        H5 username = new H5(securityService.getLoginUser().getUsername() + "　");
        Button logout = new Button("Logout", click -> securityService.logout());

        layout.add(appName, username, logout);

        Nav nav = new Nav();
        nav.addClassNames(LumoUtility.Display.FLEX, LumoUtility.Overflow.AUTO, LumoUtility.Padding.Horizontal.MEDIUM, LumoUtility.Padding.Vertical.XSMALL);

        UnorderedList list = new UnorderedList();
        list.addClassNames(LumoUtility.Display.FLEX, LumoUtility.Gap.SMALL, LumoUtility.ListStyleType.NONE, LumoUtility.Margin.NONE, LumoUtility.Padding.NONE);
        nav.add(list);

        for (MainLayout.MenuItemInfo menuItem : createMenuItems()) {
            list.add(menuItem);
        }

        header.add(layout, nav);
        return header;
    }

    private MainLayout.MenuItemInfo[] createMenuItems() {
        return new MainLayout.MenuItemInfo[]{
                new MainLayout.MenuItemInfo("Top", LineAwesomeIcon.GLOBE_SOLID.create(), TopView.class),
                new MainLayout.MenuItemInfo("成績情報", LineAwesomeIcon.ACCESSIBLE_ICON.create(), StudentView.class),
                new MainLayout.MenuItemInfo("成績統計", LineAwesomeIcon.ANGLE_DOUBLE_DOWN_SOLID.create(), CommissionView.class),
                new MainLayout.MenuItemInfo("アンケート", LineAwesomeIcon.CHART_BAR.create(), QuestionnaireTopView.class),
                new MainLayout.MenuItemInfo("IRアンケート", LineAwesomeIcon.FILE_ALT.create(), IrQuestionView.class),
                new MainLayout.MenuItemInfo("Teacher", LineAwesomeIcon.CHART_AREA_SOLID.create(), QuestionnaireTop.class),
                new MainLayout.MenuItemInfo("大学情報", LineAwesomeIcon.CHALKBOARD_TEACHER_SOLID.create(), UniversityView.class),
        };
    }

    private void createHeader() {
        DrawerToggle toggle = new DrawerToggle();
        addToNavbar(toggle);
        Span title = new Span("Test INDEX");
        addToNavbar(title);

        if (securityService.getAuthenticatedUser() != null) {
            addToNavbar(createHeaderContent());
        }
    }

    private void createDrawer() {
        VerticalLayout nav = new VerticalLayout();
        // Scrollボタンを追加するループ
        this.scrollManager = new ScrollManager();
        for (int i = 0; i < 11; i++) {
            Button scrollToTitleButton = new Button("Scroll to Q" + (i + 4));
            String id = "title-" + (i + 4);
            scrollToTitleButton.addClickListener(event -> {
                getUI().ifPresent(ui -> {
                    ui.navigate("class_select/Top/QPOJFICHKVJB#"+id); // ナビゲート
                    setDrawerOpened(false); // Drawerを閉じる

                });
            });


            nav.add(scrollToTitleButton);
        }

        addToDrawer(nav);
    }



    public static class MenuItemInfo extends ListItem {

        private final Class<? extends Component> view;

        public MenuItemInfo(String menuTitle, Component icon, Class<? extends Component> view) {
            this.view = view;
            RouterLink link = new RouterLink();
            link.addClassNames(LumoUtility.Display.FLEX, LumoUtility.Gap.XSMALL, LumoUtility.Height.MEDIUM, LumoUtility.AlignItems.CENTER, LumoUtility.Padding.Horizontal.SMALL, LumoUtility.TextColor.BODY);
            link.setRoute(view);

            Span text = new Span(menuTitle);
            text.addClassNames(LumoUtility.FontWeight.MEDIUM, LumoUtility.FontSize.MEDIUM, LumoUtility.Whitespace.NOWRAP);

            if (icon != null) {
                link.add(icon);
            }
            link.add(text);
            add(link);
        }

        public Class<?> getView() {
            return view;
        }
    }
}
