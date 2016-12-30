package com.springer.patryk.tas_android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.springer.patryk.tas_android.R;
import com.springer.patryk.tas_android.adapters.TaskListAdapter;
import com.springer.patryk.tas_android.models.Task;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import butterknife.BindView;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.RealmResults;

/**
 * Created by Patryk on 2016-12-16.
 */

public class DayDetailsFragment extends BaseFragment {

    @BindView(R.id.listOfTodaysTasks)
    RealmRecyclerView taskListView;
    @BindView(R.id.currentDay)
    TextView currentDay;

    private List<Task> taskList;
    private Context mContext;
    private DateTime currentDate;
    private TaskListAdapter adapter;

    private DateTimeFormatter dateTimeFormatter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        dateTimeFormatter= DateTimeFormat.forPattern("dd MMMM yyyy");

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        taskListView.setAdapter(adapter);
        currentDay.setText(dateTimeFormatter.print(currentDate));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tasks_list_of_day, null);
        currentDate = (DateTime) getArguments().getSerializable("tasks");

        mContext.getSharedPreferences("DayDetails",Context.MODE_PRIVATE)
                .edit()
                .putString("CurrentDate",currentDate.toString())
                .apply();


        RealmResults<Task> realmResults = realm
                .where(Task.class)
                .equalTo("startDate", currentDate.toLocalDate().toString())
                .findAllSorted("startTime");

        adapter = new TaskListAdapter(mContext, realmResults, true, true);



        return rootView;
    }


}
