package com.peacefullwarrior.eman.a30dayschallenge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Task implements Parcelable {

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
    String task_title;
    String task_desc;
    String date;
    int type; // 1 for task , 2 for event and 3 for To Buy List


    public Task() {
    }

    public Task(String task_title, String task_desc, String date, int type) {


        this.task_title = task_title;
        this.task_desc = task_desc;
        this.date = date;
        this.type = type;

    }


    protected Task(Parcel in) {
        this.task_title = in.readString();
        this.task_desc = in.readString();
        this.date = in.readString();
        this.type = in.readInt();
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("task_title", task_title);
        result.put("task_desc", task_desc);
        result.put("date", date);
        result.put("type", type);


        return result;
    }

    public String getTask_title() {
        return task_title;
    }

    public void setTask_title(String task_title) {
        this.task_title = task_title;
    }

    public String getTask_desc() {
        return task_desc;
    }

    public void setTask_desc(String task_desc) {
        this.task_desc = task_desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.task_title);
        dest.writeString(this.task_desc);
        dest.writeString(this.date);
        dest.writeInt(this.type);
    }
}
