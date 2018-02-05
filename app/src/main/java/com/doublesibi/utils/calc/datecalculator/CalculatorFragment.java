package com.doublesibi.utils.calc.datecalculator;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalculatorFragment extends Fragment implements View.OnClickListener {
    private static String LOG_TAG = "DayCalc";

    private static final double CONSUMPTION_TAX_RATE = 0.08;

    private EditText result1;
    private TextView result2;
    private ArrayList<Button> arrayNumBtnList;

    private Button btnDelete, btnClear;

    private Button btnDot;
    private Button btnCalcPlus, btnCalcMinus, btnCalcDivide, btnCalcMultiply;
    private Button btnCalcTaxInc, btnCalcTaxExc, btnCalcResult;

    private boolean bDotClicked = false;
    private boolean bCalculated = false;
    private boolean bTaxCalc = false;

    private String taxCalculatedResult = "";

    public CalculatorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calculator, container, false);
        result1 = (EditText)view.findViewById(R.id.result1);
        result2 = (TextView)view.findViewById(R.id.result2);
        arrayNumBtnList = new ArrayList<>();

        setButtonId(view);

        return view;
    }

    private String getCalculatedValue(String result1Str) {
        if (bCalculated) {
            String param[] = result1Str.split("=");
            return param[1];
        } else {
            return "";
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        String strTmp2;
        int clickedId = v.getId();;

        String strTmp1 = "";
        String result1Str = result1.getText().toString();
        String result2Str = result2.getText().toString();
        int result1Len = result1Str.length();
        int result2Len = result2Str.length();

        Button clickedBtn = (Button) v.findViewById(clickedId);
        String btnString = clickedBtn.getText().toString();
        Log.d(LOG_TAG, "result 1 : [" + result1Len + "], [" + result1Str + "]");
        Log.d(LOG_TAG, "result 2 : [" + result2Len + "], [" + result2Str + "]");
        Log.d(LOG_TAG, "btn string : " + btnString);
        Log.d(LOG_TAG, "bCalculated: " + bCalculated + ", bDotClicked:" + bDotClicked);

        switch(clickedId) {
            case R.id._1:
            case R.id._2:
            case R.id._3:
            case R.id._4:
            case R.id._5:
            case R.id._6:
            case R.id._7:
            case R.id._8:
            case R.id._9:
                 result2.setText("" + result2Str + btnString);
                break;
            case R.id._0:
            case R.id._00:
                if (result2Len > 0) {
                    result2.setText(result2Str + btnString);
                } else {
                    break;
                }
                break;
            case R.id._dot:
                if (!bDotClicked) {
                    bDotClicked = true;
                    if (result2Len > 0) {
                        result2.setText(result2Str + ".");
                    } else {
                        result2.setText(result2Str + "0.");
                    }
                } else {
                    // do nothing.
                }
                break;
            case R.id.clear:
                if (result2Len > 0) {
                    result2.setText("");
                    bDotClicked = false;
                } else {
                    result1.setText("");
                    bCalculated = false;
                }
                break;
            case R.id.delete:
                if (result2Len > 0) {
                    if (result2Str.charAt(result2Len - 1) == '.') {
                        bDotClicked = false;
                        strTmp1 = result2Str.substring(0, result2Len - 2);
                    } else {
                        strTmp1 = result2Str.substring(0, result2Len - 1);
                    }
                    result2.setText(strTmp1);
                } else {
                    break;
                }
                break;
            case R.id.calcPlus:
            case R.id.calcMinus:
            case R.id.calcDivide:
            case R.id.calcMultiply:
                if (result1Len > 0) {
                    if (bCalculated) {
                        strTmp1 = getCalculatedValue(result1Str);
                        if (result2Len > 0) {
                            result1.setText(result2Str + btnString);
                            result2.setText("");
                        } else /* result2Len <= 0 */ {
                            result1.setText(strTmp1 + btnString);
                        }
                        bCalculated = false;
                    } else /* bCalculated = false */ {
                        if (result2Len > 0) {
                            result1.setText(result1Str + result2Str + btnString);
                            result2.setText("");
                        } else /* result2Len <= 0 */ {
                            Log.d(LOG_TAG, "result1Str:" + result1Str);
                            Log.d(LOG_TAG, "result1Str(sub):" + result1Str.substring(0, result1Len - 1));
                            result1.setText(result1Str.substring(0, result1Len - 1) + btnString);
                        }
                    }
                } else  /* result1Len <= 0 */ {
                    if (bCalculated) {
                        /* impossible */
                        Log.w(LOG_TAG,"case of impossible.(result1 is null, calculated is true.)");
                        if (result2Len > 0) {
                        } else /* result2Len <= 0 */ {
                        }
                    } else /* bCalculated = false */ {
                        if (result2Len > 0) {
                            result1.setText(result2Str + btnString);
                            result2.setText("");
                        } else /* result2Len <= 0 */ {
                            /* do nothing */
                        }
                    }
                }
                break;
            case R.id.calcTaxInc:
            case R.id.calcTaxExc:
                if (result2Len > 0) {
                    result2.setText(this.calcTax(clickedId, result2Str));
                }
                break;
            case R.id.calcResult:
                if (result2Len > 0) {
                    if (result1Len > 0) {
                        if(bCalculated) {
                            break;
                        } else {
                            strTmp1 = result1Str + result2Str + "=";

                            strTmp2 = calculate(strTmp1);
                            bCalculated = true;
                            bDotClicked = false;

                            result1.setText(strTmp1 + strTmp2);
                            result2.setText("");
                        }
                    } else {
                        break;
                    }
                } else {
                    if (result1Len > 0) {
                        if (bCalculated) {
                            break;
                        } else {
                            strTmp1 = result2Str.substring(0, result2Len - 1);

                            strTmp2 = calculate(strTmp1);
                            calculate(strTmp1);
                            bCalculated = true;
                            bDotClicked = false;

                            result1.setText(strTmp1 + " = " + strTmp2);
                            result2.setText("");
                        }
                    } else {
                        break;
                    }
                }
                break;
        }
        result1.setTextSize((float) 24);
    }

    private String calculate(String strExprssion) {
        String strTmp1, strTmp2, strTmp3, strTmp4;
        double dTmp1;
        String atoken = null;
        ArrayList<String> numsArray = new ArrayList<>();
        ArrayList<String> opersArray = new ArrayList<>();
        StringTokenizer token = new StringTokenizer(strExprssion, "+-x÷=");
        while(token.hasMoreTokens()) {
            atoken = token.nextToken();
            numsArray.add(atoken);
        }

        token = new StringTokenizer(strExprssion, "1234567890.");
        while(token.hasMoreTokens()) {
            atoken = token.nextToken();
            opersArray.add(atoken);
        }

        while(opersArray.size() > 1) {

            strTmp1 = opersArray.get(0);
            if (strTmp1.equals("=")) {
                break;
            } else if (strTmp1.equals("x") || strTmp1.equals("÷")) {
                strTmp2 = numsArray.get(0);
                strTmp3 = numsArray.get(1);

                dTmp1 = calc(strTmp1, Double.parseDouble(strTmp2), Double.parseDouble(strTmp3));

                opersArray.remove(0);
                numsArray.remove(0);
                numsArray.set(0, String.valueOf(dTmp1));
            } else if (strTmp1.equals("+") || strTmp1.equals("-")) {
                strTmp2 = opersArray.get(1);
                if (strTmp2.equals("+") || strTmp2.equals("-")) {
                    strTmp3 = numsArray.get(0);
                    strTmp4 = numsArray.get(1);

                    dTmp1 = calc(strTmp1, Double.parseDouble(strTmp3), Double.parseDouble(strTmp4));

                    opersArray.remove(0);
                    numsArray.remove(0);
                    numsArray.set(0, String.valueOf(dTmp1));
                } else if (strTmp2.equals("x") || strTmp2.equals("÷")) {
                    strTmp3 = numsArray.get(1);
                    strTmp4 = numsArray.get(2);

                    dTmp1 = calc(strTmp2, Double.parseDouble(strTmp3), Double.parseDouble(strTmp4));

                    opersArray.remove(1);
                    numsArray.remove(1);
                    numsArray.set(1, String.valueOf(dTmp1));
                } else if (strTmp2.equals("=")) {
                    strTmp3 = numsArray.get(0);
                    strTmp4 = numsArray.get(1);

                    dTmp1 = calc(strTmp1, Double.parseDouble(strTmp3), Double.parseDouble(strTmp4));

                    opersArray.remove(0);
                    numsArray.remove(0);
                    numsArray.set(0, String.valueOf(dTmp1));
                }
            }
            Log.d(LOG_TAG,"result: nums:[" +numsArray.toString() + "]" + opersArray.toString());
        }

        String[] strParam = numsArray.get(0).split("\\.", -1);
        if (strParam.length == 2) {
            String strSbubstr = strParam[1].substring(0, 1);
            if (strSbubstr.equals("0"))
                return strParam[0];
            else
                return strParam[0] + "." + strSbubstr;
        } else {
            return strParam[0];
        }
    }

    private double calc(String oper, double left, double right) {
        if (oper.equals("+")) {
            return left + right;
        } else if (oper.equals("-")) {
            return left - right;
        } else if (oper.equals("x")) {
            return left * right;
        } else if (oper.equals("÷")) {
            return left / right;
        } else {
            return 0.0;
        }
    }

    private String calcTax(int clickedId, String strNum) {
        long lValue = 0L;
        switch(clickedId) {
            case R.id.calcTaxInc:
                lValue =  (long)((Long.parseLong(strNum) * 100 * (1 + CONSUMPTION_TAX_RATE)) / 100);
                break;
            case R.id.calcTaxExc:
                lValue =  (long)(((Long.parseLong(strNum) * 100) / (1 + CONSUMPTION_TAX_RATE)) / 100);
                break;
        }

        return String.valueOf(lValue);
    }

    void setButtonId(View v) {

        arrayNumBtnList.add((Button)v.findViewById(R.id._1));
        arrayNumBtnList.add((Button)v.findViewById(R.id._2));
        arrayNumBtnList.add((Button)v.findViewById(R.id._3));
        arrayNumBtnList.add((Button)v.findViewById(R.id._4));
        arrayNumBtnList.add((Button)v.findViewById(R.id._5));
        arrayNumBtnList.add((Button)v.findViewById(R.id._6));
        arrayNumBtnList.add((Button)v.findViewById(R.id._7));
        arrayNumBtnList.add((Button)v.findViewById(R.id._8));
        arrayNumBtnList.add((Button)v.findViewById(R.id._9));
        arrayNumBtnList.add((Button)v.findViewById(R.id._0));
        arrayNumBtnList.add((Button)v.findViewById(R.id._00));
        for (Button btnItem : arrayNumBtnList) {
            btnItem.setOnClickListener(this);
        }

        btnDot = (Button)v.findViewById(R.id._dot);
        btnCalcPlus = (Button)v.findViewById(R.id.calcPlus);
        btnCalcMinus = (Button)v.findViewById(R.id.calcMinus);
        btnCalcDivide = (Button)v.findViewById(R.id.calcDivide);
        btnCalcMultiply = (Button)v.findViewById(R.id.calcMultiply);
        btnCalcTaxInc = (Button)v.findViewById(R.id.calcTaxInc);
        btnCalcTaxExc= (Button)v.findViewById(R.id.calcTaxExc);

        btnDot.setOnClickListener(this);
        btnCalcPlus.setOnClickListener(this);
        btnCalcMinus.setOnClickListener(this);
        btnCalcDivide.setOnClickListener(this);
        btnCalcMultiply.setOnClickListener(this);
        btnCalcTaxInc.setOnClickListener(this);
        btnCalcTaxExc.setOnClickListener(this);

        btnDelete = (Button)v.findViewById(R.id.delete);
        btnClear = (Button)v.findViewById(R.id.clear);

        btnDelete.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        btnCalcResult = (Button)v.findViewById(R.id.calcResult);

        btnCalcResult.setOnClickListener(this);
    }
}
