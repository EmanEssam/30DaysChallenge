package com.peacefullwarrior.eman.a30dayschallenge.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Habit {

    int month;
    int day;
    Boolean selected;

    public Habit() {
    }

    public Habit(int month, int day, Boolean selected) {
        this.month = month;
        this.day = day;
        this.selected = selected;

    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
