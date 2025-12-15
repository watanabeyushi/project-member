package jp.ac.chitose.ir.application.service.helloworld;

public record HelloworldGrade(
        int 不可,
        int 可,
        int 良,
        int 優,
        int 秀,
        String lecture_teacher,
        String lecture_name) {
    public int StudentCount() {
        return 不可 + 可 + 良 + 優 + 秀;
    }
    public float Average() {
        return (float) (可 + 2 * 良 + 3 * 優 + 4 * 秀) / StudentCount();
    }

    public float Variance() {
        float ave = Average();
        return (不可 * ave * ave + 可 * (1 - ave) * (1 - ave) + 良 * (2 - ave) *
                (2 - ave) +
                優 * (3 - ave) * (3 - ave) + 秀 * (4 - ave) * (4 - ave)) /
                StudentCount();
    }
}
