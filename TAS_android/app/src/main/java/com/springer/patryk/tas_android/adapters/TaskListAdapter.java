package com.springer.patryk.tas_android.adapters;



import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.springer.patryk.tas_android.MyApp;
import com.springer.patryk.tas_android.R;
import com.springer.patryk.tas_android.fragments.CreateTaskFragment;
import com.springer.patryk.tas_android.models.Task;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Patryk on 2016-12-16.
 */

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private List<Task> tasks;
    private Context mContext;
    private static final DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");
    private android.support.v4.app.FragmentManager manager;

    public TaskListAdapter(List<Task> tasks, Context mContext) {
        this.tasks = tasks;
        this.mContext = mContext;

        manager = ((AppCompatActivity) mContext).getSupportFragmentManager();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.title.setText(task.getTitle());
        holder.description.setText(task.getDescription());
        DateTime startDate = new DateTime(task.getStartDate());
        holder.startDate.setText(fmt.print(startDate));
        holder.creator.setText(task.getUser());
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        TextView startDate;
        TextView creator;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.taskTitle);
            description = (TextView) view.findViewById(R.id.taskDescription);
            startDate = (TextView) view.findViewById(R.id.taskStartDate);
            creator = (TextView) view.findViewById(R.id.taskCreator);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
               //     args.putSerializable("Task",tasks.get(getAdapterPosition()));
                    CreateTaskFragment createTaskFragment=new CreateTaskFragment();
                    createTaskFragment.setArguments(args);
                    manager.beginTransaction().replace(R.id.mainContent,createTaskFragment,null).addToBackStack(null).commit();
                }
            });
        }
    }

    public void deleteTaskFromDB(int position) {
        Call<Void> call = MyApp.getApiService().deleteTask(tasks.get(position).getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(mContext, "Task deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
       if(removeTaskFromList(position)){
           manager.beginTransaction().replace(R.id.mainContent,new CreateTaskFragment(),null).addToBackStack(null).commit();
       }
    }

    public boolean removeTaskFromList(int position){
        tasks.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        return tasks.size()==0;
    }

}
