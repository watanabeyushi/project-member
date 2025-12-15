package jp.ac.chitose.ir.application.service.class_select;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QuestionnaireGrid {

    public Component generateGrid(int i,List<ReviewQPOJFICHKVJBDescription> reviewData) {
        i = i + 4;
        Grid<String> grid = new Grid<>(String.class, false);
        grid.addColumn(description -> String.valueOf(description)).setHeader("Q" + i);


        if (!reviewData.isEmpty()) {
            Collection<String> valuesCollection = switch (i) {
                case 7 -> reviewData.get(0).q7().values();
                case 17 -> reviewData.get(0).q17().values();
                case 18 -> reviewData.get(0).q18().values();
                case 19 -> reviewData.get(0).q19().values();
                default -> null;
            };

            if (valuesCollection != null) {
                List<String> values = new ArrayList<>(valuesCollection);
                grid.setItems(values);
            }
        }

        return grid;
    }
}