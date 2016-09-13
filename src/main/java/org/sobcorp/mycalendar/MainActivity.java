package org.sobcorp.mycalendar;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.view.View.OnClickListener;
import static android.view.View.OnTouchListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Math math = new Math();

    Calendar calendar = Calendar.getInstance();
    int curMonth = calendar.get(Calendar.MONTH) + 1;
    int curYear = calendar.get(Calendar.YEAR);
    int curDay = calendar.get(Calendar.DAY_OF_MONTH);

    List yearList = new ArrayList();
    List<TextView> daysList = new ArrayList<TextView>();
    List<TableRow> rowsList = new ArrayList<TableRow>();

    Spinner monthSpinner;
    Spinner yearSpinner;

    float x1, x2, y1, y2 = 0;

    int maxYear = curYear + 20;
    int minYear = curYear - 20;

    Animation hideAnim = null;
    Animation showAnim = null;

    boolean calendarShowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Здесь будет обработчик добавления мероприятия", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        calendarShowed = false;

        hideAnim = AnimationUtils.loadAnimation(this, R.anim.hide);
        showAnim = AnimationUtils.loadAnimation(this, R.anim.show);

        hideAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ShowCalendar();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        monthSpinner = (Spinner) findViewById(R.id.monthSpinner);
        yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
        TableLayout tableLayoutWeekDays = (TableLayout) findViewById(R.id.table_layout_week_days);
        TableLayout tableLayoutCalendar = (TableLayout) findViewById(R.id.table_layout_calendar);

        for (int i = curYear - 20; i <= curYear + 20; i++) {
            yearList.add(i);
        }

        List<Integer> list = new ArrayList<Integer>();
        for (int i = 1; i <= 42; i++) {
            list.add(i);
        }

        ArrayAdapter<CharSequence> monthSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.month_array, android.R.layout.simple_spinner_item);
        monthSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthSpinnerAdapter);

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                curMonth = selectedItemPosition + 1;
                hideAnimation();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<Integer> yearSpinnerAdapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, yearList);
        yearSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearSpinnerAdapter);

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                curYear = selectedItemPosition + calendar.get(Calendar.YEAR) - 20;
                hideAnimation();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        monthSpinner.setSelection(curMonth - 1);
        yearSpinner.setSelection(20);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metricsB = new DisplayMetrics();
        display.getMetrics(metricsB);

        int h;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            h = metricsB.heightPixels/14;
        }
        else {
            h = metricsB.heightPixels/12;
        }

        tableLayoutWeekDays.setStretchAllColumns(true);
        TableRow tableWeekDaysRow = new TableRow(this);
        tableWeekDaysRow.setGravity(Gravity.CENTER_HORIZONTAL);
        String[] weekDays = getResources().getStringArray(R.array.week_array);
        for (int i = 0; i < 7; i++) {
            TextView textView = new TextView(this);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            textView.setText(weekDays[i]);
            if (math.isChetnoe(i)) {
                textView.setBackgroundResource(R.color.darkWeekDay);
            } else {
                textView.setBackgroundResource(R.color.lightWeekDay);
            }
            if (i==math.getNumberOfCurDayOfWeek()-1) {
                textView.setBackgroundResource(R.color.today);
            }
            tableWeekDaysRow.addView(textView);
        }
        tableLayoutWeekDays.addView(tableWeekDaysRow);

        tableLayoutCalendar.setStretchAllColumns(true);

        daysList.add(new TextView(this)); // для того, чтобы дни начинались с id=1
        for (int i = 0; i < 6; i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
            TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
            tableRowParams.weight = 1;
            tableRow.setMinimumHeight(h);
            rowsList.add(tableRow);
            for (int j = 0; j < 7; j++) {
                TextView textView = new TextView(this);
                textView.setGravity(Gravity.CENTER);
                textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                daysList.add(textView);
                tableRow.addView(textView);
            }
            tableLayoutCalendar.addView(tableRow);
        }

        hideAnimation();

        ScrollView calendarScroll = (ScrollView) findViewById(R.id.calendarScroll);
        calendarScroll.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    x1 = event.getX();
                    y1 = event.getY();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    x2 = event.getX();
                    y2 = event.getY();
                    Swype();
                }
                return false;
            }
        });
    }

    public void Swype() {
        if (x1 > x2 && (x1 - x2) > 20) {
            if (!(curMonth == 12 && curYear == maxYear)) {
                if (curMonth == 12) {
                    curMonth = 1;
                    curYear = curYear + 1;
                } else {
                    curMonth = curMonth + 1;
                }
                monthSpinner.setSelection(curMonth - 1);
                yearSpinner.setSelection(curYear - (calendar.get(Calendar.YEAR) - 20));
            }
        }
        if (x1 < x2 && (x2 - x1) > 20) {
            if (!(curMonth == 1 && curYear == minYear)) {
                if (curMonth == 1) {
                    curMonth = 12;
                    curYear = curYear - 1;
                } else {
                    curMonth = curMonth - 1;
                }
                monthSpinner.setSelection(curMonth - 1);
                yearSpinner.setSelection(curYear - (calendar.get(Calendar.YEAR) - 20));
            }
        }
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_calendar) {
            // Handle the camera action
        } else if (id == R.id.nav_add_event) {

        } else if (id == R.id.nav_all_events) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_rate) {

        } else if (id == R.id.nav_money) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void ShowCalendar() {
        showAnimation();
        // очистка: начало
        for (int i = 1; i <= 42; i++) {
            if (!math.isChetnoe(i)) {
                daysList.get(i).setBackgroundResource(R.color.lightDay);
            } else {
                daysList.get(i).setBackgroundResource(R.color.darkDay);
            }
            daysList.get(i).setText("");
        }
        // очистка: конец

        // подсчет дней в тек. месяце: начало
        int daysInMonth = math.getLengthOfMonth(curYear, curMonth);
        // подсчет дней в тек. месяце: конец

        // номер дня недели 1-го числа текущей даты: начало
        int startDay = math.getStartDay(curYear, curMonth);
        // номер дня недели 1-го числа текущей даты: конец

        // расчет и задание размера текста в ячейках: начало
        if (!calendarShowed) {
            float size;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                    size = 20;
                } else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
                    size = 20;
                }
                else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                    size = 20;
                }
                else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
                    size = 15;
                }
                else {
                    size = 15;
                }
            }
            else {
                if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                    size = 30;
                } else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
                    size = 30;
                }
                else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                    size = 20;
                }
                else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
                    size = 15;
                }
                else {
                    size = 15;
                }
            }
            for (TextView tv : daysList) {
                tv.setTextSize(size);
            }
        }
        // расчет и задание размера текста в ячейках: конец

        // заполнение календаря: начало
        int tempDay = 0;
        for (int i = startDay; i < daysInMonth + startDay; i++) {
            tempDay++;
            daysList.get(i).setText(String.valueOf(tempDay));
        }
        // заполнение календаря: конец

        // отметка сегодняшнего дня: начало
        if (curYear == calendar.get(Calendar.YEAR) && curMonth == calendar.get(Calendar.MONTH) + 1) {
            for (int i = startDay; i < daysInMonth + startDay; i++) {
                int n = Integer.parseInt(daysList.get(i).getText().toString());
                if (n == curDay) {
                    daysList.get(i).setBackgroundResource(R.color.today);
                }
            }
        }
        // отметка сегодняшнего дня: конец
        calendarShowed = true;
    }

    public void onButtonTodayClick(View view) {
        curMonth = calendar.get(Calendar.MONTH) + 1;
        curYear = calendar.get(Calendar.YEAR);
        monthSpinner.setSelection(curMonth - 1);
        yearSpinner.setSelection(20);
    }

    public void hideAnimation() {
        for (TextView tv : daysList) {
            tv.startAnimation(hideAnim);
        }
    }

    public void showAnimation() {
        for (TextView tv : daysList) {
            tv.startAnimation(showAnim);
        }
    }
}
