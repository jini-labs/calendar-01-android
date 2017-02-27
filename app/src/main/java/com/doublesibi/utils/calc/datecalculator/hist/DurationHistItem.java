package com.doublesibi.utils.calc.datecalculator.hist;

/**
 * Created by hunajini on 2017/01/30.
 */

public class DurationHistItem {
    String startDate;
    String endDate;
    String durDays;
    String durWeeksDays;
    String durMonthsDays;
    String durYearsMonthsDays;
    String name;

    public DurationHistItem() {
        this.startDate = "";
        this.endDate = "";
        this.durDays = "";
        this.durWeeksDays = "";
        this.durMonthsDays = "";
        this.durYearsMonthsDays = "";
        this.name = "";
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDurDays() {
        return durDays;
    }

    public void setDurDays(String durDays) {
        this.durDays = durDays;
    }

    public String getDurWeeksDays() {
        return durWeeksDays;
    }

    public void setDurWeeksDays(String durWeeksDays) {
        this.durWeeksDays = durWeeksDays;
    }

    public String getDurMonthsDays() {
        return durMonthsDays;
    }

    public void setDurMonthsDays(String durMonthsDays) {
        this.durMonthsDays = durMonthsDays;
    }

    public String getDurYearsMonthsDays() {
        return durYearsMonthsDays;
    }

    public void setDurYearsMonthsDays(String durYearsMonthsDays) {
        this.durYearsMonthsDays = durYearsMonthsDays;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DurationHistItem{" +
                "startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", durDays='" + durDays + '\'' +
                ", durWeeksDays='" + durWeeksDays + '\'' +
                ", durMonthsDays='" + durMonthsDays + '\'' +
                ", durYearsMonthsDays='" + durYearsMonthsDays + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
