package com.springer.patryk.tas_android.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.springer.patryk.tas_android.MyApp;
import com.springer.patryk.tas_android.R;
import com.springer.patryk.tas_android.activities.MainActivity;
import com.springer.patryk.tas_android.models.Guest;
import com.springer.patryk.tas_android.models.Task;
import com.springer.patryk.tas_android.models.User;
import com.springer.patryk.tas_android.utils.InputUtils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.ISODateTimeFormat;

import java.util.List;

import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Patryk on 10.12.2016.
 */

public class CreateTaskFragment extends BaseFragment {

    @BindView(R.id.newTaskTitle)
    EditText taskTitle;
    @BindView(R.id.newTaskDescription)
    EditText taskDescription;
    @BindView(R.id.newTaskStartDate)
    DatePicker taskStartDate;
    @BindView(R.id.newTaskStartTime)
    TimePicker taskStartTime;
    @BindView(R.id.newTaskGuests)
    EditText taskGuests;
    @BindView(R.id.createNewTask)
    Button createTask;
    @BindView(R.id.publicCheckbox)
    CheckBox isPublic;
    private SharedPreferences sharedPreferences;
    private boolean isNewTask;
    private Task task;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_task_fragment, null);

        sharedPreferences = getContext()
                .getSharedPreferences("DayDetails", Context.MODE_PRIVATE);
        ((MainActivity) getActivity()).hideFabs();


        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            isNewTask = false;
            String id = (String) getArguments().getSerializable("Task");
            task = realm.where(Task.class).equalTo("id", id).findFirst();
            task = realm.copyFromRealm(task);
            setTaskDetails();
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
                if (checkInput()) {
                    convertInputGuests(taskGuests.getText().toString());
                }
            }
        });

    }

    public boolean checkInput() {
        boolean validateStatus = true;
        if (InputUtils.checkIsEmpty(taskTitle.getText().toString())) {
            validateStatus = false;
            taskTitle.setError("Title is required");
        }
        return validateStatus;
    }

    public void initTask(RealmList<Guest> guests) {
        task = new Task();
        task.setTitle(taskTitle.getText().toString());
        task.setUser(sessionManager.getUserDetails().get("id"));
        task.setDescription(taskDescription.getText().toString());
        if(isPublic.isChecked())
            task.setStatus("public");
        else
            task.setStatus("private");
        DateTime startDateTime = new DateTime(taskStartDate.getYear()
                , (taskStartDate.getMonth() + 1)
                , taskStartDate.getDayOfMonth()
                , taskStartTime.getCurrentHour()
                , taskStartTime.getCurrentMinute()
                , 0
                , 0);
        task.setStartDate(startDateTime.toLocalDate().toString());
        task.setStartTime(startDateTime.toLocalTime().toString());
        task.setGuests(guests);
    }

    public RealmList<Guest> convertInputGuests(String input) {
        final String[] splitedGuests = input.split(",");
        for (int i = 0; i < splitedGuests.length; i++) {
            splitedGuests[i] = splitedGuests[i].trim();
        }
        final RealmList<Guest> guests = new RealmList<>();
        Call<List<User>> call = MyApp.getApiService().getUsers(sessionManager.getToken(), splitedGuests);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!splitedGuests[0].equals("") && response.body()!=null) {
                    for (User user : response.body()) {
                        if(!user.getId().equals(sessionManager.getUserDetails().get("id"))) {
                            Guest guest = new Guest();
                            guest.setFlag("pending");
                            guest.setId(user.getId());
                            guest.setLogin(user.getLogin());
                            guests.add(guest);
                        }
                    }
                }

                if (isNewTask) {
                    initTask(guests);
                    createTask();
                } else {
                    editTask(guests);

                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("CreateTask", t.getMessage());
                showToast("Check internet connection");
            }
        });


        return guests;
    }

    public void createTask() {
        Call<Task> createTask = MyApp.getApiService().createTask(sessionManager.getToken(), task);
        Log.d("CreateTask", "Task: " + task.getGuests().size());
        createTask.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                showToast("Task created");
                syncWithServer(userDetails.get("id"));
                getFragmentManager().popBackStack();
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Log.d("CreateTask", t.getMessage());
                showToast("Check internet connection");
            }
        });
    }


    public void setTaskDetails() {
        taskTitle.setText(task.getTitle());
        taskDescription.setText(task.getDescription());
        LocalDate localDate = LocalDate.parse(task.getStartDate());
        LocalTime localTime = LocalTime.parse(task.getStartTime());
        taskStartDate.updateDate(localDate.getYear(), localDate.getMonthOfYear() - 1, localDate.getDayOfMonth());
        taskStartTime.setCurrentHour(localTime.getHourOfDay());
        taskStartTime.setCurrentMinute(localTime.getMinuteOfHour());
        taskGuests.setText(setGuestsInput());
        if (task.getStatus().equals("public")) {
            isPublic.setChecked(true);
        } else {
            isPublic.setChecked(false);
        }
    }

    private String setGuestsInput() {
        String guests = "";
        for (Guest guest : task.getGuests()) {
            guests = guests + guest.getLogin() + ",";
        }
        if (guests.length() > 0) {
            guests = guests.substring(0, guests.length() - 1);
        }
        return guests;
    }

    public void editTask(RealmList<Guest>guests) {

        task.setTitle(taskTitle.getText().toString());
        task.setDescription(taskDescription.getText().toString());

        DateTime startDateTime = new DateTime(taskStartDate.getYear()
                , (taskStartDate.getMonth() + 1)
                , taskStartDate.getDayOfMonth()
                , taskStartTime.getCurrentHour()
                , taskStartTime.getCurrentMinute()
                , 0
                , 0);

        task.setStartDate(startDateTime.toLocalDate().toString());
        task.setStartTime(startDateTime.toLocalTime().toString());
        task.setGuests(guests);
        if(isPublic.isChecked())
            task.setStatus("public");
        else
            task.setStatus("private");
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(task);
            }
        });

        Call<Void> call = MyApp.getApiService().editTask(sessionManager.getToken(), task.getId(), task);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                showToast("Task updated");
                getFragmentManager().popBackStack();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showToast("Check internet connection");
            }
        });
    }

    @Override
    public void onDestroyView() {
        ((MainActivity) getActivity()).showMainFab();
        super.onDestroyView();
    }
}
