package jp.ac.chitose.ir.application.service.student;

public record GradeCount(
        int 不可,
        int 可,
        int 良,
        int 優,
        int 秀,
        String 教員名,
        String 科目名
) {
    public int 合計の人数() {
        return 不可 + 可 + 良 + 優 + 秀;
    }

    public float 平均() {
        return (float) (可 + 2 * 良 + 3 * 優 + 4 * 秀) / 合計の人数();
    }

    public float 分散() {
        float ave = 平均();
        return (不可 * ave * ave + 可 * (1 - ave) * (1 - ave) + 良 * (2 - ave) * (2 - ave) +
        優 * (3 - ave) * (3 - ave) + 秀 * (4 - ave) * (4 - ave)) / 合計の人数();
    }
}
