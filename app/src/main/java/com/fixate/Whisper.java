package com.fixate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Whisper extends AppCompatActivity {

    private String token = null;
    private String url = "https://fixate.herokuapp.com/api/v2/whisper/";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private TextView incompleteLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whisper);
    }

    public void onSubmit(View v) {
        // Do something with the user input
        EditText userInputEditText = (EditText) findViewById(R.id.whisperInput);

        String userInput = userInputEditText.getText().toString();
        String userJson = jsonString(userInput);

        SharedPreferences mPrefs = this.getSharedPreferences("token", MODE_PRIVATE);
        token = mPrefs.getString("token","");

        try {
            postRequest(url, userJson);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent i = new Intent(Whisper.this, MainActivity.class);
        startActivity(i);
    }

    private String jsonString(String whisper) {
        return "{\"content\":\"" + whisper + "\"}";
    }

    private void postRequest(String postUrl, String postBody) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, postBody);
        Request request = new Request.Builder()
                .url(postUrl)
                .addHeader("token", token)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.d("TAG",response.body().string());
                String jsonData = response.body().string();
                try {
                    JSONObject jObject = new JSONObject(jsonData);
                    String content;
                    content = jObject.getString("content");
                    System.out.print("token");

                } catch (Exception e) {
                    e.printStackTrace();
                }


                // Check if response is correct
//                if () {
//                    postSendWhisper();
//                }

            }
        });
    }

    private void postSendWhisper() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
