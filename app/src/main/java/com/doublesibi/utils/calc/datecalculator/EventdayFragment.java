package com.doublesibi.utils.calc.datecalculator;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doublesibi.utils.calc.datecalculator.common.CalcEventDate;
import com.doublesibi.utils.calc.datecalculator.common.Constants;
import com.doublesibi.utils.calc.datecalculator.hist.EventdayItemOpenHelper;
import com.doublesibi.utils.calc.datecalculator.hist.HistItem;
import com.doublesibi.utils.calc.datecalculator.holiday.MyCalendar;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventdayFragment extends Fragment implements View.OnClickListener {
    private final String LOGTAG = "DayCalc";

    private TextView eventStartYY, eventStartMM, eventStartDD;
    private EditText value1, value2, value3, value4;
    private TextView result_eventday, result_eventday_week;
    private Button btnCalcEvent, btnEventDaySave;
    private Spinner spnBeAf;

    private CalcEventDate calcEventDate;
    private DatePickerDialog datePickerDialog;
    private MyCalendar myCalendar;

    private int styy = 0, stmm = 0, stdd = 0;
    private int startymd = 0;

    private boolean ableToSave = false;

    private String[] constantStr = {"年度を選択下さい。",
            "月を選択下さい。",
            "日を選択下さい。",
            "計算した結果のみ保存可能です。！",
            "年と月から入力下さい。",
            "保存",
            "キャンセル"};

    public EventdayFragment() {
        // Required empty public constructor
        calcEventDate = new CalcEventDate();
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
        menu.findItem(R.id.action_history).setVisible(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myCalendar = new MyCalendar();
        View view = inflater.inflate(R.layout.fragment_eventday, container, false);

        setTextId(view);
        setButtonId(view);
        setSpinnerId(view);

        startymd = myCalendar.getTodayYMD();
        styy = startymd / 10000;
        stmm = startymd % 10000 / 100;
        stdd = startymd % 100;

        return view;
    }

    @Override
    public void onClick(View v) {

        int maxDays = 0;

        if (v != null) {
            switch (v.getId()) {
                case R.id.event_styy:
                    this.ableToSave = false;
                    numberPickerDilaog(0, 3000, styy, Constants.INPUT_START_YEAR, constantStr[0]);
                    break;
                case R.id.event_stmm:
                    this.ableToSave = false;
                    numberPickerDilaog(1, 12, stmm, Constants.INPUT_START_MONTH, constantStr[1]);
                    break;
                case R.id.event_stdd:
                    this.ableToSave = false;
                    if (styy == 0 || stmm == 0) {
                        Toast.makeText(getContext(), constantStr[4], Toast.LENGTH_SHORT).show();
                        break;
                    }
                    maxDays = myCalendar.getMaxDayOfMonth(styy, stmm);
                    numberPickerDilaog(1, maxDays, stdd, Constants.INPUT_START_DATE, constantStr[2]);
                    //Toast.makeText(getContext(), "開始日付", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.btn_event_stdt:
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

                case R.id.btn_start_today:
                    this.ableToSave = false;
                    int date = myCalendar.getTodayYMD();

                    styy = date / 10000;
                    stmm = date % 10000 / 100;
                    stdd = date % 100;
                    eventStartYY.setText("" + styy);
                    eventStartMM.setText("" + stmm);
                    eventStartDD.setText("" + stdd);
                    break;

                case R.id.spinnerbeforeafter:
                    this.ableToSave = false;
                    break;

                case R.id.btn_calc_eventday:
                    CalcEventDate calcEventDate = new CalcEventDate();
                    int[] params = new int[8];
                    for (int i = 0; i < 8; i++) {
                        params[i] = 0;
                    }
                    params[0] = Integer.parseInt(eventStartYY.getText().toString().trim());
                    params[1] = Integer.parseInt(eventStartMM.getText().toString().trim());
                    params[2] = Integer.parseInt(eventStartDD.getText().toString().trim());
                    params[3] = spnBeAf.getSelectedItemPosition();

                    // num of day
                    if (value1.getText().length() > 0)
                        params[4] = Integer.parseInt(value1.getText().toString().trim());

                    // num of week
                    if (value2.getText().length() > 0)
                        params[5] = Integer.parseInt(value2.getText().toString().trim());

                    // num of month
                    if (value3.getText().length() > 0)
                        params[6] = Integer.parseInt(value3.getText().toString().trim());

                    // num of year
                    if (value4.getText().length() > 0)
                        params[7] = Integer.parseInt(value4.getText().toString().trim());

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

                    this.ableToSave = true;
                    break;

                case R.id.btnEventdaySave:
                    if (! this.ableToSave) {

                        Toast.makeText(getContext(), constantStr[3], Toast.LENGTH_SHORT).show();
                        break;
                    }
                    final HistItem histItem = new HistItem();

                    int saveDate = Integer.valueOf(eventStartYY.getText().toString()) * 10000 +
                            Integer.valueOf(eventStartMM.getText().toString()) * 100 +
                            Integer.valueOf(eventStartDD.getText().toString());
                    histItem.stDate = "" + saveDate;

                    histItem.days = value1.getText().toString();
                    histItem.weeks = value2.getText().toString();
                    histItem.months = value3.getText().toString();
                    histItem.years = value4.getText().toString();
                    histItem.beOrAf = "" + spnBeAf.getSelectedItemPosition();

                    String endDateStr = result_eventday.getText().toString().trim();
                    String[] splitedStr = endDateStr.split("-");

                    saveDate = Integer.valueOf(splitedStr[0]) * 10000 +
                            Integer.valueOf(splitedStr[1]) * 100 +
                            Integer.valueOf(splitedStr[2]);
                    histItem.enDate = "" + saveDate;

                    // input memo of eventday.
                    LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
                    View mView = layoutInflaterAndroid.inflate(R.layout.input_dialogbox, null);
                    AlertDialog.Builder inputDialog = new AlertDialog.Builder(getContext());
                    inputDialog.setView(mView);

                    final EditText editTextInputMemo = (EditText) mView.findViewById(R.id.inputMemo);
                    inputDialog.setCancelable(false)
                            .setPositiveButton(constantStr[5], new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    histItem.name = editTextInputMemo.getText().toString();
                                    EventdayItemOpenHelper helper = new EventdayItemOpenHelper(getActivity());
                                    final SQLiteDatabase db = helper.getWritableDatabase();

                                    long ret = helper.insertEventday(db, histItem);
                                    Log.d(LOGTAG, "(eventday) name:" + histItem.name + ", insertedId:" + ret);
                                    //Toast.makeText(getContext(), "(eventday) name:" + histItem.name + ", insertedId:" + ret, Toast.LENGTH_SHORT).show();
                                    helper.close();

                                    ableToSave = false;
                                }
                            })

                            .setNegativeButton(constantStr[6],
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogBox, int id) {
                                            dialogBox.cancel();
                                        }
                                    });

                    AlertDialog alertDialogAndroid = inputDialog.create();
                    alertDialogAndroid.show();
                    alertDialogAndroid.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                    alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);

                    break;
            }
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

        //builder.show();
        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
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
        btnEventDaySave = (Button) view.findViewById(R.id.btnEventdaySave);

        btnCalcEvent.setOnClickListener(this);
        btnEventDaySave.setOnClickListener(this);

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
}