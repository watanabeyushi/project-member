package jp.ac.chitose.ir.presentation.views.student.filter;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

import java.util.function.BiPredicate;

public class RadioButtonFilter<FilterType, ItemType> implements Filter<FilterType, ItemType> {
    private final RadioButtonGroup<FilterType> radioButton;
    private final BiPredicate<ItemType, FilterType> filter;

    public RadioButtonFilter(RadioButtonGroup<FilterType> radioButton, BiPredicate<ItemType, FilterType> filter) {
        this.radioButton = radioButton;
        this.filter = filter;
    }

    public RadioButtonFilter(FilterType[] values, BiPredicate<ItemType, FilterType> filter) {
        this.radioButton = createRadioButtonGroup(values);
        this.filter = filter;
    }

    public RadioButtonFilter(RadioButtonGroup<FilterType> radioButton, BiPredicate<ItemType, FilterType> filter, String idName) {
        this.radioButton = radioButton;
        this.filter = filter;
        radioButton.getElement().setAttribute("id", idName);
    }

    public RadioButtonFilter(FilterType[] values, BiPredicate<ItemType, FilterType> filter, String idName) {
        this.radioButton = createRadioButtonGroup(values);
        this.filter = filter;
        radioButton.getElement().setAttribute("id", idName);
    }

    private static <T> RadioButtonGroup<T> createRadioButtonGroup(T[] values) {
        RadioButtonGroup<T> radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.setItems(values);
        radioButtonGroup.setValue(values[0]);
        return radioButtonGroup;
    }

    @Override
    public void addValueChangeListener(Runnable valueChangeListener) {
        radioButton.addValueChangeListener(value -> valueChangeListener.run());
    }

    @Override
    public boolean hasComponent() {
        return true;
    }

    @Override
    public boolean applyFilter(ItemType item) {
        return filter.test(item, radioButton.getValue());
    }

    @Override
    public Component getFilterComponent() {
        return radioButton;
    }
}
