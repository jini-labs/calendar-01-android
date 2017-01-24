package com.doublesibi.utils.calc.datecalculator;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.doublesibi.utils.calc.datecalculator.hist.EventdayItemOpenHelper;

public class EventHistActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_hist);

        findViewById(R.id.btnEventReturn).setOnClickListener(this);

        EventdayItemOpenHelper helper = new EventdayItemOpenHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        // queryメソッドの実行例
        Cursor c = db.query("Eventday", new String[] {"stDate",
                        "days",
                        "weeks",
                        "months",
                        "years",
                        "beOrAf",
                        "enDate"}, null,
                null, null, null, null);

        TextView textView = (TextView)findViewById(R.id.textView);
        String text = null;
        boolean mov = c.moveToFirst();
        while (mov) {
            text = "[stDate:"   + c.getString(0) +
                    ", days:"   + c.getString(1) +
                    ", weeks:"  + c.getString(2) +
                    ", months:" + c.getString(3) +
                    ", years:"  + c.getString(4) +
                    ", beOrAf:" + c.getString(5) +
                    ", enDate:" + c.getString(6) +
                     "]";

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
        if(v.getId() == R.id.btnEventReturn) {
            finish();
        }
    }
}
