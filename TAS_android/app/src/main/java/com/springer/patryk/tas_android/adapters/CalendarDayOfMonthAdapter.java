package com.springer.patryk.tas_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.springer.patryk.tas_android.R;

import java.util.zip.Inflater;

/**
 * Created by Patryk on 2016-11-30.
 */

public class CalendarDayOfMonthAdapter extends BaseAdapter {

    private String[] days;
    private Context mContext;

    public CalendarDayOfMonthAdapter(Context context, String[] days) {
        this.mContext = context;
        this.days = days;
    }

    @Override
    public int getCount() {
        return days.length;
    }

    @Override
    public Object getItem(int i) {
        return days[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater
                    .from(mContext)
                    .inflate(R.layout.day_of_week_item, null);
        }

        TextView textView = (TextView) view.findViewById(R.id.dayOfWeekTitle);
        textView.setText(days[i]);
        return view;
    }
}
