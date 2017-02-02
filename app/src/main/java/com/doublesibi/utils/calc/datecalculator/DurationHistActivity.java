package com.doublesibi.utils.calc.datecalculator;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.doublesibi.utils.calc.datecalculator.hist.DurationHistItem;
import com.doublesibi.utils.calc.datecalculator.hist.DurationItemOpenHelper;
import com.doublesibi.utils.calc.datecalculator.holiday.MyCalendar;

import java.util.ArrayList;


public class DurationHistActivity extends AppCompatActivity {
    private final String LOGTAG = "DayCalc";

    private Integer per_page = 10;
    private int recordCount = 0;
    private int selectKey = 0;

    private View mFooter;
    private DurationHistAdaptor adapter;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duration_hist);

        adapter = new DurationHistAdaptor(DurationHistActivity.this);
        getDurationHistData(selectKey);

        listview = (ListView)findViewById(R.id.listView);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final DurationHistItem selItem=(DurationHistItem)listview.getItemAtPosition(position);
                final TextView v1 = (TextView) view.findViewById(R.id.durationStart);
                final TextView v2 = (TextView) view.findViewById(R.id.durationEnd);

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Delete...")
                        .setMessage(v1.getText().toString() + " ~ " + v2.getText().toString())
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                Log.d(LOGTAG, ", start: " + v1.getText() +", end: " + v2.getText());
//                                Toast.makeText(getApplicationContext(), "Delete record. (start:" + v1.getText().toString() +", end:" + v2.getText().toString(),
//                                        Toast.LENGTH_LONG).show();

                                if (deleteDurationHistData(selItem.getStartDate(), selItem.getEndDate())) {
                                    adapter.remove(selItem);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null);

                AlertDialog alert = builder.create();
                alert.show();
                alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);

                return false;
            }
        });

        listview.setAdapter(adapter);
        listview.addFooterView(getFooter());

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if ((totalItemCount - visibleItemCount) == firstVisibleItem) {
                    Integer ItemCount = totalItemCount - 1;

                    getDurationHistData(selectKey);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
            }
        });
    }

    private View getFooter() {
        if (mFooter == null) {
            mFooter = getLayoutInflater().inflate(R.layout.footer_duration_hist, null);
        }
        return mFooter;
    }

    private boolean deleteDurationHistData(String startDateWeekname, String endDateWeekname) {
        DurationItemOpenHelper helper = new DurationItemOpenHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String value1 = startDateWeekname.substring(0,4);
        String value2 = startDateWeekname.substring(5,7);
        String value3 = startDateWeekname.substring(8,10);
        String v1 = value1 + value2 + value3;

        value1 = endDateWeekname.substring(0,4);
        value2 = endDateWeekname.substring(5,7);
        value3 = endDateWeekname.substring(8,10);
        String v2 = value1 + value2 + value3;

        if (helper.deleteDuration(db, v1, v2) > 0) {
            Log.d(LOGTAG, "startDate:" + v1 + ", endDate:" + v2 + " deleted.!");
            return true;
        } else {
            Log.d(LOGTAG, "startDate:" + v1 + ", endDate:" + v2 + " delete fail.");
            return false;
        }
    }

    private void getDurationHistData(int startDate) {
        DurationItemOpenHelper helper = new DurationItemOpenHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns ={"stDate",
                            "enDate",
                            "days",
                            "weeks",
                            "weekdays",
                            "months",
                            "monthdays",
                            "years",
                            "yearmonths",
                            "yeardays" };

        Cursor c = db.query("DateDuration", columns,
                            "stDate>?", new String[] {""+startDate},
                            null,
                            null,
                            "stDate",
                            ""+this.per_page);

        boolean mov = c.moveToFirst();
        while (mov) {
            selectKey = Integer.parseInt(c.getString(0));

            recordCount ++;
            DurationHistItem item = new DurationHistItem();

            String temp1 = c.getString(0);
            String temp2 = MyCalendar.convertDateWeekName(getResources(), Integer.parseInt(temp1), "/");
            item.setStartDate(temp2);

            temp1 = c.getString(1);
            temp2 = MyCalendar.convertDateWeekName(getResources(), Integer.parseInt(temp1), "/");
            item.setEndDate(temp2);

            item.setDurDays(c.getString(2) + " 日");

            if (StrToInt(c.getString(3)) > 0 ) {
                item.setDurWeeksDays(c.getString(3) +
                        " 週, ");
                if (StrToInt(c.getString(4)) > 0 ) {
                    item.setDurWeeksDays(item.getDurWeeksDays() +
                            c.getString(4) +
                            " 日");
                }
            }
            if (StrToInt(c.getString(5)) > 0 ) {
                item.setDurMonthsDays(c.getString(5) +
                        "カ月, ");
                if (StrToInt(c.getString(6)) > 0 ) {
                    item.setDurMonthsDays(item.getDurMonthsDays() +
                            c.getString(6) +
                            " 日");
                }

            }
            if (StrToInt(c.getString(7)) > 0 ) {
                item.setDurYearsMonthsDays(c.getString(7)
                        + " 年, ");
                if (StrToInt(c.getString(8)) > 0) {
                    item.setDurYearsMonthsDays(item.getDurYearsMonthsDays() +
                            c.getString(8) +
                            " カ月, ");
                }
                if (StrToInt(c.getString(9)) > 0) {
                    item.setDurYearsMonthsDays(item.getDurYearsMonthsDays() +
                            c.getString(9) +
                            " 日");
                }
            }

            Log.d(LOGTAG, item.toString());
            adapter.setItems(item);
            mov = c.moveToNext();
        }
        c.close();
        db.close();
    }

    private int StrToInt(String str) {
        if (str == null)
            return 0;
        if (str.trim().length() == 0)
            return 0;
        return Integer.parseInt(str.trim());
    }
    public class DurationHistAdaptor extends BaseAdapter {
        Context context;
        LayoutInflater layoutInflater = null;
        ArrayList<DurationHistItem> items;

        public DurationHistAdaptor(Context context) {
            this.context = context;
            this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.items = new ArrayList<>();
        }

        public void setItems(DurationHistItem item) {
            this.items.add(item);
        }

        public void remove(DurationHistItem item) {
            this.items.remove(item);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.duration_hist_listview_item, parent, false);

            TextView stDateView = (TextView)convertView.findViewById(R.id.durationStart);
            stDateView.setText(items.get(position).getStartDate());
            stDateView.setTextColor(Color.rgb(35,91,164));

            TextView enDateView = (TextView)convertView.findViewById(R.id.durationEnd);
            enDateView.setText(items.get(position).getEndDate());
            enDateView.setTextColor(Color.rgb(164, 35, 121));

            ((TextView)convertView.findViewById(R.id.durationDays)).setText(items.get(position).getDurDays());
            ((TextView)convertView.findViewById(R.id.durationWeeksDays)).setText(items.get(position).getDurWeeksDays());
            ((TextView)convertView.findViewById(R.id.durationMonthsDays)).setText(items.get(position).getDurMonthsDays());
            ((TextView)convertView.findViewById(R.id.durationYearsMonthsDays)).setText(items.get(position).getDurYearsMonthsDays());

            return convertView;
        }
    }
}