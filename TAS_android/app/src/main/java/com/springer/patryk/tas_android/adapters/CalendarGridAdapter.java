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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Patryk on 25.11.2016.
 */

public class CalendarGridAdapter extends BaseAdapter {

    private Context mContext;
    private Calendar currentMonth;
    private List<Date> daysOfMonth;

    public CalendarGridAdapter(Context mContext, Calendar currentMonth) {
        this.mContext = mContext;
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

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            convertView = LayoutInflater
                    .from(mContext)
                    .inflate(R.layout.day_item, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.dayTitle);
        if (daysOfMonth.get(position).getDayOfMonth().equals("")) {
            textView.setText("");
        } else
            textView.setText(String.valueOf(daysOfMonth.get(position).getDayOfMonth()));

        return convertView;
    }

    public void setCurrentMonth(Calendar currentMonth) {
        this.currentMonth = currentMonth;
        daysOfMonth = convertToList();
        checkDayOfWeek();
    }

    private List<Date> convertToList() {
        List<Date> month = new ArrayList<>();
        Calendar calendar = currentMonth;

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        int x=1;
        while (x < firstDayOfMonth) {
            month.add(new Date(""));
            x++;
        }
        for (int i = 0; i < currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i + 1);

            month.add(new Date(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))
                        , String.valueOf(calendar.get(Calendar.DAY_OF_WEEK))
                        , String.valueOf(calendar.get(Calendar.YEAR))
                        , ""));
        }
        return month;
    }

    private void checkDayOfWeek() {
        Log.d("log", String.valueOf(currentMonth.get(Calendar.DAY_OF_WEEK)));
    }

}
