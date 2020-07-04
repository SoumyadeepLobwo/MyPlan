package com.example.myplan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.UUID;


public class AddActivity extends AppCompatActivity {

    //sqlite table features
    public final static String DB_FILE = "events.db";//name of the file containing the table
    public final static String DB_TABLE = "events_table";//name of the table
    public final static String DB_ID = "id";
    public final static String DB_UNIQUEID = "unique_id";// column representing id(i.e.,row number for convinience)
    public final static String DB_DESCRIPTION = "description";//column name representing the description
    public final static String DB_DATE = "date";//column name representing the date
    public final static String DB_DAY = "day";
    public final static String DB_MONTH = "month";//column name representing the month
    public final static String DB_YEAR = "year";//column name representing the year
    public final static String DB_HRS = "hrs";//column name representing the hrs
    public final static String DB_MIN = "min";//column name representing the min
    public final static String DB_AMPM = "ampm";//column name representing ampm
    public final static String DB_NOTIFY = "notify";//column name representing the notification period
    public final static String DB_DATE_TIME = "date_time";//column for the complete date
    public final static String DB_TIME = "time";


    public EditText description1, day1, hrs1, min1, month1, year1;
    public String description, day, hrs, min, ampm = "", notify = "", month, year;
    public String day_1, month_1, hrs_1, min_1 = "";
    public String date = "", time = "",date_time = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


    }

    public void onClickAm(View view){
        ampm="A.M.";
    }
    public void onClickPm(View view){
        ampm="P.M.";
    }
    public void onClickONCE(View view){
        notify="ONCE";
    }
    public void onClickDAILY(View view){
        notify="DAILY";
    }
    public void onClickWEEKLY(View view){
        notify="WEEKLY";
    }
    public void onClickMONTHLY(View view){
        notify="MONTHLY";
    }
    public void onClickYEARLY(View view){
        notify="YEARLY";
    }

    public void onClickAddActPrev(View view){ onBackPressed();}
    public void onClickAddActDone(View view){

        Intent intent=new Intent(AddActivity.this, OneActivity.class);

        //extracting data from the eidttext portions of addactivity.
        //in the first line findView..... method takes the id of the edittexts and returns an instance that we intend to store in description1and so on
        //in the second line we are retrieving the text part and converting it into String

        String uniqueID= UUID.randomUUID().toString();//create unique id

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

        if(description.equals("") || day.equals("") || hrs.equals("") || min.equals("") || ampm.equals("") || notify.equals("")) {
            Toast.makeText(AddActivity.this, "All details not entered ", Toast.LENGTH_SHORT).show();
        }
        else{

            //adding the day, month and year to form a single date
            if(Integer.parseInt(day)>=1 && Integer.parseInt(day)<=9)
                day_1 = "0" + day;
            else
                day_1 = day;
            if(Integer.parseInt(month)>=1 && Integer.parseInt(month)<=9)
                month_1 = "0"+month;
            else
                month_1 = month;
            date = year+"-"+month_1+"-"+day_1;//forming the sql format date

            if(Integer.parseInt(hrs)>=1 && Integer.parseInt(hrs)<=9)
                hrs_1 = "0" + hrs;
            else
                hrs_1 = hrs;
            if(Integer.parseInt(min)>=1 && Integer.parseInt(min)<=9)
                min_1 = "0" + min;
            else min_1 = min;
            //converting from 12 hr format to 24 hr format by checking conditions
            if(ampm =="P.M.") {
                if (hrs_1 != "12")
                    hrs_1 = Integer.toString(Integer.parseInt(hrs) + 12);
            }
            if(ampm == "A.M."){
                if(hrs_1 == "12")
                    hrs_1 = "00";
            }

            time=hrs_1+":"+min_1+":00";//time with hrs, min and also seconds asserted to be 00

            date_time=date+" "+time;//forming the SQL format for date and time

            // open sql db
            File database = new File( getFilesDir() + "/" + DB_FILE);
            SQLiteDatabase db = SQLiteDatabase.openDatabase(database.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.CREATE_IF_NECESSARY);
            db.disableWriteAheadLogging();//we need to disable so that the database is functional in android  versions 9 and above
            //Log.i("File Path", getFilesDir().getAbsolutePath());

            //create sql table
            db.execSQL("create table if not exists " + DB_TABLE + " (" +
                    DB_ID + " Integer Primary Key, " +
                    DB_UNIQUEID + " text, " +
                    DB_DESCRIPTION + " text, " +
                    DB_DATE + " text, " +
                    DB_DAY + " text, " +
                    DB_MONTH + " text, " +
                    DB_YEAR + " text, " +
                    DB_TIME + " text, " +
                    DB_HRS + " text, " +
                    DB_MIN + " text,  " +
                    DB_DATE_TIME + " text, " +
                    DB_NOTIFY + " text, " +
                    DB_AMPM + " text)");

            ContentValues values = new ContentValues();
            values.put(DB_UNIQUEID, uniqueID);
            values.put(DB_DESCRIPTION, description);
            values.put(DB_DATE, date);
            values.put(DB_DAY, day_1);
            values.put(DB_MONTH, month_1);
            values.put(DB_YEAR, year);
            values.put(DB_TIME, time);
            values.put(DB_HRS, hrs_1);
            values.put(DB_MIN, min_1);
            values.put(DB_DATE_TIME, date_time);
            values.put(DB_NOTIFY, notify);
            values.put(DB_AMPM, ampm);

            //insert a record(row) into sql table
            db.insertOrThrow(DB_TABLE, null, values);
            db.close();

            onBackPressed();
        }
    }
}