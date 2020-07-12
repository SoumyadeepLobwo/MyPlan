package com.example.myplan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

//import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
//import android.content.Intent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
//import android.os.Environment;
//import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


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

    private class NotifyWorker extends Worker {

        public NotifyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
            super(context, workerParams);
        }

        @NonNull
        @Override
        public Result doWork() {
            String CHANNEL_ID = "1";
            Intent intent = new Intent(AddActivity.this,CalenderActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(AddActivity.this, CHANNEL_ID)
                    .setContentTitle("Event Alert")
                    .setContentText("ABCD")
                    .setSmallIcon(R.drawable.ic_baseline_calendar_today_24)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "MyPlan Notification";
                String description = "Notifies current events";

                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                notificationManager.createNotificationChannel(channel);
            }
            Notification notification = builder.build();//created the notification
            notificationManager.notify(12, notification);

            return Result.success();
        }
    }

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

        //Intent intent=new Intent(AddActivity.this, OneActivity.class);

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

        //CHECKING ALL THE CONDITIONS
        boolean validity = true;

        if(description.equals("") || day.equals("") || hrs.equals("") || min.equals("") || ampm.equals("") || notify.equals("")) {
            Toast.makeText(AddActivity.this, "All details not entered ", Toast.LENGTH_SHORT).show();
            validity = false;
        }


        //checking date validity
        if (!isLeap(Integer.parseInt(year))) {
            if ((month.equals("2") || month.equals("02")) && Integer.parseInt(day) > 28) {
                Toast.makeText(AddActivity.this, "Invalid day as " + year + " is not a leap year", Toast.LENGTH_SHORT).show();
                validity = false;
            }
        }
        if (Integer.parseInt(month) >= 1 && Integer.parseInt(month) <= 12) {
            if ((month.equals("2") || month.equals("02")) && Integer.parseInt(day) < 1 || Integer.parseInt(day)>29) {
                Toast.makeText(AddActivity.this, "Invalid day", Toast.LENGTH_SHORT).show();
                validity = false;
            }
            if (Integer.parseInt(month) == 1 || Integer.parseInt(month) == 3 || Integer.parseInt(month) == 5 || Integer.parseInt(month) == 7 || Integer.parseInt(month) == 8 || Integer.parseInt(month) == 10 || Integer.parseInt(month) == 12) {
                if (Integer.parseInt(day) < 1 && Integer.parseInt(day) > 31) {
                    Toast.makeText(AddActivity.this, "Invalid day", Toast.LENGTH_SHORT).show(); validity = false;
                }
            }
            else if (Integer.parseInt(month) == 4 || Integer.parseInt(month) == 6 || Integer.parseInt(month) == 9 || Integer.parseInt(month) == 11) {
                if (Integer.parseInt(day) < 1 && Integer.parseInt(day) > 30)
                    Toast.makeText(AddActivity.this, "Invalid day", Toast.LENGTH_SHORT).show(); validity = false;
            }
        } else {
            Toast.makeText(AddActivity.this, "Invalid month", Toast.LENGTH_SHORT).show();
            validity = false;
        }
        //checking time validity
        if(Integer.parseInt(hrs) > 12) {
            Toast.makeText(AddActivity.this, "Hrs should be in 12-hour format", Toast.LENGTH_SHORT).show();
            validity = false;
        }
        if(Integer.parseInt(hrs)<0) {
            Toast.makeText(AddActivity.this, "Hrs cannot be negative", Toast.LENGTH_SHORT).show();
            validity = false;
        }
        if(Integer.parseInt(min)>59 || Integer.parseInt(min)<0) {
            Toast.makeText(AddActivity.this, "Minutes out of range", Toast.LENGTH_SHORT).show();
            validity = false;
        }

        if(!validity) return;//returns if any of the above conditions are nto met

        //adding the day, month and year to form a single date
        if (Integer.parseInt(day) >= 1 && Integer.parseInt(day) <= 9)
            day_1 = "0" + Integer.parseInt(day);
        else
            day_1 = day;
        if (Integer.parseInt(month) >= 1 && Integer.parseInt(month) <= 9)
            month_1 = "0" + Integer.parseInt(month);
        else
            month_1 = month;
        date = year + "-" + month_1 + "-" + day_1;//forming the sql format date

        if (Integer.parseInt(hrs) >= 1 && Integer.parseInt(hrs) <= 9)
            hrs_1 = "0" + Integer.parseInt(hrs);
        else
            hrs_1 = hrs;
        if (Integer.parseInt(min) >= 1 && Integer.parseInt(min) <= 9)
            min_1 = "0" + Integer.parseInt(min);
        else min_1 = min;
        //converting from 12 hr format to 24 hr format by checking conditions
        if (ampm.equals("P.M.")) {
            if (!hrs_1.equals("12"))
                hrs_1 = Integer.toString(Integer.parseInt(hrs) + 12);
        }
        if (ampm.equals("A.M.")) {
            if (hrs_1.equals("12"))
                hrs_1 = "00";
        }

        time = hrs_1 + ":" + min_1 + ":00";//time with hrs, min and also seconds asserted to be 00

        date_time = date + " " + time;//forming the SQL format for date and time

        // open sql db
        File database = new File(getFilesDir() + "/" + DB_FILE);
        SQLiteDatabase db = SQLiteDatabase.openDatabase(database.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.CREATE_IF_NECESSARY);
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

        //schedule notification
        scheduleNotification();

        onBackPressed();

    }

    private void scheduleNotification(){
        final String workTag = "notificationWork";

        //store DBEventID to pass it to the PendingIntent and open the appropriate event page on notification click
        // we then retrieve it inside the NotifyWorker with:
        // final int DBEventID = getInputData().getInt(DBEventIDTag, ERROR_VALUE);

        OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(NotifyWorker.class)
                .setInitialDelay(calculateDelay(), TimeUnit.MILLISECONDS)
                .addTag(workTag)
                .build();

        WorkManager.getInstance(AddActivity.this).enqueue(notificationWork);
    }
    private long calculateDelay(){
        Calendar eventcal = Calendar.getInstance();
        eventcal.set(Calendar.SECOND, 0);
        eventcal.set(Calendar.HOUR, Integer.parseInt(hrs_1));
        eventcal.set(Calendar.MINUTE, Integer.parseInt(min_1));
        eventcal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day_1));
        eventcal.set(Calendar.MONTH, Integer.parseInt(month_1));
        eventcal.set(Calendar.YEAR, Integer.parseInt(year));
        Calendar today = Calendar.getInstance();

        long diff = eventcal.getTime().getTime() - today.getTime().getTime();
        return  diff > 0 ? diff : 0;
    }

    private boolean isLeap(int year){

        return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
    }
}
