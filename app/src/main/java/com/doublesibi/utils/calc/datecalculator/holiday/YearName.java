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

public class YearName {
    private static String LOGTAG = "DayCalc";

    private String xmlFile;
    private String country;
    private ArrayList<RangeDate> yearNameList = null;

    private MyCalendar cal = null;

    public YearName() {
        this.yearNameList = new ArrayList<>();
        this.cal = new MyCalendar();
    }

    public ArrayList<RangeDate> getYearNameList() {
        return yearNameList;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setyearNameList(XmlPullParser xpp) {

        int itemType = 0;

        this.yearNameList = new ArrayList<>();

        Log.d(LOGTAG, "xml parsing start....");
        try {
            RangeDate rangeDate = null;

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                final String name = xpp.getName();
                switch(eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("eras".equals(name)) {
                        } else if ("era".equals(name)) {
                        } else {
                            if ("item".equals(name)) {
                               rangeDate = new RangeDate();
                            } else if ("startDate".equals(name)) {
                                rangeDate.startDate = Integer.parseInt(xpp.nextText().trim());
                            } else if ("endDate".equals(name)) {
                                rangeDate.endDate = Integer.parseInt(xpp.nextText().trim());
                            } else if ("name".equals(name)) {
                                rangeDate.name = xpp.nextText().trim();
                            } else if ("pronunciation".equals(name)) {
                                rangeDate.pronunciation = xpp.nextText().trim();
                            } else {
                                Log.d(LOGTAG, "etc:(tagname)" + name);
                            }

                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("item".equals(name)) {
                            this.yearNameList.add(rangeDate);
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
            if (this.yearNameList.size() > 0) {
                Collections.sort(this.yearNameList, new YearNameComparator());
            }
            // 処理なし
            // Debug log
//            if (this.yearNameList != null && this.yearNameList.size() > 0 ) {
//                Log.d(LOGTAG, "\t\tera information");
//                for (RangeDate item : this.yearNameList) {
//                    Log.d(LOGTAG, "\t\t\t" + item.toString());
//                }
//            }
        }
    }
}
