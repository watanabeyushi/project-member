package jp.ac.chitose.ir.application.service.sample;

import java.util.ArrayList;

public record SapmleGpa (
        String name,
        int a,
        int b,
        int c,
        int d,
        int e,
        int f,
        int g,
        int h,
        int i,
        int j,
        int k,
        int l,
        int m,
        int n,
        int o,
        int p

){
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
        aa.add(j);
        aa.add(k);
        aa.add(l);
        aa.add(m);
        aa.add(n);
        aa.add(o);
        aa.add(p);
        return aa;
    }

    public String getName() {
        return name();
    }
}
