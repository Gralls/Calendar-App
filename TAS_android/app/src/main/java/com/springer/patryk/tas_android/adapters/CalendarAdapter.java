package com.springer.patryk.tas_android.adapters;

import android.content.Context;
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

import java.util.Calendar;
import java.util.List;

/**
 * Created by Patryk on 25.11.2016.
 */

public class CalendarAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> currentMonth;

    public CalendarAdapter(Context mContext, List<String> currentMonth) {
        this.mContext = mContext;
        this.currentMonth = currentMonth;
    }

    @Override
    public int getCount() {
        return currentMonth.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            gridView = inflater.inflate(R.layout.day_item, null);
            TextView textView = (TextView) gridView.findViewById(R.id.dayTitle);
            textView.setText(currentMonth.get(position));
        } else {
            gridView = convertView;
        }

        return gridView;
    }

    public void setCurrentMonth(List<String> currentMonth) {
        this.currentMonth = currentMonth;
    }
}
