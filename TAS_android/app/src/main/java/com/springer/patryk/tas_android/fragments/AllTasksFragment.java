package com.springer.patryk.tas_android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.springer.patryk.tas_android.R;
import com.springer.patryk.tas_android.adapters.TaskListAdapter;
import com.springer.patryk.tas_android.models.Task;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import butterknife.BindView;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Patryk on 2017-01-02.
 */

public class AllTasksFragment extends BaseFragment {
    @BindView(R.id.listOfTodaysTasks)
    RealmRecyclerView taskListView;
    @BindView(R.id.currentDay)
    TextView currentDay;


    private Context mContext;
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
        currentDay.setText(R.string.task_list_label);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tasks_list, null);


        String[] sortFieldNames = {"startDate", "startTime"};
        Sort[] sorts = {Sort.ASCENDING, Sort.ASCENDING};
        RealmResults<Task> realmResults = realm
                .where(Task.class)
                .findAllSorted(sortFieldNames,sorts);

        adapter = new TaskListAdapter(mContext, realmResults,userDetails.get("id"), true, true);



        return rootView;
    }
}
