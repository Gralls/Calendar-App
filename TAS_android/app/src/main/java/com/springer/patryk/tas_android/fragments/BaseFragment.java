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
import com.springer.patryk.tas_android.models.Meeting;
import com.springer.patryk.tas_android.models.Task;
import com.springer.patryk.tas_android.models.User;
import com.springer.patryk.tas_android.models.UserState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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

    protected void updateTasks(final String userID) {
        Call<List<Task>> tasks = MyApp.getApiService().getTasks(userID);

        tasks.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, final Response<List<Task>> response) {
                final String[] ids = new String[response.body().size() > 0 ? response.body().size() : 1];
                if (response.body().size() > 0) {
                    for (Task task :
                            response.body()) {
                        ids[response.body().indexOf(task)] = task.getId();
                    }
                } else
                    ids[0] = "";
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<Task> realmResults = realm.where(Task.class).not().in("id", ids).findAll();
                        realmResults.deleteAllFromRealm();
                        realm.insertOrUpdate(response.body());
                    }
                });
                Call<List<Meeting>> meetings = MyApp.getApiService().getMeeting(userID);

                meetings.enqueue(new Callback<List<Meeting>>() {
                    @Override
                    public void onResponse(Call<List<Meeting>> call, final Response<List<Meeting>> response) {
                        final String[] ids = new String[response.body().size() > 0 ? response.body().size() : 1];
                        if (response.body().size() > 0) {
                            for (Meeting meeting :
                                    response.body()) {
                                ids[response.body().indexOf(meeting)] = meeting.getId();
                            }
                        } else
                            ids[0] = "";
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                RealmResults<Meeting> realmResults = realm.where(Meeting.class).not().in("id", ids).findAll();
                                realmResults.deleteAllFromRealm();
                                realm.insertOrUpdate(response.body());
                                RealmList<Task> tasks = new RealmList<Task>();
                                tasks.addAll(realm.where(Task.class).findAll());
                                RealmList<Meeting> meetings = new RealmList<Meeting>();
                                meetings.addAll(realm.where(Meeting.class).findAll());
                                UserState userState = realm.where(UserState.class).findFirst();
                                int x=1;
                                if (userState == null) {
                                    UserState newUserState = realm.createObject(UserState.class,"0");
                                    newUserState.setTask(tasks);
                                    newUserState.setMeeting(meetings);
                                }
                                else{
                                    userState.setTask(tasks);
                                    userState.setMeeting(meetings);
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<List<Meeting>> call, Throwable t) {
                        Toast.makeText(getContext(), "Check internet connection", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(getContext(), "Check internet connection", Toast.LENGTH_SHORT).show();
            }
        });


    }

}
