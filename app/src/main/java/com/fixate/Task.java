package com.fixate;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Task extends AppCompatActivity implements View.OnClickListener {

    private TextView countDownText = null;
    private int currTask;
    private long currMinutes = 0;
    private ImageButton cancelButton;
    private ImageButton pauseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        countDownText = (TextView) findViewById(R.id.countdownText);
        cancelButton = findViewById(R.id.cancelButton);
        pauseButton = findViewById(R.id.pauseButton);

        currTask = getIntent().getIntExtra("currTask", 0);
        createTimeCountDown(25);
    }

    private void createTimeCountDown(int time) {
        CountDownTimer timer = new CountDownTimer(time * 60000, 1000) {
            @Override
            public void onTick(long millisRemaining) {
                long secondsRemaining = millisRemaining / 1000;
                long minutes = secondsRemaining / 60;
                if (minutes <= 23) {
                    View b = findViewById(R.id.cancelButton);
                    b.setVisibility(View.GONE);
                }
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.cancelButton:
                Intent i = new Intent(Task.this, MainActivity.class);
                startActivity(i);
                break;

            case R.id.pauseButton:

        }
    }
}
