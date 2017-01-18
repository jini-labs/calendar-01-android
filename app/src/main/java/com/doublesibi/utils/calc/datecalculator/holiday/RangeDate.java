package com.doublesibi.utils.calc.datecalculator.holiday;

/**
 * Created by hunajini on 2016/12/31.
 */
public class RangeDate {
    public int startDate;
    public int endDate;
    public String name;
    public String pronunciation;

    public RangeDate() {
        this.startDate = 0;
        this.endDate = 0;
        this.name = null;
        this.pronunciation = null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("name").append(name).append("[");
        sb.append("startDate:").append(startDate).append(", ");
        sb.append("endDate:").append(endDate).append(", ");
        sb.append("pronunciation:").append(pronunciation).append("]");

        return sb.toString();
    }
}
