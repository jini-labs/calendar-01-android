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

    public final static int DATE_TYPE_YMD = 1;
    public final static int DATE_TYPE_MD  = 2;

    public final static int  EVENT_UNIT_YEAR = 0;
    public final static int  EVENT_UNIT_MONTH = 1;
    public final static int  EVENT_UNIT_WEEK = 2;
    public final static int  EVENT_UNIT_DATE = 3;

    public final static int  EVENT_OLD_DATE = 0;
    public final static int  EVENT_WILL_DATE = 1;

    public final static int[][] daysOfMonth = {
            {30,28,30,31,30,31,31,30,31,30,31,30},
            {30,29,30,31,30,31,31,30,31,30,31,30}};

    public static final String ROKYO_NAME[] = {"大安","赤口","先勝","友引","先負","仏滅"};

    public final static String[] WEEK_NAME_JP = {"日曜日", "月曜日", "火曜日", "水曜日",
            "木曜日", "金曜日", "土曜日"};
    public final static String[] WEEK_NAME_EN = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY",
            "THURSDAY", "FRIDAY", "SATURDAY"};
}
