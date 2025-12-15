package jp.ac.chitose.ir.application.service.class_select;

import com.vaadin.flow.component.html.H6;

import java.util.List;

public class QuestionDescribe {

    private final ClassSelect classSelect;
    public QuestionDescribe(ClassSelect classSelect) {
        this.classSelect = classSelect;
    }
    private H6 stat(int questionNum ,String subject_id,ClassQPOJFICHKVJB ranking){
        var statics = classSelect.getReviewDescribe(subject_id).data().get(0);

        List resultList = null;
        String rank = null;
        switch (questionNum){
            case 4-> {resultList = statics.q4();
                rank = ranking.順位().get(0) +"位";
            }
            case 5-> {resultList = statics.q5();
                rank = ranking.順位().get(1) +"位";
            }
            case 6-> {resultList = statics.q6();
                rank = ranking.順位().get(2) +"位";
            }
            case 7-> {resultList = statics.q7();
                rank = ranking.順位().get(3) +"位";
            }
            case 8-> {resultList = statics.q8();
                rank = ranking.順位().get(4) +"位";
            }
            case 9-> {resultList = statics.q9();
                rank = ranking.順位().get(5) +"位";
            }
            case 10-> {resultList = statics.q10();
                rank = ranking.順位().get(6) +"位";
            }
            case 11-> {resultList = statics.q11();
                rank = ranking.順位().get(7) +"位";
            }
            case 12-> {resultList = statics.q12();
                rank = ranking.順位().get(8) +"位";
            }
            case 13-> {resultList = statics.q13();
                rank = ranking.順位().get(9) +"位";
            }
            case 14-> {resultList = statics.q14();
                rank = ranking.順位().get(10) +"位";
            }
            case 15-> {resultList = statics.q15();
                rank = ranking.順位().get(11) +"位";
            }
            case 16-> {resultList = statics.q16();
                rank = ranking.順位().get(12) +"位";
            }
        }

        double variance = (double) resultList.get(2); // 分散
        double standardDeviation = Math.sqrt(variance); // 標準偏差


        String formatted = "回答数:"+ resultList.get(0) +", 平均:"+String.format("%.2f",resultList.get(1))+", 標準偏差:"+ String.format("%.2f",standardDeviation)+/*", 最小値:"+resultList.get(3)+", 25%:"+ resultList.get(4)+", 50%:"+resultList.get(5)+", 75%:"+resultList.get(6)+", 最大値:"+resultList.get(7)+*/", 順位:"+rank;
        return new H6(formatted);
    }

    public H6 getStatics(int questionnum,String subject_id,ClassQPOJFICHKVJB ranking){
        return stat(questionnum,subject_id,ranking);
    }

}
