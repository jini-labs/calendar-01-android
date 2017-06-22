package com.doublesibi.utils.calc.datecalculator.common;

/**
 * Created by hunajini on 2017/06/22.
 */

public class DateInfoLunarSolar {

    public static final String ROKYO_NAME[] = {"大安","赤口","先勝","友引","先負","仏滅"};

    int keyDateType;
    int keyDate;
    int kyear;
    int kmonth;
    int kday;
    int valueDate;
    int vdate;
    int vmonth;
    int vday;
    boolean bLeap; //潤いつき
    int rokuyoIdx;

    public DateInfoLunarSolar(int keyDate, int keyDateType, int valueDate, boolean bLeap) {
        this.keyDateType = keyDateType;
        this.keyDate = keyDate;
        this.valueDate = valueDate;
        this.bLeap = bLeap;

        this.kyear = keyDate / 10000;
        this.kmonth = keyDate / 100 % 100;
        this.kday = keyDate % 100;

        this.vdate = valueDate / 10000;
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

