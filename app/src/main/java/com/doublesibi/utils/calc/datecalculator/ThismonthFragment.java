package com.doublesibi.utils.calc.datecalculator;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.doublesibi.utils.calc.datecalculator.common.CalcDurationDate;
import com.doublesibi.utils.calc.datecalculator.common.Constants;
import com.doublesibi.utils.calc.datecalculator.common.DateInfo;
import com.doublesibi.utils.calc.datecalculator.dsblunar.SolarLunarJP;
import com.doublesibi.utils.calc.datecalculator.common.ThisMonthViewsWeek;
import com.doublesibi.utils.calc.datecalculator.holiday.HolidayItem;
import com.doublesibi.utils.calc.datecalculator.holiday.HolidayListItem;
import com.doublesibi.utils.calc.datecalculator.holiday.HolidaysInfo;
import com.doublesibi.utils.calc.datecalculator.holiday.MyCalendar;
import com.doublesibi.utils.calc.datecalculator.holiday.RangeDate;
import com.doublesibi.utils.calc.datecalculator.holiday.YearName;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThismonthFragment extends Fragment implements View.OnClickListener {
    private final String LOGTAG = "DayCalc";

    private MyCalendar myCalendar;
    private SolarLunarJP solarLunarJP;
    private XmlPullParser xmlPullParser;
    private HolidaysInfo holidaysInfo;
    private YearName yearName;
    private ArrayList<RangeDate> yearNameList;
    private DateInfo[][] dateInfos;

    private TextView tvYear, tvMonth, tvJpName, tvJpYear;

    ArrayList<ThisMonthViewsWeek> thisMonthViews;

    ArrayList<HolidayListItem> holidayListItems;

    String[] constantStr = {"今日", " 日後"};

    private Button btnRokuyo, btnMoveThisMonth;
    private Boolean bRokuyo = false;
    private int thisYearMonth = 0;

    @Override
    public void onResume() {
        super.onResume();

        if (holidayListItems == null) {
            holidayListItems = new ArrayList<>();
        }

        if (thisMonthViews == null) {
            thisMonthViews = new ArrayList<>(6);
        }

        if (solarLunarJP == null) {
            solarLunarJP = new SolarLunarJP();
        }

        if (dateInfos == null) {
            dateInfos = new DateInfo[6][7];
        }

        if (thisYearMonth == 0) {
            Calendar c = Calendar.getInstance();
            thisYearMonth = c.get(Calendar.YEAR) * 100 + (c.get(Calendar.MONTH) + 1);
        }
    }

    public ThismonthFragment() {
        if (holidayListItems == null) {
            holidayListItems = new ArrayList<>();
        }

        if (thisMonthViews == null) {
            thisMonthViews = new ArrayList<>(6);
        }

        if (solarLunarJP == null) {
            solarLunarJP = new SolarLunarJP();
        }

        if (dateInfos == null) {
            dateInfos = new DateInfo[6][7];
        }

        Calendar c = Calendar.getInstance();
        thisYearMonth = c.get(Calendar.YEAR)*100 + (c.get(Calendar.MONTH)+1);
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

        setFragmentViews(view);

        setThisMonthViews(view);

        if (bRokuyo) {
            btnRokuyo.setTextColor(getResources().getColor(R.color.colorCalcButtonNormal));
        } else {
            btnRokuyo.setTextColor(Color.BLACK);
        }

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
        setDays(myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH) + 1);

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
            case R.id.btnMoveThisMonth:
                myCalendar.setCalendar(thisYearMonth/100, (thisYearMonth%100)-1, 1);
                tempYM = myCalendar.getCurrentPrevMonth(1);
                setDays(tempYM/100, tempYM%100);
                break;
            case R.id.btnRokuyo:
                bRokuyo = !bRokuyo;
                if (bRokuyo) {
                    btnRokuyo.setTextColor(getResources().getColor(R.color.colorCalcButtonNormal));
                } else {
                    btnRokuyo.setTextColor(Color.BLACK);
                }
                displayRokuyo(bRokuyo);
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
        int currYM;
        HolidayItem holidayItem;
        HashMap<Integer, HolidayItem> holidaysMap;

        myCalendar.setCalendar(year, month, 1);
        currYM = myCalendar.getCurrentYMD() / 100;
        holidaysInfo.clearHolidays();
        holidaysInfo.setHolidayYear(currYM / 100);
        holidaysMap = holidaysInfo.getHolidaysMap();
        int today = myCalendar.getTodayYMD();

        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, 1);
        int column = c.get(Calendar.DAY_OF_WEEK);

        if(column == 1) {
            c.add(Calendar.DAY_OF_MONTH, -7);
        } else {
            c.add(Calendar.DAY_OF_MONTH, -1 * column + 1);
        }

        //solarLunarJP = new SolarLunarJP();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                DateInfo dif = new DateInfo(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1,c.get(Calendar.DAY_OF_MONTH));
                dif.lunarDate = solarLunarJP.getLunar(dif.solarDate);

                dif.l_bLeap=solarLunarJP.getLeap();
                dif.rokuyoIdx = solarLunarJP.getRokuyo();
                dif.rokuyo = Constants.ROKYO_NAME[dif.rokuyoIdx];
                if ((holidayItem = holidaysMap.get(dif.solarDate)) != null) {
                    dif.bHoliday = true;
                    dif.setHolidayName(holidayItem.name);
                } else {
                    dif.setHolidayName(null);
                }
                if (dif.solarDate / 100 == currYM) {
                    dif.bthisMonth = true;
                }
                dif.column = c.get(Calendar.DAY_OF_WEEK);
                dif.row = c.get(Calendar.DAY_OF_WEEK_IN_MONTH);
                if (today == dif.solarDate)
                    dif.bToday = true;
                dif.column = j;
                dif.row = i;

                dateInfos[dif.row][dif.column] = dif;
                c.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        // for debug
