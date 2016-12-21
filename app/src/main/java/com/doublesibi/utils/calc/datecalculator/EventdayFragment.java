package com.doublesibi.utils.calc.datecalculator;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doublesibi.utils.calc.datecalculator.common.CalcEventDate;
import com.doublesibi.utils.calc.datecalculator.common.Constants;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventdayFragment extends Fragment implements View.OnClickListener {
    private TextView startYY, startMM, startDD;
    private TextView value1, result_eventday;
    private Button btn_start_today;
    private Spinner spnUnit, spnBeAf;

    DatePickerDialog datePickerDialog;

    int styy = 0, stmm = 0, stdd = 0;
    int startymd = 0;

    public EventdayFragment() {
        // Required empty public constructor
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
                case R.id.styy:
                    numberPickerDilaog(0, 3000, styy, Constants.INPUT_START_YEAR, "年度を選択下さい。");
                    break;
                case R.id.stmm:
                    numberPickerDilaog(1, 12, stmm, Constants.INPUT_START_MONTH, "月を選択下さい。");
                    break;
                case R.id.stdd:
                    if (styy == 0 || stmm == 0) {
                        Toast.makeText(getContext(), "年と月から入力下さい。", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    maxDays = CalcEventDate.getMaxDayOfMonth(styy, stmm);
                    numberPickerDilaog(1, maxDays, stdd, Constants.INPUT_START_DATE, "日を選択下さい。");
                    Toast.makeText(getContext(), "開始日付", Toast.LENGTH_SHORT).show();
                    break;
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
                case R.id.btn_start_today:
                    int date[] = getToday();
                    styy = date[0];
                    stmm = date[1];
                    stdd = date[2];
                    startYY.setText("" + styy);
                    startMM.setText("" + stmm);
                    startDD.setText("" + stdd);
                    break;
                case R.id.spinnerdurationunit:
                    break;
                case R.id.spinnerbeforeafter:
                    break;
                case R.id.btn_calc_eventday:
                    // TODO: 計算する。
                    //       計算結果を　value1, result_eventdayにセット。
                    //
                    //
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

    public void setTextId(View view) {
        startYY = (TextView) view.findViewById(R.id.styy);
        startMM = (TextView) view.findViewById(R.id.stmm);
        startDD = (TextView) view.findViewById(R.id.stdd);

        value1 = (TextView) view.findViewById(R.id.value1);
        result_eventday = (TextView) view.findViewById(R.id.result_eventday);
    }

    public void setButtonId(View view) {
        btn_start_today = (Button) view.findViewById(R.id.btn_calc_eventday);
        view.findViewById(R.id.btnstdt).setOnClickListener(this);
        view.findViewById(R.id.btn_start_today).setOnClickListener(this);
    }

    public void setSpinnerId(View view) {

        spnUnit = (Spinner) view.findViewById(R.id.spinnerdurationunit);
        spnBeAf = (Spinner) view.findViewById(R.id.spinnerbeforeafter);
    }

    public void setStartDate(int yy, int mm, int dd) {
        startYY.setText(String.valueOf(yy));
        startMM.setText(String.valueOf(mm));
        startDD.setText(String.valueOf(dd));
        styy = yy;
        stmm = mm;
        stdd = dd;
    }

    public int[] getToday() {
        int ret[] = new int[3];
        Calendar c = Calendar.getInstance();
        ret[0] = c.get(Calendar.YEAR);
        ret[1] = c.get(Calendar.MONTH) + 1;
        ret[2] = c.get(Calendar.DAY_OF_MONTH);

        return ret;
    }
}
