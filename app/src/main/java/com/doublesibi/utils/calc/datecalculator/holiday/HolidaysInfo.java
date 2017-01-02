package com.doublesibi.utils.calc.datecalculator.holiday;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by hunajini on 2016/12/29.
 */

public class HolidaysInfo {
    private static String LOGTAG = "HOLIDAY";

    private static int SPRING_EQUINOX_DATE = 321;
    private static int AUTUMNAL_EQUINOX_DATE = 923;

    private String xmlFile;
    private int year;

    private String country;
    private ArrayList<HolidayItem> holidays;
    private ArrayList<HolidayItem> temporaryHolidays;
    private HashMap<Integer, HolidayItem> holidaysMap;
    private ArrayList<RangeDate> substitutes = null;
    private ArrayList<RangeDate> betweens = null;

    private int[][][] holidayCalendar;

    private MyCalendar cal = null;

    public HolidaysInfo(String xmlFile, int year) {
        this.xmlFile = xmlFile;
        this.year = year;

        this.holidays = new ArrayList<>();
        this.holidaysMap = new HashMap<>();

        this.temporaryHolidays= new ArrayList<>();

        this.holidayCalendar = new int[12][6][7];

        this.cal = new MyCalendar();

        this.setHolidayYear();
    }

    public ArrayList<HolidayItem> getHolidays() {
        return holidays;
    }

    public HashMap<Integer, HolidayItem> getHolidaysMap() {
        return holidaysMap;
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

    public Document readXml() {
        File file = new File(this.xmlFile);

        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }

        Document document = null;
        try {
            document = documentBuilder.parse(file);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
            return null;
        }

        return document;
    }