//        for (int i = 0; i < 6; i++) {
//            for (int j = 0; j < 7; j++) {
//                Log.d(LOGTAG, "(" + i +","+ j + ")");
//                Log.d(LOGTAG, "(" + i +","+ j + ")" + dateInfos[i][j].toString());
//            }
//        }

        // 年号を表示
        setYearMonth(currYM);

        // カレンダー表示
        displayCalendar();

        displayRokuyo(bRokuyo);

        // 今月へボタン
        if (thisYearMonth != (year * 100 + month)) {
            btnMoveThisMonth.setTextColor(getResources().getColor(R.color.colorCalcButtonNormal));
            btnMoveThisMonth.setClickable(true);

        } else {
            btnMoveThisMonth.setTextColor(getResources().getColor(R.color.colorCalcButton));
            btnMoveThisMonth.setClickable(false);
        }
    }

    private void displayCalendar() {

        for (int i = 0; i < 6; i++) {
            ThisMonthViewsWeek weekViews = thisMonthViews.get(i);
            for (int j = 0; j < 7; j++) {
                DateInfo dif = dateInfos[i][j];

                View view = weekViews.getaView(j);
                TextView tv = weekViews.getaWeekDays(j);
                tv.setText("" + dif.day);

                // 文字の色－今月　　：祝日・日曜日・土曜日・平日
                // 　　　　　今月以外：祝日・以外
                if (dif.bthisMonth) {
                    if (dif.bHoliday) {
                        if (dif.week == 1) { // sunday
                            tv.setTextColor(Color.RED);
                        } else if (dif.week == 7) { //saturday
                            tv.setTextColor(Color.rgb(170, 0, 0));
                        } else {
                            tv.setTextColor(Color.rgb(170, 0, 0));
                        }
                    } else {
                        if (dif.week == 1) { // sunday
                            tv.setTextColor(Color.RED);
                        } else if (dif.week == 7) { //saturday
                            tv.setTextColor(Color.BLUE);
                        } else {
                            tv.setTextColor(Color.BLACK);
                        }
                    }
                } else {
                    if (dif.bHoliday) {
                        tv.setTextColor(Color.rgb(215, 108, 108));
                    } else {
                        tv.setTextColor(Color.rgb(128, 128, 128));
                    }
                }

                // 背景　　－今月・以外、今日・以外
                if (dif.bToday) {
                    view.setBackground(getResources().getDrawable(R.drawable.calendar_today_box));
                } else {
                    if (dif.bthisMonth) {
                        view.setBackground(getResources().getDrawable(R.drawable.calendar_day_box));
                    } else {
                        view.setBackground(getResources().getDrawable(R.drawable.calendar_day_box_othermonth));
                    }
                }
            }
        }
    }

    private void displayRokuyo(boolean flag) {
        for (int i = 0; i < 6; i++) {
            ThisMonthViewsWeek weekViews = thisMonthViews.get(i);
            for (int j = 0; j < 7; j++) {
                DateInfo dif = dateInfos[i][j];

                //View view = weekViews.getaView(j);
                TextView tv = weekViews.getaWeekRokuyo(j);

                if (dif.lunarDate <= SolarLunarJP.LUNAR_MIN_YEAR || dif.lunarDate >= solarLunarJP.LUNAR_MAX_YEAR) {
                    tv.setText(" ");
                } else {
                    if (flag) {
                        tv.setText("" + Constants.ROKYO_NAME[dif.rokuyoIdx]);
                        Log.d(LOGTAG, "Sol:" + dif.solarDate + ", Lun:" + dif.lunarDate + ", roI:" + dif.rokuyoIdx + ", roNm:" + Constants.ROKYO_NAME[dif.rokuyoIdx]);
                        if (dif.rokuyoIdx == 0) {
                            tv.setTextColor(Color.rgb(170, 0, 0));
                        } else {
                            tv.setTextColor(Color.rgb(128, 128, 128));
                        }
                    } else {
                        tv.setText(" ");
                    }
                }
            }
        }
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

        int nextYmd = (toYmd / 10000 + 1) * 10000 + 100 + 0;
        if (holidayListItems.size() < 15) {
            hI.clearHolidays();
            hI.setHolidayYear(nextYmd / 10000);
            hI.setHolidayCalendar();
            hdis = hI.getHolidays();
            for (HolidayItem item : hdis) {
                CalcDurationDate diffDate = new CalcDurationDate();
                diffDate.setInitDate(toYmd/10000, (toYmd%10000)/100, toYmd%100,
                        item.ymd/10000, (item.ymd%10000)/100, item.ymd%100);
                diffDate.setDiffDays();

                HolidayListItem hlItem = new HolidayListItem(
                        item.ymd,
                        MyCalendar.convertDateWeekName(getResources(), item.ymd, "/"),
                        item.name,
                        ((item.ymd == nextYmd)? constantStr[0] :diffDate.getTotalDays() + constantStr[1]));

                holidayListItems.add(hlItem);
                if (holidayListItems.size() > 15) break;
            }
        }
    }

    private void setFragmentViews(View view) {
        tvYear = ((TextView)view.findViewById(R.id.this_year_solar));
        tvMonth = ((TextView)view.findViewById(R.id.this_month_solar));
        tvJpName = ((TextView)view.findViewById(R.id.this_year_jpname));
        tvJpYear = ((TextView)view.findViewById(R.id.this_year_japanes));

        btnRokuyo = (Button)(view.findViewById(R.id.btnRokuyo));
        btnMoveThisMonth = (Button)(view.findViewById(R.id.btnMoveThisMonth));
        (view.findViewById(R.id.btnPrevMonth)).setOnClickListener(this);
        (view.findViewById(R.id.btnNextMonth)).setOnClickListener(this);
        (view.findViewById(R.id.lbThisYear)).setOnClickListener(this);
        (view.findViewById(R.id.lbThisMonth)).setOnClickListener(this);
        btnRokuyo.setOnClickListener(this);
        btnMoveThisMonth.setOnClickListener(this);

        tvYear.setOnClickListener(this);
        tvMonth.setOnClickListener(this);
        tvJpName.setOnClickListener(this);
        tvJpYear.setOnClickListener(this);
    }

    private void setThisMonthViews(View view) {
        setThisMonthViewsWeek(view.findViewById(R.id.week_0), 0);
        setThisMonthViewsWeek(view.findViewById(R.id.week_1), 1);
        setThisMonthViewsWeek(view.findViewById(R.id.week_2), 2);
        setThisMonthViewsWeek(view.findViewById(R.id.week_3), 3);
        setThisMonthViewsWeek(view.findViewById(R.id.week_4), 4);
        setThisMonthViewsWeek(view.findViewById(R.id.week_5), 5);

    }
    private void setThisMonthViewsWeek(View view, int index) {
        ThisMonthViewsWeek aweek = new ThisMonthViewsWeek();
        setThisMonthViewsDays(aweek, view.findViewById(R.id.day_0), 0);
        setThisMonthViewsDays(aweek, view.findViewById(R.id.day_1), 1);
        setThisMonthViewsDays(aweek, view.findViewById(R.id.day_2), 2);
        setThisMonthViewsDays(aweek, view.findViewById(R.id.day_3), 3);
        setThisMonthViewsDays(aweek, view.findViewById(R.id.day_4), 4);
        setThisMonthViewsDays(aweek, view.findViewById(R.id.day_5), 5);
        setThisMonthViewsDays(aweek, view.findViewById(R.id.day_6), 6);

        thisMonthViews.add(index, aweek);
    }
    private void setThisMonthViewsDays(ThisMonthViewsWeek aweek, View view, int index) {
        aweek.setaView(view, index);
        aweek.setaWeekDays((TextView) view.findViewById(R.id.dayNum), index);
        aweek.setaWeekRokuyo((TextView) view.findViewById(R.id.rokuyoNum), index);
    }
}
