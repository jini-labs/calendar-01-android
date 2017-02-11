package com.doublesibi.utils.calc.datecalculator;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.doublesibi.utils.calc.datecalculator.common.CalcDurationDate;
import com.doublesibi.utils.calc.datecalculator.common.Constants;
import com.doublesibi.utils.calc.datecalculator.hist.DurationItemOpenHelper;
import com.doublesibi.utils.calc.datecalculator.hist.HistItem;
import com.doublesibi.utils.calc.datecalculator.holiday.MyCalendar;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DurationFragment extends Fragment implements View.OnClickListener{
    private final String LOGTAG = "DayCalc";

    private TextView startYY, startMM, startDD;
    private TextView endYY, endMM, endDD;
    private TextView result1_days;
    private TextView result2_weeks, result2_days;
    private TextView result3_months, result3_days;
    private TextView result4_years, result4_months, result4_days;

    private ImageButton btnStDate, btnEnDate, btnStToday, btnEnToday;
    private Button btnCalc, btnDuraSave;

    private DatePickerDialog datePickerDialog;
    private MyCalendar myCalendar;

    private int startymd = 0, endymd = 0;
    private int styy = 0, stmm = 0, stdd = 0;
    private int enyy = 0, enmm = 0, endd = 0;

    private boolean ableToSave = false;

    public DurationFragment() {
        // Required empty public constructor
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
        menu.findItem(R.id.action_settings).setVisible(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myCalendar = new MyCalendar();
        View view = inflater.inflate(R.layout.fragment_duration, container, false);

        setTextId(view);
        setButtonId(view);

        startymd = endymd = myCalendar.getTodayYMD();
        styy = enyy = startymd / 10000;
        stmm = enmm = startymd % 10000 / 100;
        stdd = endd = startymd % 100;

        return(view);
    }

    @Override
    public void onClick(View v) {

        int maxDays = 0;
        if (v != null) {
            int date = 0;
            switch (v.getId()) {
                // year or month or day
                case R.id.styy:
                    this.ableToSave = false;

                    numberPickerDilaog(0, 3000, styy, Constants.INPUT_START_YEAR, "年度を選択下さい。");
                    break;

                case R.id.stmm:
                    this.ableToSave = false;

                    numberPickerDilaog(1, 12, stmm, Constants.INPUT_START_MONTH, "月を選択下さい。");
                    break;

                case R.id.stdd:
                    this.ableToSave = false;

                    maxDays = myCalendar.getMaxDayOfMonth(enyy, enmm);
                    numberPickerDilaog(1, maxDays, stdd, Constants.INPUT_START_DATE, "日を選択下さい。");
                    //Toast.makeText(getContext(), "開始日付", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.enyy:
                    this.ableToSave = false;

                    numberPickerDilaog(0, 3000, enyy, Constants.INPUT_END_YEAR, "年度を選択下さい。");
                    break;

                case R.id.enmm:
                    this.ableToSave = false;

                    numberPickerDilaog(1, 12, enmm, Constants.INPUT_END_MONTH, "月を選択下さい。");
                    break;

                case R.id.endd:
                    this.ableToSave = false;

                    maxDays = myCalendar.getMaxDayOfMonth(enyy, enmm);
                    numberPickerDilaog(1, maxDays, endd, Constants.INPUT_END_DATE, "日を選択下さい。");
                    //Toast.makeText(getContext(), "開始日付", Toast.LENGTH_SHORT).show();
                    break;
                // button
                case R.id.btn_durat_stdt:
                    this.ableToSave = false;

                    datePickerDialog = new DatePickerDialog(
                            getContext(),
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    try {
                                        setStartDate(year, (monthOfYear+1), dayOfMonth);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, styy, stmm - 1, stdd);

                    datePickerDialog.getDatePicker().setCalendarViewShown(false);
                    datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    datePickerDialog.show();
                    break;

                case R.id.btn_durat_endt:
                    this.ableToSave = false;

                    if (enyy == 0 || enmm == 0 || endd == 0) {
                        Calendar c = Calendar.getInstance();
                        enyy = c.get(Calendar.YEAR);
                        enmm = c.get(Calendar.MONTH) + 1;
                        endd = c.get(Calendar.DAY_OF_MONTH);
                    }

                    datePickerDialog = new DatePickerDialog(
                            getContext(),
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    try {
                                        setEndDate(year, (monthOfYear+1), dayOfMonth);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, enyy, enmm - 1, endd);

                    datePickerDialog.getDatePicker().setCalendarViewShown(false);
                    datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    datePickerDialog.show();
                    break;

                case R.id.btn_start_today:
                    this.ableToSave = false;

                    date = myCalendar.getTodayYMD();
                    styy = date / 10000;
                    stmm = date % 10000 / 100;
                    stdd = date % 100;
                    startYY.setText("" + styy);
                    startMM.setText("" + stmm);
                    startDD.setText("" + stdd);
                    break;

                case R.id.btn_end_today:
                    this.ableToSave = false;

                    date = myCalendar.getTodayYMD();
                    enyy = date / 10000;
                    enmm = date % 10000 / 100;
                    endd = date % 100;
                    endYY.setText("" + enyy);
                    endMM.setText("" + enmm);
                    endDD.setText("" + endd);
                    break;

                case R.id.btnDuraSave:

                    if (! this.ableToSave) {
                        Toast.makeText(getContext(), "計算した結果のみ保存可能です。！", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    HistItem histItem = new HistItem();

                    int saveDate = Integer.valueOf(startYY.getText().toString()) * 10000 +
                                    Integer.valueOf(startMM.getText().toString()) * 100 +
                                    Integer.valueOf(startDD.getText().toString());
                    histItem.stDate = "" + saveDate;

                    saveDate = Integer.valueOf(endYY.getText().toString()) * 10000 +
                                Integer.valueOf(endMM.getText().toString()) * 100 +
                                Integer.valueOf(endDD.getText().toString());
                    histItem.enDate = "" + saveDate;

                    histItem.days = result1_days.getText().toString();
                    histItem.weeks = result2_weeks.getText().toString();
                    histItem.weekdays = result2_days.getText().toString();
                    histItem.months = result3_months.getText().toString();
                    histItem.monthdays = result3_days.getText().toString();
                    histItem.years = result4_years.getText().toString();
                    histItem.yearmonths = result4_months.getText().toString();
                    histItem.yeardays = result4_days.getText().toString();

                    DurationItemOpenHelper helper = new DurationItemOpenHelper(getActivity());
                    final SQLiteDatabase db = helper.getWritableDatabase();

                    long ret = helper.insertDuration(db, histItem);
//                    if (ret ) {
                        //Ok
                        //Duplicate
                        //Other
                        //Toast.makeText(getContext(), "(duration)inserted id :" + ret, Toast.LENGTH_SHORT).show();
//                    }
                    helper.close();

                    this.ableToSave = false;

                    break;

                case R.id.btnenter:
                    try {
                        styy = Integer.valueOf(startYY.getText().toString());
                        stmm = Integer.valueOf(startMM.getText().toString());
                        stdd = Integer.valueOf(startDD.getText().toString());

                        startymd = styy * 10000 + stmm * 100 + stdd;

                        enyy = Integer.valueOf(endYY.getText().toString());
                        enmm = Integer.valueOf(endMM.getText().toString());
                        endd = Integer.valueOf(endDD.getText().toString());
                        endymd = enyy * 10000 + enmm * 100 + endd;

                    } catch(NumberFormatException e) {
                        Toast.makeText(getContext(), "input date.!", Toast.LENGTH_SHORT).show();
                        break;
                    }


                    if(styy == 0 || stmm == 0 || stdd == 0) {
                        Toast.makeText(getContext(), "input start date.!", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    if(enyy == 0 || enmm == 0 || endd == 0) {
                        Toast.makeText(getContext(), "input end date.!", Toast.LENGTH_SHORT).show();
                        break;
                    }


                    Log.d(Constants.LOGTAG, "start: y:" + styy + ", m:" + stmm + ", d:" + stdd);
                    Log.d(Constants.LOGTAG, "end  : y:" + enyy + ", m:" + enmm + ", d:" + endd);
                    Log.d(Constants.LOGTAG,"start:" + startymd + ", end:" + endymd);

                    calcDateDiff(styy, stmm, stdd, enyy, enmm, endd);

                    this.ableToSave = true;
                    break;
            }
        }
    }

    private void setButtonId(View view) {
        btnStDate = (ImageButton) view.findViewById(R.id.btn_durat_stdt);
        btnEnDate = (ImageButton) view.findViewById(R.id.btn_durat_endt);
        btnStToday= (ImageButton) view.findViewById(R.id.btn_start_today);
        btnEnToday= (ImageButton) view.findViewById(R.id.btn_end_today);
        btnCalc = (Button) view.findViewById(R.id.btnenter);
        btnDuraSave = (Button) view.findViewById(R.id.btnDuraSave);

        btnCalc.setOnClickListener(this);
        btnDuraSave.setOnClickListener(this);

        view.findViewById(R.id.btn_durat_stdt).setOnClickListener(this);
        view.findViewById(R.id.btn_durat_endt).setOnClickListener(this);
        view.findViewById(R.id.btn_start_today).setOnClickListener(this);
        view.findViewById(R.id.btn_end_today).setOnClickListener(this);
    }

    private void setTextId(View view) {
        startYY = (TextView) view.findViewById(R.id.styy);
        startMM = (TextView) view.findViewById(R.id.stmm);
        startDD = (TextView) view.findViewById(R.id.stdd);
        endYY = (TextView) view.findViewById(R.id.enyy);
        endMM = (TextView) view.findViewById(R.id.enmm);
        endDD = (TextView) view.findViewById(R.id.endd);
        result1_days  = (TextView) view.findViewById(R.id.result_1_days);
        result2_weeks = (TextView) view.findViewById(R.id.result_2_weeks);
        result2_days  = (TextView) view.findViewById(R.id.result_2_days);
        result3_months = (TextView) view.findViewById(R.id.result_3_months);
        result3_days  = (TextView) view.findViewById(R.id.result_3_days);
        result4_years = (TextView) view.findViewById(R.id.result_4_years);
        result4_months= (TextView) view.findViewById(R.id.result_4_months);
        result4_days  = (TextView) view.findViewById(R.id.result_4_days);

        startYY.setOnClickListener(this);
        startMM.setOnClickListener(this);
        startDD.setOnClickListener(this);
        endYY.setOnClickListener(this);
        endMM.setOnClickListener(this);
        endDD.setOnClickListener(this);
    }

    private void setStartDate(int yy, int mm, int dd) {
        startYY.setText(String.valueOf(yy));
        startMM.setText(String.valueOf(mm));
        startDD.setText(String.valueOf(dd));
        styy = yy;
        stmm = mm;
        stdd = dd;
    }

    public void setEndDate(int yy, int mm, int dd) {
        endYY.setText(String.valueOf(yy));
        endMM.setText(String.valueOf(mm));
        endDD.setText(String.valueOf(dd));
        enyy = yy;
        enmm = mm;
        endd = dd;
    }

    private void calcDateDiff(int styy, int stmm, int stdd, int enyy, int enmm, int endd) {

        CalcDurationDate diffDate = new CalcDurationDate();
        if (!diffDate.setInitDate(styy, stmm, stdd, enyy, enmm, endd)) {
            Toast.makeText(getContext(), "check input date.!", Toast.LENGTH_SHORT).show();
        } else {
            diffDate.setDiffDays();

            result1_days.setText("" + diffDate.getTotalDays());
            result1_days.setTextColor(getResources().getColor(R.color.colorResultValue1));
            result2_weeks.setText("" + diffDate.getTotalWeeks());
            result2_days.setText("" + diffDate.getTotalWeekDays());
            result3_months.setText("" + diffDate.getTotalMonths());
            result3_days.setText("" + diffDate.getTotalMonthDays());
            result4_years.setText("" + diffDate.getTotalYears());
            result4_months.setText("" + diffDate.getTotalYearMonths());
            result4_days.setText("" + diffDate.getTotalYearDays());
        }
    }

    private void numberPickerDilaog(int min, int max, int curr, final int inputType, String title) {
        NumberPicker numberPicker = new NumberPicker(getContext());
        numberPicker.setMaxValue(max);
        numberPicker.setMinValue(min);
        numberPicker.setValue(curr);
        final int[] saveValue = {curr, curr};

        NumberPicker.OnValueChangeListener onValueChangeListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(saveValue[0] < 0) {
                    saveValue[0] = oldVal;
                }

                saveValue[1] = newVal;
            }
        };

        numberPicker.setOnValueChangedListener(onValueChangeListener);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(numberPicker);
        builder.setTitle(title);
        //builder.setIcon();

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(inputType) {
                    case Constants.INPUT_START_YEAR:
                        startYY.setText("" + saveValue[1]);
                        styy = saveValue[1];
                        break;
                    case Constants.INPUT_START_MONTH:
                        startMM.setText("" + saveValue[1]);
                        stmm = saveValue[1];
                        break;
                    case Constants.INPUT_START_DATE:
                        startDD.setText("" + saveValue[1]);
                        stdd = saveValue[1];
                        break;
                    case Constants.INPUT_END_YEAR:
                        endYY.setText("" + saveValue[1]);
                        enyy = saveValue[1];
                        break;
                    case Constants.INPUT_END_MONTH:
                        endMM.setText("" + saveValue[1]);
                        enmm = saveValue[1];
                        break;
                    case Constants.INPUT_END_DATE:
                        endDD.setText("" + saveValue[1]);
                        endd = saveValue[1];
                        break;
                }
                startymd = styy * 10000 + stmm * 100 + stdd;
                endymd = enyy * 10000 + enmm * 100 + endd;
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        //builder.show();

        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);

    }
}