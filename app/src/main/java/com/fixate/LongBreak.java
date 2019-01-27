package com.fixate;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LongBreak extends AppCompatActivity {

    private TextView longBreakCountDown;
    private int currTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_break);
        longBreakCountDown = (TextView) findViewById(R.id.longBreakCountDown);
        createTimeCountDown(10);
    }

    protected void createTimeCountDown(int time) {
        //TODO:change this time * 1000 back to 60000
        CountDownTimer timer = new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisRemaining) {
                long secondsRemaining = millisRemaining / 1000;
                long minutes = secondsRemaining / 60;
                long seconds = secondsRemaining % 60;
                longBreakCountDown.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                longBreakCountDown.setText("00:00");

                // TODO: Decide if the user is brought back to main activity or stays on page
//                Intent i = new Intent(LongBreak.this, MainActivity.class);
//                startActivity(i);
            }
        }.start();
    }

    public void onClickContinue(View view) {
        Intent i = new Intent(LongBreak.this, Task.class);
        // Resets the current task;
        currTask = 0;
        i.putExtra("currTask", currTask);
        startActivity(i);
    }

    public void onClickEnd(View view) {
        Intent i = new Intent(LongBreak.this, MainActivity.class);
        startActivity(i);
    }
}
