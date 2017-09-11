package com.doublesibi.utils.calc.datecalculator;


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

/**
 * A simple {@link Fragment} subclass.
 */
public class CalculatorFragment extends Fragment implements View.OnClickListener {
    private static String LOG_TAG = "DayCalc";

    private TextView inputedStatement;
    private TextView currInputTxt;

    boolean bDotClicked = false;

    public CalculatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calculator, container, false);
        inputedStatement = (TextView)view.findViewById(R.id.currInput);
        currInputTxt = (TextView)view.findViewById(R.id.currInput);

        setButtonId(view);

        return view;
    }

    @Override
    public void onClick(View v) {
        String strTmp1, strTmp2;
        int nTmp1, nTmp2;
        int clickedId = 0;
        Button numBtn;

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
                numBtn = (Button)v.findViewById(clickedId);
                numBtn.getText();
                currInputTxt.setText(currInputTxt.getText().toString()+numBtn.getText().toString());
                break;
            case R.id._0:
            case R.id._00:
                if (currInputTxt.getText().length() > 0) {
                    numBtn = (Button)v.findViewById(clickedId);
                    numBtn.getText();
                    currInputTxt.setText(currInputTxt.getText().toString()+numBtn.getText().toString());
                }
                break;
            case R.id._dot:
                if (!bDotClicked) {
                    numBtn = (Button)v.findViewById(clickedId);
                    numBtn.getText();
                    currInputTxt.setText(currInputTxt.getText().toString()+numBtn.getText().toString());
                    bDotClicked = true;
                }
                break;
            case R.id.clear:
                inputedStatement.setText("");
                currInputTxt.setText("");
                if(bDotClicked)
                    bDotClicked = !bDotClicked;
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
                Button operBtn = (Button)v.findViewById(clickedId);

                Log.d(LOG_TAG, "clicked operation button : " + operBtn.getText().toString());

                inputedStatement.setText(inputedStatement.getText().toString()
                        + operBtn.getText().toString()
                        + currInputTxt.getText().toString());
                currInputTxt.setText("");
                break;
            case R.id.calcTax:
                break;
            case R.id.calcResult:
                break;
        }

        Log.d(LOG_TAG, "Input Text : " + currInputTxt.getText().toString());
    }

    void setButtonId(View v) {
        v.findViewById(R.id._0).setOnClickListener(this);
        v.findViewById(R.id._1).setOnClickListener(this);
        v.findViewById(R.id._2).setOnClickListener(this);
        v.findViewById(R.id._3).setOnClickListener(this);
        v.findViewById(R.id._4).setOnClickListener(this);
        v.findViewById(R.id._5).setOnClickListener(this);
        v.findViewById(R.id._6).setOnClickListener(this);
        v.findViewById(R.id._7).setOnClickListener(this);
        v.findViewById(R.id._8).setOnClickListener(this);
        v.findViewById(R.id._9).setOnClickListener(this);
        v.findViewById(R.id._00).setOnClickListener(this);
        v.findViewById(R.id._dot).setOnClickListener(this);

        v.findViewById(R.id.delete).setOnClickListener(this);
        v.findViewById(R.id.clear).setOnClickListener(this);
        v.findViewById(R.id.calcPlus).setOnClickListener(this);
        v.findViewById(R.id.calcMinus).setOnClickListener(this);
        v.findViewById(R.id.calcDivide).setOnClickListener(this);
        v.findViewById(R.id.calcMultiply).setOnClickListener(this);
        v.findViewById(R.id.calcTax).setOnClickListener(this);
        v.findViewById(R.id.calcResult).setOnClickListener(this);
    }
}
