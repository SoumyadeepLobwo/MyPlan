package com.example.myplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class YourEvent_Activity extends AppCompatActivity {
    List<Event> data = new ArrayList<>();
    //opening the database
    private SQLiteDatabase db;
    EventAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_event_);

        //opening the database
        db = SQLiteDatabase.openDatabase(getFilesDir().getAbsolutePath()+"/"+AddActivity.DB_FILE,null,SQLiteDatabase.CREATE_IF_NECESSARY|SQLiteDatabase.OPEN_READWRITE);
        extractAndPopulateEventList();

        adapter = new EventAdapter(YourEvent_Activity.this, data);
        ListView listView = findViewById(R.id.list_view_your_event);
        listView.setAdapter(adapter);
    }
    //public static String uuid = "";
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
        Intent intent = new Intent(YourEvent_Activity.this, SeeOrUpdate.class);

        //passing the the uuid as string extra from this activity to SeeOrUpdate
        intent.putExtra("uuid", uuid);

        startActivity(intent);
    }

    private void extractAndPopulateEventList() {

        //opening the database in the particular path
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

        Cursor cursor = db.query(AddActivity.DB_TABLE,projection, null,null,null,null, order);
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
            TextView noEventMessage;
            if(cursor.getCount() == 0){
                noEventMessage=findViewById(R.id.no_event_message);
                noEventMessage.setVisibility(View.VISIBLE);
            }
            else {
                noEventMessage=findViewById(R.id.no_event_message);
                noEventMessage.setVisibility(View.INVISIBLE);
            }
        }
        cursor.close();

    }

    public void onClickYourEventActPrev(View view){
        onBackPressed();
    }
}