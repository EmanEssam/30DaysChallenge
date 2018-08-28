package com.peacefullwarrior.eman.a30dayschallenge.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@IgnoreExtraProperties
public class Day {
    private int month;
    private int day;
    private boolean selected;

    public Day() {
    }

    public Day(int month, int day, boolean selected) {
        this.month = month;
        this.day = day;
        this.selected = selected;
    }

    public static int hash(Object... values) {
        return Arrays.hashCode(values);
    }

    public static List<Day> generateDays(int month, int numberOfDays, List<Day> selectedDays) {
        List<Day> days = new ArrayList<>();
        for (int i = 1; i <= numberOfDays; i++) {
            Day day = new Day(month, i, false);

            day.setSelected(selectedDays.contains(day));

            days.add(day);
        }
        return days;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Day day1 = (Day) o;
        return month == day1.month &&
                day == day1.day;
    }

    @Override
    public int hashCode() {

        return hash(month, day);
    }
}
