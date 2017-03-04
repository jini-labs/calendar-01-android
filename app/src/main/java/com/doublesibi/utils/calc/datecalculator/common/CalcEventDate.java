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
    public int[] getEventYmd(int[] params) {

        Log.d(LOGTAG, "--->y:" + params[0] +
                ", m:" + params[1] +
                ", d:" + params[2] +
                ", a/b:" + params[3] +
                ", ds:" + params[4] +
                ", ws" + params[5] +
                ", ms" + params[6] +
                ", ys" + params[7]);

        c.setCalendar(params[0], params[1], params[2]);
        c.add(Calendar.YEAR, (params[3]==0? -1 : 1) * params[7]);
        c.add(Calendar.MONTH, (params[3]==0? -1 : 1) * params[6]);
        c.add(Calendar.WEEK_OF_MONTH, (params[3]==0? -1 : 1) * params[5]);
        c.add(Calendar.DATE, (params[3]==0? -1 : 1) * params[4]);

        int[] result = new int[2];
        result[0] = c.getCurrentYMD();
        result[1] = c.get(Calendar.DAY_OF_WEEK);

        return result;
    }
    public String[] getEventYmdStr(int[] params) {

        Log.d(LOGTAG, "--->y:" + params[0] +
                ", m:" + params[1] +
                ", d:" + params[2] +
                ", a/b:" + params[3] +
                ", ds:" + params[4] +
                ", ws" + params[5] +
                ", ms" + params[6] +
                ", ys" + params[7]);

        c.setCalendar(params[0], params[1], params[2]);
        c.add(Calendar.YEAR, (params[3]==0? -1 : 1) * params[7]);
        c.add(Calendar.MONTH, (params[3]==0? -1 : 1) * params[6]);
        c.add(Calendar.WEEK_OF_MONTH, (params[3]==0? -1 : 1) * params[5]);
        c.add(Calendar.DATE, (params[3]==0? -1 : 1) * params[4]);

        String[] result = new String[2];
        result[0] = c.getCurrentYMD("/");
        result[1] = "" + c.get(Calendar.DAY_OF_WEEK);

        return result;
    }
}
