package com.sagarpatel26.friedo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

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

            String q = getQuestion(currentFragmentIndex);
            ArrayList<String> s = getOptionList(currentFragmentIndex);

            InterestFragment currentFragment = InterestFragment.newInstance(q, s);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, currentFragment).commit();

        }
    }

    public void OnNextButtonClicked(ArrayList<String> a) {

        if (currentFragmentIndex + 1 >= 3) {
            return;
        }

        currentFragmentIndex++;
        String q = getQuestion(currentFragmentIndex);
        ArrayList<String> s = getOptionList(currentFragmentIndex);

        InterestFragment fragment = InterestFragment.newInstance(q, s);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
                .commit();
    }

    String getQuestion(int i) {

        String[] q = new String[]{"Question 1", "Question 2", "Question 3"};
        return q[i];
    }

    ArrayList<String> getOptionList(int i) {

        ArrayList<ArrayList<String>> al = new ArrayList<>();

        ArrayList<String> al1 = new ArrayList<>();
        al1.add("Option 1.1");
        al1.add("Option 1.2");
        al1.add("Option 1.3");
        al1.add("Option 1.4");
        al.add(al1);

        ArrayList<String> al2 = new ArrayList<>();
        al2.add("Option 2.1");
        al2.add("Option 2.2");
        al2.add("Option 2.3");
        al2.add("Option 2.4");
        al.add(al2);

        ArrayList<String> al3 = new ArrayList<>();
        al3.add("Option 3.1");
        al3.add("Option 3.2");
        al3.add("Option 3.3");
        al3.add("Option 3.4");
        al.add(al3);

        return al.get(i);
    }
/*
    private class InterestQuestionsOptions {

        static final private String URL = "http://192.168.0.100:2694/getallinterestquestions";

        ArrayList<String> questions = new ArrayList<>();
        ArrayList<ArrayList<String>> options = new ArrayList<>();

        InterestQuestionsOptions() {


        }
    }
*/

}
