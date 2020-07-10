package com.example.myplan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalenderActivity extends AppCompatActivity {

    List<Event> data = new ArrayList<>();
    //declaring the database
    private SQLiteDatabase db;
    EventAdapter adapter;
    ListView listView;
    String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        String d,m,y;//variables that will store the current day, month amd year
        Calendar calendar = Calendar.getInstance();
        y = calendar.get(Calendar.YEAR)+"";
        m = (calendar.get(Calendar.MONTH)+1)+"";
        d = calendar.get(Calendar.DAY_OF_MONTH)+"";
        if(Integer.parseInt(d)<10) d = "0"+d;
        if(Integer.parseInt(m)<10) m = "0"+m;
        selectedDate = y+"-"+m+"-"+d;// this gets stored as the default current date until another date is clicked on the calendar view

        //marking the date containing events

        //finished marking dates containing events


        CalendarView calendarView = findViewById(R.id.calendarView);
        //this line set the listener to the calender veiw which listens to the made changes if any
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //this function is called only when a different date is selected from the calendar view
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String daystr=day+"",monthstr=month+1+""; //changed by teeon "month+1"
                if(day<10) daystr = "0"+daystr;
                if(month<9) monthstr = "0"+monthstr;
                selectedDate = year+"-"+monthstr+"-"+daystr;

                //clears all the elements of the arraylist if any. This is because we have to add to the array list anyway when we start the activity or select a date on the calendar view.
                //if we dont clear all the elements of the list before each extractAnd.... func call then in the list view it will display all the events present in the list. We dont
                //want this to happen. We only want to display the events od selectedDate
                if( data.size() > 0)
                    data.clear();

                //get data from db into events list
                extractAndPopulateEventList();//this function is called only when another date is selected other wise the function call outside this function is executed

                //adapter = new EventAdapter(CalenderActivity.this, data);
                //ListView listView = findViewById(R.id.calendar_list_view);
                //listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        //opening the database
        db = SQLiteDatabase.openDatabase(getFilesDir().getAbsolutePath()+"/"+AddActivity.DB_FILE,null,SQLiteDatabase.CREATE_IF_NECESSARY|SQLiteDatabase.OPEN_READWRITE);

        //clears all the elements of the arraylist if any. This is because we have to add to the array list anyway when we start the activity or select a date on the calendar view.
        //if we dont clear all the elements of the list before each extractAnd.... func call then in the list view it will display all the events present in the list. We dont
        //want this to happen. We only want to display the events od selectedDate
        if( data.size() > 0)
            data.clear();

        //get data from db into events list
        extractAndPopulateEventList();

        adapter = new EventAdapter(CalenderActivity.this, data);
        listView = findViewById(R.id.calendar_list_view);
        listView.setAdapter(adapter);

    }

    public void onClickDelete(View view) {
        View parent = (View) view.getParent();
        TextView uuidText = parent.findViewById(R.id.uuid_text);
        String uuid1 = uuidText.getText().toString();
        db.delete(AddActivity.DB_TABLE, AddActivity.DB_UNIQUEID+" = \""+uuid1+"\"", null);
        for(int i=0; i<data.size(); i++)
        {
            if (uuid1.equals(data.get(i).uniqueID)) {
                data.remove(i);
                break;
            }
        }
        adapter.notifyDataSetChanged();//refreshes the list view after one item is deleted
    }
    public void onClickSeeOrUpdate(View view){
        View parent = (View) view.getParent();
        TextView uuidText = parent.findViewById(R.id.uuid_text);
        String uuid = uuidText.getText().toString();
        Intent intent = new Intent(CalenderActivity.this, SeeOrUpdate.class);

        //passing the the uuid as string extra from this activity to SeeOrUpdate
        intent.putExtra("uuid", uuid);

        startActivity(intent);
    }

    private void extractAndPopulateEventList() {

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
        Cursor cursor = db.query(AddActivity.DB_TABLE,projection, AddActivity.DB_DATE +" = \""+selectedDate+"\"",null,null,null, order);


        TextView noEventMessage;
        if(cursor.getCount() == 0){
            noEventMessage=findViewById(R.id.no_event_message);
            noEventMessage.setVisibility(View.VISIBLE);
        }
        else {
            noEventMessage=findViewById(R.id.no_event_message);
            noEventMessage.setVisibility(View.INVISIBLE);
        }
        if(cursor!=null) {
            while (cursor.moveToNext()) {
                Event newEvent = new Event();
                newEvent.description = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_DESCRIPTION));
                newEvent.date = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_DATE));
                newEvent.time = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_TIME));
                newEvent.ampm = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_AMPM));
                newEvent.uniqueID=cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_UNIQUEID));

                data.add(newEvent);
            }
            cursor.close();
        }
    }

    public void onClickCalenderActPrev(View view){ onBackPressed();   }

    public void onClickAddConstraint(View view){
        Intent intent = new Intent(CalenderActivity.this, AddActivity.class);
        startActivity(intent);
    }
}