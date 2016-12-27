package com.doublesibi.utils.calc.datecalculator.common;

import android.content.res.Resources;
import android.util.Log;

import com.doublesibi.utils.calc.datecalculator.R;
import com.doublesibi.utils.calc.datecalculator.util.MyCalendar;

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

    // 0:year, 1:month, 2:day, 3:unit, 4:before, after, 5:value
    public String[] getEventYmd(int[] params) {

        Log.d(LOGTAG, "--->" + params[0] + ", " +
                params[1] + ", " +
                params[2] + ", " +
                params[3] + ", " +
                params[4] + ", " );

        int field;
        c.set(params[0], params[0] - 1, params[0]);
        switch(params[3]) {
            case Constants.EVENT_UNIT_YEAR:
                field = Calendar.YEAR;
                break;
            case Constants.EVENT_UNIT_MONTH:
                field = Calendar.MONTH;
                break;
            case Constants.EVENT_UNIT_WEEK:
                field = Calendar.DATE;
                break;
            case Constants.EVENT_UNIT_DATE:
                field = Calendar.DATE;
                break;
            default:
                return null;
        }

        switch(params[4]) {
            case Constants.EVENT_OLD_DATE:
                params[5] *= -1;
                break;
            case Constants.EVENT_WILL_DATE:
                break;
            default:
                return null;
        }

        c.add(field, params[5]);

        String[] result = new String[2];
        result[0] = c.getCurrentYMD("-");
        result[1] = "" + c.get(Calendar.DAY_OF_WEEK);

        return result;
    }
}
