package com.doublesibi.utils.calc.datecalculator.holiday;

import java.util.Comparator;

/**
 * Created by hunajini on 2016/12/29.
 */

public class YearNameComparator implements Comparator<RangeDate> {
    @Override
    public int compare(RangeDate o1, RangeDate o2) {
        return (o1.startDate > o2.startDate) ? 1 : o1.startDate == o2.startDate ? 0 : -1;
    }
}
