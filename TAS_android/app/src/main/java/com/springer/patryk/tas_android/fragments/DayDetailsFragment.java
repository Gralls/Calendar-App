package com.springer.patryk.tas_android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.springer.patryk.tas_android.MyApp;
import com.springer.patryk.tas_android.R;
import com.springer.patryk.tas_android.adapters.TaskListAdapter;
import com.springer.patryk.tas_android.models.Task;
import com.springer.patryk.tas_android.utils.SwipeHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Patryk on 2016-12-16.
 */

public class DayDetailsFragment extends Fragment {

    @BindView(R.id.listOfTodaysTasks)
    RecyclerView taskListView;

    private List<Task> taskList;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tasks_list_of_day, null);
        mContext=getContext();
        ButterKnife.bind(this, rootView);
        List<Task>tasks=(List<Task>) getArguments().getSerializable("tasks");
        final TaskListAdapter adapter= new TaskListAdapter(tasks, mContext);
        ItemTouchHelper.Callback swipeCallback = new SwipeHelper(adapter);
        ItemTouchHelper swipeHelper = new ItemTouchHelper(swipeCallback);
        taskListView.setAdapter(adapter);
        taskListView.setLayoutManager(new LinearLayoutManager(mContext));
        swipeHelper.attachToRecyclerView(taskListView);



        return rootView;
    }
}
