package com.doublesibi.utils.calc.datecalculator.holiday;

import java.util.Comparator;

/**
 * Created by hunajini on 2016/12/29.
 */

public class HolidayComparator implements Comparator<HolidayItem> {
    @Override
    public int compare(HolidayItem o1, HolidayItem o2) {
        return (o1.md > o2.md) ? 1 : o1.md == o2.md ? 0 : -1;
    }
}
