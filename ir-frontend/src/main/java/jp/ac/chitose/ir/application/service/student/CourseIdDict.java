package jp.ac.chitose.ir.application.service.student;

import java.util.Map;

public record CourseIdDict (
        Map<String, Map<String, String>> courseIdDict
) {
}
