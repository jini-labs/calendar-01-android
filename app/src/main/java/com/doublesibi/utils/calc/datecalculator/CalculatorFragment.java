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

        if (bCalculated && clickedId != R.id.calcResult) bCalculated = false;

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
                if (result2Len > 0) {
                    result2.setText(result2Str + ".");
                } else {
                    result2.setText(result2Str + "0.");
                }
                break;
            case R.id.clear:
                if (result2Len > 0) {
                    result2.setText("");
                } else {
                    result1.setText("");
                }
                break;
            case R.id.delete:
                if (result2Len > 0) {
                    if (result2Str.charAt(result2Len - 1) == '.') {
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
                if (result2Len > 0) {
                    if (result1Len > 0) {
                        if (bCalculated) {
                            String param[] = result1Str.split("=");
                            result1Str = param[1] + result2Str + btnString ;
                            result1.setText(result1Str);
                            result2.setText("");
                        } else {
                            result1.setText(result1Str + result2Str + btnString);
                            result2.setText("");
                        }
                    } else {
                        result1.setText(result2Str + btnString);
                        result2.setText("");
                    }
                } else {
                    if (result1Len > 0) {
                        if (bCalculated) {
                            break;
                        } else {
                            strTmp1 = result2Str.substring(0, result2Len - 1);
                            result1.setText(strTmp1 + btnString);
                        }
                    }
                }
                break;
            case R.id.calcTaxInc:

                break;
            case R.id.calcTaxExc:

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
                            bCalculated = true;

                            result1.setText(strTmp1 + " = " + strTmp2);
                            result2.setText("");
                        }
                    } else {
                        break;
                    }
                }
                break;
        }
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

        return numsArray.get(0);
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

    private double calcTax(double orgValue, double taxRate) {

        return orgValue * this.CONSUMPTION_TAX_RATE;
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
