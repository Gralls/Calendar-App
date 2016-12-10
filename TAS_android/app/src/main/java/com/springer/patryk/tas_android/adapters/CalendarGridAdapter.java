package com.springer.patryk.tas_android.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.springer.patryk.tas_android.R;
import com.springer.patryk.tas_android.models.Date;
import com.springer.patryk.tas_android.models.Task;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Patryk on 25.11.2016.
 */

public class CalendarGridAdapter extends BaseAdapter {

    private Context mContext;
    private DateTime currentMonth;
    private List<Date> daysOfMonth;
    private List<Task> tasks;
    private DateTimeFormatter dateFormat;

    public CalendarGridAdapter(Context mContext, DateTime currentMonth) {
        this.mContext = mContext;
        tasks = new ArrayList<>();
        dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        setCurrentMonth(currentMonth);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Date date = daysOfMonth.get(position);
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            convertView = LayoutInflater
                    .from(mContext)
                    .inflate(R.layout.day_item, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.dayTitle);
        if (date.getDayOfMonth()==0) {
            convertView.findViewById(R.id.dayItem).setVisibility(View.INVISIBLE);
        } else {
            textView.setText(String.valueOf(daysOfMonth.get(position).getDayOfMonth()));
            convertView.findViewById(R.id.dayItem).setVisibility(View.VISIBLE);
            if (searchTask(date.getDayOfMonth(), date.getMonth(), date.getYear())) {
                 convertView.findViewById(R.id.dayTask).setVisibility(View.VISIBLE);
            }else{
                convertView.findViewById(R.id.dayTask).setVisibility(View.INVISIBLE);
            }
        }

        return convertView;
    }

    public void setCurrentMonth(DateTime currentMonth) {
        this.currentMonth = currentMonth;
        daysOfMonth = convertToList();

        notifyDataSetChanged();
    }

    private List<Date> convertToList() {
        List<Date> month = new ArrayList<>();
        DateTime calendar = currentMonth;

        int firstDayOfCurrentMonth = calendar.withDayOfMonth(1).getDayOfWeek() % 7;
        int x = 0;
        while (x < firstDayOfCurrentMonth) {
            month.add(new Date(0));
            x++;
        }
        calendar = calendar.dayOfMonth().withMinimumValue();
        for (int i = 0; i < currentMonth.dayOfMonth().getMaximumValue(); i++) {
            month.add(new Date(calendar.getDayOfMonth()
                    , calendar.getDayOfWeek()
                    , calendar.getMonthOfYear()
                    , calendar.getYear()
                    , ""));

            calendar = calendar.plusDays(1);
        }
        return month;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks=tasks;
        notifyDataSetChanged();
    }


    public boolean searchTask(int day, int month, int year) {
        DateTime current = new DateTime(year, month, day, 10, 30);
        for (Task task : tasks) {
                DateTime date = dateFormat.parseDateTime(task.getStartDate());
                if (current.toLocalDate().equals(date.toLocalDate())) {
                    Log.v("Date", date.toString());
                    return true;
                }

        }
        return false;
    }

}
