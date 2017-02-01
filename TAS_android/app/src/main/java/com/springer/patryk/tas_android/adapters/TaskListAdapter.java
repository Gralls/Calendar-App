package com.springer.patryk.tas_android.adapters;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.springer.patryk.tas_android.MyApp;
import com.springer.patryk.tas_android.R;
import com.springer.patryk.tas_android.fragments.CreateTaskFragment;
import com.springer.patryk.tas_android.models.Guest;
import com.springer.patryk.tas_android.models.Task;
import com.springer.patryk.tas_android.models.User;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Patryk on 2016-12-16.
 */

public class TaskListAdapter extends RealmBasedRecyclerViewAdapter<Task, TaskListAdapter.ViewHolder> {

    private final DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");
    private android.support.v4.app.FragmentManager manager;
    private String userID;
    String token;

    public TaskListAdapter(Context context, RealmResults<Task> realmResults, String userID, String token, boolean automaticUpdate, boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
        manager = ((AppCompatActivity) context).getSupportFragmentManager();
        this.userID = userID;
        this.token = token;
    }


    public class ViewHolder extends RealmViewHolder {
        TextView title;
        TextView description;
        TextView startDate;
        TextView creator;
        TextView guests;
        TextView isPublic;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.taskTitle);
            description = (TextView) view.findViewById(R.id.taskDescription);
            startDate = (TextView) view.findViewById(R.id.taskStartDate);
            creator = (TextView) view.findViewById(R.id.taskCreator);
            guests = (TextView) view.findViewById(R.id.taskGuests);
            isPublic = (TextView) view.findViewById(R.id.taskStatus);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (userID.equals(realmResults.get(getAdapterPosition()).getUser())) {
                        Bundle args = new Bundle();
                        args.putSerializable("Task", realmResults.get(getAdapterPosition()).getId());
                        CreateTaskFragment createTaskFragment = new CreateTaskFragment();
                        createTaskFragment.setArguments(args);
                        manager.beginTransaction().replace(R.id.mainContent, createTaskFragment, null).addToBackStack(null).commit();
                    } else if(!realmResults.get(getAdapterPosition()).getStatus().equals("public")){
                        final Realm realm= Realm.getDefaultInstance();
                        final Task task = realm.copyFromRealm(realmResults.get(getAdapterPosition()));
                        final RealmList<Guest> tasksGuest = task.getGuests();

                        for (final Guest guest : tasksGuest) {
                            if (guest.getId().equals(userID)) {
                                guest.setFlag("accepted");

                            }
                        }

                        Call<Void> call = MyApp.getApiService().editTask(token, task.getId(), task);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Toast.makeText(getContext(), "Invite accepted", Toast.LENGTH_SHORT).show();
                              realm.executeTransaction(new Realm.Transaction() {
                                  @Override
                                  public void execute(Realm realm) {
                                      realm.insertOrUpdate(task);
                                  }
                              });
                                realm.close();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(getContext(), "Check internet connection", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            });
        }
    }


    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.task_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindRealmViewHolder(final ViewHolder holder, int position) {
       final Task task = realmResults.get(position);
        holder.title.setText(task.getTitle());
        holder.description.setText(task.getDescription());
        LocalDate localDate = LocalDate.parse(task.getStartDate());
        DateTime startDate = localDate.toDateTime(LocalTime.parse(task.getStartTime()));
        holder.startDate.setText(fmt.print(startDate));
        holder.isPublic.setText(task.getStatus());
        String guests = "";
        for (Guest guest :
                task.getGuests()) {
            guests = guests + guest.getLogin() + " - " + guest.getFlag() + '\n';
        }
        if (guests.length() > 0) {
            guests = guests.substring(0, guests.length() - 1);
        }

        holder.guests.setText(guests);
        Call<User> call = MyApp.getApiService().getUser(token,task.getUser());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                holder.creator.setText(response.body().getLogin());

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public int getItemCount() {
        return realmResults.size();
    }


    @Override
    public void onItemSwipedDismiss(int position) {
        if (realmResults.get(position).getUser().equals(userID)) {
            deleteTaskFromDB(position);
        } else if(!realmResults.get(position).getStatus().equals("public")) {
            removeFromGuests(position);
        }
        super.onItemSwipedDismiss(position);
    }

    public void deleteTaskFromDB(int position) {
        Call<Void> call = MyApp.getApiService().deleteTask(token, realmResults.get(position).getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getContext(), "Task deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void removeFromGuests(int position) {
        final Realm realm= Realm.getDefaultInstance();
        final Task task = realm.copyFromRealm(realmResults.get(position));
        final RealmList<Guest> tasksGuest = task.getGuests();

        for (final Guest guest : tasksGuest) {
            if (guest.getId().equals(userID)) {
                        guest.setFlag("rejected");

            }
        }

        Call<Void> call = MyApp.getApiService().editTask(token, task.getId(), task);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getContext(), "Invite rejected", Toast.LENGTH_SHORT).show();
                realm.close();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Check internet connection", Toast.LENGTH_SHORT).show();
            }
        });

    }
}