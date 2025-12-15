package jp.ac.chitose.ir.presentation.views.error;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletResponse;
import jp.ac.chitose.ir.application.exception.APIServerErrorException;

@Tag(Tag.DIV)
@PermitAll
public class APIServerErrorView extends Component
        implements HasErrorParameter<APIServerErrorException> {

    @Override
    public int setErrorParameter(BeforeEnterEvent event,
                                 ErrorParameter<APIServerErrorException> parameter) {
        getElement().setText("aiueoCould not navigate to '"
                + event.getLocation().getPath()
                + "'");
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
