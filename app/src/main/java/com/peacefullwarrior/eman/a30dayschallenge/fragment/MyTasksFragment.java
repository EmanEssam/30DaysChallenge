package com.peacefullwarrior.eman.a30dayschallenge.fragment;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peacefullwarrior.eman.a30dayschallenge.Constants;
import com.peacefullwarrior.eman.a30dayschallenge.R;
import com.peacefullwarrior.eman.a30dayschallenge.adapter.MyTasksAdapter;
import com.peacefullwarrior.eman.a30dayschallenge.model.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTasksFragment extends Fragment {
    MyTasksAdapter myTasksAdapter;
    DatabaseReference taskList;
    List<Task> tasks = new ArrayList<>();
    List<String> keys = new ArrayList<>();
    private RecyclerView mTasksList;
    private LinearLayoutManager layoutManager;


    public MyTasksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        taskList = FirebaseDatabase.getInstance().getReference("tasks");

        layoutManager = new LinearLayoutManager(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(getString(R.string.my_tasks));
        View view = inflater.inflate(R.layout.fragment_my_tasks, container, false);
        mTasksList = view.findViewById(R.id.my_tasks_rv);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Constants.KEY_TASKS, (ArrayList<? extends Parcelable>) tasks);
        outState.putSerializable(Constants.KEY_KEYS, (Serializable) keys);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {

            tasks = savedInstanceState.getParcelableArrayList(Constants.KEY_TASKS);

            keys = (List<String>) savedInstanceState.getSerializable(Constants.KEY_KEYS);

            onDataReady();

        } else {

            taskList.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    tasks.clear();
                    keys.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Task task = dataSnapshot1.getValue(Task.class);
                        tasks.add(task);
                        keys.add(dataSnapshot1.getKey());

                    }

                    onDataReady();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

    private void onDataReady() {
        myTasksAdapter = new MyTasksAdapter(tasks, keys, getContext());
        mTasksList.setLayoutManager(layoutManager);
        mTasksList.setAdapter(myTasksAdapter);
        mTasksList.setHasFixedSize(true);
    }
}
