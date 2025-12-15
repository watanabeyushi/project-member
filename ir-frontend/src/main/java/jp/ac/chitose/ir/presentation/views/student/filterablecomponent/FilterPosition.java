package jp.ac.chitose.ir.presentation.views.student.filterablecomponent;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.html.H3;
import jp.ac.chitose.ir.presentation.views.student.filter.Filter;

import java.util.List;
import java.util.function.Consumer;

public enum FilterPosition {
    TOP((component) -> {
        setLayoutStyle(component);
        layoutAddFilters(component, component.getFilters());
        component.add(component.getComponent());
    }),
    BOTTOM((component) -> {
        setLayoutStyle(component);
        layoutAddFilters(component, component.getFilters());
        component.add(component.getComponent());
    });

    private final Consumer<AbstractFilterableComponent<?, ?, ?>> filterPosition;

    FilterPosition(Consumer<AbstractFilterableComponent<?, ?, ?>> filterPosition) {
        this.filterPosition = filterPosition;
    }

    public void setup(AbstractFilterableComponent<?, ?, ?> component) {
        this.filterPosition.accept(component);
    }

    private static void setLayoutStyle(HasComponents layout) {
        layout.getElement().setAttribute("style", "width:100%; height: auto;");
    }

    private static void layoutAddFilters(HasComponents layout, List<Filter<?, ?>> filters) {
        for(Filter<?, ?> filter : filters) {
            Component filterComponent = filter.getFilterComponent();
            String id = filterComponent.getElement().getAttribute("id");
            if (id == null) {
                layout.add(filterComponent);
            } else {
                layout.add(new H3(id), filterComponent);
            }
        }
    }
}