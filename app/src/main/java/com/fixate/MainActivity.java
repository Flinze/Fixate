package com.fixate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences mPrefs = this.getSharedPreferences("token", MODE_PRIVATE); //add key
        String token = mPrefs.getString("token","");

//        String token = getIntent().getStringExtra("token");

        // Code below forces login page
        if (token == "") {
            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", false);
            startActivity(intent);
        }

    }

    public void start(View v) {
        Intent intent = new Intent(MainActivity.this, Task.class);
        int currTask = 0;
        intent.putExtra("currTask", currTask);
        startActivity(intent);
    }
}
