package com.springer.patryk.tas_android.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.springer.patryk.tas_android.MyApp;
import com.springer.patryk.tas_android.R;
import com.springer.patryk.tas_android.SessionManager;
import com.springer.patryk.tas_android.activities.MainActivity;
import com.springer.patryk.tas_android.models.Task;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Patryk on 10.12.2016.
 */

public class CreateTaskFragment extends Fragment {

    @BindView(R.id.newTaskTitle)
    EditText taskTitle;
    @BindView(R.id.newTaskDescription)
    EditText taskDescription;
    @BindView(R.id.newTaskStartDate)
    DatePicker taskStartDate;
    @BindView(R.id.newTaskStartTime)
    TimePicker taskStartTime;

    @BindView(R.id.createNewTask)
    Button createTask;
    SessionManager sessionManager;
    private SharedPreferences sharedPreferences;
    private boolean isNewTask;
    private Task task;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_task_dialog, null);
        ButterKnife.bind(this, rootView);
        sessionManager = new SessionManager(getContext());
        sharedPreferences = getContext()
                .getSharedPreferences("DayDetails", Context.MODE_PRIVATE);
        ((MainActivity) getActivity()).hideFabs();
        if (getArguments() != null) {
            isNewTask = false;
            task = (Task) getArguments().getSerializable("Task");
            setTaskDetails(task);
        } else {
            isNewTask = true;
            DateTime currentDate;
            if (!sharedPreferences.getString("CurrentDate", "").equals("")) {
                currentDate = ISODateTimeFormat.dateTime().parseDateTime(sharedPreferences.getString("CurrentDate", ""));
            } else {
                currentDate = DateTime.now();
            }
            taskStartDate.updateDate(currentDate.getYear(), currentDate.getMonthOfYear() - 1, currentDate.getDayOfMonth());
            taskStartTime.setCurrentHour(currentDate.getHourOfDay());
            taskStartTime.setCurrentMinute(currentDate.getMinuteOfHour());
        }

        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNewTask)
                    createTask();
                else
                    editTask();
            }
        });


        return rootView;
    }


    public void createTask() {
        task = new Task();
        task.setTitle(taskTitle.getText().toString());
        task.setUser(sessionManager.getUserDetails().get("id"));
        task.setDescription(taskDescription.getText().toString());

        DateTime startDateTime = new DateTime(taskStartDate.getYear()
                , (taskStartDate.getMonth() + 1)
                , taskStartDate.getDayOfMonth()
                , taskStartTime.getCurrentHour()
                , taskStartTime.getCurrentMinute()
                , 0
                , 0);
        task.setStartDate(startDateTime.toString());


        Call<Task> call = MyApp.getApiService().createTask(task);
        call.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                task.setId(response.body().getId());
                Realm realm=Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(task);
                realm.commitTransaction();
                Toast.makeText(getContext(), "Task created", Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {

            }
        });
    }

    public void setTaskDetails(Task task) {
        taskTitle.setText(task.getTitle());
        taskDescription.setText(task.getDescription());
        DateTime dateTime = ISODateTimeFormat.dateTime().parseDateTime(task.getStartDate());
        dateTime.getYear();
        taskStartDate.updateDate(dateTime.getYear(), dateTime.getMonthOfYear() - 1, dateTime.getDayOfMonth());
        taskStartTime.setCurrentHour(dateTime.getHourOfDay());
        taskStartTime.setCurrentMinute(dateTime.getMinuteOfHour());
    }

    public void editTask() {
        task.setTitle(taskTitle.getText().toString());
        task.setUser(sessionManager.getUserDetails().get("id"));
        task.setDescription(taskDescription.getText().toString());

        DateTime startDateTime = new DateTime(taskStartDate.getYear()
                , (taskStartDate.getMonth() + 1)
                , taskStartDate.getDayOfMonth()
                , taskStartTime.getCurrentHour()
                , taskStartTime.getCurrentMinute()
                , 0
                , 0);
        task.setStartDate(startDateTime.toString());

        Call<Void> call = MyApp.getApiService().editTask(task.getId(), task);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getContext(), "Task updated", Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        ((MainActivity) getActivity()).showMainFab();
        super.onDestroyView();
    }
}
