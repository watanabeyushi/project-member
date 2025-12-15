package jp.ac.chitose.ir.presentation.views.student.subjectview;

public class AnswerRow implements TargetRow {
    private final static String attribute = "回答";
    private final String text;

    public AnswerRow(String text) {
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
