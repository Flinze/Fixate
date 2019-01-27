package com.fixate;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.nisrulz.sensey.ProximityDetector;
import com.github.nisrulz.sensey.Sensey;

public class Task extends AppCompatActivity implements View.OnClickListener {

    private TextView countDownText;
    private TextView warningTime;
    private int currTask;
    private long milliRemaining = 0;
    private boolean isPaused;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        Sensey.getInstance().init(this);
        Sensey.getInstance().startProximityDetection(proximityListener);

        countDownText = (TextView) findViewById(R.id.countdownText);
        warningTime = (TextView) findViewById(R.id.warningTime);
        ImageButton cancelButton = findViewById(R.id.cancelButton);
        ImageButton pauseButton = findViewById(R.id.pauseButton);
        cancelButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);

        currTask = getIntent().getIntExtra("currTask", 0);
        //
        createTimeCountDown(1210);
    }

    //TODO: MAKE IT SO THE ANDROID BACK BUTTON DOESNT WORK CANT GO BACK TO LAST ACTIVITIY
    private void createTimeCountDown(long time) {
        timer = new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisRemaining) {
                milliRemaining = millisRemaining;
                long secondsRemaining = millisRemaining / 1000;
                long minutes = secondsRemaining / 60;
                if (minutes < 20) {
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
                    Sensey.getInstance().stop();
                    Sensey.getInstance().stopProximityDetection(proximityListener);

                    startActivity(i);
                } else {
                    Intent i = new Intent(Task.this, Break.class);
                    i.putExtra("currTask", currTask);
                    Sensey.getInstance().stop();
                    Sensey.getInstance().stopProximityDetection(proximityListener);

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
                if (!isPaused) {
                    timer.cancel();
                    isPaused = true;
                } else {
                    isPaused = false;
                    createTimeCountDown(milliRemaining/1000);
                }

        }
    }

    ProximityDetector.ProximityListener proximityListener = new ProximityDetector.ProximityListener() {
        private CountDownTimer t;
        @Override public void onNear() {
            // Near to device

            t.cancel();
            System.out.println("THIS IS CLOSE@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        }

        @Override public void onFar() {
            t = new CountDownTimer(5000, 1000) {
                @Override
                public void onTick(long millisRemaining) {

                    warningTime.setText("" + millisRemaining/1000);
                }

                @Override
                public void onFinish() {
                    Intent i = new Intent(Task.this, Whisper.class);
                    startActivity(i);
                }
            }.start();
            System.out.println("THIS IS FAR@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        }
    };
}
