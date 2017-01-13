package com.doublesibi.utils.calc.datecalculator;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doublesibi.utils.calc.datecalculator.holiday.HolidayItem;
import com.doublesibi.utils.calc.datecalculator.holiday.HolidaysInfo;
import com.doublesibi.utils.calc.datecalculator.holiday.MyCalendar;

import org.xmlpull.v1.XmlPullParser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThismonthFragment extends Fragment implements View.OnClickListener {
    private final String LOGTAG = "DayCalc";

    private MyCalendar myCalendar;
    private XmlPullParser xmlPullParser;
    private HolidaysInfo holidaysInfo;

    private TextView tvYear, tvMonth, tvJpName, tvJpYear;
    private TextView[][] textViews;


    public ThismonthFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_thismonth, container, false);

        setTextViews(view);

        if (myCalendar == null) {
            myCalendar = new MyCalendar();
        }

        setHoliday("Japan");
        setDays(myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH)+1);

        return view;
    }

    @Override
    public void onClick(View v) {
        final int l_year = Integer.parseInt(this.tvYear.getText().toString().trim());
        final int l_month = Integer.parseInt(this.tvMonth.getText().toString().trim());
        switch(v.getId()) {
            case R.id.this_year_solar:
            case R.id.this_month_solar:

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                try {
                                    if (l_year != year || l_month != monthOfYear - 1) {
                                        setDays(year, monthOfYear + 1);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, l_year, l_month - 1, 1);

                datePickerDialog.getDatePicker().setCalendarViewShown(false);
                datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                datePickerDialog.show();
                break;
            case R.id.this_year_jpname:
                Toast.makeText(getContext(), "click japan year name", Toast.LENGTH_SHORT).show();
                break;
            case R.id.this_year_japanes:
                Toast.makeText(getContext(), "click japan year", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void setYearMonth(int currYM) {
        tvYear.setText("" + currYM/100);
        tvMonth.setText(" " + currYM%100);
        tvJpName.setText("平成 ");
        tvJpYear.setText("" + 29);
    }

    private void setHoliday(String country) {
        xmlPullParser = getResources().getXml(R.xml.holidayinfo_jp);
        holidaysInfo = new HolidaysInfo();
        holidaysInfo.setCountry(country);
        holidaysInfo.setBaseHolidaysInfo(xmlPullParser);
    }

    private void setDays(int year, int month) {
        int[][] prevMonthDays = new int[6][7];
        int[][] currMonthDays = new int[6][7];
        int[][] nextMonthDays = new int[6][7];

        int prevYM = 0, nextYM = 0;

        myCalendar.setCalendar(year, month, 1);
        int currYM = myCalendar.getCurrentYMD()/100;
        Log.d(LOGTAG, "setDays ->  " + "year:" + year + ", month:" + month);
        Log.d(LOGTAG, "            currYM:" + currYM);

        this.tvYear.setText("" + year);
        this.tvMonth.setText("" + month);

        if (currYM%100 == 1) {
            myCalendar.add(Calendar.MONTH, -1);
            prevYM = myCalendar.getCurrentYMD() / 100;
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

            setYearMonth(currYM);
        } else if (currYM%100 == 12) {
            myCalendar.add(Calendar.MONTH, 1);
            nextYM = myCalendar.getCurrentYMD() / 100;

            holidaysInfo.clearHolidays();
            holidaysInfo.setHolidayYear(nextYM/100);
            holidaysInfo.setHolidayCalendar();
            nextMonthDays = holidaysInfo.getHolidayCalendar(nextYM%100);

            holidaysInfo.clearHolidays();
            holidaysInfo.setHolidayYear(currYM/100);
            holidaysInfo.setHolidayCalendar();
            currMonthDays = holidaysInfo.getHolidayCalendar(currYM%100);

            prevYM = myCalendar.getYearMonth(-1);
            prevMonthDays = holidaysInfo.getHolidayCalendar(prevYM%100);

            setYearMonth(currYM);

        } else {
            prevYM = currYM - 1;
            nextYM = currYM + 1;

            holidaysInfo.clearHolidays();
            holidaysInfo.setHolidayYear(currYM/100);
            holidaysInfo.setHolidayCalendar();
            currMonthDays = holidaysInfo.getHolidayCalendar(currYM%100);

            prevMonthDays = holidaysInfo.getHolidayCalendar(prevYM%100);
            nextMonthDays = holidaysInfo.getHolidayCalendar(nextYM%100);

            setYearMonth(currYM);
        }

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                textViews[i][j].setText("");

                if (currMonthDays[i][j] != 0) {
                    if (currMonthDays[i][j] > 100) {
                        textViews[i][j].setText("" + currMonthDays[i][j]%100);
                        if (j == 0) {
                            textViews[i][j].setTextColor(Color.RED);
                        } else if (j == 6) {
                            textViews[i][j].setTextColor(Color.YELLOW);
                        } else {
                            textViews[i][j].setTextColor(Color.YELLOW);
                        }
                    } else {
                        textViews[i][j].setText("" + currMonthDays[i][j]);
                        if (j == 0) {
                            textViews[i][j].setTextColor(Color.RED);
                        } else if (j == 6) {
                            textViews[i][j].setTextColor(Color.BLUE);
                        } else {
                            textViews[i][j].setTextColor(Color.BLACK);
                        }
                    }
                }
            }
        }
    }

    private void setTextViews(View view) {
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
