package com.example.myplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SeeOrUpdate extends AppCompatActivity {

    public EditText description1, day1, hrs1, min1, month1, year1;
    public String description, day, hrs, min, ampm = "", notify = "", month, year;
    public String day_1, month_1, hrs_1, min_1 = "";
    public String date = "", time = "", date_time = "";
    private String uuid;

    public void onClickAm(View view) {
        ampm = "A.M.";
    }

    public void onClickPm(View view) {
        ampm = "P.M.";
    }

    public void onClickONCE(View view) {
        notify = "ONCE";
    }

    public void onClickDAILY(View view) {
        notify = "DAILY";
    }

    public void onClickWEEKLY(View view) {
        notify = "WEEKLY";
    }

    public void onClickMONTHLY(View view) {
        notify = "MONTHLY";
    }

    public void onClickYEARLY(View view) {
        notify = "YEARLY";
    }

    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_or_update);

        // set the description, dates and other required details.
        db = SQLiteDatabase.openDatabase(getFilesDir().getAbsolutePath() + "/" + AddActivity.DB_FILE, null, SQLiteDatabase.OPEN_READONLY);
        String[] projection = {
                AddActivity.DB_ID,
                AddActivity.DB_UNIQUEID,
                AddActivity.DB_DESCRIPTION,
                AddActivity.DB_DATE,
                AddActivity.DB_DAY,
                AddActivity.DB_MONTH,
                AddActivity.DB_YEAR,
                AddActivity.DB_TIME,
                AddActivity.DB_HRS,
                AddActivity.DB_MIN,
                AddActivity.DB_DATE_TIME,
                AddActivity.DB_NOTIFY,
                AddActivity.DB_AMPM};


        String order = AddActivity.DB_DATE + " ASC";//represents the order in which we want to sort the data in the table

        //accessing the uuid value stored in intent in yourEvent activity
        uuid = getIntent().getStringExtra("uuid");

        Cursor cursor = db.query(AddActivity.DB_TABLE, projection, null, null, null, null, order);
        Event event = new Event();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                event.uniqueID = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_UNIQUEID));
                if (event.uniqueID.equals(uuid)) {
                    description = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_DESCRIPTION));
                    day = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_DAY));
                    month = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_MONTH));
                    year = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_YEAR));
                    hrs = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_HRS));
                    if(Integer.parseInt(hrs)>12)
                        hrs = Integer.toString(Integer.parseInt(hrs)-12);
                    min = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_MIN));
                    ampm = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_AMPM));
                    notify = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_NOTIFY));
                    cursor.close();
                    break;
                }
            }
        }
        description1 = findViewById(R.id.add_edit_text);
        description1.setText(description);
        day1 = findViewById(R.id.add_day_edit_text);
        day1.setText(day);
        month1 = findViewById(R.id.add_month_edit_text);
        month1.setText(month);
        year1 = findViewById(R.id.add_year_edit_text);
        year1.setText(year);
        hrs1 = findViewById(R.id.add_time_edit_text);
        hrs1.setText(hrs);
        min1 = findViewById(R.id.add_time_2_edit_text);
        min1.setText(min);
        RadioGroup radioGroup = findViewById(R.id.radio_group_notify);
        RadioGroup radioGroup1= findViewById(R.id.radiogroup_ampm);
        if(ampm.equals("A.M."))
            radioGroup1.check(R.id.am_radio_button);
        else radioGroup1.check(R.id.pm_radio_button);

        if(notify.equals("ONCE")) radioGroup.check(R.id.once_radio_button);
        if(notify.equals("DAILY")) radioGroup.check(R.id.daily_radio_button);
        if(notify.equals("WEEKLY")) radioGroup.check(R.id.weekly_radio_button);
        if(notify.equals("MONTHLY")) radioGroup.check(R.id.monthly_radio_button);
        if(notify.equals("YEARLY")) radioGroup.check(R.id.yearly_radio_button);

        //wht abt ampm and notification


        db.close();
    }

    private boolean isLeap(int year) {

        return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
    }

    public void onClickAddActDone(View view) {

        description1 = findViewById(R.id.add_edit_text);
        description = description1.getText().toString();
        day1 = findViewById(R.id.add_day_edit_text);
        day = day1.getText().toString();
        month1 = findViewById(R.id.add_month_edit_text);
        month = month1.getText().toString();
        year1 = findViewById(R.id.add_year_edit_text);
        year = year1.getText().toString();
        hrs1 = findViewById(R.id.add_time_edit_text);
        hrs = hrs1.getText().toString();
        min1 = findViewById(R.id.add_time_2_edit_text);
        min = min1.getText().toString();


        boolean validity = true;

        if (description.equals("") || day.equals("") || hrs.equals("") || min.equals("") || ampm.equals("") || notify.equals("")) {
            Toast.makeText(SeeOrUpdate.this, "All details not entered ", Toast.LENGTH_SHORT).show();
            validity = false;
        }
        //checking date validity
        if (!isLeap(Integer.parseInt(year))) {
            if ((month.equals("2") || month.equals("02")) && Integer.parseInt(day) > 28) {
                Toast.makeText(SeeOrUpdate.this, "Invalid day as " + year + " is not a leap year", Toast.LENGTH_SHORT).show();
                validity = false;
            }
        }
        if (Integer.parseInt(month) >= 1 && Integer.parseInt(month) <= 12) {
            if ((month.equals("2") || month.equals("02")) && Integer.parseInt(day) < 1 || Integer.parseInt(day)>29) {
                Toast.makeText(SeeOrUpdate.this, "Invalid day", Toast.LENGTH_SHORT).show();
                validity = false;
            }
            if (Integer.parseInt(month) == 1 || Integer.parseInt(month) == 3 || Integer.parseInt(month) == 5 || Integer.parseInt(month) == 7 || Integer.parseInt(month) == 8 || Integer.parseInt(month) == 10 || Integer.parseInt(month) == 12) {
                if (Integer.parseInt(day) < 1 && Integer.parseInt(day) > 31) {
                    Toast.makeText(SeeOrUpdate.this, "Invalid day", Toast.LENGTH_SHORT).show();
                    validity = false;
                }
            }
            else if (Integer.parseInt(month) == 4 || Integer.parseInt(month) == 6 || Integer.parseInt(month) == 9 || Integer.parseInt(month) == 11) {
                if (Integer.parseInt(day) < 1 && Integer.parseInt(day) > 30) {
                    Toast.makeText(SeeOrUpdate.this, "Invalid day", Toast.LENGTH_SHORT).show();
                    validity = false;
                }
            }
        } else {
            Toast.makeText(SeeOrUpdate.this, "Invalid month", Toast.LENGTH_SHORT).show();
            validity = false;
        }
        //checking time validity

        if(!validity) return;

        //adding the day, month and year to form a single date
        if (Integer.parseInt(day) >= 1 && Integer.parseInt(day) <= 9)
            day_1 = "0" + day;
        else
            day_1 = day;
        if (Integer.parseInt(month) >= 1 && Integer.parseInt(month) <= 9)
            month_1 = "0" + month;
        else
            month_1 = month;
        date = year + "-" + month_1 + "-" + day_1;//forming the sql format date

        if (Integer.parseInt(hrs) >= 1 && Integer.parseInt(hrs) <= 9)
            hrs_1 = "0" + hrs;
        else
            hrs_1 = hrs;
        if (Integer.parseInt(min) >= 1 && Integer.parseInt(min) <= 9)
            min_1 = "0" + min;
        else min_1 = min;
        //converting from 12 hr format to 24 hr format by checking conditions
        /*if (ampm.equals("P.M.")) {
            if (!hrs_1.equals("12"))
                hrs_1 = Integer.toString(Integer.parseInt(hrs) + 12);
        }
        if (ampm.equals("A.M.")) {
            if (hrs_1.equals("12"))
                hrs_1 = "00";
        }*/

        time = hrs_1 + ":" + min_1 + ":00";//time with hrs, min and also seconds asserted to be 00

        date_time = date + " " + time;//forming the SQL format for date and time

        db = SQLiteDatabase.openDatabase(getFilesDir().getAbsolutePath() + "/" + AddActivity.DB_FILE, null, SQLiteDatabase.OPEN_READWRITE);
        ContentValues values = new ContentValues();
        values.put(AddActivity.DB_DESCRIPTION, description);
        values.put(AddActivity.DB_DATE, date);
        values.put(AddActivity.DB_DAY, day_1);
        values.put(AddActivity.DB_MONTH, month_1);
        values.put(AddActivity.DB_YEAR, year);
        values.put(AddActivity.DB_TIME, time);
        values.put(AddActivity.DB_HRS, hrs_1);
        values.put(AddActivity.DB_MIN, min_1);
        values.put(AddActivity.DB_DATE_TIME, date_time);
        values.put(AddActivity.DB_NOTIFY, notify);
        values.put(AddActivity.DB_AMPM, ampm);

        db.update(AddActivity.DB_TABLE, values , AddActivity.DB_UNIQUEID+" = \""+uuid+"\"", null);
        /*db.execSQL("UPDATE " + AddActivity.DB_TABLE + " SET "
                + AddActivity.DB_DESCRIPTION + " = " + description + " , "
                + AddActivity.DB_DATE + " = \""+ date +"\" , "
                + AddActivity.DB_DAY + " = " + day + " , "
                + AddActivity.DB_MONTH + " = " + month + " , "
                + AddActivity.DB_YEAR + " = " + year + " , "
                + AddActivity.DB_TIME + " = \""+ time +"\" , "
                + AddActivity.DB_HRS + " = " + hrs_1 + " , "
                + AddActivity.DB_MIN + " = " + min_1 + " , "
                + AddActivity.DB_DATE_TIME + " = \""+ date_time +"\" , "
                + AddActivity.DB_NOTIFY + " = " + notify + " , "
                + AddActivity.DB_AMPM + " = " +ampm+ " WHERE " + AddActivity.DB_UNIQUEID + " = " + YourEvent_Activity.uuid + ";");//showing error near where syntax error*/
        //Intent intent = new Intent(SeeOrUpdate.this, OneActivity.class);
        onBackPressed();
    }
    public void onClickAddActPrev(View view)
    {
        onBackPressed();
    }
}