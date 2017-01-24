package com.doublesibi.utils.calc.datecalculator.hist;

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
    }

    public String toString(int type) {
        StringBuilder sb = new StringBuilder();

        if (type == 1) {
            sb.append("<stDate:").append(stDate).append("~");
            sb.append("enDate:").append(enDate).append(">:");
            sb.append("[<days:").append(days).append(">,");
            sb.append("<months:").append(months).append(",");
            sb.append("days:").append(days).append(">,");
            sb.append("<weeks:").append(weeks).append(",");
            sb.append("days:").append(days).append(">,");
            sb.append("<years:").append(years).append(",");
            sb.append("yearmonths:").append(yearmonths).append(",");
            sb.append("yeardays:").append(yeardays).append(">]");
        } else if (type == 2) {
            sb.append("<Date:").append(stDate).append("~");
            sb.append("[<days:").append(days).append(">,");
            sb.append("<weeks:").append(weeks).append(",");
            sb.append("<months:").append(months).append(",");
            sb.append("<years:").append(years).append(">");
            sb.append("<BeOrAf:").append(beOrAf).append(">");
            sb.append("-->").append(enDate).append(">");
        }

        return sb.toString();
    }
}
