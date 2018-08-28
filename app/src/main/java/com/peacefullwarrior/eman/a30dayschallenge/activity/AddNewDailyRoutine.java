package com.peacefullwarrior.eman.a30dayschallenge.activity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.peacefullwarrior.eman.a30dayschallenge.R;
import com.peacefullwarrior.eman.a30dayschallenge.model.Task;
import com.peacefullwarrior.eman.a30dayschallenge.utils.AnalyticsApplication;

import java.util.Calendar;

public class AddNewDailyRoutine extends AppCompatActivity {
    private static TextView mDateTv;
    private static String taskDate;
    private Button mAddTaskBtn;
    private EditText mTitleTv;
    private EditText mDescriptionTv;
    private int taskType = 1;
    private boolean buy;
    private Tracker mTracker;


    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("name" + AddNewDailyRoutine.class.getSimpleName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_daily_routine);
        // Obtain the shared Tracker instance.
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
                    FirebaseApp.initializeApp(AddNewDailyRoutine.this);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    database.setPersistenceEnabled(true);
                    Task task;
                    task = new Task(mTitleTv.getText().toString(), mDescriptionTv.getText().toString(),
                            taskDate, 3);

                    DatabaseReference myRef = database.getReference("daily_routine");
                    myRef.push().setValue(task);


                    finish();
                }
            }
        });

        mDateTv.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

    }

    private void showTimePicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(AddNewDailyRoutine.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                String status;
                if (selectedHour > 11) {
                    // If the hour is greater than or equal to 12
                    // Then the current AM PM status is PM
                    status = "PM";
                } else {
                    status = "AM";
                }
                taskDate = selectedHour + ":" + selectedMinute + " " + status;
                mDateTv.setText(taskDate);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void initViews() {
        mAddTaskBtn = findViewById(R.id.add_task_btn);
        mTitleTv = findViewById(R.id.task_title);
        mDescriptionTv = findViewById(R.id.task_desc);
        mDateTv = findViewById(R.id.task_date);


    }


}
