package com.sagarpatel26.friedo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        int userId = PreferenceManager.getDefaultSharedPreferences(this).getInt(Constants.KEY_USERID, 0);
        final String token = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.KEY_TOKEN, "NoToken");
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.setBasicAuth(token, "");
        httpClient.get(this,
                Constants.BASE_URL+"/api/suggested_friends/" + String.valueOf(userId),
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        ((TextView) findViewById(R.id.et_mssg_home)).setText(responseBody.toString() + " " + token);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Constants.KEY_TOKEN, "EXPIRED");
                        editor.commit();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}
