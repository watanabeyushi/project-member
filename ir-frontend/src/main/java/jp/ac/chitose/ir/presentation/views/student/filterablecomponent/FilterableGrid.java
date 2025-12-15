package jp.ac.chitose.ir.presentation.views.student.filterablecomponent;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.function.ValueProvider;
import jp.ac.chitose.ir.presentation.views.student.filter.Filter;

import java.util.List;
import java.util.Set;

public class FilterableGrid<FilterType, ItemType> extends AbstractFilterableComponent<FilterType, ItemType, Grid<ItemType>> {
    public FilterableGrid(List<Filter<FilterType, ItemType>> filters, FilterPosition position) {
        super(filters, new Grid<>(), position);
    }

    public FilterableGrid(Class<ItemType> beanType, boolean autoCreateColumns, List<Filter<FilterType, ItemType>> filters, FilterPosition position) {
        super(filters, new Grid<>(beanType, autoCreateColumns), position);
    }

    public void setGridWidth(String width) {
        component.setWidth(width);
    }

    public void setGridHeight(String height) {
        component.setHeight(height);
    }

    public void setAllRowsVisible(boolean allRowsVisible) {
        component.setAllRowsVisible(allRowsVisible);
    }

    public Grid.Column<ItemType> addColumn(ValueProvider<ItemType, FilterType> provider) {
        return component.addColumn(provider);
    }

    public Grid.Column<ItemType> addColumn(ValueProvider<ItemType, FilterType> provider, String headerName) {
        return component.addColumn(provider).setHeader(headerName);
    }

    public Grid.Column<ItemType> addComponentColumn(ValueProvider<ItemType, Component> provider) {
        return component.addComponentColumn(provider);
    }

    public void setSelectionMode(Grid.SelectionMode selectionMode) {
        component.setSelectionMode(selectionMode);
    }

    public Set<ItemType> getSelectedItems() {
        return component.getSelectedItems();
    }

    public void addThemeVariants(GridVariant variant) {
        component.addThemeVariants(variant);
    }

    public void addItemClickListener(ComponentEventListener<ItemClickEvent<ItemType>> listener) {
        component.addItemClickListener(listener);
    }

    public GridListDataView<ItemType> setItems(List<ItemType> items) {
        return component.setItems(items);
    }
}
