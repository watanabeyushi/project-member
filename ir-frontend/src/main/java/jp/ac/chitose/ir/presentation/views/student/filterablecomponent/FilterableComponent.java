package jp.ac.chitose.ir.presentation.views.student.filterablecomponent;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.provider.ListDataView;
import jp.ac.chitose.ir.presentation.views.student.filter.Filter;

import java.util.List;

public interface FilterableComponent<FilterType, ItemType> {
    void filter(ListDataView<ItemType, ?> dataView);
    void addFilter(Filter<FilterType, ItemType> filter);
    void removeFilter(Filter<FilterType, ItemType> filter);
    void clearFilters();
    List<Filter<?, ?>> getFilters();
    List<Filter<FilterType, ItemType>> getTypeFilters();
    Component getComponent();
}
