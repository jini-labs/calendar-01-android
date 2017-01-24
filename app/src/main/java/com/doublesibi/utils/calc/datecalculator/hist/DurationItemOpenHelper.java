package com.doublesibi.utils.calc.datecalculator.hist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hunajini on 2017/01/24.
 */

public class DurationItemOpenHelper extends SQLiteOpenHelper {

    public DurationItemOpenHelper(Context context) {
        super(context, "DurationHistDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS DateDuration(" +
                "stDate text not null," +
                "enDate  text not null," +
                "days text," +
                "weeks text," +
                "weekdays text," +
                "months text," +
                "monthdays text," +
                "years text," +
                "yearmonths text," +
                "yeardays text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertDuration(SQLiteDatabase db, HistItem item) {
        ContentValues insertValues = new ContentValues();
        insertValues.put("stDate", item.stDate);
        insertValues.put("enDate", item.enDate);
        insertValues.put("days", item.days);
        insertValues.put("weeks", item.weeks);
        insertValues.put("weekdays", item.weekdays);
        insertValues.put("months", item.months);
        insertValues.put("monthdays", item.monthdays);
        insertValues.put("years", item.years);
        insertValues.put("yearmonths", item.yearmonths);
        insertValues.put("yeardays", item.yeardays);

        return db.insert("DateDuration", "stDate", insertValues);
    }
}
