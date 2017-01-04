package com.doublesibi.utils.calc.datecalculator;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doublesibi.utils.calc.datecalculator.holiday.HolidaysInfo;
import com.doublesibi.utils.calc.datecalculator.holiday.MyCalendar;

import org.xmlpull.v1.XmlPullParser;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThismonthFragment extends Fragment {
    private final String LOGTAG = "DayCalc";
    private MyCalendar myCalendar;

    TextView[][] textViews;

    public ThismonthFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_thismonth, container, false);

        setTextViews(view);
        setDays();

        return view;
    }

    public void setDays() {

        if (myCalendar == null) {
            myCalendar = new MyCalendar();
        }

        XmlPullParser xmlPullParser = getResources().getXml(R.xml.holidayinfo_kr);
        HolidaysInfo holidaysInfo = new HolidaysInfo();
        holidaysInfo.setCountry("Japan");

        int[][] prevMonthDays = new int[6][7];
        int[][] currMonthDays = new int[6][7];
        int[][] nextMonthDays = new int[6][7];

        int prevYM = 0, nextYM = 0;
        int currYM = myCalendar.getYearMonth(0);
        if (currYM/100 == 1 || currYM/100 == 12) {
            prevYM = myCalendar.getYearMonth(-1);
            holidaysInfo.setHolidayYear(xmlPullParser, prevYM/10000);
            holidaysInfo.setHolidayCalendar();
            prevMonthDays = holidaysInfo.getHolidayCalendar(prevYM%100);

            holidaysInfo.setHolidayYear(xmlPullParser, currYM/10000);
            holidaysInfo.setHolidayCalendar();
            currMonthDays = holidaysInfo.getHolidayCalendar(currYM%100);

            holidaysInfo.setHolidayYear(xmlPullParser, nextYM/10000);
            holidaysInfo.setHolidayCalendar();
            nextMonthDays = holidaysInfo.getHolidayCalendar(nextYM%100);
        } else {
            prevYM = myCalendar.getYearMonth(-1);
            nextYM = myCalendar.getYearMonth(1);
            holidaysInfo.setHolidayYear(xmlPullParser, currYM/10000);
            holidaysInfo.setHolidayCalendar();

            prevMonthDays = holidaysInfo.getHolidayCalendar(prevYM%100);
            currMonthDays = holidaysInfo.getHolidayCalendar(currYM%100);
            nextMonthDays = holidaysInfo.getHolidayCalendar(nextYM%100);
        }

        for (int i = 1; i < 6; i++) {
            for (int j = 1; j < 7; j++) {
                if (currMonthDays[i-1][j-1] == 0)
                    continue;
                Log.d(LOGTAG, "i:" + i + ", j:" + j + "->" + currMonthDays[i][j]);
                textViews[i][j].setText(currMonthDays[i-1][j-1]);
                if (i == 0)
                    textViews[i][j].setTextColor(Color.RED);
            }
        }
    }

    public void setTextViews(View view) {
        TextView[][] textViews = new TextView[6][7];

        textViews[0][0] = (TextView) view.findViewById(R.id.week_0).findViewById(R.id.day_1);
        textViews[0][1] = (TextView) view.findViewById(R.id.week_0).findViewById(R.id.day_2);
        textViews[0][2] = (TextView) view.findViewById(R.id.week_0).findViewById(R.id.day_3);
        textViews[0][3] = (TextView) view.findViewById(R.id.week_0).findViewById(R.id.day_4);
        textViews[0][4] = (TextView) view.findViewById(R.id.week_0).findViewById(R.id.day_5);
        textViews[0][5] = (TextView) view.findViewById(R.id.week_0).findViewById(R.id.day_6);
        textViews[0][6] = (TextView) view.findViewById(R.id.week_0).findViewById(R.id.day_7);

        textViews[1][0] = (TextView) view.findViewById(R.id.week_1).findViewById(R.id.day_1);
        textViews[1][1] = (TextView) view.findViewById(R.id.week_1).findViewById(R.id.day_2);
        textViews[1][2] = (TextView) view.findViewById(R.id.week_1).findViewById(R.id.day_3);
        textViews[1][3] = (TextView) view.findViewById(R.id.week_1).findViewById(R.id.day_4);
        textViews[1][4] = (TextView) view.findViewById(R.id.week_1).findViewById(R.id.day_5);
        textViews[1][5] = (TextView) view.findViewById(R.id.week_1).findViewById(R.id.day_6);
        textViews[1][6] = (TextView) view.findViewById(R.id.week_1).findViewById(R.id.day_7);

        textViews[2][0] = (TextView) view.findViewById(R.id.week_2).findViewById(R.id.day_1);
        textViews[2][1] = (TextView) view.findViewById(R.id.week_2).findViewById(R.id.day_2);
        textViews[2][2] = (TextView) view.findViewById(R.id.week_2).findViewById(R.id.day_3);
        textViews[2][3] = (TextView) view.findViewById(R.id.week_2).findViewById(R.id.day_4);
        textViews[2][4] = (TextView) view.findViewById(R.id.week_2).findViewById(R.id.day_5);
        textViews[2][5] = (TextView) view.findViewById(R.id.week_2).findViewById(R.id.day_6);
        textViews[2][6] = (TextView) view.findViewById(R.id.week_2).findViewById(R.id.day_7);

        textViews[3][0] = (TextView) view.findViewById(R.id.week_3).findViewById(R.id.day_1);
        textViews[3][1] = (TextView) view.findViewById(R.id.week_3).findViewById(R.id.day_2);
        textViews[3][2] = (TextView) view.findViewById(R.id.week_3).findViewById(R.id.day_3);
        textViews[3][3] = (TextView) view.findViewById(R.id.week_3).findViewById(R.id.day_4);
        textViews[3][4] = (TextView) view.findViewById(R.id.week_3).findViewById(R.id.day_5);
        textViews[3][5] = (TextView) view.findViewById(R.id.week_3).findViewById(R.id.day_6);
        textViews[3][6] = (TextView) view.findViewById(R.id.week_3).findViewById(R.id.day_7);

        textViews[4][0] = (TextView) view.findViewById(R.id.week_4).findViewById(R.id.day_1);
        textViews[4][1] = (TextView) view.findViewById(R.id.week_4).findViewById(R.id.day_2);
        textViews[4][2] = (TextView) view.findViewById(R.id.week_4).findViewById(R.id.day_3);
        textViews[4][3] = (TextView) view.findViewById(R.id.week_4).findViewById(R.id.day_4);
        textViews[4][4] = (TextView) view.findViewById(R.id.week_4).findViewById(R.id.day_5);
        textViews[4][5] = (TextView) view.findViewById(R.id.week_4).findViewById(R.id.day_6);
        textViews[4][6] = (TextView) view.findViewById(R.id.week_4).findViewById(R.id.day_7);

        textViews[5][0] = (TextView) view.findViewById(R.id.week_5).findViewById(R.id.day_1);
        textViews[5][1] = (TextView) view.findViewById(R.id.week_5).findViewById(R.id.day_2);
        textViews[5][2] = (TextView) view.findViewById(R.id.week_5).findViewById(R.id.day_3);
        textViews[5][3] = (TextView) view.findViewById(R.id.week_5).findViewById(R.id.day_4);
        textViews[5][4] = (TextView) view.findViewById(R.id.week_5).findViewById(R.id.day_5);
        textViews[5][5] = (TextView) view.findViewById(R.id.week_5).findViewById(R.id.day_6);
        textViews[5][6] = (TextView) view.findViewById(R.id.week_5).findViewById(R.id.day_7);
    }

}
