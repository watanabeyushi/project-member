package jp.ac.chitose.ir.application.service.commission;

public record ScholarShipJasso(String academic_category,
                               String loan_type,
                               int reservation,
                               int enrollment,
                               int emergency_support,
                               int total
) {

}
