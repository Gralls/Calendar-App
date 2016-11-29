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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.springer.patryk.tas_android.R;
import com.springer.patryk.tas_android.adapters.CalendarAdapter;

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

    private Calendar date;
    private CalendarAdapter calendarAdapter;
    private List<String>month;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.calendar_fragment, container, false);
        mContext = getContext();
        ButterKnife.bind(this, rootView);
        date = Calendar.getInstance();
        month = new ArrayList<>();
        calendarAdapter = new CalendarAdapter(mContext, month);
        setAdapter();
        monthView.setAdapter(calendarAdapter);
        monthView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext,String.valueOf(position),Toast.LENGTH_LONG).show();
            }
        });
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
        calendarAdapter.setCurrentMonth(month);
        setAdapter();
        Log.v(LOG_TAG, String.valueOf(date.getActualMaximum(Calendar.DAY_OF_MONTH)));
    }

    public void setPreviousMonth(){
        date.set(Calendar.MONTH, date.get(Calendar.MONTH) - 1);
    }
    public void setNextMonth(){
        date.set(Calendar.MONTH, date.get(Calendar.MONTH) + 1);
    }
    public void setAdapter(){
        month.clear();
        for(int i=1;i<date.getActualMaximum(Calendar.DAY_OF_MONTH)+1;i++){
            month.add(String.valueOf(i));
        }

    }

}
