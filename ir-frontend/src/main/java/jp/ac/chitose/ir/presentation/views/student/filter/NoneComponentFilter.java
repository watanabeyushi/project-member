package jp.ac.chitose.ir.presentation.views.student.filter;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;

import java.util.function.BiPredicate;

public class NoneComponentFilter<FilterType, ItemType> implements Filter<FilterType, ItemType> {
    private FilterType value;
    private final BiPredicate<ItemType, FilterType> filter;
    private Runnable valueChangeListener;

    public NoneComponentFilter(BiPredicate<ItemType, FilterType> filter) {
        this.filter = filter;
    }

    public NoneComponentFilter(BiPredicate<ItemType, FilterType> filter, FilterType value) {
        this(filter);
        this.value = value;
    }

    @Override
    public void addValueChangeListener(Runnable valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }

    @Override
    public boolean hasComponent() {
        return false;
    }

    @Override
    public Component getFilterComponent() {
        return new Div(); // 何も起きないダミーコンポーネントを返す
    }

    @Override
    public boolean applyFilter(ItemType item) {
        return filter.test(item, value);
    }

    public void setValue(FilterType value) {
        this.value = value;
        if(valueChangeListener == null) return;
        valueChangeListener.run();
    }
}
