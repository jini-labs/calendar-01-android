package com.doublesibi.utils.calc.datecalculator.holiday;

import com.ibm.icu.util.ChineseCalendar;

import java.util.Calendar;

/**
 * Created by hunajini on 2017/01/01.
 */
public class LunarSolar {

    public static int getSolar(int ymd) {
        ChineseCalendar chCal = new ChineseCalendar();
        Calendar cal = Calendar.getInstance();

        chCal.set(ChineseCalendar.EXTENDED_YEAR, (ymd / 10000) + 2637);
        chCal.set(ChineseCalendar.MONTH, (ymd % 10000 / 100) - 1);
        chCal.set(ChineseCalendar.DAY_OF_MONTH, (ymd % 100));

        cal.setTimeInMillis(chCal.getTimeInMillis());
        return cal.get(Calendar.YEAR) * 10000 + (cal.get(Calendar.MONTH) + 1) * 100 + cal.get(Calendar.DATE);
    }

    public static int getSolar(int year, int month, int date) {
        ChineseCalendar chCal = new ChineseCalendar();
        Calendar cal = Calendar.getInstance();

        chCal.set(ChineseCalendar.EXTENDED_YEAR, year + 2637);
        chCal.set(ChineseCalendar.MONTH, month - 1);
        chCal.set(ChineseCalendar.DAY_OF_MONTH, date);

        cal.setTimeInMillis(chCal.getTimeInMillis());
        return cal.get(Calendar.YEAR) * 10000 + (cal.get(Calendar.MONTH) + 1) * 100 + cal.get(Calendar.DATE);
    }

    public static int getLunar(int ymd) {
        ChineseCalendar chCal = new ChineseCalendar();
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, ymd / 10000);
        cal.set(Calendar.MONTH, (ymd % 10000 / 100 ) - 1);
        cal.set(Calendar.DAY_OF_MONTH, (ymd % 100));

        chCal.setTimeInMillis(cal.getTimeInMillis());

        int l_year = chCal.get(ChineseCalendar.EXTENDED_YEAR) - 2637;
        int l_month = chCal.get(ChineseCalendar.MONTH) + 1;
        int l_date = chCal.get(ChineseCalendar.DAY_OF_MONTH);

        return l_year * 10000 + l_month * 100 + l_date;
    }

    public static int getLunar(int year, int month, int date) {
        ChineseCalendar chCal = new ChineseCalendar();
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, date);

        chCal.setTimeInMillis(cal.getTimeInMillis());

        int l_year = chCal.get(ChineseCalendar.EXTENDED_YEAR) - 2637;
        int l_month = chCal.get(ChineseCalendar.MONTH) + 1;
        int l_date = chCal.get(ChineseCalendar.DAY_OF_MONTH);

        return l_year * 10000 + l_month * 100 + l_date;
    }
}
