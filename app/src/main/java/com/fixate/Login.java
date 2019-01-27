package com.fixate;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private String postUrl = "https://fixate.herokuapp.com/api/user/login/";
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLoginClick(View v) {

        emailEditText = findViewById(R.id.emailInput);
        passwordEditText = findViewById(R.id.passwordInput);

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        String postJson =
                "{\"email\":\"" + email + "\","
                + "\"password\":\"" + password + "\"}";
        try {
            postRequest(postUrl,postJson);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void postRequest(String postUrl, String postBody) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, postBody);

        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                System.out.println("ON FAIL @@@@@@@@@@@@@@@@@@@@@@@22");
                System.out.println(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("TAG",response.body().string());
                System.out.println("ON RESPONSE: " + response.body().string());
                String jsonData = response.body().string();
            try {
                JSONObject jObject = new JSONObject(jsonData);
                JSONArray jArray = jObject.getJSONArray("token");

                for (int i = 0; i < jArray.length(); i++) {
                    token = jArray.getString(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
                System.out.println(token);
                SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                preferences.edit().putString("token", token).commit();
            }
        });
    }
}
