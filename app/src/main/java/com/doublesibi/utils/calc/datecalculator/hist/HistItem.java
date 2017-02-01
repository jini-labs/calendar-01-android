package com.doublesibi.utils.calc.datecalculator.hist;

import java.util.Calendar;

/**
 * Created by hunajini on 2017/01/23.
 */

public class HistItem {
    public final static int DURATION_PREF = 1;
    public final static int EVENTDAY_PREF = 2;

    public final static String DURATION_PREF_NAME = "DurationP";
    public final static String EVENTDAY_PREF_NAME = "EventDayP";

    public String stDate;      //　保存：両方に使用
    public String enDate;      //　保存：両方に使用
    public String days;        //　保存：両方に使用
    public String weeks;       //　保存：両方に使用
    public String weekdays;
    public String months;      //　保存：両方に使用
    public String monthdays;
    public String years;       //　保存：両方に使用
    public String yearmonths;
    public String yeardays;
    public String beOrAf;      // 0: before, 1: after
    public String name;


    public HistItem() {
        this.stDate = "";
        this.enDate = "";
        this.days = "";
        this.weeks = "";
        this.weekdays = "";
        this.months = "";
        this.monthdays = "";
        this.years = "";
        this.yearmonths = "";
        this.yeardays = "";
        this.beOrAf = "";

        Calendar cal = Calendar.getInstance();
        cal.getTime().getTime();
        this.name = "" + cal.getTime().getTime();
    }

    @Override
    public String toString() {
        return "HistItem{" +
                "stDate='" + stDate + '\'' +
                ", enDate='" + enDate + '\'' +
                ", days='" + days + '\'' +
                ", weeks='" + weeks + '\'' +
                ", weekdays='" + weekdays + '\'' +
                ", months='" + months + '\'' +
                ", monthdays='" + monthdays + '\'' +
                ", years='" + years + '\'' +
                ", yearmonths='" + yearmonths + '\'' +
                ", yeardays='" + yeardays + '\'' +
                ", beOrAf='" + beOrAf + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String toString(int type) {
        if (type == 1) {
            return "HistItem{" +
                    "stDate='" + stDate + '\'' +
                    ", enDate='" + enDate + '\'' +
                    ", days='" + days + '\'' +
                    ", weeks='" + weeks + '\'' +
                    ", weekdays='" + weekdays + '\'' +
                    ", months='" + months + '\'' +
                    ", monthdays='" + monthdays + '\'' +
                    ", years='" + years + '\'' +
                    ", yearmonths='" + yearmonths + '\'' +
                    ", yeardays='" + yeardays + '\'' +
                    '}';
        } else if (type == 2) {
            return "HistItem{" +
                    "stDate='" + stDate + '\'' +
                    ", enDate='" + enDate + '\'' +
                    ", days='" + days + '\'' +
                    ", weeks='" + weeks + '\'' +
                    ", months='" + months + '\'' +
                    ", years='" + years + '\'' +
                    ", beOrAf='" + beOrAf + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        } else {
            return "";
        }

    }
}
