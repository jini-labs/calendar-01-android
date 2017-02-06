package com.doublesibi.utils.calc.datecalculator.hist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hunajini on 2017/01/24.
 */

public class EventdayItemOpenHelper extends SQLiteOpenHelper {
    private final static String dbname = "EventdayHistDB";
    private final static String tablename = "Eventday";

    public EventdayItemOpenHelper(Context context) {
        super(context, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS "  +
                tablename + "(" +
                "name text not null," +
                "stDate text not null," +
                "days text," +
                "weeks text," +
                "months text," +
                "years text," +
                "beOrAf text," +
                "enDate text," +
                "temp1 text," +
                "temp2 text," +
                "temp3 text," +
                "temp4 text," +
                "primary key(stDate, name)" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int deleteEventday(SQLiteDatabase db, String stDate, String name) {

        return db.delete(tablename, "stDate=? and name=?", new String[] {stDate, name});
    }

    public long insertEventday(SQLiteDatabase db, HistItem item) {
        ContentValues insertValues = new ContentValues();
        insertValues.put("stDate", item.stDate);
        insertValues.put("days", item.days);
        insertValues.put("weeks", item.weeks);
        insertValues.put("months", item.months);
        insertValues.put("years", item.years);
        insertValues.put("beOrAf", item.beOrAf);
        insertValues.put("enDate", item.enDate);
        insertValues.put("name", item.name);

        return db.insert(tablename, "stDate", insertValues);
    }
}