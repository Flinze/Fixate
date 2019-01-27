package com.fixate;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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
    private boolean isOnBreak;
    private boolean isWhisper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        Sensey.getInstance().init(this);
        Sensey.getInstance().startProximityDetection(proximityListener);

        countDownText = (TextView) findViewById(R.id.countdownText);
        warningTime = (TextView) findViewById(R.id.warningTime);
        ImageButton cancelButton = findViewById(R.id.cancelButton);
        //ImageButton pauseButton = findViewById(R.id.pauseButton);
        cancelButton.setOnClickListener(this);
        //pauseButton.setOnClickListener(this);

        currTask = getIntent().getIntExtra("currTask", 0);
        //
        createTimeCountDown(25*60);
    }

    //TODO: MAKE IT SO THE ANDROID BACK BUTTON DOESNT WORK CANT GO BACK TO LAST ACTIVITIY
    private void createTimeCountDown(long time) {

        timer = new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisRemaining) {
                isOnBreak = false;
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
                if (!isWhisper) {
                    isOnBreak = true;
                    NotificationCompat.Builder mBuilder;
                    if (Build.VERSION.SDK_INT >= 26) {
                        NotificationChannel channel = new NotificationChannel("channel1", "Channel 1", NotificationManager.IMPORTANCE_DEFAULT);
                        mBuilder = new NotificationCompat.Builder(Task.this, "channel1")
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("Break Time")
                                .setContentText("Your break has started")
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText("Your break has started"))
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Task.this);
                        NotificationManager notifManager = getSystemService(NotificationManager.class);
                        notifManager.createNotificationChannel(channel);
                        notificationManager.notify(123, mBuilder.build());
                    } else {
                        mBuilder = new NotificationCompat.Builder(Task.this)
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("Break Time")
                                .setContentText("Your break has started")
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText("Your break has started"))
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Task.this);
                        notificationManager.notify(123, mBuilder.build());
                    }


                    countDownText.setText("00:00");
                    if (currTask == 3) {
                        Intent i = new Intent(Task.this, LongBreak.class);
                        Sensey.getInstance().stop();
                        Sensey.getInstance().stopProximityDetection(proximityListener);

                        startActivity(i);
                    } else {
                        Sensey.getInstance().stop();
                        Sensey.getInstance().stopProximityDetection(proximityListener);
                        Intent i = new Intent(Task.this, Break.class);
                        i.putExtra("currTask", currTask);

                        startActivity(i);
                    }
                }
            }
        }.start();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.cancelButton:
                isOnBreak = true;
                Intent i = new Intent(Task.this, MainActivity.class);
                startActivity(i);
                break;

           /*
            case R.id.pauseButton:
                if (!isPaused) {

                    timer.cancel();
                    isPaused = true;


                } else {

                    isPaused = false;
                    createTimeCountDown(milliRemaining/1000);

                }*/

        }
    }

    ProximityDetector.ProximityListener proximityListener = new ProximityDetector.ProximityListener() {
        private CountDownTimer t;
        @Override public void onNear() {
            // Near to device
            t.cancel();
        }

        @Override public void onFar() {
            t = new CountDownTimer(10000, 1000) {
                @Override
                public void onTick(long millisRemaining) {
                    warningTime.setText("" + millisRemaining/1000);
                }

                @Override
                public void onFinish() {
                    if (!isOnBreak) {
                        isWhisper = true;
                        Intent i = new Intent(Task.this, Whisper.class);
                        startActivity(i);
                    }
                }
            }.start();
        }
    };
}
