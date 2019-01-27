package com.fixate;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Task extends AppCompatActivity {

    private TextView countDownText = null;
    private int currTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        countDownText = (TextView) findViewById(R.id.countdownText);
        currTask = getIntent().getIntExtra("currTask", 0);
        createTimeCountDown(25);
    }

    protected void createTimeCountDown(int time) {
        CountDownTimer timer = new CountDownTimer(time * 60000, 1000) {
            @Override
            public void onTick(long millisRemaining) {
                long secondsRemaining = millisRemaining / 1000;
                long minutes = secondsRemaining / 60;
                long seconds = secondsRemaining % 60;
                countDownText.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                countDownText.setText("00:00");
                if (currTask == 3) {
                    Intent i = new Intent(Task.this, LongBreak.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(Task.this, Break.class);
                    i.putExtra("currTask", currTask);
                    startActivity(i);
                }
            }
        }.start();
    }

}
