package jp.ac.chitose.ir.presentation.views.helloworld;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;

@Tag("hello-world")
@NpmPackage(value = "@axa-ch/input-text", version = "4.3.11")
@JsModule("./src/hello-world.ts")
public class HelloWorld extends LitTemplate {

    @Id("firstInput")
    private TextField firstInput;

    @Id("secondInput")
    private TextField secondInput;

    @Id("helloButton")
    private Button helloButton;

    /**
     * Creates the hello world template.
     */
    public HelloWorld() {
        helloButton.addClickListener(buttonClickEvent -> {
            Notification.show(firstInput.getValue() + ";" + secondInput.getValue());
        });
    }
}