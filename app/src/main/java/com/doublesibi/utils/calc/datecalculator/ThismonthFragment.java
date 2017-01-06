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
import android.widget.Toast;

import com.doublesibi.utils.calc.datecalculator.holiday.HolidayItem;
import com.doublesibi.utils.calc.datecalculator.holiday.HolidaysInfo;
import com.doublesibi.utils.calc.datecalculator.holiday.MyCalendar;

import org.xmlpull.v1.XmlPullParser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThismonthFragment extends Fragment implements View.OnClickListener {
    private final String LOGTAG = "DayCalc";
    private MyCalendar myCalendar;

    TextView tvYear, tvMonth, tvJpName, tvJpYear;
    private TextView[][] textViews;


    public ThismonthFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_thismonth, container, false);

        if (myCalendar == null) {
            myCalendar = new MyCalendar();
        }


        setTextViews(view);
        setDays(view);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.this_year_solar:
                Toast.makeText(getContext(), "click year", Toast.LENGTH_SHORT).show();
                break;
            case R.id.this_month_solar:
                Toast.makeText(getContext(), "click month", Toast.LENGTH_SHORT).show();
                break;
            case R.id.this_year_jpname:
                Toast.makeText(getContext(), "click japan year name", Toast.LENGTH_SHORT).show();
                break;
            case R.id.this_year_japanes:
                Toast.makeText(getContext(), "click japan year", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void setYearMonth(View view, int currYM) {
        tvYear.setText("" + currYM/100);
        tvMonth.setText(" " + currYM%100);
        tvJpName.setText("平成 ");
        tvJpYear.setText("" + 29);
    }

    public void setDays(View view) {

        XmlPullParser xmlPullParser = getResources().getXml(R.xml.holidayinfo_jp);

        HolidaysInfo holidaysInfo = new HolidaysInfo();
        holidaysInfo.setCountry("Japan");
        holidaysInfo.setBaseHolidaysInfo(xmlPullParser);

        int[][] prevMonthDays = new int[6][7];
        int[][] currMonthDays = new int[6][7];
        int[][] nextMonthDays = new int[6][7];

        int prevYM = 0, nextYM = 0;

        int currYM = myCalendar.getYearMonth(0);
        if (currYM%100 == 1) {
            prevYM = myCalendar.getYearMonth(-1);
            Log.d(LOGTAG,"----->prev year month :" + prevYM);
            holidaysInfo.clearHolidays();
            holidaysInfo.setHolidayYear(prevYM/100);
            holidaysInfo.setHolidayCalendar();
            prevMonthDays = holidaysInfo.getHolidayCalendar(prevYM%100);

            Log.d(LOGTAG,"----->curr year month :" + currYM);
            holidaysInfo.clearHolidays();
            holidaysInfo.setHolidayYear(currYM/100);
            holidaysInfo.setHolidayCalendar();
            currMonthDays = holidaysInfo.getHolidayCalendar(currYM%100);

            nextYM = myCalendar.getYearMonth(1);
            nextMonthDays = holidaysInfo.getHolidayCalendar(nextYM%100);

            setYearMonth(view, currYM);
        } else if (currYM%100 == 12) {
            nextYM = myCalendar.getYearMonth(1);
            holidaysInfo.clearHolidays();
            holidaysInfo = new HolidaysInfo();
            holidaysInfo.setHolidayYear(nextYM/100);
            holidaysInfo.setHolidayCalendar();
            nextMonthDays = holidaysInfo.getHolidayCalendar(nextYM%100);

            holidaysInfo.clearHolidays();
            holidaysInfo.setHolidayYear(currYM/100);
            holidaysInfo.setHolidayCalendar();
            currMonthDays = holidaysInfo.getHolidayCalendar(currYM%100);

            prevYM = myCalendar.getYearMonth(-1);
            prevMonthDays = holidaysInfo.getHolidayCalendar(prevYM%100);

            setYearMonth(view, currYM);

        } else {
            prevYM = myCalendar.getYearMonth(-1);
            nextYM = myCalendar.getYearMonth(1);

            holidaysInfo.clearHolidays();
            holidaysInfo.setHolidayYear(currYM/100);
            holidaysInfo.setHolidayCalendar();
            currMonthDays = holidaysInfo.getHolidayCalendar(currYM%100);

            prevMonthDays = holidaysInfo.getHolidayCalendar(prevYM%100);
            nextMonthDays = holidaysInfo.getHolidayCalendar(nextYM%100);

            setYearMonth(view, currYM);
        }

        for (int i = 1; i < 6; i++) {
            for (int j = 0; j < 7; j++) {

                //Log.d(LOGTAG, "i:" + (i-1) + ", j:" + j + "->" + currMonthDays[i-1][j]);


                if (currMonthDays[i-1][j] != 0) {
                    if (currMonthDays[i-1][j] > 100) {
                        textViews[i][j].setText("" + currMonthDays[i-1][j]%100);
                        if (j != 0 && j != 6) {
                            textViews[i][j].setTextColor(Color.YELLOW);
                        } else {
                            if (j == 0) {
                                textViews[i][j].setTextColor(Color.RED);
                            } else if (j == 6) {
                                textViews[i][j].setTextColor(Color.YELLOW);
                            }
                        }
                    } else {
                        textViews[i][j].setText("" + currMonthDays[i-1][j]);
                        if (j == 0) {
                            textViews[i][j].setTextColor(Color.RED);
                        } else if (j == 6) {
                            textViews[i][j].setTextColor(Color.BLUE);
                        }
                    }
                }
            }
        }
    }

    public void setTextViews(View view) {
        tvYear = ((TextView)view.findViewById(R.id.this_year_solar));
        tvMonth = ((TextView)view.findViewById(R.id.this_month_solar));
        tvJpName = ((TextView)view.findViewById(R.id.this_year_jpname));
        tvJpYear = ((TextView)view.findViewById(R.id.this_year_japanes));

        tvYear.setOnClickListener(this);
        tvMonth.setOnClickListener(this);
        tvJpName.setOnClickListener(this);
        tvJpYear.setOnClickListener(this);

        textViews = new TextView[6][7];

        textViews[0][0] =(TextView)(view.findViewById(R.id.week_0).findViewById(R.id.day_1));
        textViews[0][0] = (TextView) (view.findViewById(R.id.week_0)).findViewById(R.id.day_1);
        textViews[0][1] = (TextView) (view.findViewById(R.id.week_0)).findViewById(R.id.day_2);
        textViews[0][2] = (TextView) (view.findViewById(R.id.week_0)).findViewById(R.id.day_3);
        textViews[0][3] = (TextView) (view.findViewById(R.id.week_0)).findViewById(R.id.day_4);
        textViews[0][4] = (TextView) (view.findViewById(R.id.week_0)).findViewById(R.id.day_5);
        textViews[0][5] = (TextView) (view.findViewById(R.id.week_0)).findViewById(R.id.day_6);
        textViews[0][6] = (TextView) (view.findViewById(R.id.week_0)).findViewById(R.id.day_7);

        textViews[1][0] = (TextView) (view.findViewById(R.id.week_1)).findViewById(R.id.day_1);
        textViews[1][1] = (TextView) (view.findViewById(R.id.week_1)).findViewById(R.id.day_2);
        textViews[1][2] = (TextView) (view.findViewById(R.id.week_1)).findViewById(R.id.day_3);
        textViews[1][3] = (TextView) (view.findViewById(R.id.week_1)).findViewById(R.id.day_4);
        textViews[1][4] = (TextView) (view.findViewById(R.id.week_1)).findViewById(R.id.day_5);
        textViews[1][5] = (TextView) (view.findViewById(R.id.week_1)).findViewById(R.id.day_6);
        textViews[1][6] = (TextView) (view.findViewById(R.id.week_1)).findViewById(R.id.day_7);

        textViews[2][0] = (TextView) (view.findViewById(R.id.week_2)).findViewById(R.id.day_1);
        textViews[2][1] = (TextView) (view.findViewById(R.id.week_2)).findViewById(R.id.day_2);
        textViews[2][2] = (TextView) (view.findViewById(R.id.week_2)).findViewById(R.id.day_3);
        textViews[2][3] = (TextView) (view.findViewById(R.id.week_2)).findViewById(R.id.day_4);
        textViews[2][4] = (TextView) (view.findViewById(R.id.week_2)).findViewById(R.id.day_5);
        textViews[2][5] = (TextView) (view.findViewById(R.id.week_2)).findViewById(R.id.day_6);
        textViews[2][6] = (TextView) (view.findViewById(R.id.week_2)).findViewById(R.id.day_7);

        textViews[3][0] = (TextView) (view.findViewById(R.id.week_3)).findViewById(R.id.day_1);
        textViews[3][1] = (TextView) (view.findViewById(R.id.week_3)).findViewById(R.id.day_2);
        textViews[3][2] = (TextView) (view.findViewById(R.id.week_3)).findViewById(R.id.day_3);
        textViews[3][3] = (TextView) (view.findViewById(R.id.week_3)).findViewById(R.id.day_4);
        textViews[3][4] = (TextView) (view.findViewById(R.id.week_3)).findViewById(R.id.day_5);
        textViews[3][5] = (TextView) (view.findViewById(R.id.week_3)).findViewById(R.id.day_6);
        textViews[3][6] = (TextView) (view.findViewById(R.id.week_3)).findViewById(R.id.day_7);

        textViews[4][0] = (TextView) (view.findViewById(R.id.week_4)).findViewById(R.id.day_1);
        textViews[4][1] = (TextView) (view.findViewById(R.id.week_4)).findViewById(R.id.day_2);
        textViews[4][2] = (TextView) (view.findViewById(R.id.week_4)).findViewById(R.id.day_3);
        textViews[4][3] = (TextView) (view.findViewById(R.id.week_4)).findViewById(R.id.day_4);
        textViews[4][4] = (TextView) (view.findViewById(R.id.week_4)).findViewById(R.id.day_5);
        textViews[4][5] = (TextView) (view.findViewById(R.id.week_4)).findViewById(R.id.day_6);
        textViews[4][6] = (TextView) (view.findViewById(R.id.week_4)).findViewById(R.id.day_7);

        textViews[5][0] = (TextView) (view.findViewById(R.id.week_5)).findViewById(R.id.day_1);
        textViews[5][1] = (TextView) (view.findViewById(R.id.week_5)).findViewById(R.id.day_2);
        textViews[5][2] = (TextView) (view.findViewById(R.id.week_5)).findViewById(R.id.day_3);
        textViews[5][3] = (TextView) (view.findViewById(R.id.week_5)).findViewById(R.id.day_4);
        textViews[5][4] = (TextView) (view.findViewById(R.id.week_5)).findViewById(R.id.day_5);
        textViews[5][5] = (TextView) (view.findViewById(R.id.week_5)).findViewById(R.id.day_6);
        textViews[5][6] = (TextView) (view.findViewById(R.id.week_5)).findViewById(R.id.day_7);
    }
}
