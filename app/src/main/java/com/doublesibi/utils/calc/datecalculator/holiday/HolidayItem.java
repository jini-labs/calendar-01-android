package com.doublesibi.utils.calc.datecalculator.holiday;

/**
 * Created by hunajini on 2016/12/29.
 */

public class HolidayItem {
    public int ymd;
    public int md;

    public boolean substitute;
    public int startDate;
    public int endDate;

    public String name;
    public String engName;
    public String extendFunc;

    public int monthOfYear;
    public int weekOfMonth;
    public int dayOfWeek;

    public HolidayItem() {
        this.ymd = 0;
        this.md = 0;
        this.substitute = false;
        this.startDate = 0;
        this.endDate = 0;
        this.name = null;
        this.engName = null;
        this.extendFunc = null;
        this.monthOfYear = 0;
        this.weekOfMonth = 0;
        this.dayOfWeek = 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ymd:").append(ymd);
        sb.append("[").append("Name:").append(name);
        sb.append(",").append("md:").append(md);
        sb.append(",").append("startDate:").append(startDate);
        sb.append(",").append("endDate:").append(endDate);
        sb.append(",").append("substitute:").append(substitute);
        sb.append(",").append("monthOfYear:").append(monthOfYear);
        sb.append(",").append("weekOfMonth:").append(weekOfMonth);
        sb.append(",").append("dayOfWeek:").append(dayOfWeek);
        sb.append("]");

        return sb.toString();
    }
}
