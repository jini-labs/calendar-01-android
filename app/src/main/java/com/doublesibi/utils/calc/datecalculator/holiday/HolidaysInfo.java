package com.doublesibi.utils.calc.datecalculator.holiday;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by hunajini on 2016/12/29.
 */

public class HolidaysInfo {
    private static String LOGTAG = "DayCalc";

    private static int SPRING_EQUINOX_DATE = 321;
    private static int AUTUMNAL_EQUINOX_DATE = 923;

    private String xmlFile;
    private int year;

    private String country;
    private ArrayList<HolidayItem> baseHolidaysInfo;
    private ArrayList<HolidayItem> holidays;
    private ArrayList<HolidayItem> temporaryHolidays;
    private HashMap<Integer, HolidayItem> holidaysMap;
    private ArrayList<RangeDate> substitutes = null;
    private ArrayList<RangeDate> betweens = null;

    private int[][][] holidayCalendar;

    private MyCalendar cal = null;

    public HolidaysInfo() {
        this.holidays = new ArrayList<>();
        this.holidaysMap = new HashMap<>();
        this.temporaryHolidays= new ArrayList<>();
        this.substitutes = new ArrayList<>();
        this.betweens = new ArrayList<>();

        this.holidayCalendar = new int[12][6][7];
        this.cal = new MyCalendar();
    }

    public ArrayList<HolidayItem> getHolidays() {
        return holidays;
    }

