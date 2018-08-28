package com.peacefullwarrior.eman.a30dayschallenge.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.peacefullwarrior.eman.a30dayschallenge.R;
import com.peacefullwarrior.eman.a30dayschallenge.model.Task;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditToBuyListActivity extends AppCompatActivity {
    private static TextView mDateTv;
    private static String taskDate;
    private Button mUpdatTaskBtn;
    private TextView mTitleTv;
    private TextView mDescriptionTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_to_buy_list);
        initViews();
        mUpdatTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Write a message to the database
                if (TextUtils.isEmpty(mTitleTv.getText())) {
                    mTitleTv.setError(getString(R.string.task_title_is_empty));
                } else {
                    final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                    Query query = dbRef
                            .child("tasks").orderByKey().equalTo(getIntent().getStringExtra("key"));
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Log.d("test", dataSnapshot1.getKey() + "  " + dataSnapshot1.getValue(Task.class)
                                        .toString());
                                updateTask(dbRef, dataSnapshot1.getKey(), mTitleTv.getText().toString(), mDescriptionTv.getText().toString(), taskDate, 3);
                            }
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                            Log.e("update", databaseError.getMessage());
                        }
                    });
                    finish();
                }
            }
        });
        mDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });


    }

    private void updateTask(DatabaseReference db, String key, String title, String decription, String date, int type) {
        Task task = new Task(title, decription, date, type);
        Map<String, Object> postValues = task.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, postValues);
        childUpdates.put("/tasks/" + key, postValues);


        db.updateChildren(childUpdates);
    }

    private void showTimePickerDialog() {
        DialogFragment newFragment = new EditToBuyListActivity.DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void initViews() {
        mUpdatTaskBtn = findViewById(R.id.edit_task_btn);
        mTitleTv = findViewById(R.id.task_title);
        mDescriptionTv = findViewById(R.id.task_desc);
        mDateTv = findViewById(R.id.task_date);


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
            mDateTv.setText(taskDate);

        }
    }
}

