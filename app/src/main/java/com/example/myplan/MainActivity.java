package com.example.myplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, OneActivity.class);
                startActivity(intent);
                finish();//because of this line  Main_activity gets removed from the stack
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 3000);
    }
}