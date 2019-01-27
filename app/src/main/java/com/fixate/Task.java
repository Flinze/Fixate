package com.fixate;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Task extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
    }

    protected void createTimeCountDown(int time) {
        CountDownTimer timer = new CountDownTimer(time * 60000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

            }
        };
    }

}
