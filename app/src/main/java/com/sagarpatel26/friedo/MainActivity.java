package com.sagarpatel26.friedo;


import android.content.Intent;
import android.os.Bundle;
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

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_login_submit = (Button)findViewById(R.id.btn_login_submit);
        btn_login_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    JSONObject jsonParams = new JSONObject();
                    jsonParams.put("username", ((EditText) findViewById(R.id.et_username)).getText());
                    jsonParams.put("password", ((EditText) findViewById(R.id.et_password)).getText());
                    StringEntity entity = new StringEntity(jsonParams.toString());

                    AsyncHttpClient httpClient = new AsyncHttpClient();
                    httpClient.post(getBaseContext(),
                            "http://192.168.43.205:5000/login",
                            entity,
                            "application/json",
                            new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                                    String mssg = new String(responseBody);
                                    ((TextView) findViewById(R.id.tv_response)).setText(mssg);

                                    if (mssg.equals("OK")) {

                                        Intent intent = new Intent(getApplicationContext(), InterestUpdateActivity.class);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                    ((TextView) findViewById(R.id.tv_response)).setText(error.getMessage());
                                }
                            });
                } catch (UnsupportedEncodingException | JSONException e) {
                    Log.d("LoginReq", e.getMessage());
                }
            }
        });


    }
}
