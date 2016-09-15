package com.sagarpatel26.friedo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btn = (Button) findViewById(R.id.btn_register);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username      = ((EditText)findViewById(R.id.et_uname)).getText().toString();
                String email         = ((EditText)findViewById(R.id.et_email)).getText().toString();
                String passw         = ((EditText)findViewById(R.id.et_passw)).getText().toString();
                String confirm_passw = ((EditText)findViewById(R.id.et_confirm_passw)).getText().toString();

                if (!passw.equals(confirm_passw)) {
                    ((TextView) findViewById(R.id.tv_error_mssg)).setText("Passwords do not Match");
                    return;
                }

                AsyncHttpClient httpClient = new AsyncHttpClient();
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("username ", username);
                    jsonParams.put("email", email);
                    jsonParams.put("password", passw);

                    StringEntity entity = new StringEntity(jsonParams.toString());

                    httpClient.post(getBaseContext(),
                            Constants.BASE_URL + Constants.URL_USERS,
                            entity,
                            "application/json",
                            new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                                    Toast.makeText(getBaseContext(), "Successfully Registered Please Login to Continue!", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                                    finish();
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                    Log.d("REG", new String(responseBody));

                                    if (statusCode == 409)
                                        ((TextView) findViewById(R.id.tv_error_mssg)).setText("Username or Email Alredy exists!!");
                                    else
                                        ((TextView) findViewById(R.id.tv_error_mssg)).setText(error.getMessage());
                                }
                            });

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
