package com.doublesibi.utils.calc.datecalculator.common;

import android.util.Log;

import com.doublesibi.utils.calc.datecalculator.holiday.MyCalendar;

import java.util.Calendar;

/**
 * Created by hunajini on 2016/12/19.
 */

public class CalcEventDate {
    private final String LOGTAG = "DayCalc";

    private MyCalendar c;

    public CalcEventDate() {
        this.c = new MyCalendar();
    }

    // 0:year, 1:month, 2:day, 3:before, after,
    // 4:numofday, 5:numofweek, 6:numofmonth, 7:numofyear
    public String[] getEventYmd(int[] params) {

        Log.d(LOGTAG, "--->" + params[0] + ", " +
                params[1] + ", " +
                params[2] + ", " +
                params[3] + ", " +
                params[4] + ", " +
                params[5] + ", " +
                params[6] + ", " +
                params[7]);

        c.set(params[0], params[0] - 1, params[0]);
        c.add(Calendar.YEAR, params[7]);
        c.add(Calendar.MONTH, params[6]);
        c.add(Calendar.DATE, params[5]);
        c.add(Calendar.DATE, params[4]);

        String[] result = new String[2];
        result[0] = c.getCurrentYMD("-");
        result[1] = "" + c.get(Calendar.DAY_OF_WEEK);

        return result;
    }
}
