package com.sagarpatel26.friedo;

/**
 * Created by sagarpatel26 on 11/9/16.
 * As a part of the project SillyHelloWorld.
 */
public class Interest {

    private String interest;
    private boolean isChecked;

    public Interest(String interest, boolean isChecked) {
        this.interest = interest;
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }
}
