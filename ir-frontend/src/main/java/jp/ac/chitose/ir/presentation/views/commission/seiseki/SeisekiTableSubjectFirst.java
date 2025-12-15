package jp.ac.chitose.ir.presentation.views.commission.seiseki;

import jp.ac.chitose.ir.application.service.TableData;
import jp.ac.chitose.ir.application.service.commission.*;

import java.util.ArrayList;
import java.util.List;

public class SeisekiTableSubjectFirst {
    private TableData<GradeGpaStat> tableAll;
    private TableData<GradeGpaStat> tableFirst;
    private TableData<GradeGpaStat> tableSecond;
    private TableData<GradeGpaStat> tableThird;
    private TableData<GradeGpaStat> tableFourth;
    //学年別の基本統計量を学科別に変換するクラス
    public SeisekiTableSubjectFirst(TableData<GradeGpaStat> tableAll,
                                    TableData<GradeGpaStat> tableFirst,
                                    TableData<GradeGpaStat> tableSecond,
                                    TableData<GradeGpaStat> tableThird,
                                    TableData<GradeGpaStat> tableFourth
                                    ){
        this.tableAll = tableAll;
        this.tableFirst = tableFirst;
        this.tableSecond = tableSecond;
        this.tableThird = tableThird;
        this.tableFourth = tableFourth;
    }

    public List<GetTableData> getTable(int i) {

        List<GetTableData> list = new ArrayList<>();
        GradeGpaStat all = tableAll.data().get(i);
        GradeGpaStat first = tableFirst.data().get(i);
        GradeGpaStat second = tableSecond.data().get(i);
        GradeGpaStat third = tableThird.data().get(i);
        GradeGpaStat fourth = tableFourth.data().get(i);
        list.add(new GetTableData("全体", all.human(), all.average(), all.mid(), all.min(), all.max(), all.std()));
        list.add(new GetTableData("1年生", first.human(), first.average(), first.mid(), first.min(), first.max(), first.std()));
        list.add(new GetTableData("２年生", second.human(), second.average(), second.mid(), second.min(), second.max(), second.std()));
        list.add(new GetTableData("３年生", third.human(), third.average(), third.mid(), third.min(), third.max(), third.std()));
        list.add(new GetTableData("４年生", fourth.human(), fourth.average(), fourth.mid(), fourth.min(), fourth.max(), fourth.std()));
        if(i != 0){
            list.remove(1);
        }
        return list;

        }



}
