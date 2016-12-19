package com.springer.patryk.tas_android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.springer.patryk.tas_android.R;
import com.springer.patryk.tas_android.adapters.TaskListAdapter;
import com.springer.patryk.tas_android.models.Task;
import com.springer.patryk.tas_android.utils.SwipeHelper;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Patryk on 2016-12-16.
 */

public class DayDetailsFragment extends Fragment {

    @BindView(R.id.listOfTodaysTasks)
    RecyclerView taskListView;
    @BindView(R.id.currentDay)
    TextView currentDay;

    private List<Task> taskList;
    private Context mContext;
    private DateTime currentDate;
    private TaskListAdapter adapter;
    private ItemTouchHelper swipeHelper;
    private DateTimeFormatter dateTimeFormatter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        dateTimeFormatter= DateTimeFormat.forPattern("dd MMMM yyyy");
        taskList = (List<Task>) getArguments().getSerializable("tasks");
        currentDate = ISODateTimeFormat.dateTime().parseDateTime(taskList.get(0).getStartDate());
        mContext.getSharedPreferences("DayDetails",Context.MODE_PRIVATE)
                .edit()
                .putString("CurrentDate",currentDate.toString())
                .apply();
        adapter = new TaskListAdapter(taskList, mContext);
        ItemTouchHelper.Callback swipeCallback = new SwipeHelper(adapter);
        swipeHelper = new ItemTouchHelper(swipeCallback);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tasks_list_of_day, null);
        ButterKnife.bind(this, rootView);

        taskListView.setAdapter(adapter);
        taskListView.setItemAnimator(new DefaultItemAnimator());
        taskListView.setLayoutManager(new LinearLayoutManager(mContext));
        swipeHelper.attachToRecyclerView(taskListView);
        currentDay.setText(dateTimeFormatter.print(currentDate));
        checkDates();

        return rootView;
    }

    public void checkDates() {
        List<Integer> indexToRemove = new ArrayList<>();
        for (Task task : taskList) {
            DateTime taskDate = ISODateTimeFormat.dateTime().parseDateTime(task.getStartDate());
            if (!currentDate.toLocalDate().equals(taskDate.toLocalDate())) {
                indexToRemove.add(taskList.indexOf(task));
            }
        }

        for (int i : indexToRemove) {
            adapter.removeTaskFromList(i);
        }

    }
}
