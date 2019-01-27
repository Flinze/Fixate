package com.fixate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Whisper extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whisper);
    }

    public void onSubmit(View v) {
        // Do something with the user input
        EditText userInput = (EditText) findViewById(R.id.whisperInput);

        Intent i = new Intent(Whisper.this, MainActivity.class);
        startActivity(i);
    }
}
