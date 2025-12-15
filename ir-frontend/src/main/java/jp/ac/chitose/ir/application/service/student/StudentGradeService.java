package jp.ac.chitose.ir.application.service.student;

import jp.ac.chitose.ir.application.service.TableData;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(value = "/grade", accept = "application/json", contentType = "application/json")
public interface StudentGradeService {
    @GetExchange("/grade_graph/{courseId}")
    TableData<GradeCount> getGradeGraph(@PathVariable String courseId);

    @GetExchange("/subject/{studentNumber}")
    TableData<StudentGrade> getStudentNumberSubjects(@PathVariable String studentNumber);

    @GetExchange("/target/{accountId}/{courseId}")
    TableData<Target> getSubjectTarget(@PathVariable String accountId, @PathVariable String courseId);

    @GetExchange("/subject")
    TableData<StudentGrade> getSubjectStudents();

    @GetExchange("/subjectAll")
    TableData<StudentGrade> getAllSubjectStudents();

    @GetExchange("/courseIds")
    CourseIdDict getCourseIdDict();
}
