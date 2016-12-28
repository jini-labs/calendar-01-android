package com.doublesibi.utils.calc.datecalculator;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doublesibi.utils.calc.datecalculator.common.CalcEventDate;
import com.doublesibi.utils.calc.datecalculator.common.Constants;
import com.doublesibi.utils.calc.datecalculator.util.MyCalendar;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventdayFragment extends Fragment implements View.OnClickListener {
    private final String LOGTAG = "DayCalc";

    private TextView eventStartYY, eventStartMM, eventStartDD;
    private EditText value1, value2, value3, value4;
    private TextView result_eventday, result_eventday_week;
    private Button btnCalcEvent;
    private Spinner spnBeAf;

    private CalcEventDate calcEventDate;
    private DatePickerDialog datePickerDialog;

    private int styy = 0, stmm = 0, stdd = 0;
    private int startymd = 0;

    public EventdayFragment() {
        // Required empty public constructor
        calcEventDate = new CalcEventDate();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_eventday, container, false);

        setTextId(view);
        setButtonId(view);
        setSpinnerId(view);

        return view;
    }

    @Override
    public void onClick(View v) {

        int maxDays = 0;

        if (v != null) {
            switch (v.getId()) {
                case R.id.event_styy:
                    numberPickerDilaog(0, 3000, styy, Constants.INPUT_START_YEAR, "年度を選択下さい。");
                    break;
                case R.id.event_stmm:
                    numberPickerDilaog(1, 12, stmm, Constants.INPUT_START_MONTH, "月を選択下さい。");
                    break;
                case R.id.event_stdd:
                    if (styy == 0 || stmm == 0) {
                        Toast.makeText(getContext(), "年と月から入力下さい。", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    maxDays = MyCalendar.getMaxDayOfMonth(styy, stmm);
                    numberPickerDilaog(1, maxDays, stdd, Constants.INPUT_START_DATE, "日を選択下さい。");
                    Toast.makeText(getContext(), "開始日付", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.btn_event_stdt:
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

                case R.id.btn_start_today:
                    int date[] = getToday();
                    styy = date[0];
                    stmm = date[1];
                    stdd = date[2];
                    eventStartYY.setText("" + styy);
                    eventStartMM.setText("" + stmm);
                    eventStartDD.setText("" + stdd);
                    break;

                case R.id.spinnerbeforeafter:
                    break;

                case R.id.btn_calc_eventday:
                    CalcEventDate calcEventDate = new CalcEventDate();
                    int[] params = new int[8];
                    params[0] = styy;
                    params[1] = stmm;
                    params[2] = stdd;
                    params[3] = spnBeAf.getSelectedItemPosition();
                    // num of day
                    if (value1.getText().length() > 0)
                        params[4] = Integer.parseInt(value1.getText().toString());
                    else
                        params[4] = 0;
                    // num of week
                    if (value2.getText().length() > 0)
                        params[5] = Integer.parseInt(value2.getText().toString());
                    else
                        params[5] = 0;
                    // num of month
                    if (value3.getText().length() > 0)
                        params[6] = Integer.parseInt(value3.getText().toString());
                    else
                        params[6] = 0;
                    // num of year
                    if (value4.getText().length() > 0)
                        params[7] = Integer.parseInt(value4.getText().toString());
                    else
                        params[7] = 0;

                    String[] retYmd = calcEventDate.getEventYmd(params);

                    // ymd
                    result_eventday.setText(retYmd[0]);

                    // day of week
                    int tmp = Integer.parseInt(retYmd[1]);
                    Resources r = getResources();
                    String[] weekofName = r.getStringArray(R.array.nameOfWeek);
                    result_eventday_week.setText(weekofName[tmp-1]);
                    switch(tmp) {
                        case 6:
                            result_eventday_week.setTextColor(Color.RED);
                            break;
                        case 7:
                            result_eventday_week.setTextColor(Color.RED);
                            break;
                    }



                    break;
            }
        }
    }

    private void numberPickerDilaog(int min, int max, int curr, final int inputType, String title) {
        NumberPicker numberPicker = new NumberPicker(getContext());
        numberPicker.setMaxValue(max);
        numberPicker.setMinValue(min);
        numberPicker.setValue(curr);
        final int[] saveValue = {-1, -1};
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
                        eventStartYY.setText("" + saveValue[1]);
                        styy = saveValue[1];
                        break;
                    case Constants.INPUT_START_MONTH:
                        eventStartMM.setText("" + saveValue[1]);
                        stmm = saveValue[1];
                        break;
                    case Constants.INPUT_START_DATE:
                        eventStartDD.setText("" + saveValue[1]);
                        stdd = saveValue[1];
                        break;
                }
                startymd = styy * 10000 + stmm * 100 + stdd;
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void setTextId(View view) {
        eventStartYY = (TextView) view.findViewById(R.id.event_styy);
        eventStartMM = (TextView) view.findViewById(R.id.event_stmm);
        eventStartDD = (TextView) view.findViewById(R.id.event_stdd);

        value1 = (EditText) view.findViewById(R.id.value1);
        value2 = (EditText) view.findViewById(R.id.value2);
        value3 = (EditText) view.findViewById(R.id.value3);
        value4 = (EditText) view.findViewById(R.id.value4);

        eventStartYY.setOnClickListener(this);
        eventStartMM.setOnClickListener(this);
        eventStartDD.setOnClickListener(this);

        result_eventday = (TextView) view.findViewById(R.id.result_eventday);
        result_eventday_week = (TextView) view.findViewById(R.id.result_eventday_week);
    }


    private void setButtonId(View view) {
        btnCalcEvent = (Button) view.findViewById(R.id.btn_calc_eventday);

        btnCalcEvent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d(LOGTAG,"btnCalcEvent down.");
                    btnCalcEvent.setBackgroundResource(R.color.colorCalcButtonPress);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.d(LOGTAG,"btnCalcEvent up.");
                    btnCalcEvent.setBackgroundResource(R.color.colorCalcButtonNormal);
                }
                return false;
            }
        });

        btnCalcEvent.setOnClickListener(this);
        view.findViewById(R.id.btn_event_stdt).setOnClickListener(this);
        view.findViewById(R.id.btn_start_today).setOnClickListener(this);
    }

    private void setSpinnerId(View view) {


        spnBeAf = (Spinner) view.findViewById(R.id.spinnerbeforeafter);
    }

    private void setStartDate(int yy, int mm, int dd) {
        eventStartYY.setText(String.valueOf(yy));
        eventStartMM.setText(String.valueOf(mm));
        eventStartDD.setText(String.valueOf(dd));
        styy = yy;
        stmm = mm;
        stdd = dd;
    }

    // 0:ymd, 1:dayofweek(1~7)
    public int[] getToday() {
        int ret[] = new int[3];
        Calendar c = Calendar.getInstance();
        ret[0] = c.get(Calendar.YEAR);
        ret[1] = c.get(Calendar.MONTH) + 1;

        return ret;
    }
}
