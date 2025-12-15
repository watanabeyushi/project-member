package jp.ac.chitose.ir.presentation.views.student.filterablecomponent;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.combobox.ComboBox;
import jp.ac.chitose.ir.presentation.views.student.filter.Filter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class FilterableComboBox<FilterType, ItemType> extends AbstractFilterableComponent<FilterType, ItemType, ComboBox<ItemType>> {
    public FilterableComboBox(List<Filter<FilterType, ItemType>> filters, FilterPosition position) {
        super(filters, new ComboBox<>(), position);
    }

    public FilterableComboBox(String label, List<Filter<FilterType, ItemType>> filters, FilterPosition position) {
        super(filters, new ComboBox<>(label), position);
    }

    public FilterableComboBox(String label, ItemType[] items, List<Filter<FilterType, ItemType>> filters, FilterPosition position) {
        super(filters, new ComboBox<>(label, items), position);
    }

    public FilterableComboBox(String label, Collection<ItemType> items, List<Filter<FilterType, ItemType>> filters, FilterPosition position) {
        super(filters, new ComboBox<>(label, items), position);
    }

    public void setComboBoxWidth(String width) {
        component.setWidth(width);
    }

    public void setComboBoxHeight(String height) {
        component.setHeight(height);
    }

    public void setItemLabelGenerator(ItemLabelGenerator<ItemType> itemLabelGenerator) {
        component.setItemLabelGenerator(itemLabelGenerator);
    }

    public void setPlaceholder(String placeholder) {
        component.setPlaceholder(placeholder);
    }

    public void setClearButtonVisible(boolean clearButtonVisible) {
        component.setClearButtonVisible(clearButtonVisible);
    }

    public void addValueChangeListener(HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<ItemType>, ItemType>> listener) {
        component.addValueChangeListener(listener);
    }

    public Stream<ItemType> getItems() {
        return dataView.getItems();
    }

    public void setItems(List<ItemType> data) {
        component.setItems(data);
    }

    public void setValue(ItemType item) {
        component.setValue(item);
    }
}
