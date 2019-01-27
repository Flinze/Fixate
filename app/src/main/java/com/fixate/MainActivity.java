package com.fixate;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        // Code below forces login page
        Intent intent = new Intent(this, Login.class);
        intent.putExtra("EXIT", false);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void start(View v) {
        Intent intent = new Intent(MainActivity.this, Task.class);
        int currTask = 0;
        intent.putExtra("currTask", currTask);
        startActivity(intent);
    }
}
