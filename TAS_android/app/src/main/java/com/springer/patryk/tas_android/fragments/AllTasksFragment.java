package com.springer.patryk.tas_android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.springer.patryk.tas_android.R;
import com.springer.patryk.tas_android.adapters.TaskListAdapter;
import com.springer.patryk.tas_android.models.Task;

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
    @BindView(R.id.allTaskLabel)
    TextView currentDay;


    private TaskListAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] sortFieldNames = {"startDate", "startTime"};
        Sort[] sorts = {Sort.ASCENDING, Sort.ASCENDING};
        RealmResults<Task> realmResults;
        if (getArguments() == null) {
            realmResults = realm
                    .where(Task.class).findAllSorted(sortFieldNames,sorts);
        }else{
            String date = (String) getArguments().getSerializable("TaskDate");
            realmResults = realm
                    .where(Task.class)
                    .equalTo("startDate", date)
                    .findAllSorted(sortFieldNames, sorts);
        }
        adapter = new TaskListAdapter(getContext(), realmResults, userDetails.get("id"), true, true);
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
        return inflater.inflate(R.layout.tasks_list, null);
    }
}
