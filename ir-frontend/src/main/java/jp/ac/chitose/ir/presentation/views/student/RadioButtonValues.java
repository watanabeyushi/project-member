package jp.ac.chitose.ir.presentation.views.student;

public enum RadioButtonValues {
    SCHOOL_YEARS(new String[]{"全体", "1年生", "2年生", "3年生", "4年生", "修士1年生", "修士2年生"}),
    DEPARTMENTS(new String[]{"全体", "理工学部", "応用化学生物学科", "電子光工学科", "情報システム工学科", "理工学研究科"}),
    SUBJECT_TYPES(new String[]{"全体", "必修", "選択必修", "選択"}),
    GRADES(new String[]{"全体", "不可", "可", "良", "優", "秀"});

    private final String[] values;
    RadioButtonValues(String[] values) {
        this.values = values;
    }

    public String[] getValues() {
        return values;
    }
}
