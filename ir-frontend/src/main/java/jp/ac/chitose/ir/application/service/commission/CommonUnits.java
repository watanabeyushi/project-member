package jp.ac.chitose.ir.application.service.commission;

public record CommonUnits(String department, int required, int required_elective_specialty1,
                          int required_elective_specialty2, int required_elective_education, int elective,
                          int foreign_language_required1, int foreign_language_required2,
                          int physical_education_elective, int total
) {
}
