package jp.ac.chitose.ir.application.service.student;

public record StudentSubjectCalc(
        int 開講年,
        int 欠席,
        int 不可,
        int 可,
        int 良,
        int 優,
        int 秀,
        int 合計の人数,
        float 平均,
        float 分散
) {
}