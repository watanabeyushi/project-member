package jp.ac.chitose.ir.presentation.views.student.filterablecomponent;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.HasListDataView;
import com.vaadin.flow.data.provider.ListDataView;
import jp.ac.chitose.ir.presentation.views.student.filter.Filter;

import java.util.Collections;
import java.util.List;

abstract public class AbstractFilterableComponent<FilterType, ItemType, ComponentType extends HasListDataView<ItemType, ?>> extends VerticalLayout implements FilterableComponent<FilterType, ItemType> {
    protected final List<Filter<FilterType, ItemType>> filters;
    protected final ComponentType component;
    protected final ListDataView<ItemType, ?> dataView;

    public AbstractFilterableComponent(List<Filter<FilterType, ItemType>> filters, ComponentType component, FilterPosition position) {
        this.filters = filters;
        this.component = component;
        this.dataView = component.getListDataView();
        addListenerToFilters();
        position.setup(this);
    }

    private void addListenerToFilters() {
        filters.forEach(filter -> filter.addValueChangeListener(() -> filter(dataView)));
    }

    @Override
    public void filter(ListDataView<ItemType, ?> dataView) {
        dataView.setFilter(item -> filters.stream()
                .allMatch(filter -> filter.applyFilter(item)));
    }


    @Override
    public void addFilter(Filter<FilterType, ItemType> filter) {
        filters.add(filter);
        filter.addValueChangeListener(() -> filter(dataView));
    }

    @Override
    public void removeFilter(Filter<FilterType, ItemType> filter) {
        filters.remove(filter);
        filter(dataView);
    }

    @Override
    public void clearFilters() {
        filters.clear();
        filter(dataView);
    }

    @Override
    public List<Filter<?, ?>> getFilters() {
        return Collections.unmodifiableList(filters);
    }

    @Override
    public List<Filter<FilterType, ItemType>> getTypeFilters() {
        return Collections.unmodifiableList(filters);
    }

    @Override
    public Component getComponent() {
        return (Component) component;
    }
}
