package com.example.myplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
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