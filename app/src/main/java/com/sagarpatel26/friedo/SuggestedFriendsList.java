package com.sagarpatel26.friedo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SuggestedFriendsList extends Fragment {

    private OnFragmentInteractionListener mListener;

    public SuggestedFriendsList() {
    }

    ArrayList<String> suggestedFriendsArrayList;

    public static SuggestedFriendsList newInstance(ArrayList<String> suggestedFriends) {
        SuggestedFriendsList fragment = new SuggestedFriendsList();
        Bundle args = new Bundle();
        args.putStringArrayList("sfl", suggestedFriends);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            Bundle args = this.getArguments();
            suggestedFriendsArrayList = args.getStringArrayList("sfl");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggested_friends_list, container, false);
        ListView friendsList = ((ListView) view.findViewById(R.id.lv_suggested_friends));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, suggestedFriendsArrayList);
        friendsList.setAdapter(adapter);

        final Context context = getActivity();

        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String username_clicked = suggestedFriendsArrayList.get(i);
                Intent user_profile_intent = new Intent(context, UserProfileActivity.class);
                user_profile_intent.putExtra("username", username_clicked);
                startActivity(user_profile_intent);
            }
        });
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }
}
