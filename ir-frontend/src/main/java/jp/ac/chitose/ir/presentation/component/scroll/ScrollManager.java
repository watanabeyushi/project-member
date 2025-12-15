package jp.ac.chitose.ir.presentation.component.scroll;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ScrollOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class ScrollManager {
    private final Map<String, Component> scrollComponents;

    public ScrollManager() {
        scrollComponents = new HashMap<>();
    }

    public ScrollManager(Map<String, Component> scrolls) {
        this.scrollComponents = scrolls;
    }

    public ScrollManager(Component component, String idName) {
        this();
        scrollComponents.put(idName, component);
    }

    public ScrollManager(List<Component> components, List<String> idNames) {
        this();
        for(int i = 0; i < Math.min(components.size(), idNames.size()); i++) {
            scrollComponents.put(idNames.get(i), components.get(i));
        }
    }

    public void add(Component component, String idName) {
        scrollComponents.put(idName, component);
    }

    public void remove(String idName) {
        scrollComponents.remove(idName);
    }

    public void scrollToComponentById(String idName) {
        Component component = scrollComponents.get(idName);
        if(component == null) return;
        component.scrollIntoView();
    }

    public void scrollToComponentById(String idName, ScrollOptions options) {
        Component component = scrollComponents.get(idName);
        if(component == null) return;
        component.scrollIntoView(options);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Component> entry : scrollComponents.entrySet()) {
            String id = entry.getKey();
            Component component = entry.getValue();
            builder.append("Id: ").append(id).append(", Component: ").append(component.getClass().getSimpleName());
            builder.append("\n");
        }
        return builder.toString();
    }
}
