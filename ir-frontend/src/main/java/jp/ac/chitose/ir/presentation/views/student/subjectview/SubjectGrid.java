package jp.ac.chitose.ir.presentation.views.student.subjectview;

import jp.ac.chitose.ir.application.service.student.GradeCount;
import jp.ac.chitose.ir.presentation.views.student.filter.Filter;
import jp.ac.chitose.ir.presentation.views.student.filterablecomponent.FilterPosition;
import jp.ac.chitose.ir.presentation.views.student.filterablecomponent.FilterableGrid;

import java.util.List;

public class SubjectGrid extends FilterableGrid<Number, GradeCount> {
    public SubjectGrid(List<Filter<Number, GradeCount>> list, FilterPosition position) {
        super(GradeCount.class, false, list, position);
    }
}
