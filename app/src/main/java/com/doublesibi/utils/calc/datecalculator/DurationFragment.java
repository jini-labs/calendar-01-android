package com.doublesibi.utils.calc.datecalculator;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.doublesibi.utils.calc.datecalculator.common.Constants;
import com.doublesibi.utils.calc.datecalculator.common.CalcEventDate;
import com.doublesibi.utils.calc.datecalculator.common.CalcDurationDate;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DurationFragment extends Fragment implements View.OnClickListener{
    private final String LOGTAG = "DayCalc";

    TextView startYY, startMM, startDD;
    TextView endYY, endMM, endDD;
    TextView result1_days;
    TextView result2_weeks, result2_days;
    TextView result3_months, result3_days;
    TextView result4_years, result4_months, result4_days;

    ImageButton btnStDate, btnEnDate, btnStToday, btnEnToday;
    Button btnCalc;

    DatePickerDialog datePickerDialog;

    int startymd = 0, endymd = 0;
    int styy = 0, stmm = 0, stdd = 0;
    int enyy = 0, enmm = 0, endd = 0;

    public DurationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_duration, container, false);

        setTextId(view);
        setButtonId(view);

        return(view);
    }

    @Override
    public void onClick(View v) {

        int maxDays = 0;
        if (v != null) {
            switch (v.getId()) {
                // year or month or day
                case R.id.styy:
                    numberPickerDilaog(0, 3000, styy, Constants.INPUT_START_YEAR, "年度を選択下さい。");
                    break;
                case R.id.stmm:
                    numberPickerDilaog(1, 12, stmm, Constants.INPUT_START_MONTH, "月を選択下さい。");
                    break;
                case R.id.stdd:
                    maxDays = CalcEventDate.getMaxDayOfMonth(enyy, enmm);
                    numberPickerDilaog(1, maxDays, stdd, Constants.INPUT_START_DATE, "日を選択下さい。");
                    Toast.makeText(getContext(), "開始日付", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.enyy:
                    numberPickerDilaog(0, 3000, enyy, Constants.INPUT_END_YEAR, "年度を選択下さい。");
                    break;
                case R.id.enmm:
                    numberPickerDilaog(1, 12, enmm, Constants.INPUT_END_MONTH, "月を選択下さい。");
                    break;
                case R.id.endd:
                    maxDays = CalcEventDate.getMaxDayOfMonth(enyy, enmm);
                    numberPickerDilaog(1, maxDays, endd, Constants.INPUT_END_DATE, "日を選択下さい。");
                    Toast.makeText(getContext(), "開始日付", Toast.LENGTH_SHORT).show();
                    break;
                // button
                case R.id.btnstdt:
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
                case R.id.btnendt:
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

                case R.id.btn_start_today: {
                    int date[] = getToday();
                    styy = date[0];
                    stmm = date[1];
                    stdd = date[2];
                    startYY.setText("" + styy);
                    startMM.setText("" + stmm);
                    startDD.setText("" + stdd);
                }
                break;

                case R.id.btn_end_today: {
                    int date[] = getToday();
                    enyy = date[0];
                    enmm = date[1];
                    endd = date[2];
                    endYY.setText("" + enyy);
                    endMM.setText("" + enmm);
                    endDD.setText("" + endd);
                }
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
                    Toast.makeText(getContext(), "計算しよう。", Toast.LENGTH_SHORT).show();

                    calcDateDiff(styy, stmm, stdd, enyy, enmm, endd);

                    break;
            }
        }
    }

    public void setButtonId(View view) {
        btnStDate = (ImageButton) view.findViewById(R.id.btnstdt);
        btnEnDate = (ImageButton) view.findViewById(R.id.btnendt);
        btnStToday= (ImageButton) view.findViewById(R.id.btn_start_today);
        btnEnToday= (ImageButton) view.findViewById(R.id.btn_end_today);
        btnCalc = (Button) view.findViewById(R.id.btnenter);

        btnCalc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d(LOGTAG,"btnCalc down.");
                    btnCalc.setBackgroundResource(R.color.colorCalcButtonPress);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.d(LOGTAG,"btnCalc up.");
                    btnCalc.setBackgroundResource(R.color.colorCalcButtonNormal);
                }
                return false;
            }
        });

        view.findViewById(R.id.btnstdt).setOnClickListener(this);
        view.findViewById(R.id.btnendt).setOnClickListener(this);
        view.findViewById(R.id.btn_start_today).setOnClickListener(this);
        view.findViewById(R.id.btn_end_today).setOnClickListener(this);
        view.findViewById(R.id.btnenter).setOnClickListener(this);
    }

    public void setTextId(View view) {
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

    public void setStartDate(int yy, int mm, int dd) {
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

    public int[] getToday() {
        int ret[] = new int[3];
        Calendar c = Calendar.getInstance();
        ret[0] = c.get(Calendar.YEAR);
        ret[1] = c.get(Calendar.MONTH) + 1;
        ret[2] = c.get(Calendar.DAY_OF_MONTH);

        return ret;
    }

    private void calcDateDiff(int styy, int stmm, int stdd, int enyy, int enmm, int endd) {

        CalcDurationDate diffDate = new CalcDurationDate();
        if (!diffDate.setInitDate(styy, stmm, stdd, enyy, enmm, endd)) {
            Toast.makeText(getContext(), "check input date.!", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(Constants.LOGTAG, "step 0001");
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
        builder.show();
    }

}
