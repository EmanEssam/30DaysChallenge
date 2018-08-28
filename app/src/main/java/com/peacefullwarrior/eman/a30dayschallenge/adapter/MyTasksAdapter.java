package com.peacefullwarrior.eman.a30dayschallenge.adapter;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.peacefullwarrior.eman.a30dayschallenge.R;
import com.peacefullwarrior.eman.a30dayschallenge.activity.EditTaskActivity;
import com.peacefullwarrior.eman.a30dayschallenge.model.Task;
import com.peacefullwarrior.eman.a30dayschallenge.utils.TaskWidget;
import com.peacefullwarrior.eman.a30dayschallenge.utils.TinyDB;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MyTasksAdapter extends RecyclerView.Adapter<MyTasksAdapter.TaskViewHolder> {

    List<Task> taskList = new ArrayList<>();
    Context context;
    List<String> keys = new ArrayList<>();

    public MyTasksAdapter() {
    }

    public MyTasksAdapter(List<Task> taskList, List<String> keys, Context context) {
        this.taskList = taskList;
        this.context = context;
        this.keys = keys;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.task_row, parent, false);
        return new TaskViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, final int position) {

        holder.taskTitle.setText(taskList.get(position).getTask_title());
        holder.taskDate.setText(taskList.get(position).getDate());
        holder.deleteOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(keys.get(position));
            }
        });
        holder.editOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditActivity(keys.get(position));
            }
        });

        holder.edit_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TinyDB tinyDB = new TinyDB(context);
                tinyDB.putString("task", taskList.get(position).getTask_title());
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                        new ComponentName(context, TaskWidget.class));
                for (int appWidgetId : appWidgetIds) {
                    TaskWidget.updateAppWidget(context, appWidgetManager, appWidgetId);
                }
//                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_text);
                Toast.makeText(context, R.string.task_added, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteDialog(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.dialog_message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query applesQuery = ref.child("tasks").orderByKey().equalTo(key);

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    private void openEditActivity(String key) {
        Intent intent = new Intent(context, EditTaskActivity.class);
        intent.putExtra("key", key);
        context.startActivity(intent);

    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle, taskDate;
        ImageView deleteOption, editOption, edit_pin;

        public TaskViewHolder(View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.task_title);
            taskDate = itemView.findViewById(R.id.task_date);
            deleteOption = itemView.findViewById(R.id.delete_icon);
            editOption = itemView.findViewById(R.id.edit_icon);
            edit_pin = itemView.findViewById(R.id.edit_pin);
        }
    }
}
