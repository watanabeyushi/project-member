package jp.ac.chitose.ir.application.service.commission;

import java.util.ArrayList;

public record GradeGpaGraph(
        String name,
        int a,
        int b,
        int c,
        int d,
        int e,
        int f,
        int g,
        int h,
        int i
) {
    public ArrayList<Integer> getData(){
        ArrayList<Integer> aa = new ArrayList<>();
        aa.add(a);
        aa.add(b);
        aa.add(c);
        aa.add(d);
        aa.add(e);
        aa.add(f);
        aa.add(g);
        aa.add(h);
        aa.add(i);
        return aa;
    }
    public String getName() {
        return name();
    }
}
