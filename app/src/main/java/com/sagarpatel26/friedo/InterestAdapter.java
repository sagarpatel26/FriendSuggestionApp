package com.sagarpatel26.friedo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sagarpatel on 11/9/16.
 * As a part of the project SillyHelloWorld.
 */
public class InterestAdapter extends ArrayAdapter<Interest> {

    private LayoutInflater inflater;

    public InterestAdapter(Context context, List<Interest> objects) {
        super(context, R.layout.interest_row, objects);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Interest interest = (Interest) this.getItem(position);
        CheckBox checkBox;
        TextView textView;

        if (convertView == null) {

            Log.d("Adapter", interest.getInterest());

            convertView = inflater.inflate(R.layout.interest_row, null);

            textView = (TextView) convertView.findViewById(R.id.interesttxt);
            checkBox = (CheckBox) convertView.findViewById(R.id.interestckbx);

            convertView.setTag(new ViewHolder(checkBox, textView));

            // If CheckBox is toggled, update the Interest it is tagged with.
            checkBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    CheckBox cb = (CheckBox) v;
                    Interest interest = (Interest) cb.getTag();
                    interest.setChecked(cb.isChecked());
                }
            });
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TextView tv = (TextView) view;
                    CheckBox cb = (CheckBox) tv.getTag();
                    cb.setChecked(!cb.isChecked());
                    cb.callOnClick();
                }
            });
        }
        // Reuse existing row view
        else {
            // Because we use a ViewHolder, we avoid having to call findViewById().
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            checkBox = viewHolder.getCheckBox();
            textView = viewHolder.getTextView();
        }

        // Tag the CheckBox with the Planet it is displaying, so that we can
        // access the planet in onClick() when the CheckBox is toggled.
        checkBox.setTag(interest);
        textView.setTag(checkBox);

        // Display planet data
        checkBox.setChecked(interest.isChecked());
        textView.setText(interest.getInterest());
        //checkBox.setText( interest.getInterest() );

        return convertView;
    }

    private class ViewHolder {

        CheckBox checkBox;
        TextView textView;

        public ViewHolder(CheckBox checkBox, TextView textView) {
            this.checkBox = checkBox;
            this.textView = textView;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }

        public TextView getTextView() {
            return textView;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }
    }
}
