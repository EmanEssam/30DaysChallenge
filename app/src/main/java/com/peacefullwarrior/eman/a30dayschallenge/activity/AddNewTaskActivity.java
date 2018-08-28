package com.peacefullwarrior.eman.a30dayschallenge.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.peacefullwarrior.eman.a30dayschallenge.R;
import com.peacefullwarrior.eman.a30dayschallenge.model.Task;
import com.peacefullwarrior.eman.a30dayschallenge.utils.AnalyticsApplication;

import java.util.Calendar;

public class AddNewTaskActivity extends AppCompatActivity {
    private static TextView mDateTv;
    private static String taskDate;
    FirebaseDatabase database;
    private Button mAddTaskBtn;
    private TextView mTitleTv;
    private TextView mDescriptionTv;
    private RadioButton mTaskRB, mEventRB;
    private int taskType = 1;
    private boolean buy;
    private Tracker mTracker;


    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("name" + AddNewTaskActivity.class.getSimpleName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        initViews();
        mAddTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Write a message to the database
                if (TextUtils.isEmpty(mTitleTv.getText())) {
                    mTitleTv.setError(getString(R.string.task_title_is_empty));
                } else {
//                    database.setPersistenceEnabled(true);
                    FirebaseApp.initializeApp(AddNewTaskActivity.this);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();

                    Task task;
                    if (getIntent().getExtras().getBoolean("buy")) {
                        task = new Task(mTitleTv.getText().toString(), mDescriptionTv.getText().toString(),
                                taskDate, 3);
                    } else {
                        task = new Task(mTitleTv.getText().toString(), mDescriptionTv.getText().toString(),
                                taskDate, taskType);
                    }
                    DatabaseReference myRef = database.getReference("tasks");
//                myRef.child("task").setValue(task);
                    myRef.push().setValue(task);
                    finish();
                }

            }
        });
        mDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void initViews() {
        mAddTaskBtn = findViewById(R.id.add_task_btn);
        mTitleTv = findViewById(R.id.task_title);
        mDescriptionTv = findViewById(R.id.task_desc);
        mDateTv = findViewById(R.id.task_date);
        mTaskRB = findViewById(R.id.task_radio_btn);
        mEventRB = findViewById(R.id.event_radio_btn);
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getBoolean("buy")) {
                mTaskRB.setVisibility(View.GONE);
                mEventRB.setVisibility(View.GONE);
            } else {
                mTaskRB.setVisibility(View.VISIBLE);
                mEventRB.setVisibility(View.VISIBLE);
            }
        } else {

            mTaskRB.setVisibility(View.VISIBLE);
            mEventRB.setVisibility(View.VISIBLE);
        }


    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.task_radio_btn:
                if (checked)
                    taskType = 1;
                break;
            case R.id.event_radio_btn:
                if (checked)
                    taskType = 2;
                // Ninjas rule
                break;
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            taskDate = day + "/" + month + "/" + year;
            if (mDateTv != null) {
                mDateTv.setText(taskDate);
            }
        }
    }
}
