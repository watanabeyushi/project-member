package jp.ac.chitose.ir.application.service.sample;

public record SampleFive(
        String 科目名,
        String 一位,
        String 二位,
        String 三位

) {
    public String kamoku(){return 科目名();}
    public String itii(){
        return 一位();
    }
    public String nii(){
        return 二位();
    }
    public String sanni(){
        return 三位();
    }
}
