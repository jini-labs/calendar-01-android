package com.doublesibi.utils.calc.datecalculator.holiday;

/**
 * Created by hunajini on 2017/02/07.
 */

public class HolidayListItem {
    int    date;
    String holiday;
    String holidayName;
    String remainDates;

    public HolidayListItem() {
        date = 0;
        holiday = "";
        holidayName = "";
        remainDates = "";
    }

    public HolidayListItem(int date, String holiday, String holidayName, String remainDates) {

        this.date = date;
        this.holiday = (holiday != null?holiday:"");
        this.holidayName = (holidayName != null?holidayName:"");
        this.remainDates = (remainDates != null?remainDates:"");
     }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public String getRemainDates() {
        return remainDates;
    }

    public void setRemainDates(String remainDates) {
        this.remainDates = remainDates;
    }
}
