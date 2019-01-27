package com.fixate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
    private String token = null;

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

        SharedPreferences mPrefs = this.getSharedPreferences("token", MODE_PRIVATE); //add key
        token = mPrefs.getString("token","");

            String postJson =
                    "{\"email\":\"" + email + "\","
                            + "\"password\":\"" + password + "\"}";
            try {
                postRequest(postUrl, postJson);
            } catch (IOException e) {
                e.printStackTrace();
            }

        if (token != null) {
            loginVerified();
        } else {
            // TODO: response takes longer to come back, may display invalid even if valid
            Toast.makeText(this, "Invalid login",
                    Toast.LENGTH_LONG).show();
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
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.d("TAG",response.body().string());
                String jsonData = response.body().string();
            try {
                JSONObject jObject = new JSONObject(jsonData);
                token = jObject.getString("token");

            } catch (Exception e) {
                e.printStackTrace();
            }
                System.out.println(token);
                SharedPreferences preferences = getSharedPreferences("token", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("token", token);
                editor.commit();

                if (token != null) {
                    loginVerified();
                }
            }
        });
    }

    private void loginVerified() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("token", token);
        startActivity(intent);
    }
}
