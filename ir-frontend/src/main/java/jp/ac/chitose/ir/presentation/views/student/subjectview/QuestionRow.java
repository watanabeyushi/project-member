package jp.ac.chitose.ir.presentation.views.student.subjectview;

public class QuestionRow implements TargetRow {
    private final static String attribute = "質問";
    private final String text;

    public QuestionRow(String text) {
        this.text = text;
    }

    @Override
    public String attribute() {
        return attribute;
    }

    @Override
    public String text() {
        return text;
    }
}
