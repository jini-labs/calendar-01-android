package com.doublesibi.utils.calc.datecalculator;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.doublesibi.utils.calc.datecalculator.hist.DurationItemOpenHelper;


public class DurationHistActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duration_hist);

        findViewById(R.id.btnDuraReturn).setOnClickListener(this);

        DurationItemOpenHelper helper = new DurationItemOpenHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        // queryメソッドの実行例
        Cursor c = db.query("DateDuration", new String[] {  "stDate",
                                                            "enDate",
                                                            "days",
                                                            "weeks",
                                                            "weekdays",
                                                            "months",
                                                            "monthdays",
                                                            "years",
                                                            "yearmonths",
                                                            "yeardays" }, null,
        null, null, null, null);

        TextView textView = (TextView)findViewById(R.id.textView);
        String text = null;
        boolean mov = c.moveToFirst();
        while (mov) {
            text = "[stDate:"      + c.getString(0) +
                    ", enDate:"     + c.getString(1) +
                    ", days:"       + c.getString(2) +
                    ", weeks:"      + c.getString(3) +
                    ", weekdays:"   + c.getString(4) +
                    ", months:"     + c.getString(5) +
                    ", monthdays:"  + c.getString(6) +
                    ", years:"      + c.getString(7) +
                    ", yearmonths:" + c.getString(8) +
                    ", yeardays:"   + c.getString(9) + "]";

            if (textView.getText().toString().length() > 0)
                text = textView.getText().toString() + "\n" + text;

            textView.setText(text);
            mov = c.moveToNext();
        }
        c.close();
        db.close();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnDuraReturn) {
            finish();
        }
    }
}
