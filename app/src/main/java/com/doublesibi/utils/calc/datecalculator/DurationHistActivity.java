package com.doublesibi.utils.calc.datecalculator;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.doublesibi.utils.calc.datecalculator.hist.DurationHistItem;
import com.doublesibi.utils.calc.datecalculator.hist.DurationItemOpenHelper;

import java.util.ArrayList;


public class DurationHistActivity extends AppCompatActivity {
    private final String LOGTAG = "DayCalc";

    // 1ページ辺りの項目数
    Integer per_page = 10;
    private int recordCount = 0;
    private int selectKey = 0;

    // フッターのプログレスバー（クルクル）
    View mFooter;

    // 予報表示用リストビューのアダプター
    //ArrayAdapter<String> adapter;
    private DurationHistAdaptor adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duration_hist);

        // リスト用のアダプターを準備
        //adapter = new ArrayAdapter<String>(this, R.layout.duration_hist_listview_item);
        adapter = new DurationHistAdaptor(DurationHistActivity.this);

                // アダプターにアイテムを追加します
//        for (int i = 0; i < per_page; i++) {
//            adapter.add("リストビュー：" + i);
//        }
        getDurationHistData(selectKey);

        // リストビューへ紐付け
        ListView listview = (ListView)findViewById(R.id.listView);

        // リストビューにアダプターを設定します
        listview.setAdapter(adapter);

        // リストビューにフッターを追加
        listview.addFooterView(getFooter());

        // スクロールのリスナー
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {

            // スクロール中の処理
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                // 最初とスクロール完了したとき
                if ((totalItemCount - visibleItemCount) == firstVisibleItem) {

                    // アイテムの数 フッター分の1を引く
                    Integer ItemCount = totalItemCount - 1;

                    // アダプターにアイテムを追加します
//                    for (int i = ItemCount; i < (ItemCount + per_page); i++) {
//                        adapter.add("リストビュー：" + i);
//                    }
                    getDurationHistData(selectKey);
                }
            }

            // ListViewがスクロール中かどうか状態を返すメソッドです
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
        // queryメソッドの実行例
        Cursor c = db.query("DateDuration", columns,
                            "stDate>?", new String[] {""+startDate},
                            null,
                            null,
                            "stDate",
                            ""+this.per_page);

        String text = null;
        boolean mov = c.moveToFirst();
        while (mov) {
            selectKey = Integer.parseInt(c.getString(0));

            text = "[stDate:"      + selectKey +
                    ", enDate:"     + c.getString(1) +
                    ", days:"       + c.getString(2) +
                    ", weeks:"      + c.getString(3) +
                    ", weekdays:"   + c.getString(4) +
                    ", months:"     + c.getString(5) +
                    ", monthdays:"  + c.getString(6) +
                    ", years:"      + c.getString(7) +
                    ", yearmonths:" + c.getString(8) +
                    ", yeardays:"   + c.getString(9) + "]";


            recordCount ++;
            DurationHistItem item = new DurationHistItem();
            item.setStartDate(c.getString(0));
            item.setEndDate(c.getString(1));
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

            ((TextView)convertView.findViewById(R.id.durationStart)).setText(items.get(position).getStartDate());
            ((TextView)convertView.findViewById(R.id.durationEnd)).setText(items.get(position).getEndDate());

            ((TextView)convertView.findViewById(R.id.durationDays)).setText(items.get(position).getDurDays());
            ((TextView)convertView.findViewById(R.id.durationWeeksDays)).setText(items.get(position).getDurWeeksDays());
            ((TextView)convertView.findViewById(R.id.durationMonthsDays)).setText(items.get(position).getDurMonthsDays());
            ((TextView)convertView.findViewById(R.id.durationYearsMonthsDays)).setText(items.get(position).getDurYearsMonthsDays());

            return convertView;
        }
    }
}
