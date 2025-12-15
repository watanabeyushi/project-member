package jp.ac.chitose.ir.application.service.questionnaire;

public record QuestionnaireTopGrid(
        String lecture_name,
        String available_year,
        int target_grade,
        String target_department,
        String compulsory_subjects,
        String number_credits_course
){
}
