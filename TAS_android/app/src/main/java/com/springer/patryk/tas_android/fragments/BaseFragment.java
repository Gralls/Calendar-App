package com.springer.patryk.tas_android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.springer.patryk.tas_android.MyApp;
import com.springer.patryk.tas_android.SessionManager;
import com.springer.patryk.tas_android.models.Guest;
import com.springer.patryk.tas_android.models.Meeting;
import com.springer.patryk.tas_android.models.Task;
import com.springer.patryk.tas_android.models.UserState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Patryk on 27.12.2016.
 */

public class BaseFragment extends Fragment {
    protected Realm realm;
    protected HashMap<String, String> userDetails;
    protected SessionManager sessionManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        sessionManager = new SessionManager(getContext());
        userDetails = sessionManager.getUserDetails();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    protected void syncWithServer(String userID) {
        updateTasks(userID);
    }

    public void updateTasks(final String userID) {
        final Call<List<Task>> tasks = MyApp.getApiService().getTasks(sessionManager.getToken(),userID);

        tasks.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, final Response<List<Task>> response) {
                final List<Task> tasksToInsert = new ArrayList<Task>();
                for(Task task:response.body()){
                    if(task.getUser().equals(userID)||task.getStatus().equals("public"))
                        tasksToInsert.add(task);
                    else{
                        for(Guest guest:task.getGuests()){
                            if(guest.getId().equals(userID)&&(guest.getFlag().equals("pending")||guest.getFlag().equals("accepted"))){
                                tasksToInsert.add(task);
                            }
                        }
                    }
                }
                final String[] ids = new String[tasksToInsert.size() > 0 ? tasksToInsert.size() : 1];
                if (tasksToInsert.size() > 0) {
                    Log.d("BaseFragment", tasksToInsert.toString());
                    for (Task task : tasksToInsert) {
                        ids[tasksToInsert.indexOf(task)] = task.getId();
                    }
                } else
                    ids[0] = "";
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<Task> realmResults = realm.where(Task.class).not().in("id", ids).findAll();
                        realmResults.deleteAllFromRealm();
                        realm.insertOrUpdate(tasksToInsert);
                    }
                });
                updateMeetings(userID);

            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Log.d("Task", t.getMessage());
                showToast("Check internet connection");
            }
        });

    }


    public void updateMeetings(final String userID) {
        Call<List<Meeting>> meetings = MyApp.getApiService().getMeeting(sessionManager.getToken(),userID);

        meetings.enqueue(new Callback<List<Meeting>>() {
            @Override
            public void onResponse(Call<List<Meeting>> call, final Response<List<Meeting>> response) {
                Log.d("Meetings", response.code() + response.message());
                final List<Meeting> meetingToInsert = new ArrayList<Meeting>();
                for (Meeting meeting : response.body()) {
                    if (meeting.getUser().equals(userID) || meeting.getStatus().equals("public")) {
                        meetingToInsert.add(meeting);
                    }else{
                        for (Guest guest : meeting.getGuests()) {
                            if(guest.getId().equals(userID)&&(guest.getFlag().equals("pending")||guest.getFlag().equals("accepted"))){
                                meetingToInsert.add(meeting);
                            }
                        }
                    }
                }
                final String[] ids = new String[meetingToInsert.size() > 0 ? meetingToInsert.size() : 1];
                if (meetingToInsert.size() > 0) {
                    for (Meeting meeting :
                            meetingToInsert) {
                        ids[meetingToInsert.indexOf(meeting)] = meeting.getId();
                    }
                } else
                    ids[0] = "";
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<Meeting> realmResults = realm.where(Meeting.class).not().in("id", ids).findAll();
                        realmResults.deleteAllFromRealm();
                        realm.insertOrUpdate(meetingToInsert);
                        RealmList<Task> tasks = new RealmList<Task>();
                        tasks.addAll(realm.where(Task.class).findAll());
                        RealmList<Meeting> meetings = new RealmList<Meeting>();
                        meetings.addAll(realm.where(Meeting.class).findAll());
                        UserState userState = realm.where(UserState.class).findFirst();

                        if (userState == null) {
                            UserState newUserState = realm.createObject(UserState.class, "0");
                            newUserState.setTask(tasks);
                            newUserState.setMeeting(meetings);
                        } else {
                            userState.setTask(tasks);
                            userState.setMeeting(meetings);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Meeting>> call, Throwable t) {
                showToast("Check internet connection");
            }
        });

    }

    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}
