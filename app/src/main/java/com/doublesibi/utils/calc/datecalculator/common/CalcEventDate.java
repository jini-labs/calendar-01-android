package com.doublesibi.utils.calc.datecalculator.common;

/**
 * Created by hunajini on 2016/12/19.
 */

public class CalcEventDate {
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
}
