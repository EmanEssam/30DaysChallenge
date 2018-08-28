package com.peacefullwarrior.eman.a30dayschallenge.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emredavarci.circleprogressbar.CircleProgressBar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peacefullwarrior.eman.a30dayschallenge.R;
import com.peacefullwarrior.eman.a30dayschallenge.model.Day;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressFragment extends Fragment {
    DatabaseReference habitList;
    private long habitSize;
    private List<Day> habits;

    private CircleProgressBar progressBar;

    public ProgressFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        habits = new ArrayList<>();
        habitList = FirebaseDatabase.getInstance().getReference("habits");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(getString(R.string.my_progress));
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        progressBar = view.findViewById(R.id.progressBar);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        habitList.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                habits.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Day task = dataSnapshot1.getValue(Day.class);
                    habits.add(task);
                }

                float percentage = (float) habits.size() / (float) getCurrentMonthDays();
                int result = (int) (percentage * 100);
                progressBar.setProgress(result);
                progressBar.setText(String.valueOf(result));    // set progress text

                // set progress value


                progressBar.setMaxValue(100);            // set progress max value
                progressBar.setStrokeWidth(10);        // set stroke width
                progressBar.setBackgroundWidth(10);        // set progress background width
                progressBar.setProgressColor("#FF6FD99D");    // set progress color
                progressBar.setBackgroundColor("#FFF9916B");    // set progress backgorund color
                progressBar.setTextColor("#FF6FD99D");        // set text color
                progressBar.setSuffix("%");            // set suffix
                progressBar.setPrefix("");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private int getCurrentMonthDays() {

        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return maxDay;
    }

}
