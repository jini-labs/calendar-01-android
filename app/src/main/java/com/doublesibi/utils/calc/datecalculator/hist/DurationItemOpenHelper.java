package com.doublesibi.utils.calc.datecalculator.hist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hunajini on 2017/01/24.
 */

public class DurationItemOpenHelper extends SQLiteOpenHelper {
    private final static String dbname = "DurationHistDB";
    private final static String tablename = "DateDuration";

    public DurationItemOpenHelper(Context context) {
        super(context, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS " +
                tablename + "(" +
                "stDate text not null," +
                "enDate text not null," +
                "name text not null," +
                "days text," +
                "weeks text," +
                "weekdays text," +
                "months text," +
                "monthdays text," +
                "years text," +
                "yearmonths text," +
                "yeardays text," +
                "temp1 text," +
                "temp2 text," +
                "temp3 text," +
                "temp4 text," +
                "primary key(stDate, enDate)" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int deleteDuration(SQLiteDatabase db, String stDate, String enDate) {

        return db.delete(tablename, "stDate=? and enDate=?", new String[] {stDate, enDate});
    }

    public long insertDuration(SQLiteDatabase db, HistItem item) {
        ContentValues insertValues = new ContentValues();
        insertValues.put("stDate", item.stDate);
        insertValues.put("enDate", item.enDate);
        insertValues.put("name", item.name);
        insertValues.put("days", item.days);
        insertValues.put("weeks", item.weeks);
        insertValues.put("weekdays", item.weekdays);
        insertValues.put("months", item.months);
        insertValues.put("monthdays", item.monthdays);
        insertValues.put("years", item.years);
        insertValues.put("yearmonths", item.yearmonths);
        insertValues.put("yeardays", item.yeardays);

        return db.insert(tablename, "stDate", insertValues);
    }
}
