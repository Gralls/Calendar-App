package com.springer.patryk.tas_android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.springer.patryk.tas_android.R;
import com.springer.patryk.tas_android.adapters.CalendarGridAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Patryk on 23.11.2016.
 */

public class CalendarFragment extends Fragment {

    private Context mContext;
    private static final String LOG_TAG=CalendarFragment.class.getSimpleName();


    @BindView(R.id.backArrow)
    ImageView backArrow;
    @BindView(R.id.nextArrow)
    ImageView nextArrow;
    @BindView(R.id.monthText)
    TextView currentMonth;
    @BindView(R.id.monthView)
    GridView monthView;
    @BindView(R.id.daysTitle)
    GridView dayTitles;

    private Calendar date;
    private CalendarGridAdapter calendarGridAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.calendar_fragment, container, false);
        mContext = getContext();
        ButterKnife.bind(this, rootView);
        date = Calendar.getInstance();

        calendarGridAdapter = new CalendarGridAdapter(mContext, date);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.day_names));
        dayTitles.setAdapter(adapter);
        monthView.setAdapter(calendarGridAdapter);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPreviousMonth();
                updateDate();
            }
        });
        nextArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNextMonth();
                updateDate();
            }
        });

        updateDate();

        return rootView;
    }

    public void updateDate() {
        currentMonth.setText(android.text.format.DateFormat.format("MMMM yyyy", date));
        calendarGridAdapter.setCurrentMonth(date);

        Log.v(LOG_TAG, String.valueOf(date.getActualMaximum(Calendar.DAY_OF_MONTH)));
    }

    public void setPreviousMonth(){
        date.set(Calendar.MONTH, date.get(Calendar.MONTH) - 1);
    }
    public void setNextMonth(){
        date.set(Calendar.MONTH, date.get(Calendar.MONTH) + 1);
    }


}
