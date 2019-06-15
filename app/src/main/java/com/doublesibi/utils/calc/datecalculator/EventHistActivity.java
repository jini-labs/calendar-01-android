package com.doublesibi.utils.calc.datecalculator;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.doublesibi.utils.calc.datecalculator.hist.EventdayHistItem;
import com.doublesibi.utils.calc.datecalculator.hist.EventdayItemOpenHelper;
import com.doublesibi.utils.calc.datecalculator.holiday.MyCalendar;

import java.util.ArrayList;

public class EventHistActivity extends AppCompatActivity {
    private final String LOGTAG = "DayCalc";

    private Integer per_page = 10;
    private int recordCount = 0;
    private int selectKey = 0;

    private View mFooter;
    private EventdayHistAdaptor adapter;
    private ListView listView;

    private String[] constantStr = {"年度を選択下さい。",
            "月を選択下さい。",
            "日を選択下さい。",
            "計算した結果のみ保存可能です。！",
            "年と月から入力下さい。",
            "保存",
            "キャンセル",
            "、",
            " 年", //8
            " カ月", //9
            " 週", //10
            " 日", //11
            " 前",
            " 後",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_hist);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_prev);
        actionBar.setDisplayHomeAsUpEnabled(true);

        listView = (ListView)findViewById(R.id.listView);
        adapter = new EventdayHistAdaptor(EventHistActivity.this);
        getEventdayHistData(selectKey);
        listView.setAdapter(adapter);
        listView.addFooterView(getFooter());

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final EventdayHistItem selItem=(EventdayHistItem)listView.getItemAtPosition(position);
                final TextView v1 = (TextView) view.findViewById(R.id.eventStart);
                final TextView v2 = (TextView) view.findViewById(R.id.eventName);

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Delete...")
                        .setMessage(v1.getText().toString() + " ~ " + v2.getText().toString())
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                Log.d(LOGTAG, "start: " + v1.getText() +", end: " + v2.getText());
//                                Toast.makeText(getApplicationContext(), "Delete record. (start:" + v1.getText().toString() +", end:" + v2.getText().toString(),
//                                        Toast.LENGTH_LONG).show();

                                if (deleteEventdayHistData(selItem.getStartDate(), selItem.getEveName())) {
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

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            boolean isLoading = false;
            boolean isAllLoaded = false;

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (isLoading){
                    return;
                }

                if (isAllLoaded){
                    return;
                }

                Log.d(LOGTAG,"firstVisibleItem:" + firstVisibleItem + ", visibleItemCount:" + visibleItemCount + ", totalItemCount:" + totalItemCount);

                if ((totalItemCount - visibleItemCount) == firstVisibleItem) {
                    Log.d(LOGTAG, "selectKey:" + selectKey);
                    Integer ItemCount = totalItemCount - 1;

                    isLoading = true;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                Thread.sleep(3000);
                            }
                            catch (InterruptedException exception){
                                // 処理なし
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!getEventdayHistData(selectKey)) {
                                        isAllLoaded = true;
                                    }
                                }
                            });
                            isLoading = false;
                        }
                    }).start();
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private View getFooter() {
        if (mFooter == null) {
            mFooter = getLayoutInflater().inflate(R.layout.footer_eventday_hist, null);
        }
        return mFooter;
    }

    private boolean deleteEventdayHistData(String startDateWeekname, String eventName) {
        EventdayItemOpenHelper helper = new EventdayItemOpenHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String value1 = startDateWeekname.substring(0,4);
        String value2 = startDateWeekname.substring(5,7);
        String value3 = startDateWeekname.substring(8,10);
        String v1 = value1 + value2 + value3;

        if (helper.deleteEventday(db, v1, eventName) > 0) {
            Log.d(LOGTAG, "startDate:" + v1 + ", name:" + eventName + " deleted.!");
            return true;
        } else {
            Log.d(LOGTAG, "startDate:" + v1 + ", name:" + eventName + " delete fail.");
            return false;
        }
    }

    private boolean getEventdayHistData(int startDate) {
        EventdayItemOpenHelper helper = new EventdayItemOpenHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns ={"name",
                "stDate",
                "days",
                "weeks",
                "months",
                "years",
                "beOrAf",
                "enDate" };

        Cursor c = db.query("Eventday", columns,
                "stDate>?", new String[] {""+startDate},
                null,
                null,
                "stDate",
                ""+this.per_page);

        boolean mov = c.moveToFirst();
        while (mov) {
            String message = null;
            int count = c.getColumnCount();
            for(int i=0; i < count; i++) {
                message += ", " + i + ":" + c.getString(i);
            }
            Log.d(LOGTAG, "message : " + message);
            selectKey = Integer.parseInt(c.getString(1));

            recordCount ++;
            EventdayHistItem item = new EventdayHistItem();

            item.setEveName(c.getString(0));

            String temp1 = c.getString(1);
            String temp2 = MyCalendar.convertDateWeekName(getResources(), Integer.parseInt(temp1), "/");
            item.setStartDate(temp2);

            Log.d(LOGTAG, ">>>>>> "+item.toString());
            if (StrToInt(c.getString(5)) > 0) {
                item.setEveDayMonWeeYea(c.getString(5) + constantStr[8]);
            }
            if (StrToInt(c.getString(4)) > 0) {
                if (item.getEveDayMonWeeYea().length() > 0)
                    item.setEveDayMonWeeYea(item.getEveDayMonWeeYea() + constantStr[7] +
                        c.getString(4) + constantStr[9]);
                else
                    item.setEveDayMonWeeYea(c.getString(4) + constantStr[9]);
            }
            if (StrToInt(c.getString(3)) > 0) {
                if (item.getEveDayMonWeeYea().length() > 0)
                    item.setEveDayMonWeeYea(item.getEveDayMonWeeYea() + constantStr[7] +
                            c.getString(3) + constantStr[10]);
                else
                    item.setEveDayMonWeeYea(c.getString(3) + constantStr[10]);
            }
            if (StrToInt(c.getString(2)) > 0) {
                if (item.getEveDayMonWeeYea().length() > 0)
                    item.setEveDayMonWeeYea(item.getEveDayMonWeeYea() + constantStr[7] +
                            c.getString(2) + constantStr[11]);
                else
                    item.setEveDayMonWeeYea(c.getString(2) + constantStr[11]);
            }

            if (StrToInt(c.getString(6)) > 0) {
                item.setBeforeOrAfter(1);
                item.setEveDayMonWeeYea(item.getEveDayMonWeeYea() + constantStr[13]);
            } else {
                item.setBeforeOrAfter(-1);
                item.setEveDayMonWeeYea(item.getEveDayMonWeeYea() + constantStr[12]);
            }

            temp1 = c.getString(7);
            temp2 = MyCalendar.convertDateWeekName(getResources(), Integer.parseInt(temp1), "/");
            item.setEveDate(temp2);

            Log.d(LOGTAG, item.toString());
            adapter.setItems(item);
            mov = c.moveToNext();
        }
        c.close();
        db.close();

        if (c.getCount() > 0) {
            listView.invalidateViews();
            return true;
        }

        return true;
    }

    private int StrToInt(String str) {
        if (str == null)
            return 0;
        if (str.trim().length() == 0)
            return 0;
        return Integer.parseInt(str.trim());
    }

    public class EventdayHistAdaptor extends BaseAdapter {
        Context context;
        LayoutInflater layoutInflater = null;
        ArrayList<EventdayHistItem> items;

        public EventdayHistAdaptor(Context context) {
            this.context = context;
            this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.items = new ArrayList<>();
        }

        public void setItems(EventdayHistItem item) {
            this.items.add(item);
        }

        public void remove(EventdayHistItem item) {
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
            convertView = layoutInflater.inflate(R.layout.eventday_hist_listview_item, parent, false);

            TextView stDateView = (TextView)convertView.findViewById(R.id.eventStart);
            stDateView.setText(items.get(position).getStartDate());
            stDateView.setTextColor(Color.rgb(35,91,164));

            TextView enDateView = (TextView)convertView.findViewById(R.id.eventDate);
            enDateView.setText(items.get(position).getEveDate());
            enDateView.setTextColor(Color.rgb(164, 35, 121));

            ((TextView)convertView.findViewById(R.id.eventName)).setText(items.get(position).getEveName());
            ((TextView)convertView.findViewById(R.id.eventCalcOptions)).setText(items.get(position).getEveDayMonWeeYea());

            return convertView;
        }
    }
}