package com.sagarpatel26.friedo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class InterestUpdateActivity extends AppCompatActivity
        implements InterestFragment.OnNextButtonClickedListener {

    private int currentFragmentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_update);


        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }


            progressDialog = new ProgressDialog(this);
            InterestQuestionsOptionsFetcher iqof = new InterestQuestionsOptionsFetcher();
            iqof.execute();
        }

    }

    private ArrayList<String> optionsSelected = new ArrayList<>();

    void addToSelectedOptions(ArrayList<Integer> arg) {

        StringBuilder sb = new StringBuilder();
        for (int i=0;i<Constants.MAX_SELECTION;++i)
            if (i<arg.size())
                sb.append(String.valueOf(arg.get(i)+1)).append(",");
            else
                sb.append("0,");
        sb.replace(sb.length()-1, sb.length(), "");
        optionsSelected.add(sb.toString());
        Log.d("SELECTED", sb.toString());
    }

    public void OnNextButtonClicked(ArrayList<Integer> arg) {

        if (currentFragmentIndex + 1 == optionsList.size()) {

            addToSelectedOptions(arg);

            AsyncHttpClient httpClient = new AsyncHttpClient();
            final JSONObject jsonParams = new JSONObject();

            try {
                jsonParams.put(Constants.KEY_USERID, PreferenceManager.getDefaultSharedPreferences(this).getInt(Constants.KEY_USERID, 0));
                jsonParams.put(Constants.KEY_BOOKS, optionsSelected.get(0));
                jsonParams.put(Constants.KEY_MOVIES,optionsSelected.get(1));
                jsonParams.put(Constants.KEY_TVSHOWS,optionsSelected.get(2) );
                jsonParams.put(Constants.KEY_FOOD, optionsSelected.get(3));
                jsonParams.put(Constants.KEY_HOBBIES, optionsSelected.get(4));
                jsonParams.put(Constants.KEY_HATE, optionsSelected.get(5));
                jsonParams.put(Constants.KEY_DREAMS, optionsSelected.get(6));
                jsonParams.put(Constants.KEY_DREAMCITY, optionsSelected.get(7));

                final StringEntity entity = new StringEntity(jsonParams.toString());
                httpClient.setBasicAuth(PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.KEY_TOKEN, ""), "");
                httpClient.post(getBaseContext(),
                        Constants.BASE_URL + Constants.URL_INTERESTS,
                        entity,
                        "application/json",
                        new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                                Log.d("IRET", new String(responseBody));
                                startActivity(new Intent(getBaseContext(), Thanks.class));
                                finish();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


                                Log.d("IRETERR", entity.toString());
                                Log.d("IRETERR", jsonParams.toString());


                                Log.d("IRETERR", new String(responseBody));
                                Log.d("IRETERR", error.getMessage());
                                startActivity(new Intent(getBaseContext(), Failure.class));
                                finish();
                            }
                        });

            } catch (JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return;
        }

        addToSelectedOptions(arg);

        currentFragmentIndex++;
        String q = getQuestion(currentFragmentIndex);
        ArrayList<String> s = getOptionList(currentFragmentIndex);

        InterestFragment fragment = InterestFragment.newInstance(q, s);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
                .commit();
    }

    String getQuestion(int i) {

        return questionsList.get(i);
    }

    ArrayList<String> getOptionList(int i) {

        return optionsList.get(i);
    }


    private ArrayList<String> questionsList = new ArrayList<>();
    private ArrayList<ArrayList<String>> optionsList = new ArrayList<>();

    private ProgressDialog progressDialog;


    private class InterestQuestionsOptionsFetcher extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d("NOW1", "PreShit");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Please Wait !!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.equals("NOTOK")) {

                progressDialog.dismiss();

                String q = getQuestion(currentFragmentIndex);
                ArrayList<String> sl = getOptionList(currentFragmentIndex);

                InterestFragment currentFragment = InterestFragment.newInstance(q, sl);
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, currentFragment).commit();


                Toast.makeText(getBaseContext(), "Note: Once you click Next you want to go back, for any change. You change it later from Profile page.", Toast.LENGTH_LONG).show();


            } else {

                Toast.makeText(getBaseContext(), "Problem Occurred, Please try again later", Toast.LENGTH_LONG).show();
                Log.d("ASYNC", "doPostExecute Error");
            }
        }

        @Override
        protected String doInBackground(Void... voids) {


            Log.d("NOW1", "Starting doInBackground!!");
            String response = "NOTOK";

            try {
                URL url = new URL(Constants.BASE_URL + Constants.URL_QUESTIONS);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    StringBuilder stringBuilder = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null)
                        stringBuilder.append(line);
                    response = stringBuilder.toString();
                    Log.d("NOW1", "Done Downloading");

                }

            } catch (MalformedURLException e) {
                Log.d("ASYNC", e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("NOW1", "JSON Start");
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray questionsJsonArray = jsonObject.getJSONArray(Constants.KEY_QUESTIONS);
                for (int i = 0; i < questionsJsonArray.length(); ++i) {


                    JSONObject questionJsonObject = questionsJsonArray.getJSONObject(i);
                    String question = questionJsonObject.getString(Constants.KEY_QUESTION);
                    questionsList.add(question);

                    // Log.d("Async", question);

                    ArrayList<String> options = new ArrayList<>();
                    JSONArray optionsArray = questionJsonObject.getJSONArray(Constants.KEY_OPTIONS);
                    for (int j = 0; j < optionsArray.length(); ++j) {

                        String option = optionsArray.getString(j);
                        options.add(option);
                    }
                    optionsList.add(options);
                }

            } catch (JSONException e) {
                Log.d("Async", "JSON ERROR JSON ERROR JSON ERROR!!!! ");
                e.printStackTrace();
            }

            Log.d("ASYNC", response);
            return response;
        }
    }
}
