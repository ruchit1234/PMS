package com.kitelytech.pmsapp.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.kitelytech.pmsapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ApplyLeaveActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ApplyLeaveActivity.class.getSimpleName();
    private EditText etLeaveFromDate, etLeaveToDate, etReason;
    private TextView tvNoOfDays;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_leave);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.leave_details);

        etLeaveFromDate = (EditText) findViewById(R.id.etLeaveFromDate);
        etLeaveToDate = (EditText) findViewById(R.id.etLeaveToDate);
        etReason = (EditText) findViewById(R.id.etLeaveReason);
        tvNoOfDays = (TextView) findViewById(R.id.tv_NoOfDays);

        etLeaveFromDate.setCursorVisible(false);
        etLeaveFromDate.setFocusableInTouchMode(false);
        etLeaveFromDate.setFocusable(false);

        etLeaveToDate.setCursorVisible(false);
        etLeaveToDate.setFocusableInTouchMode(false);
        etLeaveToDate.setFocusable(false);

        etLeaveFromDate.setOnClickListener(this);
        etLeaveToDate.setOnClickListener(this);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_leave, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_submit) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setCalenderView(EditText e) {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                e.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                if (etLeaveFromDate.getText().equals("")){
                    if (etLeaveToDate.getText().equals("")){
                        findNoOfDays();
                    }
                }

            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == etLeaveFromDate) {
            setCalenderView(etLeaveFromDate);
        } else if (v == etLeaveToDate) {
            setCalenderView(etLeaveToDate);
        }
    }

    public void findNoOfDays() {
        String from = String.valueOf(etLeaveFromDate.getText());
        String to = String.valueOf(etLeaveToDate.getText());
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date fromDate = null;
        Date toDate = null;
        try {
            fromDate = formatter.parse(from);
            toDate = formatter.parse(to);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d(TAG + "----->", String.valueOf(etLeaveFromDate.getText()));
        Log.d(TAG + "----->", String.valueOf(etLeaveToDate.getText()));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(fromDate);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(toDate);
        long workDays = days(cal1.getTime(), cal2.getTime());

        tvNoOfDays.setText((String.valueOf(workDays)));


    }

    static long days(Date start, Date end){
        //Ignore argument check

        Calendar c1 = Calendar.getInstance();
        c1.setTime(start);
        int w1 = c1.get(Calendar.DAY_OF_WEEK);
        c1.add(Calendar.DAY_OF_WEEK, -w1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(end);
        int w2 = c2.get(Calendar.DAY_OF_WEEK);
        c2.add(Calendar.DAY_OF_WEEK, -w2);

        //end Saturday to start Saturday
        long days = (c2.getTimeInMillis()-c1.getTimeInMillis())/(1000*60*60*24);
        long daysWithoutWeekendDays = days-(days*2/7);

        // Adjust days to add on (w2) and days to subtract (w1) so that Saturday
        // and Sunday are not included
        if (w1 == Calendar.SUNDAY && w2 != Calendar.SATURDAY) {
            w1 = Calendar.MONDAY;
        } else if (w1 == Calendar.SATURDAY && w2 != Calendar.SUNDAY) {
            w1 = Calendar.FRIDAY;
        }

        if (w2 == Calendar.SUNDAY) {
            w2 = Calendar.MONDAY;
        } else if (w2 == Calendar.SATURDAY) {
            w2 = Calendar.FRIDAY;
        }

        return daysWithoutWeekendDays-w1+w2;
    }
}
