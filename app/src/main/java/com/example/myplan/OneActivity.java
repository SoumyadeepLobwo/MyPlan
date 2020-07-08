package com.example.myplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

        if(cursor != null){
            while(cursor.moveToNext()) {
                edate = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_DATE));
                ey = Integer.parseInt(edate.substring(0, 4));
                em = Integer.parseInt(edate.substring(5, 7));
                ed = Integer.parseInt(edate.substring(8, 10));

                if(ifIsSmall(ed,em,ey) == true)//condition to check if the date is before current date. returns false if the passed date matches the
                    //current date or exceeds it
                {
                    eventdatelist.add(edate); // extrating all the events prior to the events of current day
                }
            }//ending the while loop
        }
        cursor.close();
        Cursor cursor1;
        String notify,date_Time;
        int n_ey,n_em,n_ed;
        int n_ey1,n_ed1;
        for(int i = 0; i < eventdatelist.size(); i++)
        {
            edate = eventdatelist.get(i);
            n_ey = Integer.parseInt(edate.substring(0, 4));
            n_em = Integer.parseInt(edate.substring(5, 7));
            n_ed = Integer.parseInt(edate.substring(8, 10));

            cursor1 = db.query(AddActivity.DB_TABLE, projection, AddActivity.DB_DATE+" = \""+edate+"\"", null, null, null,null);
            notify = cursor1.getString(cursor1.getColumnIndexOrThrow(AddActivity.DB_NOTIFY));
            date_Time = cursor1.getString(cursor1.getColumnIndexOrThrow(AddActivity.DB_DATE_TIME));


            Calendar c;

            ContentValues values = new ContentValues();

            if(notify.equals("ONCE")){
                db.delete(AddActivity.DB_TABLE, AddActivity.DB_DATE+" = \""+edate+"\"", null);
            }
            if(notify.equalsIgnoreCase("Daily")) {
                c = Calendar.getInstance();
                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                c.add(Calendar.DATE, 1);  // number of days to add
                //edate = sdf.format(c.getTime());  // edate is now the new date

                //updating the database
                values.put(AddActivity.DB_DAY,c.get(Calendar.DATE)+"");
                values.put(AddActivity.DB_MONTH,c.get(Calendar.MONTH)+1+"");
                values.put(AddActivity.DB_YEAR,c.get(Calendar.YEAR)+"");
                values.put(AddActivity.DB_DATE,c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DATE));
                values.put(AddActivity.DB_DATE_TIME,c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DATE)+date_Time.substring(10));

            }
            if(notify.equalsIgnoreCase("Weekly")){

            }
            if(notify.equals("MONTHLY")){

            }
            if(notify.equals("YEARLY")){n_ey1=n_ey;
                if(isLeap(n_ey)) {
                    n_ey1+=4;
                }else {
                    while (!ifIsSmall(n_ed, n_em, n_ey)) {
                        n_ey1++;
                    }
                }
                values.put(AddActivity.DB_YEAR,n_ey1+"");
                values.put(AddActivity.DB_DATE,(n_ey1+"")+"-"+edate.substring(5, 7)+"-"+edate.substring(8, 10));
                values.put(AddActivity.DB_DATE_TIME,(n_ey1+"")+date_Time.substring(4));
            }
            db.update(AddActivity.DB_TABLE, values,null,null);
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
        } else if (ey > cy) {
            ifsmall = true;
        } else if(ey < cy) {
            ifsmall = false;
        }
        return ifsmall;
    }
    private boolean isLeap(int year) {

        return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
    }
    private int dayNo(int ed, int em, int ey){
        int days[]={0,31,28,31,30,31,30,31,31,30,31,30,31};
        int no=0;
        if(isLeap(em))
            days[2]=29;
        for(int i = 1; i < em; i++)
            no = no + days[i];
        no = no + ed;
        return no;
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