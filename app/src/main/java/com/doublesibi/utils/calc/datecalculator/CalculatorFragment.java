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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalculatorFragment extends Fragment implements View.OnClickListener {
    private static String LOG_TAG = "DayCalc";

    private static final double CONSUMPTION_TAX_RATE = 0.08;

    private EditText inputedStatement;
    private TextView currInputTxt;
    private ArrayList<Button> arrayNumBtnList;

    private Button btnDelete, btnClear;

    private Button btnDot;
    private Button btnCalcPlus, btnCalcMinus, btnCalcDivide, btnCalcMultiply;
    private Button btnCalcTax, btnCalcResult;

    boolean bDotClicked = false;

    public CalculatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calculator, container, false);
        inputedStatement = (EditText)view.findViewById(R.id.inputedStatement);
        currInputTxt = (TextView)view.findViewById(R.id.currInput);
        arrayNumBtnList = new ArrayList<>();

        setButtonId(view);

        return view;
    }

    @Override
    public void onClick(View v) {
        String strTmp1, strTmp2, strTmp3, strTmp4;
        String strRtn = null;
        int nTmp1, nTmp2;
        double dTmp1, dTmp2, dTmp3;
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
                if (currInputTxt.getText().length() > 0) {
                    numBtn = (Button) v.findViewById(clickedId);
                    numBtn.getText();
                    currInputTxt.setText(currInputTxt.getText().toString() + numBtn.getText().toString());
                }
                break;
            case R.id._dot:
                if (!bDotClicked) {
                    numBtn = (Button) v.findViewById(clickedId);
                    numBtn.getText();
                    currInputTxt.setText(currInputTxt.getText().toString() + numBtn.getText().toString());
                    bDotClicked = true;
                }
                break;
            case R.id.clear:
                inputedStatement.setText("");
                currInputTxt.setText("");
                if (bDotClicked)
                    bDotClicked = !bDotClicked;

                for (Button btnItem : arrayNumBtnList) {
                    btnItem.setClickable(true);
                }

                for (Button btnItem : arrayNumBtnList) {
                    btnItem.setClickable(true);
                }

                disalbeButton(v, true, 10,10,10);
                break;
            case R.id.delete:
                if (currInputTxt.getText().length() > 0) {
                    strTmp1 = currInputTxt.getText().toString();
                    nTmp1 = strTmp1.length();
                    if (strTmp1.charAt(nTmp1 - 1) == '.') {
                        bDotClicked = !bDotClicked;
                    }
                    strTmp2 = strTmp1.substring(0, nTmp1 - 1);

                    currInputTxt.setText(strTmp2);
                }
                break;
            case R.id.calcPlus:
            case R.id.calcMinus:
            case R.id.calcDivide:
            case R.id.calcMultiply:
                if (currInputTxt.getText().length() > 0) {
                    operBtn = (Button) v.findViewById(clickedId);

                    Log.d(LOG_TAG, "clicked operation button : " + operBtn.getText().toString());

                    if (inputedStatement.getText().length() > 0) {
                        inputedStatement.setText(inputedStatement.getText().toString()
                                + currInputTxt.getText().toString()
                                + operBtn.getText().toString());
                    } else {
                        inputedStatement.setText(currInputTxt.getText().toString() + operBtn.getText().toString());
                    }

                    //Log.d(LOG_TAG, "inputedStatement : " + inputedStatement.getText());
                    currInputTxt.setText("");
                }
                break;
            case R.id.calcTax:
                if (btnCalcTax.getText().toString().equals("税込")) {
                    strTmp1 = currInputTxt.getText().toString();
                    dTmp1 = Integer.parseInt(strTmp1) * (1 + CONSUMPTION_TAX_RATE);
                    currInputTxt.setText("" + (int) dTmp1);

                    btnCalcTax.setText("税抜");
                } else {
                    strTmp1 = currInputTxt.getText().toString();
                    dTmp1 = Integer.parseInt(strTmp1) / (1 + CONSUMPTION_TAX_RATE);
                    currInputTxt.setText("" + (int) dTmp1);

                    btnCalcTax.setText("税込");
                }
                break;
            case R.id.calcResult:
                if (inputedStatement.getText().length() > 0) {
                    if (currInputTxt.getText().length() > 0) {
                        inputedStatement.setText(inputedStatement.getText().toString() + currInputTxt.getText().toString() + "=");
                    } else {
                        strTmp1 = inputedStatement.getText().toString();
                        strTmp2 = strTmp1.substring(0, strTmp1.length()-1);
                        strTmp2 = strTmp2 + "=";
                        inputedStatement.setText(strTmp2);
                    }

                    disalbeButton(v, false, 200,196,196);
                    // ...
                    strRtn = calculate(inputedStatement.getText().toString());
                    // ...

                    dTmp1 = Double.parseDouble(strRtn);
                    dTmp2 = dTmp1 % 1;
                    if (dTmp2 == 0.0) {
                        inputedStatement.setText(inputedStatement.getText().toString() + "  " + (int)dTmp1);
                    } else {
                        inputedStatement.setText(inputedStatement.getText().toString() + "  " + dTmp1);
                    }

                    currInputTxt.setText("");

                } else {
                    if (currInputTxt.getText().length() > 0) {
                    } else {
                    }
                }

                break;
        }

        //Log.d(LOG_TAG, "Input Text : " + currInputTxt.getText().toString() + ", inputedStat:" + inputedStatement.getText().toString());
    }

    private String calculate(String strExprssion) {
        String strTmp1, strTmp2, strTmp3, strTmp4;
        int nTmp1, nTmp2;
        double dTmp1, dTmp2, dTmp3;
        String atoken = null;
        ArrayList<String> numsArray = new ArrayList<>();
        ArrayList<String> opersArray = new ArrayList<>();
        StringTokenizer token = new StringTokenizer(strExprssion, "+-x/=");
        while(token.hasMoreTokens()) {
            atoken = token.nextToken();
            numsArray.add(atoken);
        }

        token = new StringTokenizer(strExprssion, "1234567890.");
        while(token.hasMoreTokens()) {
            atoken = token.nextToken();
            opersArray.add(atoken);
        }

        Log.d(LOG_TAG, "opersArray.size:" + opersArray.size() + ", numsArray.size:" + numsArray.size());

        while(opersArray.size() > 1) {

            strTmp1 = opersArray.get(0);
            if (strTmp1.equals("=")) {
                break;
            } else if (strTmp1.equals("x") || strTmp1.equals("/")) {
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
                } else if (strTmp2.equals("x") || strTmp2.equals("/")) {
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
        } else if (oper.equals("/")) {
            return left / right;
        } else {
            return 0.0;
        }
    }
    void disalbeButton(View v, boolean flag, int r, int g, int b) {

        for (Button btnItem : arrayNumBtnList) {
            btnItem.setClickable(flag);
            btnItem.setTextColor(Color.rgb(r, g, b));
        }

        for (Button btnItem : arrayNumBtnList) {
            btnItem.setClickable(flag);
            btnItem.setTextColor(Color.rgb(r,g,b));
        }

        btnDelete.setClickable(flag);
        btnDot.setClickable(flag);

        btnDelete.setTextColor(Color.rgb(r,g,b));
        btnDot.setTextColor(Color.rgb(r, g, b));

        btnCalcPlus.setTextColor(Color.rgb(r,g,b));
        btnCalcMinus.setTextColor(Color.rgb(r,g,b));
        btnCalcDivide.setTextColor(Color.rgb(r,g,b));
        btnCalcMultiply.setTextColor(Color.rgb(r,g,b));

        btnCalcResult.setTextColor(Color.rgb(r,g,b));
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
        btnCalcTax = (Button)v.findViewById(R.id.calcTax);

        btnDot.setOnClickListener(this);
        btnCalcPlus.setOnClickListener(this);
        btnCalcMinus.setOnClickListener(this);
        btnCalcDivide.setOnClickListener(this);
        btnCalcMultiply.setOnClickListener(this);
        btnCalcTax.setOnClickListener(this);

        btnDelete = (Button)v.findViewById(R.id.delete);
        btnClear = (Button)v.findViewById(R.id.clear);

        btnDelete.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        btnCalcResult = (Button)v.findViewById(R.id.calcResult);

        btnCalcResult.setOnClickListener(this);
    }
}
