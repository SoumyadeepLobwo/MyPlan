package com.example.myplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
//import android.util.SparseArray;
import android.view.View;
import java.util.ArrayList;
import java.util.Calendar;
//import java.util.Date;
import java.util.List;

public class OneActivity extends AppCompatActivity {

    String currentDate;
    int cd, cm, cy;
    String d,m,y;//variables that will store the current day, month amd year

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);

        Calendar calendar = Calendar.getInstance();
        cy = calendar.get(Calendar.YEAR);
        y = cy+"";
        cm = calendar.get(Calendar.MONTH)+1;
        m = cm+"";
        cd = calendar.get(Calendar.DAY_OF_MONTH);
        d = cd+"";
        if(Integer.parseInt(d)<10) d = "0"+d;
        if(Integer.parseInt(m)<10) m = "0"+m;
        currentDate = y+"-"+m+"-"+d;// this gets stored as the default current date until another date is clicked on the calendar view

        //opening the database and accessing all the events
        SQLiteDatabase db = SQLiteDatabase.openDatabase(getFilesDir().getAbsolutePath()+"/"+AddActivity.DB_FILE, null,SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.CREATE_IF_NECESSARY);
        //creating the table if nescessary
        db.execSQL("create table if not exists " + AddActivity.DB_TABLE + " (" +
                AddActivity.DB_ID + " Integer Primary Key, " +
                AddActivity.DB_UNIQUEID + " text, " +
                AddActivity.DB_DESCRIPTION + " text, " +
                AddActivity.DB_DATE + " text, " +
                AddActivity.DB_DAY + " text, " +
                AddActivity.DB_MONTH + " text, " +
                AddActivity.DB_YEAR + " text, " +
                AddActivity.DB_TIME + " text, " +
                AddActivity.DB_HRS + " text, " +
                AddActivity.DB_MIN + " text,  " +
                AddActivity.DB_DATE_TIME + " text, " +
                AddActivity.DB_NOTIFY + " text, " +
                AddActivity.DB_AMPM + " text)");


        String[] projection={
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



        String order=AddActivity.DB_DATE + " ASC";//represents the order in which we want to sort the data in the table
        // eg: String.format("%s = \"%s\" ", AddActivity.DB_DATE, selectedDate);
        Cursor cursor = db.query(AddActivity.DB_TABLE,projection, null,null,null,null, order);

        Event e;

        List<Event> eventdatelist = new ArrayList<>();

        if(cursor != null){
            while(cursor.moveToNext()) {
                e = new Event();
                e.min = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_MIN));
                e.hrs = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_HRS));
                e.date_time = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_DATE_TIME));
                e.date = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_DATE));
                e.year = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_YEAR));
                e.month = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_MONTH));
                e.day = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_DAY));
                e.notify = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_NOTIFY));
                e.uniqueID = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_UNIQUEID));

                if(ifIsSmall(Integer.parseInt(e.day), Integer.parseInt(e.month), Integer.parseInt(e.year)))//condition to check if the date is before current date. returns false if the passed date matches the
                    //current date or exceeds it
                {
                    eventdatelist.add(e); // extracting all the objects of events prior to the events of current day
                }
            }//ending the while loop
            cursor.close();//closing the cursor
        }

        //Cursor cursor1;
        String edate, notify,date_Time,uuid;
        int ey,em,ed;//emin,ehrs;
        Calendar c;
        String dayUp,monthUp;
        for(int i = 0; i < eventdatelist.size(); i++)
        {
            edate = eventdatelist.get(i).date;
            ey = Integer.parseInt(eventdatelist.get(i).year);
            em = Integer.parseInt(eventdatelist.get(i).month);
            ed = Integer.parseInt(eventdatelist.get(i).day);
            //cursor1 = db.query(AddActivity.DB_TABLE, projection, AddActivity.DB_DATE+" = \""+edate+"\"", null, null, null,null);
            notify = eventdatelist.get(i).notify;
            //ehrs = Integer.parseInt(eventdatelist.get(i).hrs);
            //emin = Integer.parseInt(eventdatelist.get(i).min);
            date_Time = eventdatelist.get(i).date_time;
            uuid = eventdatelist.get(i).uniqueID;

            ContentValues values = new ContentValues();

            if(notify.equals("ONCE")){
                db.delete(AddActivity.DB_TABLE, AddActivity.DB_DATE+" = \""+edate+"\"", null);
                continue;//deleting a row means that we dont need to update it so we are going to the net element of the array-list immediately aft deleting
            }
            if(notify.equalsIgnoreCase("Daily")) {//we need to check whether the current day event "time" have been overcome
                c = Calendar.getInstance();
                //c.add(Calendar.DATE, 1);  // number of days to add//

                //updating the database
                if(c.get(Calendar.DATE)<10)
                    dayUp = "0"+c.get(Calendar.DATE);
                else
                    dayUp = c.get(Calendar.DATE)+"";
                if((c.get(Calendar.MONTH)+1)<10)
                    monthUp = "0"+c.get(Calendar.MONTH);
                else
                    monthUp = (c.get(Calendar.MONTH)+1)+"";

                values.put(AddActivity.DB_DAY,dayUp);
                values.put(AddActivity.DB_MONTH,monthUp);
                values.put(AddActivity.DB_YEAR,c.get(Calendar.YEAR)+"");
                values.put(AddActivity.DB_DATE,c.get(Calendar.YEAR)+"-"+monthUp+"-"+dayUp);
                values.put(AddActivity.DB_DATE_TIME,c.get(Calendar.YEAR)+"-"+monthUp+"-"+dayUp+" "+date_Time.substring(10));

            }
            if(notify.equalsIgnoreCase("Weekly")){
                c = Calendar.getInstance();
                c.add(Calendar.DATE, 6);  // number of days to add

            //updating the database
                if(c.get(Calendar.DATE)<10)
                    dayUp = "0"+c.get(Calendar.DATE);
                else
                    dayUp = c.get(Calendar.DATE)+"";
                if((c.get(Calendar.MONTH)+1)<10)
                    monthUp = "0"+(c.get(Calendar.MONTH)+1);
                else
                    monthUp = (c.get(Calendar.MONTH)+1)+"";

                values.put(AddActivity.DB_DAY,dayUp);
                values.put(AddActivity.DB_MONTH,monthUp);
                values.put(AddActivity.DB_YEAR,c.get(Calendar.YEAR)+"");
                values.put(AddActivity.DB_DATE,c.get(Calendar.YEAR)+"-"+monthUp+"-"+dayUp);
                values.put(AddActivity.DB_DATE_TIME,c.get(Calendar.YEAR)+"-"+monthUp+"-"+dayUp+" "+date_Time.substring(10));
            }
            if(notify.equals("MONTHLY")){
                c = Calendar.getInstance();
                if(c.get(Calendar.DATE)==1){
                    if(ed<10)
                        dayUp = "0"+ed;
                    else
                        dayUp = ed+"";
                    if((c.get(Calendar.MONTH)+1)<10)
                        monthUp = "0"+(c.get(Calendar.MONTH)+1);
                    else
                        monthUp = (c.get(Calendar.MONTH)+1)+"";
                    values.put(AddActivity.DB_DAY, dayUp);
                    values.put(AddActivity.DB_MONTH,monthUp);
                    values.put(AddActivity.DB_YEAR,c.get(Calendar.YEAR)+"");
                    values.put(AddActivity.DB_DATE,c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+eventdatelist.get(i).day);
                    values.put(AddActivity.DB_DATE_TIME,c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+eventdatelist.get(i).day+" "+date_Time.substring(10));

                } else {
                    c.add(Calendar.MONTH, 1);  // number of days to add

                    //updating the database
                    if(ed<10)
                        dayUp = "0"+ed;
                    else
                        dayUp = ed+"";
                    if((c.get(Calendar.MONTH)+1)<10)
                        monthUp = "0"+(c.get(Calendar.MONTH)+1);
                    else
                        monthUp = (c.get(Calendar.MONTH)+1)+"";

                    values.put(AddActivity.DB_DAY,dayUp);
                    values.put(AddActivity.DB_MONTH,monthUp);
                    values.put(AddActivity.DB_YEAR,c.get(Calendar.YEAR)+"");
                    values.put(AddActivity.DB_DATE,c.get(Calendar.YEAR)+"-"+monthUp+"-"+dayUp);
                    values.put(AddActivity.DB_DATE_TIME,c.get(Calendar.YEAR)+"-"+monthUp+"-"+dayUp+" "+date_Time.substring(10));
                }

            }
            if(notify.equals("YEARLY")){//alright
                if(isLeap(ey)) {
                    ey=ey+4;
                }else {
                    while (!ifIsSmall(ed, em, ey)) {//need to modify
                        ey+=1;
                    }
                }
                values.put(AddActivity.DB_YEAR,ey+"");
                values.put(AddActivity.DB_DATE,(ey+"")+"-"+edate.substring(5, 7)+"-"+edate.substring(8, 10));
                values.put(AddActivity.DB_DATE_TIME,(ey+"")+date_Time.substring(4));
            }
            db.update(AddActivity.DB_TABLE, values,AddActivity.DB_UNIQUEID+" = \""+uuid+"\"",null);
        }
    }


    private boolean ifIsSmall(int ed, int em, int ey){
        boolean ifsmall= false;
        if (ey == cy && em == cm) { //the whole if block returns true if the eventdate is small compared to the current date
            if (ed < cd) {
                ifsmall = true;
            }
        } else if (ey == cy && em < cm) {
            ifsmall = true;
        } else if (ey < cy) {
            ifsmall = true;
        }
        return ifsmall;
    }
    private boolean isLeap(int year) {
        return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
    }
    /*private int dayNo(int ed, int em, int ey){
        int days[]={0,31,28,31,30,31,30,31,31,30,31,30,31};
        int no=0;
        if(isLeap(em))
            days[2]=29;
        for(int i = 1; i < em; i++)
            no = no + days[i];
        no = no + ed;
        return no;
    }*/


    public void onClickAddConstraint(View view){
        Intent intent = new Intent(OneActivity.this, AddActivity.class);
        startActivity(intent);
    }
    public void onClickSeeConstraint(View view){
        Intent intent = new Intent(OneActivity.this, YourEvent_Activity.class);
        startActivity(intent);
    }
    public void onClickCalender(View view){
        Intent intent = new Intent(OneActivity.this, CalenderActivity.class);
        startActivity(intent);
    }
    public void onClickNotify(View view){
        //NotificationEventRecceiver.setupAlarm(getApplicationContext());
    }
}