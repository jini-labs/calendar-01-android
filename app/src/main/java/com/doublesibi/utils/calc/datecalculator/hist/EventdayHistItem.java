package com.doublesibi.utils.calc.datecalculator.hist;

/**
 * Created by hunajini on 2017/01/30.
 */

public class EventdayHistItem {
    String eveName;
    String startDate;
    String eveDate;
    String eveDayMonWeeYea;
    String beforeOrAfterStr;
    int    beforeOrAfter;

    public EventdayHistItem() {
        this.eveName = "";
        this.startDate = "";
        this.eveDate = "";
        this.eveDayMonWeeYea = "";
        this.beforeOrAfterStr = "";
        this.beforeOrAfter = 0;

    }

    public String getEveName() {
        return eveName;
    }

    public void setEveName(String eveName) {
        this.eveName = eveName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEveDate() {
        return eveDate;
    }

    public void setEveDate(String eveDate) {
        this.eveDate = eveDate;
    }

    public String getEveDayMonWeeYea() {
        return eveDayMonWeeYea;
    }

    public void setEveDayMonWeeYea(String eveDayMonWeeYea) {
        this.eveDayMonWeeYea = eveDayMonWeeYea;
    }

    public String getBeforeOrAfterStr() {
        return beforeOrAfterStr;
    }

    public int getBeforeOrAfter() {
        return beforeOrAfter;
    }

    public void setBeforeOrAfterStr(String beforeOrAfterStr) {
        if (beforeOrAfterStr.equals("0")) {
            this.beforeOrAfter = -1;
        } else if (beforeOrAfterStr.equals("1")) {
            this.beforeOrAfter = 1;
        } else {
            this.beforeOrAfter = 0;
        }

        this.beforeOrAfterStr = beforeOrAfterStr;
    }

    public void setBeforeOrAfter(int beforeOrAfter) {
        if (beforeOrAfter == -1) {
            this.beforeOrAfterStr = "0";
        } else if (beforeOrAfter == 1) {
            this.beforeOrAfterStr = "1";
        } else {
            this.beforeOrAfterStr = "";
        }

        this.beforeOrAfter = beforeOrAfter;
    }

    @Override
    public String toString() {
        return "EventdayHistItem{" +
                "eveName='" + eveName + '\'' +
                ", startDate='" + startDate + '\'' +
                ", eveDate='" + eveDate + '\'' +
                ", eveDayMonWeeYea='" + eveDayMonWeeYea + '\'' +
                ", beforeOrAfterStr='" + beforeOrAfterStr + '\'' +
                ", beforeOrAfter=" + beforeOrAfter +
                '}';
    }
}
