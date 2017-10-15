package com.doublesibi.utils.calc.datecalculator;


import android.graphics.Color;
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

    private EditText inputedStatement2;
    private TextView currInputTxt;
    private ArrayList<Button> arrayNumBtnList;

    private Button btnDelete, btnClear;

    private Button btnDot;
    private Button btnCalcPlus, btnCalcMinus, btnCalcDivide, btnCalcMultiply;
    private Button btnCalcTaxInc, btnCalcTaxExc, btnCalcResult;

    private boolean bDotClicked = false;
    private boolean bCalculated = false;
    private int nCalculatable = 0;

    private String strTaxCalculate;
    private boolean bTaxCalculated;

    public CalculatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calculator, container, false);
        inputedStatement2 = (EditText)view.findViewById(R.id.inputedStatement2);
        currInputTxt = (TextView)view.findViewById(R.id.currInput);
        arrayNumBtnList = new ArrayList<>();

        setButtonId(view);

        return view;
    }

    @Override
    public void onClick(View v) {
        String strTmp1, strTmp2;
        String strRtn = null;
        int nTmp1;
        double dTmp1, dTmp2;
        int clickedId = 0;
        Button numBtn;
        Button operBtn;

        clickedId = v.getId();
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
                numBtn = (Button) v.findViewById(clickedId);
                numBtn.getText();
                currInputTxt.setText(currInputTxt.getText().toString() + numBtn.getText().toString());
                break;
            case R.id._0:
            case R.id._00:
                if (currInputTxt.getText().length() > 0 || bDotClicked) {
                    numBtn = (Button) v.findViewById(clickedId);
                    numBtn.getText();
                    currInputTxt.setText(currInputTxt.getText().toString() + numBtn.getText().toString());
                }
                break;
            case R.id._dot:
                if (!bDotClicked) {
                    currInputTxt.setText("0");
                }
                currInputTxt.setText(currInputTxt.getText().toString() + ".");
                bDotClicked = true;
                break;
            case R.id.clear:
                inputedStatement2.setText("");
                currInputTxt.setText("");
                bDotClicked = false;
                bCalculated = false;
                break;
            case R.id.delete:
                if (currInputTxt.getText().length() > 0) {
                    if (bDotClicked || currInputTxt.getText().toString().equals("0.")) {
                        currInputTxt.setText("");
                        bDotClicked = false;
                        break;
                    }

                    strTmp1 = currInputTxt.getText().toString();
                    nTmp1 = strTmp1.length();
                    strTmp2 = strTmp1.substring(0, nTmp1 - 1);

                    currInputTxt.setText(strTmp2);
                }
                break;
            case R.id.calcPlus:
            case R.id.calcMinus:
            case R.id.calcDivide:
            case R.id.calcMultiply:
                if (bCalculated) {
                    inputedStatement2.setText("");
                    bCalculated=false;
                    nCalculatable=0;
                }
                operBtn = (Button) v.findViewById(clickedId);
                if (currInputTxt.getText().length() > 0) {
                    String tmp = inputedStatement2.getText().toString();
                    inputedStatement2.setText(tmp + currInputTxt.getText().toString() + operBtn.getText().toString().trim());
                    nCalculatable ++;
                    currInputTxt.setText("");
                }
                break;
            case R.id.calcTaxInc:
                if (currInputTxt.getText().length() > 0) {
                    strTaxCalculate = currInputTxt.getText().toString();
                    dTmp1 = Integer.parseInt(currInputTxt.getText().toString()) * (1 + CONSUMPTION_TAX_RATE);
                    dTmp1 = Math.floor(dTmp1);
                    currInputTxt.setText("" + (int) dTmp1);

                    bTaxCalculated = true;
                }
                break;
            case R.id.calcTaxExc:
                if (currInputTxt.getText().length() > 0) {
                    strTaxCalculate = currInputTxt.getText().toString();
                    dTmp1 = Integer.parseInt(currInputTxt.getText().toString()) / (1 + CONSUMPTION_TAX_RATE);
                    currInputTxt.setText("" + (int) (dTmp1 + 0.5));

                    bTaxCalculated = true;
                }
                break;
            case R.id.calcResult:
                if (nCalculatable == 0) {
                    break;
                } else if (nCalculatable == 1) {
                    if (currInputTxt.getText().length() > 0) {
                        strTmp1 = inputedStatement2.getText().toString();
                        inputedStatement2.setText(strTmp1 + currInputTxt.getText().toString() + "=");

                        currInputTxt.setText("");
                        nCalculatable++;
                    } else {
                        break;
                    }
                }

                if (nCalculatable > 1) {
                    if (currInputTxt.getText().length() > 0) {
                        strTmp1 = inputedStatement2.getText().toString();
                        inputedStatement2.setText(strTmp1 + currInputTxt.getText().toString() + "=");
                        currInputTxt.setText("");
                        nCalculatable++;
                    }

                    strTmp1 = inputedStatement2.getText().toString();
                    strTmp2 = strTmp1.substring(0, strTmp1.length()-1);
                    inputedStatement2.setText(strTmp2 + "=");

                    // ...
                    strRtn = calculate(inputedStatement2.getText().toString());
                    // ...

                    dTmp1 = Double.parseDouble(strRtn);
                    dTmp2 = dTmp1 % 1;
                    if (dTmp2 == 0.0) {
                        inputedStatement2.setText(inputedStatement2.getText().toString() + "  " + (int)dTmp1);
                    } else {
                        inputedStatement2.setText(inputedStatement2.getText().toString() + "  " + dTmp1);
                    }

                    bCalculated = true;
                    nCalculatable = 0;
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
