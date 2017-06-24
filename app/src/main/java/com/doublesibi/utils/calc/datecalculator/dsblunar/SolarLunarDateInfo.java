package com.doublesibi.utils.calc.datecalculator.dsblunar;

/**
 * Created by hunajini on 2017/06/22.
 */

public class SolarLunarDateInfo {

    int keyDateType;
    private int keyDate;
    private int kyear;
    private int kmonth;
    private int kday;
    int valueDate;
    private int vyear;
    private int vmonth;
    private int vday;
    boolean bLeap; //潤いつき
    int rokuyoIdx;

    public SolarLunarDateInfo(int keyDate, int keyDateType, int valueDate, boolean bLeap) {
        this.keyDateType = keyDateType;
        this.keyDate = keyDate;
        this.valueDate = valueDate;
        this.bLeap = bLeap;

        this.kyear = keyDate / 10000;
        this.kmonth = keyDate / 100 % 100;
        this.kday = keyDate % 100;

        this.vyear = valueDate / 10000;
        this.vmonth = valueDate / 100 % 100;
        this.vday = valueDate % 100;

        this.rokuyoIdx = (this.vmonth + this.vday) % 6;
    }

    @Override
    public String toString() {
        return "DateInfo{" +
                "keyDateType=" + keyDateType +
                ", keyDate=" + keyDate +
                ", valueDate=" + valueDate +
                ", bLeap=" + bLeap +
                ", rokuyoIdx=" + rokuyoIdx +
                '}';
    }
}
