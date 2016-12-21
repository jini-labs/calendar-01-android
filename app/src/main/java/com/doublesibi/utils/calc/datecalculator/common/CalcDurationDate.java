package com.doublesibi.utils.calc.datecalculator.common;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by hunajini on 2016/12/04.
 */

public class CalcDurationDate {

    private int styy = 0, stmm = 0, stdd = 0, stymd = 0, stw = 0;
    private int enyy = 0, enmm = 0, endd = 0, enymd = 0, enw = 0;

    private int totalDays = 0;

    private int totalWeeks = 0;
    private int totalWeekDays = 0;

    private int totalMonths = 0;
    private int totalMonthDays = 0;

    private int totalYears = 0;
    private int totalYearMonths= 0;
    private int totalYearDays = 0;

    public CalcDurationDate() {
    }

    public CalcDurationDate(int styy, int stmm, int stdd, int enyy, int enmm, int endd) {
        setInitDate(styy, stmm, stdd, enyy, enmm, endd);
        setDiffDays();
    }

    public int getTotalDays() {
        return totalDays;
    }

    public int getTotalWeeks() {
        return totalWeeks;
    }

    public int getTotalWeekDays() {
        return totalWeekDays;
    }

    public int getTotalMonths() {
        return totalMonths;
    }

    public int getTotalMonthDays() {
        return totalMonthDays;
    }

    public int getTotalYears() {
        return totalYears;
    }

    public int getTotalYearMonths() {
        return totalYearMonths;
    }

    public int getTotalYearDays() {
        return totalYearDays;
    }

    public int getEnymd() {
        return enymd;
    }

    public int getStymd() {
        return stymd;
    }

    public void setDiffDays() {
        int _sy = this.styy;
        int _sm = this.stmm;
        int _y, _m, _d, _w;

        Calendar c = Calendar.getInstance();
        c.set(this.styy, this.stmm - 1, this.stdd);

        if(this.stymd != this.enymd) {

            while (true) {

                c.add(Calendar.DATE, 1);

                _y = c.get(Calendar.YEAR);
                _m = c.get(Calendar.MONTH) + 1;
                _d = c.get(Calendar.DAY_OF_MONTH);
                _w = c.get(Calendar.DAY_OF_WEEK);
                Log.d(Constants.LOGTAG, "" + (_y * 10000 + _m * 100 + _d) + " " + _w +", st:"+ this.stymd +", en:"+ this.enymd);

                this.totalDays++;
                this.totalWeekDays++;
                this.totalMonthDays++;
                this.totalYearDays++;

                if (this.stw == _w) {
                    this.totalWeeks++;

                    this.totalWeekDays = 0;
                }

                if (_sm != _m && this.stdd == _d) {
                    this.totalMonths++;
                    this.totalYearMonths++;

                    this.totalMonthDays = 0;
                    this.totalYearDays = 0;
                    _sm = _m;
                }

                if (_sy != _y && (this.stmm == _m && this.stdd == _d)) {
                    this.totalYears++;

                    this.totalMonthDays = 0;
                    this.totalYearMonths = 0;
                    this.totalYearDays = 0;
                }

                if (this.enymd == (_y * 10000 + _m * 100 + _d)) {
                    break;
                }
            }
        }
    }

    public boolean setInitDate(int startY, int startM, int startD, int endY, int endM, int endD) {

        if (!CalcEventDate.isValidDate(startY, startM, startD) || !CalcEventDate.isValidDate(endY, endM, endD))
            return false;

        if ((startY * 10000 + startM * 100 + startD) <=
                (endY * 10000 + endM * 100 + endD)) {
            this.styy = startY;
            this.stmm = startM;
            this.stdd = startD;
            this.enyy = endY;
            this.enmm = endM;
            this.endd = endD;
        } else {
            this.enyy = startY;
            this.enmm = startM;
            this.endd = startD;
            this.styy = endY;
            this.stmm = endM;
            this.stdd = endD;
        }

        this.stymd = this.styy * 10000 + this.stmm * 100 + this.stdd;
        this.enymd = this.enyy * 10000 + this.enmm * 100 + this.endd;

        Calendar c = Calendar.getInstance();
        c.set(styy, stmm-1, stdd);
        this.stw = c.get(Calendar.DAY_OF_WEEK);

        c.set(enyy, enmm-1, endd);
        this.enw = c.get(Calendar.DAY_OF_WEEK);

        return true;
    }
}
