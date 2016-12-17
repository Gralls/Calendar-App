package com.springer.patryk.tas_android.utils;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.springer.patryk.tas_android.adapters.TaskListAdapter;

/**
 * Created by Patryk on 2016-12-17.
 */

public class SwipeHelper extends ItemTouchHelper.SimpleCallback {

    private TaskListAdapter taskListAdapter;

    public SwipeHelper(TaskListAdapter taskListAdapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.taskListAdapter=taskListAdapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        taskListAdapter.remove(viewHolder.getAdapterPosition());
    }
}