    public HashMap<Integer, HolidayItem> getHolidaysMap() {
        return holidaysMap;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void add(HolidayItem item) {
        this.holidays.add(item);
        this.holidaysMap.put(item.ymd, item);

        Collections.sort(this.holidays, new HolidayComparator());
    }

    public HolidayItem get(int ymd) {
        return this.holidaysMap.get(ymd);
    }

    public boolean remove(int ymd) {
        for (HolidayItem item : this.holidays) {
            if (item.ymd == ymd) {
                holidays.remove(item);
                if (this.holidaysMap.remove(ymd) != null)
                    return true;
            }
        }

        return false;
    }

    public void setBaseHolidaysInfo(XmlPullParser xpp) {
        final int ITEM_HOLIDAY = 1;
        final int ITEM_TEMPORARY = 2;
        final int ITEM_DIVISION = 10;
        final int ITEM_SUBSTITUTE = 11;
        final int ITEM_BETWEEN = 12;

        int itemType = 0;

        this.baseHolidaysInfo = new ArrayList<>();

        Log.d(LOGTAG, "xml parsing start....");
        try {
            HolidayItem holidayItem = null;
            RangeDate rangeDate = null;

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                final String name = xpp.getName();
                switch(eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("holidays".equals(name)) {
                            itemType = ITEM_HOLIDAY;
                        } else if ("substitutes".equals(name)) {
                            itemType = ITEM_SUBSTITUTE;
                        } else if ("betweens".equals(name)) {
                            itemType = ITEM_BETWEEN;
                        } else if ("temporarys".equals(name)) {
                            itemType = ITEM_TEMPORARY;
                        } else {
                            if ("item".equals(name)) {
                                switch (itemType) {
                                    case ITEM_HOLIDAY:
                                    case ITEM_TEMPORARY:
                                        holidayItem = new HolidayItem();
                                        break;
                                    case ITEM_SUBSTITUTE:
                                    case ITEM_BETWEEN:
                                        rangeDate = new RangeDate();
                                        break;
                                    default:
                                        Log.w(LOGTAG, "unknown itemType.(" + itemType + ")");
                                        break;
                                }
                            } else {
                                if ("name".equals(name)) {
                                    holidayItem.name = xpp.nextText().trim();
                                } else if ("engname".equals(name)) {
                                    // TODO: next version.
                                } else if ("date".equals(name)) {
                                    switch (itemType) {
                                        case ITEM_HOLIDAY:
                                            holidayItem.md = Integer.parseInt(xpp.nextText().trim());
                                            break;
                                        case ITEM_TEMPORARY:
                                            holidayItem.ymd = Integer.parseInt(xpp.nextText().trim());
                                            holidayItem.md = holidayItem.ymd % 10000;
                                            break;
                                        default:
                                            Log.w(LOGTAG, "check itemType on tag 'date'.(" + itemType + ")");
                                            break;
                                    }
                                } else if ("monthOfYear".equals(name)) {
                                    holidayItem.monthOfYear = Integer.parseInt(xpp.nextText().trim());
                                } else if ("weekOfMonth".equals(name)) {
                                    holidayItem.weekOfMonth = Integer.parseInt(xpp.nextText().trim());
                                } else if ("dayOfWeek".equals(name)) {
                                    holidayItem.dayOfWeek= Integer.parseInt(xpp.nextText().trim());
                                } else if ("substitute".equals(name)) {
                                    holidayItem.substitute = Boolean.valueOf(xpp.nextText().trim());
                                } else if ("specialFunction".equals(name)) {
                                    holidayItem.extendFunc = xpp.nextText().trim();
                                } else if ("startDate".equals(name)) {
                                    if (itemType < ITEM_DIVISION) {
                                        holidayItem.startDate = Integer.parseInt(xpp.nextText().trim());
                                    } else {
                                        rangeDate.startDate  = Integer.parseInt(xpp.nextText().trim());
                                    }
                                } else if ("endDate".equals(name)) {
                                    if (itemType < ITEM_DIVISION) {
                                        holidayItem.endDate = Integer.parseInt(xpp.nextText().trim());
                                    } else {
                                        rangeDate.endDate  = Integer.parseInt(xpp.nextText().trim());
                                    }
                                } else {
                                    if (name != null)
                                        Log.d(LOGTAG, "etc:(tagname)" + name);
                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("item".equals(name)) {
                            switch (itemType) {
                                case ITEM_HOLIDAY:
                                    this.baseHolidaysInfo.add(holidayItem);
                                    break;
                                case ITEM_TEMPORARY:
                                    this.temporaryHolidays.add(holidayItem);
                                    break;
                                case ITEM_SUBSTITUTE:
                                    this.substitutes.add(rangeDate);
                                    break;
                                case ITEM_BETWEEN:
                                    this.betweens.add(rangeDate);
                                    break;
                                default:
                                    Log.w(LOGTAG, "unknown itemType on END_TAG.()" + itemType + ")");
                                    break;
                            }
                        }
                        break;
                    default:
                        if (name != null)
                            Log.d(LOGTAG,"not implemented eventType.(tagname:" + name + ", eventType:" + eventType);
                        break;
                }

                eventType = xpp.next();
            }
        }  catch (XmlPullParserException e) {
            Log.e(LOGTAG, e.toString());
            Log.e(LOGTAG, e.getStackTrace().toString());
        } catch (Exception e) {
            Log.e(LOGTAG, e.toString());
            Log.e(LOGTAG, e.getStackTrace().toString());
        } finally {
            // 処理なし
            // Debug log
            Log.d(LOGTAG, "\tBase information of holiday in " + this.country + ".");
            Log.d(LOGTAG, "\t\tHoliday information");
            for (HolidayItem item: this.baseHolidaysInfo) {
                Log.d(LOGTAG, "\t\t" + item.toString());
            }

            if (this.substitutes != null && this.substitutes.size() > 0 ) {
                Log.d(LOGTAG, "\t\tSubstitute information");
                for (RangeDate item : this.substitutes) {
                    Log.d(LOGTAG, "\t\t\t" + item.startDate + " ~ " + item.endDate);
                }
            }
            if (this.betweens != null && this.betweens.size() > 0 ) {
                Log.d(LOGTAG, "\t\tBetween information");
                for (RangeDate item : this.betweens) {
                    Log.d(LOGTAG, "\t\t\t" + item.startDate + " ~ " + item.endDate);
                }
            }
        }
    }

    public ArrayList<HolidayItem> getPreference(XmlPullParser xpp) {
        final int ITEM_HOLIDAY = 1;
        final int ITEM_TEMPORARY = 2;
        final int ITEM_DIVISION = 10;
        final int ITEM_SUBSTITUTE = 11;
        final int ITEM_BETWEEN = 12;

        int itemType = 0;
        ArrayList<HolidayItem> holidaysInfo = new ArrayList<>();

        Log.d(LOGTAG, "xml parsing start....");
        try {
            HolidayItem holidayItem = null;
            RangeDate rangeDate = null;

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                final String name = xpp.getName();
                switch(eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("holidays".equals(name)) {
                            itemType = ITEM_HOLIDAY;
                        } else if ("substitutes".equals(name)) {
                            itemType = ITEM_SUBSTITUTE;
                        } else if ("betweens".equals(name)) {
                            itemType = ITEM_BETWEEN;
                        } else if ("temporarys".equals(name)) {
                            itemType = ITEM_TEMPORARY;
                        } else {
                            if ("item".equals(name)) {
                                switch (itemType) {
                                    case ITEM_HOLIDAY:
                                    case ITEM_TEMPORARY:
                                        holidayItem = new HolidayItem();
                                        break;
                                    case ITEM_SUBSTITUTE:
                                    case ITEM_BETWEEN:
                                        rangeDate = new RangeDate();
                                        break;
                                    default:
                                        Log.w(LOGTAG, "unknown itemType.(" + itemType + ")");
                                        break;
                                }
                            } else {
                                if ("name".equals(name)) {
                                    holidayItem.name = xpp.nextText().trim();
                                } else if ("engname".equals(name)) {
                                    // TODO: next version.
                                } else if ("date".equals(name)) {
                                    switch (itemType) {
                                        case ITEM_HOLIDAY:
                                            holidayItem.md = Integer.parseInt(xpp.nextText().trim());
                                            break;
                                        case ITEM_TEMPORARY:
                                            holidayItem.ymd = Integer.parseInt(xpp.nextText().trim());
                                            holidayItem.md = holidayItem.ymd % 10000;
                                            break;
                                        default:
                                            Log.w(LOGTAG, "check itemType on tag 'date'.(" + itemType + ")");
                                            break;
                                    }
                                } else if ("monthOfYear".equals(name)) {
                                    holidayItem.monthOfYear = Integer.parseInt(xpp.nextText().trim());
                                } else if ("weekOfMonth".equals(name)) {
                                    holidayItem.weekOfMonth = Integer.parseInt(xpp.nextText().trim());
                                } else if ("dayOfWeek".equals(name)) {
                                    holidayItem.dayOfWeek= Integer.parseInt(xpp.nextText().trim());
                                } else if ("substitute".equals(name)) {
                                    holidayItem.substitute = Boolean.valueOf(xpp.nextText().trim());
                                } else if ("specialFunction".equals(name)) {
                                    holidayItem.extendFunc = xpp.nextText().trim();
                                } else if ("startDate".equals(name)) {
                                    if (itemType < ITEM_DIVISION) {
                                        holidayItem.startDate = Integer.parseInt(xpp.nextText().trim());
                                    } else {
                                        rangeDate.startDate  = Integer.parseInt(xpp.nextText().trim());
                                    }
                                } else if ("endDate".equals(name)) {
                                    if (itemType < ITEM_DIVISION) {
                                        holidayItem.endDate = Integer.parseInt(xpp.nextText().trim());
                                    } else {
                                        rangeDate.endDate  = Integer.parseInt(xpp.nextText().trim());
                                    }
                                } else {
                                    if (name != null)
                                        Log.d(LOGTAG, "etc:(tagname)" + name);
                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("item".equals(name)) {
                            switch (itemType) {
                                case ITEM_HOLIDAY:
                                    holidaysInfo.add(holidayItem);
                                    break;
                                case ITEM_TEMPORARY:
                                    this.temporaryHolidays.add(holidayItem);
                                    break;
                                case ITEM_SUBSTITUTE:
                                    this.substitutes.add(rangeDate);
                                    break;
                                case ITEM_BETWEEN:
                                    this.betweens.add(rangeDate);
                                    break;
                                default:
                                    Log.w(LOGTAG, "unknown itemType on END_TAG.()" + itemType + ")");
                                    break;
                            }
                        }
                        break;
                    default:
                        if (name != null)
                            Log.d(LOGTAG,"not implemented eventType.(tagname:" + name + ", eventType:" + eventType);
                        break;
                }

                eventType = xpp.next();
            }
        }  catch (XmlPullParserException e) {
            Log.e(LOGTAG, e.toString());
            Log.e(LOGTAG, e.getStackTrace().toString());
        } catch (Exception e) {
            Log.e(LOGTAG, e.toString());
            Log.e(LOGTAG, e.getStackTrace().toString());
        } finally {
            // 処理なし
            // Debug log
        Log.d(LOGTAG, "\tBase information of holiday in " + this.country + ".");
        Log.d(LOGTAG, "\t\tHoliday information");
        for (HolidayItem item: holidaysInfo) {
            Log.d(LOGTAG, "\t\t" + item.toString());
        }

        if (this.substitutes != null && this.substitutes.size() > 0 ) {
            Log.d(LOGTAG, "\t\tSubstitute information");
            for (RangeDate item : this.substitutes) {
                Log.d(LOGTAG, "\t\t\t" + item.startDate + " ~ " + item.endDate);
            }
        }
        if (this.betweens != null && this.betweens.size() > 0 ) {
            Log.d(LOGTAG, "\t\tBetween information");
            for (RangeDate item : this.betweens) {
                Log.d(LOGTAG, "\t\t\t" + item.startDate + " ~ " + item.endDate);
            }
        }
        }

        return holidaysInfo;
    }

    public void clearHolidays() {
        if (this.holidays != null)
            this.holidays.clear();
        if (this.holidaysMap != null)
            this.holidaysMap.clear();
    }

    public boolean setHolidayYear(int year) {
        this.year = year;

        for (HolidayItem item : this.baseHolidaysInfo) {

            if(item.startDate == 0 && item.endDate == 0) {
                continue;
            }

            if(year < item.startDate / 10000
                    || (item.endDate > 0 && year > item.endDate/ 10000)) {
                continue;
            }

            // 特別な計算（旧暦の日付から等の計算）
            if (item.extendFunc != null && item.extendFunc.length() > 0) {
                if (this.country.equals("Japan")) {
                    if (item.extendFunc.equals("SpringEquinox")) {
                        item.md = SpringEquinox(year);
                        item.ymd = year * 10000 + item.md;
                        this.add(item);
                    } else if (item.extendFunc.equals("AutumnalEquinox")) {
                        item.md = AutumnalEquinox(year);
                        item.ymd = year * 10000 + item.md;
                        this.add(item);
                    }
                } if (this.country.equals("Korea")) {
                    if (item.extendFunc.equals("LunarFirstPrevday")) {
                        int tmp = MyCalendar.getSolar(year, 1, 1);
                        this.cal.setCalendar(year, (tmp % 10000 / 100), tmp % 100);
                        this.cal.add(Calendar.DATE, -1);
                        item.ymd = this.cal.getCurrentYMD();
                    } else if (item.extendFunc.equals("LunarFirstday")) {
                        item.ymd = MyCalendar.getSolar(year, 1, 1);
                    } else if (item.extendFunc.equals("LunarFirstNextday")) {
                        item.ymd = MyCalendar.getSolar(year, 1, 2);
                    } else if (item.extendFunc.equals("BuddhaBirth")) {
                        item.ymd = MyCalendar.getSolar(year, 4, 8);
                    } else if (item.extendFunc.equals("SuperMoonPrevday")) {
                        item.ymd = MyCalendar.getSolar(year, 8, 14);
                    } else if (item.extendFunc.equals("SuperMoonday")) {
                        item.ymd = MyCalendar.getSolar(year, 8, 15);
                    } else if (item.extendFunc.equals("SuperMoonNextday")) {
                        item.ymd = MyCalendar.getSolar(year, 8, 16);
                    }

                    if (item.ymd != 0) {
                        item.md = item.ymd % 10000;
                        this.add(item);
                    }
                }
            } else if (item.monthOfYear > 0 && item.weekOfMonth > 0
                    && item.dayOfWeek > 0) {
                int weekOfCount = 0;
                this.cal.setCalendar(year, item.monthOfYear, 1);
                while(true) {
                    if (this.cal.get(Calendar.DAY_OF_WEEK) == item.dayOfWeek) {
                        weekOfCount++;
                    }

                    if (weekOfCount == item.weekOfMonth) {
                        item.md = item.monthOfYear * 100 + this.cal.get(Calendar.DATE);
                        item.ymd = year * 10000 + item.md;

                        this.add(item);
                        break;
                    }

                    this.cal.add(Calendar.DATE, 1);
                    if ((cal.get(Calendar.MONTH) + 1) != item.monthOfYear) {
                        break;
                    }
                }
            } else {
                item.ymd = year * 10000 + item.md;
                this.add(item);
            }
        }

        ArrayList<HolidayItem> tmpHolidays = new ArrayList<>();
        // 振替休日の計算
        tmpHolidays = getSubstituteDay();
        if (tmpHolidays.size() > 0) {
            for (HolidayItem item : tmpHolidays) {
                this.add(item);
            }
        }

        // 前日と翌日が祝日の場合は休日にする。
        tmpHolidays.clear();
        tmpHolidays = getBetweenDay();
        if (tmpHolidays.size() > 0) {
            for (HolidayItem item : tmpHolidays) {
                this.add(item);
            }
        }

        tmpHolidays.clear();
        tmpHolidays = getTemporary();
        if (tmpHolidays.size() > 0) {
            for (HolidayItem item : tmpHolidays) {
                this.add(item);
            }
        }

        // debug
        Log.d(LOGTAG, year + "'s holiday");
        for (HolidayItem item : this.holidays) {
            Log.d(LOGTAG, "\t\t" + item.toString());
        }

        return false;
    }

    public ArrayList<HolidayItem> getSubstituteDay() {
        ArrayList<HolidayItem> retSubstitutesDay = new ArrayList<>();

        if (this.country.equals("Japan")) {
            if (this.substitutes != null && this.substitutes.size() > 0) {
                for (HolidayItem item : this.holidays) {
//                    if (!item.substitute)
//                        continue;

                    boolean bContinue = false;
                    for (RangeDate range : this.substitutes) {
                        if (item.ymd > range.startDate && ((range.endDate == 0) ? true : item.ymd < range.endDate)) {
                            bContinue = true;
                            break;
                        }
                    }

                    if (!bContinue) {
                        continue;
                    }

                    this.cal.setCalendar(year, item.md / 100, item.md % 100);
                    if (this.cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        this.cal.add(Calendar.DATE, 1);
                        while (true) {
                            if (this.holidaysMap.get(this.cal.getCurrentYMD()) == null) {
                                HolidayItem newItem = new HolidayItem();
                                newItem.ymd = this.cal.getCurrentYMD();
                                newItem.md = newItem.ymd % 10000;
                                newItem.name = "substitute day";

                                retSubstitutesDay.add(newItem);
                                break;
                            } else {
                                this.cal.add(Calendar.DATE, 1);

                                if (item.md / 100 != (this.cal.get(Calendar.MONTH) + 1))
                                    break;
                            }
                        }
                    }
                }
            }
        } else if (this.country.equals("Korea")) {
            if (this.substitutes != null && this.substitutes.size() > 0) {
                for (HolidayItem item : this.holidays) {
                    if (!item.substitute)
                        continue;

                    boolean bContinue = false;
                    for (RangeDate range : this.substitutes) {
                        if (item.ymd > range.startDate && ((range.endDate == 0) ? true : item.ymd < range.endDate)) {
                            bContinue = true;
                            break;
                        }
                    }

                    if (!bContinue) {
                        continue;
                    }

                    boolean condition1 = false;
                    this.cal.setCalendar(year, item.md / 100, item.md % 100);
                    for (HolidayItem foundItem : this.holidays) {
                        if (!item.name.equals(foundItem.name)) {
                            if (item.ymd == foundItem.ymd) {
                                condition1 = true;
                                break;
                            }
                        }
                    }
                    if (condition1 || this.cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        this.cal.add(Calendar.DATE, 1);
                        while (true) {
                            if (this.holidaysMap.get(this.cal.getCurrentYMD()) == null) {
                                HolidayItem newItem = new HolidayItem();
                                newItem.ymd = this.cal.getCurrentYMD();
                                newItem.md = newItem.ymd % 10000;
                                newItem.name = "substitute day";

                                retSubstitutesDay.add(newItem);
                                break;
                            } else {
                                this.cal.add(Calendar.DATE, 1);

                                if (item.md / 100 != (this.cal.get(Calendar.MONTH) + 1))
                                    break;
                            }
                        }
                    }
                }
            }
        }

        return retSubstitutesDay;
    }

    public ArrayList<HolidayItem> getBetweenDay() {
        ArrayList<HolidayItem> retBetweensDay = new ArrayList<>();

        if (country.equals("Japan")) {
            if (this.betweens != null && this.betweens.size() > 0) {
                for (HolidayItem item : this.holidays) {
                    boolean bContinue = false;
                    for (RangeDate range : this.betweens) {
                        if (item.ymd > range.startDate && ((range.endDate == 0) ? true : item.ymd < range.endDate)) {
                            bContinue = true;
                            break;
                        }
                    }

                    if (!bContinue)
                        break;

                    this.cal.setCalendar(year, item.md / 100, item.md % 100);
                    switch (this.cal.get(Calendar.DAY_OF_WEEK)) {
                        case Calendar.MONDAY:
                        case Calendar.TUESDAY:
                        case Calendar.WEDNESDAY:
                            this.cal.add(Calendar.DATE, 2);
                            if (this.holidaysMap.get(this.cal.getCurrentYMD()) != null) {
                                this.cal.add(Calendar.DATE, -1);
                                HolidayItem newItem = new HolidayItem();
                                newItem.ymd = this.cal.getCurrentYMD();
                                newItem.md = newItem.ymd % 10000;
                                newItem.name = "between holiday";
                                retBetweensDay.add(newItem);
                            }

                            break;
                        default:
                            break;
                    }
                }
            }
        }

        return retBetweensDay;
    }

    public ArrayList<HolidayItem> getTemporary() {
        ArrayList<HolidayItem> retTemporaryHolidays = new ArrayList<>();
        for (HolidayItem item : this.temporaryHolidays) {
            if (item.ymd / 10000 == this.year) {
                retTemporaryHolidays.add(item);
            }
        }

        return retTemporaryHolidays;
    }

    private int SpringEquinox(int year) {
        int tmp = -99;
        switch(year % 4) {
            case 0:
                if (year >= 1900 && year <= 1959) tmp = 0;
                else if (year >= 1960 && year <= 2088) tmp = -1;
                else if (year >= 2092 && year <= 2096) tmp = -2;
                break;
            case 1:
                if (year >= 1901 && year <= 1989) tmp = 0;
                else if (year >= 1993 && year <= 2097) tmp = -1;
                break;
            case 2:
                if (year >= 1902 && year <= 2022) tmp = 0;
                else if (year >= 2026 && year <= 2098) tmp = -1;
                break;
            case 3:
                if (year >= 1903 && year <= 1923) tmp = 1;
                else if (year >= 1927 && year <= 2025) tmp = 0;
                else if (year >= 2059 && year <= 2099) tmp = -1;
                break;
        }

        if (tmp == -99) {
            return SPRING_EQUINOX_DATE + (-1) * cal.leapYear(year);
        }

        return SPRING_EQUINOX_DATE + tmp;
    }

    private int AutumnalEquinox(int year) {
        int tmp = -99;
        switch(year % 4) {
            case 0:
                if (year >= 1900 && year <= 2008) tmp = 0;
                else if (year >= 2012 && year <= 2096) tmp = -1;
                break;
            case 1:
                if (year >= 1901 && year <= 1917) tmp = 1;
                else if (year >= 1921 && year <= 2041) tmp = 0;
                else if (year >= 2045 && year <= 2097) tmp = -1;
                break;
            case 2:
                if (year >= 1902 && year <= 1946) tmp = 1;
                else if (year >= 1950 && year <= 2074) tmp = 0;
                else if (year >= 2078 && year <= 2098) tmp = -1;
                break;
            case 3:
                if (year >= 1903 && year <= 1979) tmp = 1;
                else if (year >= 1983&& year <= 2099) tmp = 0;
                break;
        }

        if (tmp == -99) {
            return AUTUMNAL_EQUINOX_DATE + (-1) * cal.leapYear(year);
        }

        return AUTUMNAL_EQUINOX_DATE + tmp;
    }

    public void setHolidayCalendar() {
        int stdd, stwd, stwc;
        int mm, dd, wd, wc = 0;
        int weekCount, beforewd;

        Calendar calendar = Calendar.getInstance();
        calendar.set(this.year, 0, 1);
        stdd = 0;
        stwd = calendar.get(Calendar.DAY_OF_WEEK);
        stwc = 0;
        while (true) {
            mm = calendar.get(Calendar.MONTH);
            dd = calendar.get(Calendar.DATE);
            wd = calendar.get(Calendar.DAY_OF_WEEK);

            if (dd == 1) {
                wc = 0;
            } else {
                if (wd == 1) {
                    wc++;
                }
            }

            if (this.holidaysMap.get(this.cal.getCurrentYMD(calendar)) != null)
                dd += 1000;
            this.holidayCalendar[mm][wc][wd - 1] = dd;

            calendar.add(Calendar.DATE, 1);
            if (year != calendar.get(Calendar.YEAR))
                break;
        }
    }

    public int[][][] getHolidayCalendar() {
        return this.holidayCalendar;
    }

    public int[][] getHolidayCalendar(int month) {
        return this.holidayCalendar[month-1];
    }

    public void printHolidayCalendar() {
        int stdd, stwd, stwc;
        int mm, dd, wd, wc=0;
        int weekCount, beforewd;

        Calendar calendar = Calendar.getInstance();
        calendar.set(this.year, 0, 1);
        stdd = 0;
        stwd = calendar.get(Calendar.DAY_OF_WEEK);
        stwc = 0;
        while(true) {
            mm = calendar.get(Calendar.MONTH);
            dd = calendar.get(Calendar.DATE);
            wd = calendar.get(Calendar.DAY_OF_WEEK);

            if (dd == 1) {
                wc = 0;
            } else {
                if(wd == 1) {
                    wc++;
                }
            }

            if (this.holidaysMap.get(this.cal.getCurrentYMD(calendar)) != null)
                dd += 1000;
            this.holidayCalendar[mm][wc][wd - 1] = dd;

            calendar.add(Calendar.DATE, 1);
            if (year != calendar.get(Calendar.YEAR))
                break;
        }

        String weekStr = "";

        Log.d(LOGTAG, "[ " + year + " ]");
        for (int i = 0; i < 12; i++) {
            Log.d(LOGTAG, "[ " + year + " ] (" + (i+1) + ")");
            weekStr = "";
            for (int j = 0; j < 6; j++) {
                weekStr = "";
                for (int k = 0; k < 7; k++) {
                    if (this.holidayCalendar[i][j][k] == 0) {
                        String s = String.format("    ");
                        weekStr = weekStr + "    ";
                    } else {
                        if (this.holidayCalendar[i][j][k] > 1000) {
                            String s = String.format(" *%2d", (this.holidayCalendar[i][j][k] - 1000));
                            weekStr = weekStr + s;
                        }
                        else {
                            String s = String.format("  %2d", this.holidayCalendar[i][j][k]);
                            weekStr = weekStr + s;
                        }
                    }
                }
                Log.d(LOGTAG, weekStr);
            }
        }
    }
}
