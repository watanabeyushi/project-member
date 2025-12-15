package jp.ac.chitose.ir.application.service.student;

import jp.ac.chitose.ir.application.service.TableData;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(value = "/student", accept = "application/json", contentType = "application/json")
public interface StudentService {
    @GetExchange("/grade")
    TableData<StudentGrade> getStudentGrade();

    @GetExchange("/grade/{studentNumber}")
    TableData<StudentGrade> getStudentNumberGrades(@PathVariable String studentNumber);

    @GetExchange("/grade/{studentNumber}/{subject}")
    TableData<StudentGrade> getStudentNumberGrade(@PathVariable String studentNumber, @PathVariable String subject);

    @GetExchange("/gpa_calc_par_year")
    TableData<StudentGPA> getStudentGPA();

    @GetExchange("/subject_calc_par_year/{subject}")
    TableData<StudentSubjectCalc> getStudentSubjectCalc(@PathVariable String subject);

    @GetExchange("/school_year/{studentNumber}")
    TableData<StudentSchoolYear> getStudentSchoolYear(@PathVariable String studentNumber);
}