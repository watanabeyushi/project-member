package jp.ac.chitose.ir.application.service.commission;

public record GradeGpaStat(String 学科,
                           int 人数,
                           float 平均値,
                           float 中央値,
                           float 最小値,
                           float 最大値,
                           float 標準偏差

) {
    public String subject(){
        return 学科();
    }
    public int human(){
        return 人数();
    }
    public float average(){
        return 平均値();
    }
    public float mid(){return 中央値();}
    public float min(){
        return 最小値();
    }
    public float max(){
        return 最大値();
    }
    public float std(){
        return 標準偏差();
    }
}
