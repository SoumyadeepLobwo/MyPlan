package com.example.myplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OneActivity extends AppCompatActivity {

    String currentDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        int cd, cm, cy;
        String d,m,y;//variables that will store the current day, month amd year
        Calendar calendar = Calendar.getInstance();
        cy = calendar.get(Calendar.YEAR);
        y = cy+"";
        cm = calendar.get(Calendar.MONTH)+1;
        m = cm+"";
        cd = calendar.get(Calendar.DAY_OF_MONTH);
        d = cd+"";
        if(Integer.parseInt(d)<10) d = "0"+d;
        if(Integer.parseInt(m)<9) m = "0"+m;
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
        Event e = new Event();
        List<String> eventdatelist = new ArrayList<>();
        String edate="";
        int ed, em, ey;

        boolean ifsmall = false;

        if(cursor != null){
            while(cursor.moveToNext()) {
                edate = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_DATE));
                ey = Integer.parseInt(edate.substring(0, 4));
                em = Integer.parseInt(edate.substring(5, 7));
                ed = Integer.parseInt(edate.substring(8, 10));
                if (ey == cy && em == cm) { //the whole if block returns true if the eventdate is small compared to the current date
                    if (ed < cd) {
                        ifsmall = true;
                    }
                } else if (ey == cy && em < cm) {
                    ifsmall = true;
                } else if (ey > cy) {
                    ifsmall = true;
                } else if(ey < cy) {
                    ifsmall = false;
                }
                if(ifsmall == true)//condition to check if the date is before current date
                {
                    eventdatelist.add(edate); // extrating all the events prior to the events of current day
                }
            }//ending the while loop
        }
        cursor.close();
        Cursor cursor1;
        for(int i = 0; i < eventdatelist.size(); i++)
        {
            edate = eventdatelist.get(i);
            cursor1 = db.query(AddActivity.DB_TABLE, projection, AddActivity.DB_DATE+" = \""+edate+"\"", null, null, null,null);

        }
    }
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
}