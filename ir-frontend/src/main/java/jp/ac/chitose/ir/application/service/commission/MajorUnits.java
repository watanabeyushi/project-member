package jp.ac.chitose.ir.application.service.commission;
//卒業単位：専門
public record MajorUnits(String department, int required,
                         int required_elective, int elective, int specialty_total,
                         int others,int total
) {
}
