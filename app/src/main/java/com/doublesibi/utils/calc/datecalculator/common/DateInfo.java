package com.doublesibi.utils.calc.datecalculator.common;

import java.util.Calendar;

/**
 * Created by hunajini on 2017/06/14.
 */

public class DateInfo {
    private final String LOGTAG = "DayCalc";

    public int      solarDate;      // yyyymmdd
    public int      week;            // 1:sun, 2:mon, 3...
    public boolean  bthisMonth;     // true:this month, false:other month
    public String   enWeekname;
    public String   loWeekname;
    public int      lunarDate;     // yyyymmdd
    public int      rokuyoIdx;     // japanese name of date(rokuyo)
    public String   rokuyoName;
    public String   rokuyo;
    public boolean  bHoliday;
    public String   holidayName;
    public boolean  bToday;

    public int     year;
    public int     month;
    public int     day;
    private int     l_year;
    private int     l_month;
    private int     l_day;
    public  boolean l_bLeap;

    public int column; // [6][7] の列
    public int row;    // [6][7] の行

    public DateInfo() {
    }

    public DateInfo(int year_month, int day) {
        init(year_month * 100 + day);
    }

    public DateInfo(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.bHoliday = false;
        this.bToday = false;
        this.bthisMonth = false;

        init(year * 10000 + month * 100 + day);
    }

    public DateInfo(int solar_date) {
        init(solar_date);
    }
    private void init(int solar_date) {
        this.solarDate = solar_date;

        this.year = solar_date / 10000;
        this.month = (solar_date % 10000) / 100;
        this.day = solar_date % 100;

        Calendar c = Calendar.getInstance();

        c.set(this.year, this.month - 1, this.day);
        this.week = c.get(Calendar.DAY_OF_WEEK);
        this.enWeekname = Constants.WEEK_NAME_EN[this.week-1];
        this.loWeekname = Constants.WEEK_NAME_JP[this.week-1];
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    @Override
    public String toString() {
        return "DateInfo{" +
                "LOGTAG='" + LOGTAG + '\'' +
                ", solarDate=" + solarDate +
                ", week=" + week +
                ", bthisMonth=" + bthisMonth +
                ", enWeekname='" + enWeekname + '\'' +
                ", loWeekname='" + loWeekname + '\'' +
                ", lunarDate=" + lunarDate +
                ", rokuyoIdx=" + rokuyoIdx +
                ", rokuyoName='" + rokuyoName + '\'' +
                ", rokuyo='" + rokuyo + '\'' +
                ", bHoliday=" + bHoliday +
                ", holidayName='" + holidayName + '\'' +
                ", bToday=" + bToday +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", l_year=" + l_year +
                ", l_month=" + l_month +
                ", l_day=" + l_day +
                ", l_bLeap=" + l_bLeap +
                ", column=" + column +
                ", row=" + row +
                '}';
    }
}
