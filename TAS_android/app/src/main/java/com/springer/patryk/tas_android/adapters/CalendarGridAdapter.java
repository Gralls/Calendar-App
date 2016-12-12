package com.springer.patryk.tas_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.springer.patryk.tas_android.R;
import com.springer.patryk.tas_android.models.Date;
import com.springer.patryk.tas_android.models.Task;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patryk on 25.11.2016.
 */

public class CalendarGridAdapter extends BaseAdapter {

    private Context mContext;
    private Date currentDay;
    private List<Date> daysOfMonth;
    private List<Task> tasks;
    private DateTimeFormatter dateFormat;

    public CalendarGridAdapter(Context mContext, DateTime currentMonth) {
        this.mContext = mContext;
        tasks = new ArrayList<>();
        dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
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
        return daysOfMonth.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        TextView dayNumber;
        ImageView dayTask;
        ImageView dayMeeting;
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
        holder.dayTask = (ImageView) convertView.findViewById(R.id.dayTask);
        holder.dayMeeting = (ImageView) convertView.findViewById(R.id.dayMeeting);
        holder.day = (LinearLayout) convertView.findViewById(R.id.dayItem);
        convertView.setTag(holder);

        if (date == null) {
            holder.day.setVisibility(View.INVISIBLE);
        } else {
            holder.dayNumber.setText(String.valueOf(daysOfMonth.get(position).getDayOfMonth()));
            convertView.findViewById(R.id.dayItem).setVisibility(View.VISIBLE);
            if (searchTask(date.getDayOfMonth(), date.getMonth(), date.getYear())) {
                holder.dayTask.setVisibility(View.VISIBLE);
            } else {
                holder.dayTask.setVisibility(View.INVISIBLE);
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


    public boolean searchTask(int day, int month, int year) {
        DateTime current = new DateTime(year, month, day, 0, 0);
        for (Task task : tasks) {
            DateTime date = dateFormat.parseDateTime(task.getStartDate());
            if (current.toLocalDate().equals(date.toLocalDate())) {
                return true;
            }
        }
        return false;
    }
}
