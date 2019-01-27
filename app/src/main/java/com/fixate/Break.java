package com.fixate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Break extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_break);
        
    }

    private void nextTask() {
        Intent intent = new Intent(Break.this, Task.class);
        Bundle extras = getIntent().getExtras();
        int currTask = (Integer) getIntent().getExtras().get("currTask");
        currTask++;

        intent.putExtra("currTask", currTask);
        startActivity(intent);
    }
}
