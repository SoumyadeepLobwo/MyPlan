package com.example.myplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CalenderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
    }
    public void onClickCalenderActPrev(View view){ onBackPressed();   }

    public void onClickAddConstraint(View view){
        Intent intent = new Intent(CalenderActivity.this, AddActivity.class);
        startActivity(intent);
    }
}