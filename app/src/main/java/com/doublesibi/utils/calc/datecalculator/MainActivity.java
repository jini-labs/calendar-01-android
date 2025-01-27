package com.doublesibi.utils.calc.datecalculator;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.doublesibi.utils.calc.datecalculator.holiday.MyCalendar;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final String LOGTAG = "DayCalc";
    private int selectedMenu = 0;
    private Menu mMenu;
    private MyCalendar myCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        if (myCalendar == null) {
            myCalendar = new MyCalendar();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View v = navigationView.getHeaderView(0);
        ((TextView) v.findViewById(R.id.header_date)).setText(myCalendar.getTodayYMD("/"));
        Resources r = getResources();
        String[] weekofName = r.getStringArray(R.array.nameOfWeek);
        int weekOfNum = myCalendar.get(Calendar.DAY_OF_WEEK);
        ((TextView) v.findViewById(R.id.header_wdate)).setText("("+weekofName[weekOfNum-1]+")");
        if (weekOfNum == Calendar.SUNDAY || weekOfNum == Calendar.SATURDAY) {
            ((TextView) v.findViewById(R.id.header_wdate)).setTextColor(Color.RED);
        }

        onNavigationItemSelected(navigationView.getMenu().getItem(selectedMenu));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_history) {
            Intent intent;
            Log.d(LOGTAG, "selected action menu.(" + selectedMenu + ")");
            switch(selectedMenu) {
                case 1:
                    intent = new Intent(MainActivity.this, DurationHistActivity.class);
                    startActivity(intent);

                    break;
                case 2:
                    intent = new Intent(MainActivity.this, EventHistActivity.class);
                    startActivity(intent);

                    break;
                case 0:
                default:
                    break;
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        Class fragmentClass;

        switch(item.getItemId()) {
            case R.id.calendar_fragment:
                selectedMenu = 0;
                fragmentClass = ThismonthFragment.class;
                break;
            case R.id.calc_duration_fragment:
                selectedMenu = 1;
                fragmentClass = DurationFragment.class;
                break;
            case R.id.calc_eventday__fragment:
                selectedMenu = 2;
                fragmentClass = EventdayFragment.class;
                break;
            default:
                selectedMenu = 0;
                this.mMenu.getItem(0).setVisible(false);
                fragmentClass = ThismonthFragment.class;
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        item.setChecked(true);
        // Set action bar title
        setTitle(item.getTitle());

        // Close the navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
