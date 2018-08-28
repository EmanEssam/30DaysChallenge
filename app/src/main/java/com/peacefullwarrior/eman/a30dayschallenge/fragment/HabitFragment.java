package com.peacefullwarrior.eman.a30dayschallenge.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peacefullwarrior.eman.a30dayschallenge.R;
import com.peacefullwarrior.eman.a30dayschallenge.adapter.HabitAdapter;
import com.peacefullwarrior.eman.a30dayschallenge.model.Day;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class HabitFragment extends Fragment {
    HabitAdapter myTasksAdapter;
    DatabaseReference habitList;
    private RecyclerView mTasksList;
    private List<Day> habits;


    public HabitFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        habits = new ArrayList<>();
        final List<String> keys = new ArrayList<>();
        habitList = FirebaseDatabase.getInstance().getReference("habits");
        habitList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                habits.clear();
                keys.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Day task = dataSnapshot1.getValue(Day.class);
                    habits.add(task);
                    keys.add(dataSnapshot1.getKey());

                }
                mTasksList.setLayoutManager(new GridLayoutManager(getContext(), 6));
                myTasksAdapter = new HabitAdapter(Day.generateDays(getCurrentMonthNumber(), getCurrentMonthDays(), habits), getContext(), keys);
                mTasksList.setAdapter(myTasksAdapter);
                mTasksList.setHasFixedSize(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(getString(R.string.habit));
        View view = inflater.inflate(R.layout.fragment_habit, container, false);

        mTasksList = view.findViewById(R.id.my_habits_rv);
        return view;

    }

    private int getCurrentMonthNumber() {
//        kindly implement this to get current month
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    private int getCurrentMonthDays() {

        final Calendar cal = Calendar.getInstance();
        String month_name = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
        int current_day = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        SimpleDateFormat df = new SimpleDateFormat("MMM d, yyyy");
        final List<String> monthCalendar = new ArrayList<>();
        return maxDay;
    }

}
