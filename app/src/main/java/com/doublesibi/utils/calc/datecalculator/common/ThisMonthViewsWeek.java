package com.doublesibi.utils.calc.datecalculator.common;

import android.view.View;
import android.widget.TextView;


/**
 * Created by hunajini on 2017/06/02.
 */

public class ThisMonthViewsWeek {
    public View[] aView;
    public TextView[] aWeekDays;
    public TextView[] aWeekRokuyo;

    public ThisMonthViewsWeek() {

        aView = new View[7];
        aWeekDays = new TextView[7];
        aWeekRokuyo = new TextView[7];
    }

    public View getaView(int index) {
        return aView[index];
    }

    public void setaView(View aView, int index) {
        this.aView[index] = aView;
    }

    public TextView getaWeekDays(int index) {
        return aWeekDays[index];
    }

    public void setaWeekDays(TextView aWeekDays, int index) {
        this.aWeekDays[index] = aWeekDays;
    }

    public TextView getaWeekRokuyo(int index) {
        return aWeekRokuyo[index];
    }

    public void setaWeekRokuyo(TextView aWeekRokuyo, int index) {
        this.aWeekRokuyo[index] = aWeekRokuyo;
    }
}