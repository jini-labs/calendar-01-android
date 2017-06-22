package com.doublesibi.utils.calc.datecalculator.common;

import android.util.Log;

import com.ibm.icu.util.ChineseCalendar;

import java.util.Calendar;

import ajd4jp.AJD;
import ajd4jp.AJDException;
import ajd4jp.Era;
import ajd4jp.LunisolarYear;

/**
 * Created by hunajini on 2017/06/14.
 */

public class DateInfo {
    private final String LOGTAG = "DayCalc";

    private final static String[] WEEK_NAME_JP = {"日曜日", "月曜日", "火曜日", "水曜日",
            "木曜日", "金曜日", "土曜日"};
    private final static String[] WEEK_NAME_EN = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY",
            "THURSDAY", "FRIDAY", "SATURDAY"};

    private final static String[] ROKUYO_NAME_REMAIN = {"大安", "赤口", "先勝", "友引", "先負", "仏滅"};

    private final static String[] ROKUYO_NAME = {"先勝", "友引", "先負", "仏滅", "大安", "赤口"};
    private final static String[] START_MONTH_ROKUYO = {"先勝", "友引", "先負", "仏滅", "大安", "赤口",
            "先勝", "友引", "先負", "仏滅", "大安", "赤口"};
    private final static int[] START_MONTH_ROKUYO_IDX = {0, 1, 2, 3, 4, 5, 0, 1, 2, 3, 4, 5};

    public int      solar_date;      // yyyymmdd
    public int      week;            // 1:sun, 2:mon, 3...
    public String   en_weekname;
    public String   lo_weekname;
    public int      lunar_date;     // yyyymmdd
    public int      rokuyo_idx;     // japanese name of date(rokuyo)
    public String   rokuyo;
    public String   holidayName;

    private int     year;
    private int     month;
    private int     day;
    private int     l_year;
    private int     l_month;
    private int     l_day;
    private boolean l_bLeap;

    public DateInfo(int year_month, int day) {
        init(year_month * 100 + day);
    }

    public DateInfo(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;

        init(year * 10000 + month * 100 + day);
    }

    public DateInfo(int solar_date) {
        init(solar_date);
    }
    private void init(int solar_date) {
        this.solar_date = solar_date;

        this.year = solar_date / 10000;
        this.month = (solar_date % 10000) / 100;
        this.day = solar_date % 100;

        Calendar c = Calendar.getInstance();

        c.set(this.year, this.month - 1, this.day);
        this.week = c.get(Calendar.DAY_OF_WEEK);
        this.en_weekname = WEEK_NAME_EN[this.week-1];
        this.lo_weekname = WEEK_NAME_JP[this.week-1];

        //setLunar(solar_date);

//         setLunar1(solar_date);
//        setRokuyoRemain();
        //setRokuyo();
    }

    private void setLunar1(int solar_date) {
Log.d(LOGTAG, "---1--");
        AJD ajd = null;
        try {
            ajd = new AJD(this.year, this.month, this.day);
        } catch (AJDException e) {
            e.printStackTrace();
        }
        Log.d(LOGTAG, "---2--");
        Era.Year era = ajd.getEra();
        this.l_year = era.getYear();
        this.l_month = ajd.getMonth();
        this.l_day = ajd.getDay();
        Log.d(LOGTAG, "---3--");
        Log.d(LOGTAG, "---4--");
        this.lunar_date = this.l_year * 10000 + this.l_month * 100 + this.l_day;
        this.l_bLeap = false;
        Log.d(LOGTAG, "---5--");
        if (this.l_month == getLeapMonth(this.year)) {
            Log.d(LOGTAG, "---7--");
            this.l_bLeap = true;
        }
    }

    private int getLeapMonth(int year) {
        LunisolarYear lunisolarYear = null;
        try {
            lunisolarYear = LunisolarYear.getLunisolarYear(year);
        } catch (AJDException e) {
            e.printStackTrace();
        }
        Log.d(LOGTAG, "---6--");
        return  lunisolarYear.getLeapMonth();
    }

    private void setLunar(int solar_date) {
        ChineseCalendar chCal = new ChineseCalendar();
        Calendar cal = Calendar.getInstance();

        cal.set(this.year, this.month - 1, this.day);
        chCal.setTimeInMillis(cal.getTimeInMillis());

        this.l_year = (chCal.get(ChineseCalendar.EXTENDED_YEAR) - 2637);
        this.l_month = (chCal.get(ChineseCalendar.MONTH) + 1);
        this.l_day = chCal.get(ChineseCalendar.DAY_OF_MONTH);

        if (chCal.get(ChineseCalendar.IS_LEAP_MONTH) == 1) {
            this.l_bLeap = true;
        } else {
            this.l_bLeap = false;
        }
        this.lunar_date = (this.l_year * 10000) + (this.l_month * 100) + this.l_day;
        Log.d(LOGTAG,this.solar_date + "->" + this.lunar_date + ", leap:" + chCal.get(ChineseCalendar.IS_LEAP_MONTH));
    }

    private void setRokuyoRemain() {
        Log.d(LOGTAG, "---8--");
        this.rokuyo_idx = (this.l_month + this.l_day) % 6;
        this.rokuyo = this.ROKUYO_NAME_REMAIN[this.rokuyo_idx];
    }

    private void setRokuyo() {
        if (solar_date != 0 && lunar_date != 0) {

            int start_r = this.START_MONTH_ROKUYO_IDX[this.l_month-1];

            this.rokuyo_idx = (start_r + this.l_day - 1) % 6;
            this.rokuyo = this.ROKUYO_NAME[this.rokuyo_idx];
        }
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    @Override
    public String toString() {
        return "DateInfo{" +
                "solar_date=" + solar_date +
                ", week=" + week +
                ", en_weekname='" + en_weekname + '\'' +
                ", lo_weekname='" + lo_weekname + '\'' +
                ", lunar_date=" + lunar_date +
                ", rokuyo_idx=" + rokuyo_idx +
                ", rokuyo='" + rokuyo + '\'' +
                ", holidayName='" + holidayName + '\'' +
                ", l_bLeap=" + l_bLeap +
                '}';
    }
}
