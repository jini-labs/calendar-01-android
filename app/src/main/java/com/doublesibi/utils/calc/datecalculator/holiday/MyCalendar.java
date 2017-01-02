package com.doublesibi.utils.calc.datecalculator.holiday;

import com.doublesibi.utils.calc.datecalculator.common.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by hunajini on 2016/12/24.
 */

public final class MyCalendar extends Calendar {

    private final Calendar c;

    public MyCalendar() {
        this.c = Calendar.getInstance();
    }

    public MyCalendar(Calendar c) {
        this.c = c;
    }

    public MyCalendar(TimeZone zone, Locale aLocale, Calendar c) {
        super(zone, aLocale);
        this.c = c;
    }

    public int getTodayYMD() {
        Calendar cal = Calendar.getInstance();

        return cal.get(Calendar.YEAR) * 10000 +
                (cal.get(Calendar.MONTH) + 1) * 100 +
                cal.get(Calendar.DATE);
    }

    public String getTodayYMD(String delm) {
        Calendar cal = Calendar.getInstance();

        if (delm != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + delm + "MM" + delm + "dd");
            return sdf.format(cal.getTime());
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            return sdf.format(cal.getTime());
        }
    }

    public int getCurrentYMD() {
        return this.c.get(Calendar.YEAR) * 10000 +
                (this.c.get(Calendar.MONTH) + 1) * 100 +
                this.c.get(Calendar.DATE);
    }

    public int getCurrentYMD(Calendar calendar) {
        return calendar.get(Calendar.YEAR) * 10000 +
                (calendar.get(Calendar.MONTH) + 1) * 100 +
                calendar.get(Calendar.DATE);
    }

    public String getCurrentYMD(String delm) {
        if (delm != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + delm + "MM" + delm + "dd");
            return sdf.format(this.c.getTime());
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            return sdf.format(this.c.getTime());
        }
    }

    public String getCurrentYMD(Calendar calendar, String delm) {
        if (delm != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + delm + "MM" + delm + "dd");
            return sdf.format(calendar.getTime());
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            return sdf.format(calendar.getTime());
        }
    }

    public int getDate(int dateType) {
        switch(dateType) {
            case Constants.DATE_TYPE_YMD:
                return getCurrentYMD();
            case Constants.DATE_TYPE_MD:
                return getCurrentYMD() % 10000;
            default:
                return 0;
        }
    }

    public String getDate(String delm, int dateType) {
        switch(dateType) {
            case Constants.DATE_TYPE_YMD:
                return getCurrentYMD(delm);
            case Constants.DATE_TYPE_MD:
                if (delm != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM" + delm + "dd");
                    return sdf.format(this.c.getTime());
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
                    return sdf.format(this.c.getTime());
                }
            default:
                return "";
        }
    }

    public void setCalendar(int y, int m, int d) {
        this.c.set(y, m-1, d);
    }

    public void setCalendar(String y, String m, String d) {
        this.c.set(Integer.parseInt(y), Integer.parseInt(m) - 1, Integer.parseInt(d));
    }

    public static int getMaxDayOfMonth(int y, int m) {
        if (y > 0 && y < 9999) {
            if (m > 0 && m <= 12) {
                return Constants.daysOfMonth[leapYear(y)][m - 1];
            }
        }

        return 0;
    }

    public static int leapYear(int y) {
        if (y % 4 == 0 && !(y % 100 == 0 && y % 400 == 0)) {
            return 1;
        } else {
            return 0;
        }
    }

    public static boolean isValidDate(int y, int m, int d) {
        if(y > 0 && y < 9999) {
            if(m > 0 && m <= 12) {
                if (d > 0 && d <= Constants.daysOfMonth[leapYear(y)][m-1]) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected void computeTime() {

    }

    @Override
    protected void computeFields() {

    }

    @Override
    public int get(int field) {
        return c.get(field);
    }

    @Override
    public void add(int field, int amount) {
        this.c.add(field, amount);
    }

    @Override
    public void roll(int field, boolean up) {

    }

    @Override
    public int getMinimum(int field) {
        return 0;
    }

    @Override
    public int getMaximum(int field) {
        return 0;
    }

    @Override
    public int getGreatestMinimum(int field) {
        return 0;
    }

    @Override
    public int getLeastMaximum(int field) {
        return 0;
    }
}