    public ArrayList<HolidayItem> getPreference() {
        ArrayList<HolidayItem> holidaysInfo = new ArrayList<>();

        Log.d(LOGTAG, "Start..!! - read xml..");
        Document document = readXml();
        Element root = document.getDocumentElement();
        NodeList rootChildren = root.getChildNodes();

        this.country = root.getAttribute("country");

        for (int i = 0; i < rootChildren.getLength(); i++) {
            Node node = rootChildren.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element)node;
                if (element.getNodeName().equals("holiday")) {
                    HolidayItem holidayItem = new HolidayItem();
                    holidayItem.name = element.getAttribute("name");

                    NodeList nodeList = node.getChildNodes();
                    for (int j = 0; j < nodeList.getLength(); j++) {
                        Node subNode = nodeList.item(j);

                        if (subNode.getNodeName().equals("date")) {
                            holidayItem.md = Integer.parseInt(subNode.getTextContent());
                        }
                        else if (subNode.getNodeName().equals("startDate")) {
                            holidayItem.startDate = Integer.parseInt(subNode.getTextContent());
                        }
                        else if (subNode.getNodeName().equals("endDate")) {
                            holidayItem.endDate = Integer.parseInt(subNode.getTextContent());
                        }
                        else if (subNode.getNodeName().equals("substitute")) {
                            if (subNode.getTextContent() != null) {
                                holidayItem.substitute = Boolean.parseBoolean(subNode.getTextContent());
                            }
                        }
                        else if (subNode.getNodeName().equals("engName")) {
                            if (subNode.getTextContent() != null)
                                holidayItem.engName = subNode.getTextContent();
                        }
                        else if (subNode.getNodeName().equals("monthOfYear")) {
                            holidayItem.monthOfYear= Integer.parseInt(subNode.getTextContent());
                        }
                        else if (subNode.getNodeName().equals("weekOfMonth")) {
                            holidayItem.weekOfMonth= Integer.parseInt(subNode.getTextContent());
                        }
                        else if (subNode.getNodeName().equals("dayOfWeek")) {
                            holidayItem.dayOfWeek= Integer.parseInt(subNode.getTextContent());
                        }
                        else if (subNode.getNodeName().equals("specialFunction")) {
                            if (subNode.getTextContent() != null)
                                holidayItem.extendFunc = subNode.getTextContent();
                        } else {
                            // nothing.
                        }
                    }

                    holidaysInfo.add(holidayItem);
                } else if (element.getNodeName().equals("substitutes")) {
                    if (this.substitutes == null)
                        this.substitutes = new ArrayList<>();

                    NodeList nodeList = element.getChildNodes();
                    for (int j = 0; j < nodeList.getLength(); j++) {
                        Node subNode = nodeList.item(j);
                        if (subNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element subElement = (Element)subNode;
                            if (subNode.getNodeName().equals("substitute")) {
                                RangeDate rangeDate = new RangeDate();
                                NodeList subNodeList =subElement.getChildNodes();
                                for (int k = 0; k < subNodeList.getLength(); k++) {
                                    Node sSubNode = subNodeList.item(k);
                                    if (sSubNode.getNodeName().equals("startDate")) {
                                        rangeDate.startDate = Integer.parseInt(sSubNode.getTextContent());
                                    } else if (sSubNode.getNodeName().equals("endDate")) {
                                        rangeDate.endDate = Integer.parseInt(sSubNode.getTextContent());
                                    } else {
                                        // nothing.
                                    }
                                }
                                this.substitutes.add(rangeDate);
                            }
                        }
                    }
                } else if (element.getNodeName().equals("betweens")) {
                    if (this.betweens == null)
                        this.betweens = new ArrayList<>();

                    NodeList nodeList = element.getChildNodes();
                    for (int j = 0; j < nodeList.getLength(); j++) {
                        Node subNode = nodeList.item(j);
                        if (subNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element subElement = (Element) subNode;
                            if (subNode.getNodeName().equals("between")) {
                                RangeDate rangeDate = new RangeDate();
                                NodeList subNodeList = subElement.getChildNodes();
                                for (int k = 0; k < subNodeList.getLength(); k++) {
                                    Node sSubNode = subNodeList.item(k);
                                    if (sSubNode.getNodeName().equals("startDate")) {
                                        rangeDate.startDate = Integer.parseInt(sSubNode.getTextContent());
                                    } else if (sSubNode.getNodeName().equals("endDate")) {
                                        rangeDate.endDate = Integer.parseInt(sSubNode.getTextContent());
                                    } else {
                                        // nothing.
                                    }
                                }
                                this.betweens.add(rangeDate);
                            }
                        }
                    }
                } else if (element.getNodeName().equals("temporary")) {
                    NodeList nodeList = element.getChildNodes();
                    for (int j = 0; j < nodeList.getLength(); j++) {
                        Node subNode = nodeList.item(j);
                        if (subNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element subElement = (Element) subNode;
                            if (subNode.getNodeName().equals("dates")) {

                                NodeList subNodeList = subElement.getChildNodes();
                                for (int k = 0; k < subNodeList.getLength(); k++) {
                                    Node sSubNode = subNodeList.item(k);
                                    if (sSubNode.getNodeType() == Node.ELEMENT_NODE) {
                                        HolidayItem holidayItemTemp = new HolidayItem();

                                        Element sSubElement = (Element) sSubNode;
                                        holidayItemTemp.name = sSubElement.getAttribute("name");
                                        holidayItemTemp.ymd = Integer.parseInt(sSubNode.getTextContent().trim());
                                        holidayItemTemp.md = holidayItemTemp.ymd % 10000;
                                        //Log.d(LOGTAG, "step 9, " + sSubElement.getAttribute("name") + ":" + sSubNode.getNodeName() + ":" + sSubNode.getTextContent());

                                        temporaryHolidays.add(holidayItemTemp);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (this.country.equals("Japan")) {
            for (HolidayItem item: holidaysInfo) {
                item.substitute = true;
            }
        }


        // Debug log
//        Log.d(LOGTAG, "\tBase information of holiday in " + this.country + ".");
//        Log.d(LOGTAG, "\t\tHoliday information");
//        for (HolidayItem item: holidaysInfo) {
//            Log.d(LOGTAG, "\t\t" + item.toString());
//        }
//
//        if (this.substitutes != null && this.substitutes.size() > 0 ) {
//            Log.d(LOGTAG, "\t\tSubstitute information");
//            for (RangeDate item : this.substitutes) {
//                Log.d(LOGTAG, "\t\t\t" + item.startDate + " ~ " + item.endDate);
//            }
//        }
//        if (this.betweens != null && this.betweens.size() > 0 ) {
//            Log.d(LOGTAG, "\t\tBetween information");
//            for (RangeDate item : this.betweens) {
//                Log.d(LOGTAG, "\t\t\t" + item.startDate + " ~ " + item.endDate);
//            }
//        }

        return holidaysInfo;
    }

    public boolean setHolidayYear() {
        if (this.year > 0) {
            return setHolidayYear(this.year);
        }
        return false;
    }

    public boolean setHolidayYear(int year) {
        ArrayList<HolidayItem> holidaysInfo = getPreference();

        for (HolidayItem item: holidaysInfo) {

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
                        int tmp = LunarSolar.getSolar(year, 1, 1);
                        this.cal.setCalendar(year, (tmp % 10000 / 100), tmp % 100);
                        this.cal.add(Calendar.DATE, -1);
                        item.ymd = this.cal.getCurrentYMD();
                    } else if (item.extendFunc.equals("LunarFirstday")) {
                        item.ymd = LunarSolar.getSolar(year, 1, 1);
                    } else if (item.extendFunc.equals("LunarFirstNextday")) {
                        item.ymd = LunarSolar.getSolar(year, 1, 2);
                    } else if (item.extendFunc.equals("BuddhaBirth")) {
                        item.ymd = LunarSolar.getSolar(year, 4, 8);
                    } else if (item.extendFunc.equals("SuperMoonPrevday")) {
                        item.ymd = LunarSolar.getSolar(year, 8, 14);
                    } else if (item.extendFunc.equals("SuperMoonday")) {
                        item.ymd = LunarSolar.getSolar(year, 8, 15);
                    } else if (item.extendFunc.equals("SuperMoonNextday")) {
                        item.ymd = LunarSolar.getSolar(year, 8, 16);
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
        System.out.println(year + "'s holiday");
        for (HolidayItem item : this.holidays) {
            System.out.println("\t\t" + item.toString());
        }

        return false;
    }

    public ArrayList<HolidayItem> getSubstituteDay() {
        ArrayList<HolidayItem> retSubstitutesDay = new ArrayList<>();

        if (this.country.equals("Japan")) {
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
        return this.holidayCalendar[month];
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
