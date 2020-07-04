package com.example.myplan;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.icu.text.MessagePattern.ArgType.SELECT;

public class YourEvent_Activity extends AppCompatActivity {
    List<Event> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_event_);

        extractAndPopulateEventList();

        EventAdapter adapter = new EventAdapter(YourEvent_Activity.this, data);
        ListView listView = findViewById(R.id.list_view_your_event);
        listView.setAdapter(adapter);
    }
  //  public void onclickDelete(View view) {
  //      SQLiteDatabase db = SQLiteDatabase.openDatabase(getFilesDir().getAbsolutePath(),null,SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.OPEN_READONLY);
  //  }

    private void extractAndPopulateEventList() {

        //opening the database in the particular path
        SQLiteDatabase db = SQLiteDatabase.openDatabase(getFilesDir().getAbsolutePath()+"/"+AddActivity.DB_FILE,null,SQLiteDatabase.OPEN_READONLY);

        String[] projection={
                AddActivity.DB_ID,
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

        Cursor cursor = db.query(AddActivity.DB_TABLE,projection, null,null,null,null, order);
        if(cursor!=null) {
            while (cursor.moveToNext()) {
                Event newEvent = new Event();
                newEvent.description = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_DESCRIPTION));
                newEvent.date = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_DATE));
                newEvent.time = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_TIME));
                newEvent.ampm = cursor.getString(cursor.getColumnIndexOrThrow(AddActivity.DB_AMPM));

                data.add(newEvent);
            }
        }
    }

    public void onClickYourEventActPrev(View view){
        Intent intent=new Intent(YourEvent_Activity.this, OneActivity.class);
        onBackPressed();
    }
}