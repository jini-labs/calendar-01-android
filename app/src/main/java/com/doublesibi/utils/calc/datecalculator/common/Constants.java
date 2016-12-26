package com.doublesibi.utils.calc.datecalculator.common;

/**
 * Created by hunajini on 2016/12/19.
 */

public final class Constants {
    public final static String LOGTAG = "DAY_CALC";

    public final static int INPUT_START_YEAR  = 1;
    public final static int INPUT_START_MONTH = 2;
    public final static int INPUT_START_DATE  = 3;
    public final static int INPUT_END_YEAR  = 11;
    public final static int INPUT_END_MONTH = 12;
    public final static int INPUT_END_DATE  = 13;

    public final static int[][] daysOfMonth = {
            {30,28,30,31,30,31,31,30,31,30,31,30},
            {30,29,30,31,30,31,31,30,31,30,31,30}};
}