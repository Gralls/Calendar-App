package com.springer.patryk.tas_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.springer.patryk.tas_android.R;
import com.springer.patryk.tas_android.models.Date;
import com.springer.patryk.tas_android.models.Task;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by Patryk on 25.11.2016.
 */

public class CalendarGridAdapter extends BaseAdapter {

    private Context mContext;
    private Date currentDay;
    private List<Date> daysOfMonth;
    private List<Task> tasks;


    public CalendarGridAdapter(Context mContext, DateTime currentMonth) {
        this.mContext = mContext;
        tasks = new ArrayList<>();


        this.currentDay = new Date(
                currentMonth.getDayOfMonth()
                , currentMonth.getDayOfWeek()
                , currentMonth.getMonthOfYear()
                , currentMonth.getYear()
                , "");
        setMonthToShow(currentMonth);
    }

    @Override
    public int getCount() {
        return daysOfMonth.size();
    }

    @Override
    public Object getItem(int position) {
        return daysOfMonth.get(position).getTasks();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        TextView dayNumber;
        TextView dayTask;
        TextView dayMeeting;
        LinearLayout day;
        int position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Date date = daysOfMonth.get(position);
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater
                    .from(mContext)
                    .inflate(R.layout.day_item, null);
        }
        holder.dayNumber = (TextView) convertView.findViewById(R.id.dayTitle);
        holder.dayTask = (TextView) convertView.findViewById(R.id.dayTask);
        holder.dayMeeting = (TextView) convertView.findViewById(R.id.dayMeeting);
        holder.day = (LinearLayout) convertView.findViewById(R.id.dayItem);
        convertView.setTag(holder);
        if (date == null) {
            holder.day.setVisibility(View.INVISIBLE);
        } else {
            holder.dayNumber.setText(String.valueOf(date.getDayOfMonth()));
            holder.day.setVisibility(View.VISIBLE);
            if (searchHasTask(position)) {
                holder.dayTask.setVisibility(View.VISIBLE);
                holder.dayTask.setText(String.valueOf(date.getTasks().size()));

            } else {
                holder.dayTask.setVisibility(View.GONE);
                holder.dayMeeting.setVisibility(View.GONE);
            }
            if (date.equals(currentDay)) {
                holder.day.setBackgroundResource(R.drawable.today_background);
            } else {
                holder.day.setBackgroundResource(0);
            }
        }

        return convertView;
    }

    public void setMonthToShow(DateTime monthToShow) {
        daysOfMonth = convertToList(monthToShow);
        notifyDataSetChanged();
    }

    private List<Date> convertToList(DateTime monthToShow) {
        List<Date> month = new ArrayList<>();

        int firstDayOfDisplayedMonth = monthToShow.withDayOfMonth(1).getDayOfWeek() % 7;
        int emptyPosition = 0;

        while (emptyPosition < firstDayOfDisplayedMonth) {
            month.add(null);
            emptyPosition++;
        }

        int daysCountInMonth = monthToShow.dayOfMonth().getMaximumValue();

        monthToShow = monthToShow
                .dayOfMonth()
                .withMinimumValue();

        for (int i = 0; i < daysCountInMonth; i++) {
            month.add(new Date
                    (monthToShow.getDayOfMonth()
                            , monthToShow.getDayOfWeek()
                            , monthToShow.getMonthOfYear()
                            , monthToShow.getYear()
                            , "")
            );
            monthToShow = monthToShow.plusDays(1);
        }
        return month;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }


    public boolean searchHasTask(int position) {
        Date dayAtPosition = daysOfMonth.get(position);
        DateTime current = new DateTime(dayAtPosition.getYear(), dayAtPosition.getMonth(), dayAtPosition.getDayOfMonth(), 0, 0);
        List<Task> tasksOfDay = new ArrayList<>();
        for (Task task : tasks) {
            DateTime date = new DateTime(task.getStartDate());

            if (current.toLocalDate().equals(date.toLocalDate())) {
                tasksOfDay.add(task);
            }
        }
        daysOfMonth.get(position).setTask(tasksOfDay);
        return tasksOfDay.size() > 0;
    }
}
