package com.doublesibi.utils.calc.datecalculator;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.doublesibi.utils.calc.datecalculator.common.CalcDurationDate;
import com.doublesibi.utils.calc.datecalculator.common.Constants;
import com.doublesibi.utils.calc.datecalculator.hist.DurationHistItem;
import com.doublesibi.utils.calc.datecalculator.holiday.HolidayItem;
import com.doublesibi.utils.calc.datecalculator.holiday.HolidayListItem;
import com.doublesibi.utils.calc.datecalculator.holiday.HolidaysInfo;
import com.doublesibi.utils.calc.datecalculator.holiday.MyCalendar;
import com.doublesibi.utils.calc.datecalculator.holiday.RangeDate;
import com.doublesibi.utils.calc.datecalculator.holiday.YearName;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThismonthFragment extends Fragment implements View.OnClickListener {
    private final String LOGTAG = "DayCalc";

    private MyCalendar myCalendar;
    private XmlPullParser xmlPullParser;
    private HolidaysInfo holidaysInfo;
    private YearName yearName;
    private ArrayList<RangeDate> yearNameList;

    private TextView tvYear, tvMonth, tvJpName, tvJpYear;
    private TextView[][] textViews;

    ArrayList<HolidayListItem> holidayListItems;

    String[] constantStr = {"今日", " 日後"};

    public ThismonthFragment() {
        if (holidayListItems == null) {
            holidayListItems = new ArrayList<>();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
        menu.findItem(R.id.action_history).setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_thismonth, container, false);

        setTextViews(view);

        if (myCalendar == null) {
            myCalendar = new MyCalendar();
        }

        if (yearName == null) {
            yearName = new YearName();
            yearName.setCountry("Japan");
            yearName.setyearNameList(getResources().getXml(R.xml.japan_era));
            yearNameList = yearName.getYearNameList();
        }

        setHoliday("Japan");
        setDays(myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH)+1);

        ListView listView = (ListView)view.findViewById(R.id.listViewHoliday);
        HolidayListAdaptor holidayListAdaptor = new HolidayListAdaptor(getActivity());
        getHolidayList("Japan");
        Log.d(LOGTAG,"h list item count : " + holidayListItems.size());
        listView.setAdapter(holidayListAdaptor);

        return view;
    }

    @Override
    public void onClick(View v) {
        int tempYM;
        final int l_year = Integer.parseInt(this.tvYear.getText().toString().trim());
        final int l_month = Integer.parseInt(this.tvMonth.getText().toString().trim());
        switch(v.getId()) {
            case R.id.this_year_solar:
            case R.id.this_month_solar:
                showYearMonthDialog(l_year, l_month);
                break;

            case R.id.btnPrevMonth:
                myCalendar.setCalendar(l_year, l_month, 1);
                tempYM = myCalendar.getCurrentPrevMonth(-1);
                setDays(tempYM/100, tempYM%100);
                break;

            case R.id.btnNextMonth:
                myCalendar.setCalendar(l_year, l_month, 1);
                tempYM = myCalendar.getCurrentPrevMonth(1);
                setDays(tempYM/100, tempYM%100);
                break;

            case R.id.this_year_jpname:
                // TODO : spinner or dialogで
                //Toast.makeText(getContext(), "click japan year name", Toast.LENGTH_SHORT).show();
                break;

            case R.id.this_year_japanes:
                // TODO : spinner or dialogで
                //Toast.makeText(getContext(), "click japan year", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void showYearMonthDialog(int year, int month) {
        int curY = year;
        int curM = month;
        this.tvYear.getText().toString();

        LayoutInflater dialog = LayoutInflater.from(getContext());
        final View dialogLayout = dialog.inflate(R.layout.yearmonth_picker_dialog, null);
        final Dialog myDialog = new Dialog(getContext());

        myDialog.setTitle(getActivity().getResources().getString(R.string.lb_select_yearmonth));
        myDialog.setContentView(dialogLayout);
        myDialog.show();

        final TextView selYear  = (TextView)dialogLayout.findViewById(R.id.textViewYear);
        final TextView selMonth = (TextView)dialogLayout.findViewById(R.id.textViewMonth);

        final TextView btn_upYY = (TextView)dialogLayout.findViewById(R.id.increaseYear);
        final TextView btn_upMM = (TextView)dialogLayout.findViewById(R.id.increaseMonth);
        final TextView btn_dnYY = (TextView)dialogLayout.findViewById(R.id.decreaseYear);
        final TextView btn_dnMM = (TextView)dialogLayout.findViewById(R.id.decreaseMonth);
        final Button btn_ok = (Button)dialogLayout.findViewById(R.id.btnOk);
        final Button btn_cancel = (Button)dialogLayout.findViewById(R.id.btnCancel);

        selYear.setText("" + curY);
        if (curY == 1) {
            btn_upYY.setText("" + (curY - 1));
            btn_dnYY.setText("" + 1);
        } else if (curY == 1) {
            btn_upYY.setText("" + 9999);
            btn_dnYY.setText("" + (curY + 1));
        }else {
            btn_upYY.setText("" + (curY - 1));
            btn_dnYY.setText("" + (curY + 1));
        }

        selMonth.setText("" + curM);
        if (curM == 12) {
            btn_upMM.setText("" + (curM - 1));
            btn_dnMM.setText("" + 1);
        } if (curM == 1) {
            btn_upMM.setText("" + 12);
            btn_dnMM.setText("" + (curM + 1));
        } else {
            btn_upMM.setText("" + (curM - 1));
            btn_dnMM.setText("" + (curM + 1));
        }

        btn_upYY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] retval = addNumberPicker(1, 999, selYear.getText().toString(), btn_upYY.getText().toString(), 1);
                btn_dnYY.setText("" + retval[3]);
                selYear.setText("" + retval[2]);
                btn_upYY.setText("" + retval[1]);
            }
        });

        btn_dnYY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] retval = addNumberPicker(1, 999, selYear.getText().toString(), btn_dnYY.getText().toString(), -1);
                btn_dnYY.setText("" + retval[3]);
                selYear.setText("" + retval[2]);
                btn_upYY.setText("" + retval[1]);
            }
        });

        btn_upMM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] retval = addNumberPicker(1, 12, selMonth.getText().toString(), btn_upMM.getText().toString(), 1);
                btn_dnMM.setText("" + retval[3]);
                selMonth.setText("" + retval[2]);
                btn_upMM.setText("" + retval[1]);

                if (retval[0] == -1) {
                    retval = addNumberPicker(1, 999, selYear.getText().toString(), btn_upYY.getText().toString(), 1);
                    btn_dnYY.setText("" + retval[3]);
                    selYear.setText("" + retval[2]);
                    btn_upYY.setText("" + retval[1]);
                }
            }
        });

        btn_dnMM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] retval = addNumberPicker(1, 12, selMonth.getText().toString(), btn_dnMM.getText().toString(), -1);
                btn_dnMM.setText("" + retval[3]);
                selMonth.setText("" + retval[2]);
                btn_upMM.setText("" + retval[1]);

                if (retval[0] == 1) {
                    retval = addNumberPicker(1, 999, selYear.getText().toString(), btn_dnYY.getText().toString(), -1);
                    btn_dnYY.setText("" + retval[3]);
                    selYear.setText("" + retval[2]);
                    btn_upYY.setText("" + retval[1]);
                }
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                setTvYearMonth(selYear.getText().toString().trim(), selMonth.getText().toString().trim());
                myDialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myDialog.cancel();
            }
        });
    }

    /*
    *  return :
     *  0 : 1 : max -> min, -1: min->max, 0: others
     *  1 : location of upper
     *  2 : cur(selected value)
     *  3 : location of lower
     */
    private int[] addNumberPicker(int min, int max, String curVal, String newVal, int val) {
        int[] retVal = new int[4];

        retVal[2] = Integer.parseInt(newVal);
        if (val < 0) {
            if (retVal[2] == max) {
                retVal[3] = min;
                retVal[1] = retVal[2] - 1;
            } else if (retVal[2] == min) {
                retVal[3] = retVal[2] + 1;
                retVal[1] = max;
            } else {
                retVal[3] = retVal[2] + 1;
                retVal[1] = retVal[2] - 1;
            }
        } else {
            if (retVal[2] == max) {
                retVal[3] = min;
                retVal[1] = retVal[2] - 1;
            } else if (retVal[2] == min) {
                retVal[3] = retVal[2] + 1;
                retVal[1] = max;
            } else {
                retVal[3] = retVal[2] + 1;
                retVal[1] = retVal[2] - 1;
            }
        }

        retVal[0] = 0;
        if (Integer.parseInt(curVal) == max) {
            if (retVal[2] == min) retVal[0] = 1;
        } else if (Integer.parseInt(curVal) == min) {
            if (retVal[2] == max) retVal[0] = -1;
        }

        return retVal;
    }

    private void setTvYearMonth(String year, String month) {
        setDays(Integer.parseInt(year), Integer.parseInt(month));
    }

    private void setYearMonth(int currYM) {
        tvYear.setText("" + currYM/100);
        tvMonth.setText(" " + currYM%100);

        for (RangeDate item : yearNameList) {
            if (currYM*100+1 >= item.startDate && currYM*100+1 < item.endDate) {
                tvJpName.setText(item.name.trim());
                tvJpYear.setText("" + (currYM/100 - item.startDate/10000 + 1));
            }
        }
    }

    public class HolidayListAdaptor extends BaseAdapter {
        Context context;
        LayoutInflater layoutInflater = null;

        public HolidayListAdaptor(Context context) {
            this.context = context;
            this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return holidayListItems.size();
        }

        @Override
        public Object getItem(int position) {
            return holidayListItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.holiday_listview_item, parent, false);
                holder = new ViewHolder();
                holder.t1 = (TextView) convertView.findViewById(R.id.holiday);
                holder.t2 = (TextView) convertView.findViewById(R.id.nameOfHolday);
                holder.t3 = (TextView) convertView.findViewById(R.id.remainTo);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.t1.setText(holidayListItems.get(position).getHoliday());
            holder.t2.setText(holidayListItems.get(position).getHolidayName());
            holder.t3.setText(holidayListItems.get(position).getRemainDates());

            return convertView;
        }
    }

    static class ViewHolder {
        TextView t1, t2, t3;
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

        int currYM = 0, prevYM = 0, nextYM = 0;

        myCalendar.setCalendar(year, month, 1);

        // this month
        currYM = myCalendar.getCurrentYMD() / 100;
        holidaysInfo.clearHolidays();
        holidaysInfo.setHolidayYear(currYM / 100);
        holidaysInfo.setHolidayCalendar();
        currMonthDays = holidaysInfo.getHolidayCalendar(currYM % 100);

        this.tvYear.setText("" + year);
        this.tvMonth.setText("" + month);

        HolidaysInfo prevHolidayInfo = null;
        HolidaysInfo nextHolidayInfo = null;

        // 年号を表示
        setYearMonth(currYM);

        if (currYM % 100 == 1) {
            // next month
            nextYM = currYM + 1;
            nextMonthDays = holidaysInfo.getHolidayCalendar(nextYM % 100);

            // prev month
            myCalendar.setCalendar(year, month, 1);
            myCalendar.add(Calendar.MONTH, -1);
            Log.d(LOGTAG, "----->curr year month(1) :" + myCalendar.getCurrentYMD("-"));
            prevYM = myCalendar.getCurrentYMD() / 100;
            prevHolidayInfo = new HolidaysInfo();
            prevHolidayInfo.setBaseHolidaysInfo(this.xmlPullParser);
            prevHolidayInfo.setCountry(holidaysInfo.getCountry());
            prevHolidayInfo.clearHolidays();
            prevHolidayInfo.setHolidayYear(prevYM / 100);
            prevHolidayInfo.setHolidayCalendar();
            prevMonthDays = prevHolidayInfo.getHolidayCalendar(prevYM % 100);

            Log.d(LOGTAG, "----->curr year month(1) :" + prevYM + " - " + currYM + " - " +  nextYM);
        } else if (currYM % 100 == 12) {
            prevYM = currYM - 1;
            prevMonthDays = holidaysInfo.getHolidayCalendar(prevYM % 100);

            myCalendar.setCalendar(year, month, 1);
            myCalendar.add(Calendar.MONTH, 1);
            nextYM = myCalendar.getCurrentYMD() / 100;

            nextHolidayInfo = new HolidaysInfo();
            nextHolidayInfo.setBaseHolidaysInfo(this.xmlPullParser);
            nextHolidayInfo.setCountry(holidaysInfo.getCountry());
            nextHolidayInfo.setHolidayYear(nextYM / 100);
            nextHolidayInfo.setHolidayCalendar();
            nextMonthDays = nextHolidayInfo.getHolidayCalendar(nextYM % 100);
            Log.d(LOGTAG, "----->curr year month(2) :" + prevYM + " - " + currYM + " - " +  nextYM);
        } else {
            prevYM = currYM - 1;
            prevMonthDays = holidaysInfo.getHolidayCalendar(prevYM % 100);
            nextYM = currYM + 1;
            nextMonthDays = holidaysInfo.getHolidayCalendar(nextYM % 100);
            Log.d(LOGTAG, "----->curr year month(3) :" + prevYM + " - " + currYM + " - " +  nextYM);
        }

        // for debug
//        for (int j = 0; j < 6; j++) {
//            String weekStr = "";
//            for (int k = 0; k < 7; k++) {
//                if (prevMonthDays[j][k] == 0) {
//                    String s = String.format("    ");
//                    weekStr = weekStr + "    ";
//                } else {
//                    String s = String.format("%4d", prevMonthDays[j][k]);
//                    weekStr = weekStr + s;
//                }
//            }
//            Log.d(LOGTAG, "Prev month :" + weekStr);
//        }

        // for debug
//        for (int j = 0; j < 6; j++) {
//            String weekStr = "";
//            for (int k = 0; k < 7; k++) {
//                if (currMonthDays[j][k] == 0) {
//                    String s = String.format("    ");
//                    weekStr = weekStr + "    ";
//                } else {
//                    String s = String.format("%4d", currMonthDays[j][k]);
//                    weekStr = weekStr + s;
//                }
//            }
//            Log.d(LOGTAG, "Curr month :" + weekStr);
//        }

        int prevMonthStartIdx = 0;
        for (int i = 5; i >= 0; i--) {
            for (int j = 6; j >= 0; j--) {
                if (prevMonthDays[i][j] != 0) {
                    prevMonthStartIdx = i;
                    break;
                }
            }
            if (prevMonthStartIdx > 0) {
                break;
            }
        }

        int curMonthLatestIdx_i = 0;
        int curMonthLatestIdx_j = 0;
        for (int i = 5; i >= 0; i--) {
            for (int j = 6; j >= 0; j--) {
                if (currMonthDays[i][j] != 0) {
                    curMonthLatestIdx_i = i;
                    curMonthLatestIdx_j = j;

                    break;
                }
            }
            if (curMonthLatestIdx_i != 0 || curMonthLatestIdx_j != 0) {
                if (curMonthLatestIdx_j == 6) {
                    curMonthLatestIdx_i++;
                    curMonthLatestIdx_j = 0;

                }

                break;
            }
        }

        boolean includeToday = false;
        int today = myCalendar.getTodayYMD();
        if (today / 100 == currYM) {
            includeToday = true;
        }
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                textViews[i][j].setText("");
                if (currMonthDays[i][j] == 0) {
                    if (i == 0) {
                        textViews[i][j].setText("" + prevMonthDays[prevMonthStartIdx][j] % 100);
                        if (prevMonthDays[prevMonthStartIdx][j] > 100) {
                            textViews[i][j].setTextColor(Color.rgb(215, 108, 108));
                        } else {
                            textViews[i][j].setTextColor(Color.rgb(128, 128, 128));
                        }
                        textViews[i][j].setBackground(getResources().getDrawable(R.drawable.calendar_day_box_othermonth));
                    } else if (i >= curMonthLatestIdx_i) {
                        textViews[i][j].setText("" + nextMonthDays[i-curMonthLatestIdx_i][j] % 100);
                        if (nextMonthDays[i-curMonthLatestIdx_i][j] > 100) {
                            textViews[i][j].setTextColor(Color.rgb(215, 108, 108));
                        } else {
                            textViews[i][j].setTextColor(Color.rgb(128, 128, 128));
                        }
                        textViews[i][j].setBackground(getResources().getDrawable(R.drawable.calendar_day_box_othermonth));
                    }
                } else {
                    if (currMonthDays[i][j] > 100) {
                        textViews[i][j].setText("" + currMonthDays[i][j] % 100);
                        if (j == 0) {
                            textViews[i][j].setTextColor(Color.RED);
                        } else if (j == 6) {
                            //textViews[i][j].setTextColor(Color.YELLOW);
                            textViews[i][j].setTextColor(Color.rgb(170, 0, 0));
                        } else {
                            //textViews[i][j].setTextColor(Color.YELLOW);
                            textViews[i][j].setTextColor(Color.rgb(170, 0, 0));
                            //text color #50F03C
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


                    // set today
                    if (includeToday && textViews[i][j].getText().toString().equals("" + today % 100)) {
                        //textViews[i][j].setBackgroundColor(Color.CYAN);
                        textViews[i][j].setBackground(getResources().getDrawable(R.drawable.calendar_today_box));
                    } else {
                        textViews[i][j].setBackground(getResources().getDrawable(R.drawable.calendar_day_box));
                    }

                }
            }
        }

        // for debug
//        for (int j = 0; j < 6; j++) {
//            String weekStr = "";
//            for (int k = 0; k < 7; k++) {
//                if (nextMonthDays[j][k] == 0) {
//                    String s = String.format("    ");
//                    weekStr = weekStr + "    ";
//                } else {
//                    String s = String.format("%4d", nextMonthDays[j][k]);
//                    weekStr = weekStr + s;
//                }
//            }
//            Log.d(LOGTAG, "Next month :" + weekStr);
//        }
    }

    private void getHolidayList(String country) {
        XmlPullParser xpp= getResources().getXml(R.xml.holidayinfo_jp);
        HolidaysInfo hI= new HolidaysInfo();
        hI.setCountry(country);
        hI.setBaseHolidaysInfo(xpp);

        int toYmd = myCalendar.getTodayYMD();
        hI.clearHolidays();
        hI.setHolidayYear(toYmd / 10000);
        hI.setHolidayCalendar();
        ArrayList<HolidayItem> hdis = hI.getHolidays();

        for (HolidayItem item : hdis) {
            if (item.ymd >= toYmd) {

                CalcDurationDate diffDate = new CalcDurationDate();
                diffDate.setInitDate(toYmd/10000, (toYmd%10000)/100, toYmd%100,
                                    item.ymd/10000, (item.ymd%10000)/100, item.ymd%100);
                diffDate.setDiffDays();

                HolidayListItem hlItem = new HolidayListItem(
                    item.ymd,
                    MyCalendar.convertDateWeekName(getResources(), item.ymd, "/"),
                    item.name,
                    ((item.ymd == toYmd)? constantStr[0] :diffDate.getTotalDays() + constantStr[1]));

                holidayListItems.add(hlItem);
            }
        }
    }

    private void setTextViews(View view) {

        tvYear = ((TextView)view.findViewById(R.id.this_year_solar));
        tvMonth = ((TextView)view.findViewById(R.id.this_month_solar));
        tvJpName = ((TextView)view.findViewById(R.id.this_year_jpname));
        tvJpYear = ((TextView)view.findViewById(R.id.this_year_japanes));

        ((Button) view.findViewById(R.id.btnPrevMonth)).setOnClickListener(this);
        ((Button) view.findViewById(R.id.btnNextMonth)).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.lbThisYear)).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.lbThisMonth)).setOnClickListener(this);

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
