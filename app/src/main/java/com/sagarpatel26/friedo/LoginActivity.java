package com.sagarpatel26.friedo;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String token = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.KEY_TOKEN, "EXPIRED");
        if (!token.equals("EXPIRED")) {

            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }

        Button btn_login_submit = (Button)findViewById(R.id.btn_login_submit);
        btn_login_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = ((EditText) findViewById(R.id.et_username)).getText().toString();
                String password = ((EditText) findViewById(R.id.et_password)).getText().toString();

                AsyncHttpClient httpClient = new AsyncHttpClient();
                httpClient.setBasicAuth(username, password);
                httpClient.get(getBaseContext(),
                        Constants.BASE_URL + Constants.URL_TOKEN,
                        new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                                String responseString = new String(responseBody);
                                if (responseString.equals("Unauthorized Access")) {

                                    ((TextView) findViewById(R.id.tv_response)).setText("Wrong Credentials");
                                }
                                else {

                                    JSONObject jsonReader = null;
                                    try {
                                        jsonReader = new JSONObject(responseString);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        int userId = jsonReader.getInt(Constants.KEY_USERID);
                                        String httpAuthToken = jsonReader.getString(Constants.KEY_TOKEN);
                                        boolean first_time = jsonReader.getBoolean("first_time");

                                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putInt(Constants.KEY_USERID, userId);
                                        editor.putString(Constants.KEY_TOKEN, httpAuthToken);
                                        editor.commit();

                                        if (first_time) {
                                            Intent intent = new Intent(getApplicationContext(), InterestUpdateActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
                                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                ((TextView) findViewById(R.id.tv_response)).setText(error.getMessage());
                            }
                        });
            }

        });


        Button btn_main_register = (Button) findViewById(R.id.btn_main_register);
        btn_main_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), RegisterActivity.class));
                finish();
            }
        });

    }
}
