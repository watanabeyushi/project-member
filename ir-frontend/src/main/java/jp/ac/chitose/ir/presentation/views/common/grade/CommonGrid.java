package jp.ac.chitose.ir.presentation.views.common.grade;

import jp.ac.chitose.ir.application.service.student.StudentGrade;
import jp.ac.chitose.ir.presentation.views.student.filter.Filter;
import jp.ac.chitose.ir.presentation.views.student.filterablecomponent.FilterPosition;
import jp.ac.chitose.ir.presentation.views.student.filterablecomponent.FilterableGrid;

import java.util.List;

public class CommonGrid extends FilterableGrid<String, StudentGrade> {
    public CommonGrid(List<Filter<String, StudentGrade>> filters, FilterPosition position) {
        super(StudentGrade.class, false, filters, position);
    }
}
