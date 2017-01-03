package com.springer.patryk.tas_android.models;

import android.widget.Toast;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Patryk on 2017-01-02.
 */

public class UserState extends RealmObject {

    @PrimaryKey
    private String id;

    private RealmList<Meeting> meeting;
    private RealmList<Task> task;

    public RealmList<Task> getTask() {
        return task;
    }

    public void setTask(RealmList<Task> task) {
        this.task = task;
    }

    public RealmList<Meeting> getMeeting() {
        return meeting;
    }

    public void setMeeting(RealmList<Meeting> meeting) {
        this.meeting = meeting;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
