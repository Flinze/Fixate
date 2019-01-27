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
        createTimeCountDown(1);
    }

    protected void createTimeCountDown(int time) {
        CountDownTimer timer = new CountDownTimer(time * 60000, 1000) {
            @Override
            public void onTick(long timeRemaining) {
                countDownText.setText("" + timeRemaining / 60000);
            }

            @Override
            public void onFinish() {
                countDownText.setText("00:00");
                Intent i = new Intent(Task.this, Break.class);
                i.putExtra("currIntent", currTask);
                startActivity(i);
            }
        };
    }

}
