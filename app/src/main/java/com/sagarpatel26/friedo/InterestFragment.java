package com.sagarpatel26.friedo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class InterestFragment extends Fragment {


    private static final int MAX_SELECTION = 3;

    private static final String ARG_QUESTION = "question";
    private static final String ARG_INTEREST_LIST = "interestList";
    private static final String ARG_INTEREST_SELECTED = "selectedinterest";
    OnNextButtonClickedListener mListener;
    private String question;
    private ArrayList<Interest> interestList;

    public InterestFragment() {
        // Required empty public constructor
    }

    public static InterestFragment newInstance(String question, ArrayList<String> interestList) {

        InterestFragment fragment = new InterestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUESTION, question);
        args.putStringArrayList(ARG_INTEREST_LIST, interestList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = getArguments().getString(ARG_QUESTION);
            ArrayList<String> tmp_list = getArguments().getStringArrayList(ARG_INTEREST_LIST);

            if (tmp_list != null) {

                interestList = new ArrayList<>();
                Log.d("AdapterD", "done creating intereslist");
                for (String interest : tmp_list) {

                    interestList.add(new Interest(interest, false));
                }
            }
            if (savedInstanceState != null) {

                ArrayList<Integer> al = savedInstanceState.getIntegerArrayList(ARG_INTEREST_SELECTED);

                if (al != null) {
                    for (int i = 0; i < al.size(); ++i) {

                        if (al.get(i) == 1)
                            interestList.get(i).setChecked(true);
                        else
                            interestList.get(i).setChecked(false);
                    }
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_interest_question, container, false);

        ((TextView) (view.findViewById(R.id.question))).setText(question);

        final ListView optionsList = (ListView) (view.findViewById(R.id.optionslist));
        InterestAdapter interestAdapter = new InterestAdapter(getActivity(), interestList);
        optionsList.setAdapter(interestAdapter);
        Log.d("AdapterD", "SetAdpter");
        Button nextBtn = (Button) (view.findViewById(R.id.nextbtn));
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO: write a method that would create a list of all interest selected by the user, ALSO IF possible limit them to max 3
                ArrayList<String> arg = new ArrayList<>();

                View child_view;
                for (int i = 0; i < optionsList.getCount(); ++i) {

                    child_view = optionsList.getChildAt(i);
                    CheckBox checkBox = (CheckBox) child_view.findViewById(R.id.interestckbx);
                    TextView textView = (TextView) child_view.findViewById(R.id.interesttxt);
                    if (checkBox.isChecked())
                        arg.add(textView.getText().toString());
                }

                if (arg.size() > MAX_SELECTION) {

                    Toast.makeText(getActivity(), "You cannot select more than " + String.valueOf(MAX_SELECTION) + " choices", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    mListener = (OnNextButtonClickedListener) getActivity();
                } catch (ClassCastException e) {
                    Log.d("Fragment", getActivity().toString() + " must implement OnHeadlineSelectedListener");
                    throw new ClassCastException(getActivity().toString()
                            + " must implement OnHeadlineSelectedListener");
                }
                mListener.OnNextButtonClicked(arg);
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Integer> tmp_al = new ArrayList<>();
        for (Interest interest : interestList) {
            tmp_al.add(interest.isChecked() ? 1 : 0);
        }
        outState.putIntegerArrayList(ARG_INTEREST_SELECTED, tmp_al);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    public interface OnNextButtonClickedListener {
        void OnNextButtonClicked(ArrayList<String> selectedOptions);
    }
}
