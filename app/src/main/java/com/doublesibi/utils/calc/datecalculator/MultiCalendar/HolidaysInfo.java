package com.doublesibi.utils.calc.datecalculator.MultiCalendar;

import com.doublesibi.utils.calc.datecalculator.util.MyCalendar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by hunajini on 2016/12/29.
 */

public class HolidaysInfo {
    private String xmlFile;
    private int year;

    private ArrayList<HolidayItem> holidays;
    private HashMap<Integer, HolidayItem> holidaysMap;

    public HolidaysInfo() {
    }

    public HolidaysInfo(String xmlFile, int year) {
        this.xmlFile = xmlFile;
        this.year = year;

        this.holidays = new ArrayList<>();
        this.holidaysMap = new HashMap<>();
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

        Document document = readXml();
        Element root = document.getDocumentElement();
        NodeList rootChildren = root.getChildNodes();

        // TODO
        for (int i = 0; i < rootChildren.getLength(); i++) {
            Node node = rootChildren.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element)node;
                if (element.getNodeName().equals("holiday")) {
                    //TODO
                } else if (element.getNodeName().equals("substitutes")) {
                    //TODO
                } else if (element.getNodeName().equals("betweens")) {
                    //TODO
                } else if (element.getNodeName().equals("temporary")) {
                    //TODO
                }
            }
        }

        return holidaysInfo;
    }

    public boolean setHolidayYear() {
        if (this.year > 0) {
            return setHolidayYear(this.year);
        }
        return false;
    }

    public boolean setHolidayYear(int year) {
        // TODO
        ArrayList<HolidayItem> holidaysInfo = getPreference();
        MyCalendar c = new MyCalendar();
        for (HolidayItem item: holidaysInfo) {

            if(item.startyear == 0 && item.endyear == 0) {
                continue;
            }

            if(year < item.startyear / 10000
                    || (item.endyear > 0 && year > item.endyear/ 10000)) {
                continue;
            }

            // 特別な計算（旧暦の日付から等の計算）
            if (item.extendFunc != null && item.extendFunc.length() > 0) {
                //TODO

            } else if (item.monthofyear > 0 && item.weekofmonth > 0
                    && item.dayofweek > 0) {
                //TODO
            }
        }

        // 振替休日の計算（韓国、日本、米国）

        // 前日、翌日が祭日の場合（日本）

        // 臨時休日（韓国、または TODO：ほかの国も確認が要る）

        return false;
    }
}
