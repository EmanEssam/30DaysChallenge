package com.peacefullwarrior.eman.a30dayschallenge.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.peacefullwarrior.eman.a30dayschallenge.R;
import com.peacefullwarrior.eman.a30dayschallenge.model.Day;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {

    private Context context;
    private List<String> keys = new ArrayList<>();
    private List<Day> days;


    public HabitAdapter() {
    }

    public HabitAdapter(List<Day> days, Context context, List<String> keys) {
        this.days = days;
        this.context = context;
        this.keys = keys;
    }

    @NonNull
    @Override
    public HabitAdapter.HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.habit_row, parent, false);
        return new HabitViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final HabitAdapter.HabitViewHolder holder, final int position) {
        final Day day = days.get(holder.getAdapterPosition());
        holder.currentDay.setText(String.valueOf(day.getDay()));
//        for (int i = 0; i < habits.size(); i++) {
//            if (habits.get(i).getDay() == position) {
//                selectedPosition = i;
//                holder.currentDay.setBackground(context.getDrawable(R.drawable.ic_done));
//            }
//
//
//        }
        holder.currentDay.setSelected(day.isSelected());

        holder.currentDay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (day.isSelected()) {
                    // TODO: 28/08/18 remove item from firebase INS
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    Query applesQuery = ref.child("habits").orderByChild("day").equalTo(day.getDay());
                    applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                appleSnapshot.getRef().removeValue();
                                day.setSelected(false);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(TAG, "onCancelled", databaseError.toException());
                        }
                    });

                } else {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("habits");
                    day.setSelected(true);
                    Day habit = new Day(day.getMonth(), day.getDay(), day.isSelected());
//                myRef.child("task").setValue(task);
                    myRef.push().setValue(habit);

                }
                notifyItemChanged(holder.getAdapterPosition());

            }
        });
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public class HabitViewHolder extends RecyclerView.ViewHolder {
        TextView currentDay;

        public HabitViewHolder(View itemView) {
            super(itemView);
            currentDay = itemView.findViewById(R.id.current_day_tv);
        }
    }
}
