package com.sagarpatel26.friedo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        Intent  activity_intent = getIntent();
        String username = activity_intent.getStringExtra("username");
        if (username==null)
            username="ERROR";
        TextView tv_username = (TextView) findViewById(R.id.tv_pusername);
        final TextView tv_email = (TextView) findViewById(R.id.tv_pemail);
        //api/users/profile/

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(this,
                Constants.BASE_URL + "/api/users/profile/" + username,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String response = new String(responseBody);
                        try {

                            JSONObject user = (new JSONObject(response)).getJSONObject("user");
                            String email = user.getString("email");
                            Log.d("UP", email);
                            tv_email.setText(email);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        Intent intent = new Intent(getBaseContext(), Failure.class);
                        startActivity(intent);
                        finish();
                    }
                });

        tv_username.setText(username);

        Button btn_sm = (Button) findViewById(R.id.btn_sendMessage);
        btn_sm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{tv_email.getText().toString()});
                email.putExtra(Intent.EXTRA_SUBJECT, "Friedo Friend Request!");
                email.putExtra(Intent.EXTRA_TEXT, "Hi, Can we be Friends?");
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });

    }
}
